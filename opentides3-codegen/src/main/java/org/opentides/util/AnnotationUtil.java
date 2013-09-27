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
import org.opentides.annotation.field.CheckBox;
import org.opentides.annotation.field.DropDown;
import org.opentides.annotation.field.RadioButton;
import org.opentides.bean.AnnotationDefinition;
import org.opentides.bean.FieldDefinition;

/**
 * @author allantan
 *
 */
public class AnnotationUtil {
    private static Logger _log = Logger.getLogger(AnnotationUtil.class);
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

    /**
     * Checks if the field is a list field (e.g. dropdown, radio button).
     *
     * @param field
     * @return
     */
    public static final boolean isListField(Field field) {
        if (field.isAnnotationPresent(DropDown.class)
                || field.isAnnotationPresent(CheckBox.class)
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

    /**
     * @param clazz
     *            - bean class to check the titleField
     * @param isObject
     *            - determine if the titleField to retrieve is an
     *            objectTitleField or not.
     * @return
     */
    /*
    public static String getTitleField(Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (isAnnotatedWith("titleField", field)) {
                    if (SystemCodes.class.isAssignableFrom(field.getType())) {
                        return field.getName() + ".value";
                    } else if (BaseEntity.class.isAssignableFrom(field.getType())) {
                        return field.getName()
                                + "."
                                + AnnotationUtil.getTitleField(field
                                .getType());
                    } else
                        return field.getName();
                }
            }
        } catch (Exception e) {
            // do nothing
            _log.debug("Unable to find annotation attribute titleField for class ["
                    + clazz.getName());
        }
        return "";
    }
    */
}
