/**
 * 
 */
package org.opentides.bean;

import java.util.HashMap;
import java.util.Map;

import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;

/**
 * This bean contains the definition of a field. 
 * A FieldDefinition is associated to a BeanDefinition and contains 
 * AnnotationDefinition.
 * 
 * @author allantan
 */
public class FieldDefinition implements Definition {

	private BeanDefinition bean;
	
	private String fieldName;
	
	private String type;
	
	private String label;
	
	private String getterName;
	
	private String setterName;

	private Map<String, AnnotationDefinition> annotations = new HashMap<String, AnnotationDefinition>();

	/**
	 * Constructor with initial values
	 * 
	 * @param fieldName
	 * @param type
	 * @param label
	 */
	public FieldDefinition(BeanDefinition bean,
			String fieldName, 
			String type,
			String label) {
		super();
		this.bean = bean;
		this.fieldName = fieldName;
		this.type = type;
		// check if label is specified, otherwise use default field label
		if (StringUtil.isEmpty(label)) 
			this.label = NamingUtil.toLabel(fieldName);
		else 
			this.label = label.replaceAll("\"", "");
		this.getterName = NamingUtil.toGetterName(fieldName);
		this.setterName = NamingUtil.toSetterName(fieldName);
	}
	
	public final void addAnnotation(AnnotationDefinition annotationDefn) {
		annotations.put(annotationDefn.getName(), annotationDefn);
	}

	/**
	 * @return the bean
	 */
	public final BeanDefinition getBean() {
		return bean;
	}

	/**
	 * @return the fieldName
	 */
	public final String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @return the label
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * @return the getterName
	 */
	public final String getGetterName() {
		return getterName;
	}

	/**
	 * @return the setterName
	 */
	public final String getSetterName() {
		return setterName;
	}

	/**
	 * @return the annotations
	 */
	public final Map<String, AnnotationDefinition> getAnnotations() {
		return annotations;
	}
	
	/**
	 * Returns the attribute setting for the given annotation.
	 * @param name
	 * @return
	 */
	public final Object getAttribute(String attribute) {
		for (String key:annotations.keySet()) {
			AnnotationDefinition defn = annotations.get(key);
			if (defn.getParams() != null && defn.getParams().containsKey(attribute)) 
				return defn.getParams().get(attribute);			
		}
		return "";
	}
	
	public final Boolean isByOptions() {
		Object o = this.getAttribute("options");
		if ( o instanceof String && StringUtil.isEmpty((String)o))
			return new Boolean(false);
		return new Boolean(true);
	}
	
	public final Boolean isByCategory() {
		Object o = this.getAttribute("category");
		if (o instanceof String && StringUtil.isEmpty((String)o))
			return new Boolean(false);
		return new Boolean(true);		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bean == null) ? 0 : bean.hashCode());
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldDefinition other = (FieldDefinition) obj;
		if (bean == null) {
			if (other.bean != null)
				return false;
		} else if (!bean.equals(other.bean))
			return false;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return bean.toString() + "." + fieldName;
	}

}
