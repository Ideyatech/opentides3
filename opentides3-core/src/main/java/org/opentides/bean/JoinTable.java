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
package org.opentides.bean;

/**
 * JoinTable contains definition of fields with @ManyToMany mapping. 
 * 
 * @author allantan
 *
 */
public class JoinTable {

	private String tableName;
	
	private String column1;
	
	private String column2;

	
	/**
	 * @param tableName
	 * @param column1
	 * @param column2
	 */
	public JoinTable(String tableName, String column1, String column2) {
		super();
		this.tableName = tableName;
		this.column1 = column1;
		this.column2 = column2;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the column1
	 */
	public String getColumn1() {
		return column1;
	}

	/**
	 * @param column1 the column1 to set
	 */
	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	/**
	 * @return the column2
	 */
	public String getColumn2() {
		return column2;
	}

	/**
	 * @param column2 the column2 to set
	 */
	public void setColumn2(String column2) {
		this.column2 = column2;
	}
	
	
}
