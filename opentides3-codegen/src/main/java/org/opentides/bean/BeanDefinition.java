/**
 * 
 */
package org.opentides.bean;

import java.util.Map;
import java.util.Set;

import org.opentides.util.NamingUtil;

/**
 * @author allantan
 *
 */
public class BeanDefinition implements Definition {
 
	private String className;
	
	private String modelName;
	
	private String formName;
	
	private String prefix;
	
	private String modelPackage;
	
	private String package_;
	
	private Set<AnnotationDefinition> annotations;
	
	private Set<FieldDefinition> fields;
		
	private Map<String, Object> params;
	
	/**
	 * Constructor using fully qualified class name
	 * 
	 * @param qualifiedName - fully qualified name (e.g. org.opentides.bean.SystemCodes)
	 */
	public BeanDefinition(String qualifiedName) {
		this(NamingUtil.getPackageName(qualifiedName), 
			 NamingUtil.getSimpleName(qualifiedName));
	}
	
	/**
	 * Constructor using package and class name.
	 * 
	 * Attributes are initialized based on convention below:
	 * 	 className - SystemCodes
	 *   modelName - systemCodes
	 *   formName  - system-codes
	 *   prefix    - system-codes
	 *   modelPackage - org.opentides.bean
	 *   package   - org.opentides
	 *   
	 * @param package_ - name of the package (e.g. org.opentides.bean)
	 * @param name - name of the class (e.g. SystemCodes)
	 */
	public BeanDefinition(String package_, String name) {
		this.className = name;
		this.modelName = NamingUtil.toAttributeName(name);
		this.formName = NamingUtil.toElementName(name);
		this.prefix = NamingUtil.toElementName(name);
		this.modelPackage = package_;
		int t = package_.lastIndexOf(".");
		if (t > 0)
			this.package_ = package_.substring(0, t);
		else 
			this.package_ = "";
	}
	
	public boolean containsDate() {
		for (FieldDefinition field:fields) {
			System.out.println("Field =" + field.getType() +":" + field.getFieldName());
			if ("java.util.Date".equals(field.getType()))
				return true;
		}
		return false;
	}
	
	public boolean containsList() {
		for (FieldDefinition field:fields) {
			if ("java.util.List".equals(field.getType()))
				return true;
		}
		return false;		
	}
		
	public boolean containsByOptions() {
		for (FieldDefinition field:fields) {
			if (field.isByOptions())
				return true;
		}
		return false;
	}
	
	public boolean containsByCategory() {
		for (FieldDefinition field:fields) {
			if (field.isByCategory())
				return true;
		}
		return false;		
	}
	
	/**
	 * @return the className
	 */
	public final String getClassName() {
		return className;
	}

	/**
	 * @return the modelName
	 */
	public final String getModelName() {
		return modelName;
	}

	/**
	 * @return the formName
	 */
	public final String getFormName() {
		return formName;
	}

	/**
	 * @return the prefix
	 */
	public final String getPrefix() {
		return prefix;
	}

	/**
	 * @return the modelPackage
	 */
	public final String getModelPackage() {
		return modelPackage;
	}

	/**
	 * @return the package_
	 */
	public final String getPackage_() {
		return package_;
	}

	/**
	 * @return the annotations
	 */
	public final Set<AnnotationDefinition> getAnnotations() {
		return annotations;
	}

	
	/**
	 * @param annotations the annotations to set
	 */
	public final void setAnnotations(Set<AnnotationDefinition> annotations) {
		this.annotations = annotations;
	}

	/**
	 * @return the fields
	 */
	public final Set<FieldDefinition> getFields() {
		return fields;
	}
	

	/**
	 * @param fields the fields to set
	 */
	public final void setFields(Set<FieldDefinition> fields) {
		this.fields = fields;
	}

	/**
	 * @return the params
	 */
	public final Map<String, Object> getParams() {
		return params;
	}
	
	/**
	 * @return fully qualified class name
	 */
	public final String getQualifiedName() {
		return modelPackage + "." + className;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((modelPackage == null) ? 0 : modelPackage.hashCode());
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
		BeanDefinition other = (BeanDefinition) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (modelPackage == null) {
			if (other.modelPackage != null)
				return false;
		} else if (!modelPackage.equals(other.modelPackage))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return modelPackage + "." + className;
	}

}
