/**
 * 
 */
package org.opentides.persistence.evolve;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.opentides.util.EvolveUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

/**
 * This is a generic implementation to populate the database
 * using CSV files. This works by using the filename as the classname
 * and the first row of the csv contains the fieldNames. 
 * Using reflection, the entity is created and persisted.
 * 
 * @author allantan
 *
 */
public class BaseCSVEvolve extends Evolver implements DBEvolve {
	
	private static final Logger _log = Logger.getLogger(BaseCSVEvolve.class);

	private static final Resource[] NO_FILES = {};

	private String[] csvFiles = {"classpath:app/csv/*.csv"};
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private String description;
	
	private int version;
	
	private String tenant;

    private List<Resource> loadResources() {
    	List<Resource> resources = new ArrayList<Resource>();
        try {
        	for (String f:csvFiles) {
        		Resource[] rs =  ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(f);
        		for (Resource r:rs) {
        			boolean found = false;
        			for (Resource s:resources) {
        				if (r.getFile().equals(s.getFile()))
        					found = true;
        			}
            		if (!found)
            			resources.add(r);        			
        		}
        	}
        	return resources;
		} catch (IOException e) {
			_log.error("Unable to find files for csv import.", e);
			return new ArrayList<Resource>();
		}
    }
	
	/* (non-Javadoc)
	 * @see org.opentides.persistence.evolve.DBEvolve#execute()
	 */
	@Override
	public void execute() {
		// get all csv files in the folder
		List<Resource> rs = loadResources();
		for (Resource data:rs) {			
			String filename = data.getFilename();
			String entityName = filename.replace(".csv", "");			
			try {
				if (!StringUtil.isEmpty(tenant))
					em.createNativeQuery("USE " + tenant).executeUpdate();
				EvolveUtil.importCSVAsObject(data.getFile().getAbsolutePath(), entityName, em.unwrap(Session.class));
			} catch (Exception e) {
				_log.error("Failed to import " + data.getFilename(), e);
				throw new RuntimeException(e);
			}
		}			
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param csvFiles the csvFiles to set
	 */
	public void setCsvFiles(String[] csvFiles) {
		this.csvFiles = csvFiles;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

}
