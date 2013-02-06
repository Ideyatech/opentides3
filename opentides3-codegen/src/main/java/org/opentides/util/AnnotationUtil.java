/**
 * 
 */
package org.opentides.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.opentides.bean.AnnotationDefinition;
import org.opentides.bean.FieldDefinition;

/**
 * @author allantan
 *
 */
public class AnnotationUtil {

	/**
	 * Hides the constructor. This is a singleton.
	 */
	private AnnotationUtil() {
		
	}
	
	/**
	 * Retrieves the parameters that are declared in the annotation.
	 * @param te
	 * @param e
	 * @return
	 */
	public static final AnnotationDefinition getAnnotationDefinition(TypeElement te, Element e) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		// get all specified parameters		
		List<? extends AnnotationMirror> mirrors = e.getAnnotationMirrors();
		for (AnnotationMirror mirror : mirrors) {
			if (mirror.getAnnotationType().equals(te.asType())) {
				Map<? extends ExecutableElement, ? extends AnnotationValue> values = mirror.getElementValues();
				for (ExecutableElement ee:values.keySet()) {
					params.put(ee.getSimpleName().toString(), values.get(ee));
				}				
			}			
		}
		
		AnnotationDefinition annotationDefn = new AnnotationDefinition(te.toString());
		annotationDefn.setParams(params);
		return annotationDefn;
	}
	
	public static void buildListDefinition(FieldDefinition field, 
			AnnotationDefinition annotation) {
		Map<String, Object> params = annotation.getParams();
		if (params.get("category") != null && 
			!StringUtil.isEmpty(params.get("category").toString())) {
			field.setCategory(params.get("category").toString());
		} else if(params.get("options") != null && 
			!StringUtil.isEmpty(params.get("options").toString()) ){
			field.setOptions(params.get("options").toString().split(","));			
		}
	}
}
