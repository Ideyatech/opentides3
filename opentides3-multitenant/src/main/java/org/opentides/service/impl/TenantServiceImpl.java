/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;
import org.opentides.dao.TenantDao;
import org.opentides.persistence.hibernate.MultiTenantSchemaUpdate;
import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.opentides.service.MultitenantUserService;
import org.opentides.service.TenantService;
import org.opentides.util.DateUtil;
import org.opentides.util.FileUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author allantan
 *
 */
@Service("tenantService")
public class TenantServiceImpl extends BaseCrudServiceImpl<Tenant> implements
		TenantService {

	private static final Logger _log = Logger
			.getLogger(TenantServiceImpl.class);
	
	@Value("${database.default_schema}")
	private String defaultSchema = "master";

	@Autowired
	private MultiTenantSchemaUpdate multiTenantSchemaUpdate;

	@Autowired
	private MultitenantUserService multitenantUserService;
	
	@Autowired
	private MultitenantJdbcTemplate jdbcTemplate;
	
	@Value("${jpa.log_ddl.directory}")
	private String ddlLogs = "/var/log/ss_ddl/";
	
	@Value("${database.username}")
	private String sqlUsername = "ss";
	
	@Value("${database.password}")
	private String sqlPassword = "ideyatech";
	
	@Value("${database.mysql.bin}")
	private String mySQLBin = "/usr/local/mysql/bin";

	@Override
	public String findUniqueSchemaName(final String company) {
		final String schema = company.replaceAll("[^a-zA-Z]", "");
		final StringBuffer uniqueSchema = new StringBuffer(defaultSchema + "_"
				+ schema);
		Tenant t = ((TenantDao) getDao()).loadBySchema(uniqueSchema.toString());

		while (t != null) {
			uniqueSchema.append(StringUtil.generateRandomString(3));
			t = ((TenantDao) getDao()).loadBySchema(uniqueSchema.toString());
		}

		return uniqueSchema.toString();
	}
	

	@Override
	public Tenant findByName(String company) {
		return ((TenantDao) getDao()).findByName(company);	
	}



	@Override
	public Tenant findBySchema(String schema) {
		return ((TenantDao) getDao()).loadBySchema(schema);	
	}

	@Override
	public void createTenantSchema(final Tenant tenant,
			final MultitenantUser owner) {
		final String company = tenant.getCompany();
		final String schema = findUniqueSchemaName(company);

		tenant.setSchema(schema);
		tenant.setDbVersion(1l);

		multiTenantSchemaUpdate.schemaEvolve(schema);
		multitenantUserService.persistUserToTenantDb(tenant, owner, new String[] {"Administrator"});
		
		// disable the copy in the master db so the owner won't be able to log
		// in there
		owner.getCredential().setEnabled(Boolean.FALSE);
	}
	
	@Override
	public void createTemplateSchema(Tenant tenant) {
		final String schema = "tpl_" + tenant.getCompany();
		tenant.setSchema(schema);
		tenant.setDbVersion(1l);
		save(tenant);
		multiTenantSchemaUpdate.schemaEvolve(schema);		
	}

	@Override
	public boolean deleteTenantSchema(final Tenant tenant, final boolean createBackup) {
		throw new UnsupportedOperationException(
				"Deleting of tenant schema is not yet supported.");
	}

	@Override
	public String getTenantSchemaName(String tenantName) {
		return ((TenantDao) getDao()).getTenantSchemaName(tenantName);
	}

	@Override
	public void changeSchema(String schemaName) throws SQLException {
		if (!jdbcTemplate.getCurrentSchemaName().equalsIgnoreCase(schemaName)) {
			jdbcTemplate.switchSchema(schemaName);
		}

	}


	@Override
	public void cloneTenantSchema(Tenant template, Tenant tenant) {
		final String dir = ddlLogs + "/"
				+ DateUtil.convertShortDate(new Date());
		final String company = tenant.getCompany();
		final String schema = findUniqueSchemaName(company);
		tenant.setSchema(schema);
		tenant.setDbVersion(1l);
		
		final File sqlFile = new File(dir + "/schema-" + schema + ".sql");
		final File logFile = new File(dir + "/schema-" + schema + ".log");
		String credential = " -u "+sqlUsername+" -p"+sqlPassword;
		_log.info("DDL logs can be found in " + sqlFile.getAbsolutePath());
		FileUtil.createDirectory(dir);

		// sample command is 
		//   mysqladmin -u gt -pideyatech create test
		//   mysqldump -u root tpl_Retail_Master | mysql -u root test --verbose
		final String createSchema = mySQLBin + "/mysqladmin " + credential + " create "+ schema;
		final String dumpSchema = mySQLBin + "/mysqldump " +credential+" " + template.getSchema() + " > " +sqlFile.getAbsolutePath();
		final String loadSchema = mySQLBin +"/mysql " + credential + " " + schema + " < " + sqlFile.getAbsolutePath();
		try {
			this.executeShell(createSchema, logFile);
			this.executeShell(dumpSchema, logFile);
			this.executeShell(loadSchema, logFile);
		} catch (Exception e) {
			_log.error("Failed to execute command for cloning tenant.",e);
		}
	}
		
	
	/**
	 * Private helper that executes shell command. 
	 * Used for cloning database schema.
	 * @param command
	 */
	private void executeShell(String command, File outputFile) throws Exception {
		Process p;
		BufferedWriter writer = null;
		BufferedReader reader = null;
		BufferedReader error  = null;
		int exitStatus = 0;

		String line = "";
		_log.info("Executing shell command :" + command);
		try {			
			String[] script = {
					"/bin/sh",
					"-c",
					command
			};

			p = Runtime.getRuntime().exec(script);
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    writer = new BufferedWriter(new FileWriter(outputFile));				
			
			while (true) {
				try {
					exitStatus = p.waitFor();
					break;
				} catch (java.lang.InterruptedException e) {
					// do nothing...
				}
			}
			
			// write any result to file
			while ((line = reader.readLine())!= null) {
				writer.write(line + "\n");
			}					

			if (exitStatus != 0) {
				// get the error stream of the process and print it
				error = new BufferedReader(
						new InputStreamReader(p.getErrorStream()));
				StringBuffer errorMsg = new StringBuffer();
				while ((line = error.readLine()) != null) {
					errorMsg.append(line);
				}
				throw new Exception("Error executing command" + command 
						+ " with return value :" + exitStatus + "\n" + errorMsg);
			}
			
			_log.info("Execution of " +command+" successful.");			
		} catch (Exception e) {
			throw new Exception("Failed to record log file during tenant creation.", e);
		} finally {
	        if ( writer != null)
	        	try { writer.close( ); } catch (Exception e) { };
	        if (reader != null)
	        	try { reader.close( ); } catch (Exception e) { };			
		}
	}
}
