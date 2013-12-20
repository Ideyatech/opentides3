package org.opentides.web.validator;

import javax.persistence.EntityNotFoundException;

import org.opentides.bean.Widget;
import org.opentides.service.WidgetService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class WidgetValidator implements Validator {
	
	@Autowired
	@Qualifier("widgetService")
	private WidgetService widgetService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Widget.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object command, Errors errors) {
		Widget widget = (Widget)command;
		
		if(!StringUtil.isEmpty(widget.getName()) && isDuplicateName(widget))
			errors.reject("error.duplicate-field", new Object[]{"\""+widget.getName()+"\"","name"}, "\""+widget.getName() +"\" already exists. Please try a different name.");
	
		if(!StringUtil.isEmpty(widget.getTitle()) && isDuplicateUrl(widget))
			errors.reject("error.duplicate-field", new Object[]{"\""+widget.getUrl()+"\"","url"}, "\""+widget.getUrl() +"\" already exists. Please update the existing url.");
		
		ValidationUtils.rejectIfEmpty(errors, "name", "error.required", new Object[]{"Name"});
		ValidationUtils.rejectIfEmpty(errors, "title", "error.required", new Object[]{"title"});
		ValidationUtils.rejectIfEmpty(errors, "url", "error.required", new Object[]{"URL"});
	}
	
	/**
	 * 
	 * @param fieldName name of field
	 * @return boolean returns true if duplicate name was found, false otherwise
	 */
	public boolean isDuplicateName(Widget widget){
		Widget key;
		try {
			key = this.widgetService.findByName(widget.getName());
			if (key != null){
				if(!key.getId().equals(widget.getId()))
					return true;
			}
		} catch (EntityNotFoundException e) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param fieldName name of field
	 * @return boolean returns true if duplicate name was found, false otherwise
	 */
	public boolean isDuplicateUrl(Widget widget){
		Widget key;
		try {
			key = this.widgetService.findByUrl(widget.getUrl());
			if (key != null){
				if (!key.getId().equals(widget.getId()))
					return true;
			}
		} catch (EntityNotFoundException e) {
			return false;
		}
		return false;
	}

}
