/**
 * 
 */
package org.opentides.bean;

import java.util.Map;

/**
 * This bean is the container of definition for one annotation.
 * AnnotationDefinition is associated to a BeanDefinition or FieldDefinition.
 * 
 * @author allantan
 */
public class AnnotationDefinition implements Definition {

	private String name;
	
	private Map<String, Object> params;

	/**
	 * Constructor with annotation name
	 * @param name
	 */
	public AnnotationDefinition(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the params
	 */
	public final Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public final void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
		
}
