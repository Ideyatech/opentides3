/**
 * 
 */
package org.opentides.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.apache.log4j.Logger;
import org.opentides.annotation.field.Checkbox;
import org.opentides.annotation.field.Dropdown;
import org.opentides.annotation.field.RadioButton;
import org.opentides.bean.AnnotationDefinition;

/**
 * @author allantan
 *
 */
public class AnnotationUtil {
    private static final Logger _log = Logger.getLogger(AnnotationUtil.class);
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
		
		AnnotationDefinition annotationDefn = new AnnotationDefinition(te.getSimpleName().toString());
		annotationDefn.setParams(params);
		return annotationDefn;
	}
	
    /**
     * Checks if the field is a list field (e.g. dropdown, radio button).
     *
     * @param field
     * @return
     */
    public static final boolean isListField(Field field) {
        if (field.isAnnotationPresent(Dropdown.class)
                || field.isAnnotationPresent(Checkbox.class)
                || field.isAnnotationPresent(RadioButton.class)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a field is annotated with the given annotation.
     *
     * @param annotName
     * @param field
     * @return
     */
    public static Boolean isAnnotatedWith(String annotName, Field field) {
        Annotation[] classAnnotations = field.getAnnotations();
        for (Annotation annotation : classAnnotations) {
            Method m;
            try {
                m = annotation.getClass().getDeclaredMethod(annotName);
                if (m != null) {
                    return (Boolean) m.invoke(annotation);
                }
            } catch (Exception e) {
                // do nothing
            }
        }
        return false;
    }
}
