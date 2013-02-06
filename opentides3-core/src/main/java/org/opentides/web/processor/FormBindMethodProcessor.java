/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.web.processor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.FormBind;
import org.opentides.bean.BaseEntity;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.BaseCrudService;
import org.opentides.util.CacheUtil;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * This method processor is 
 * @author allantan
 *
 */
public class FormBindMethodProcessor implements HandlerMethodArgumentResolver {
	
	@Autowired
	private BeanFactory beanFactory;
	
	/**
	 * @return true if the parameter is annotated with {@link FormBind}
	 */
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(FormBind.class)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Resolve the argument from the model or if not found instantiate it with
	 * its default. The model attribute is then populated with request values 
	 * via data binding and validated. If no validation error, the model
	 * is retrieved from database and merged with the submitted form.
	 *   
	 * @throws BindException if data binding and validation result in an error
	 * @throws Exception if WebDataBinder initialization fails.
	 */
	@SuppressWarnings("unchecked")
	public final Object resolveArgument(
			MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest nativeRequest, WebDataBinderFactory binderFactory)
			throws Exception {

		FormBind annot = parameter.getParameterAnnotation(FormBind.class);
		Class<?> clazz = parameter.getDeclaringClass();
		String name = getName(annot, parameter);
		String id = nativeRequest.getParameter("id");
		HttpServletRequest request =
				 (HttpServletRequest) nativeRequest.getNativeRequest();

		Method addForm = CacheUtil.getNewFormBindMethod(clazz);
		Object controller = beanFactory.getBean(parameter.getDeclaringClass());		
		Object target = (addForm!=null) ?
						addForm.invoke(controller, request) : 
						BeanUtils.instantiateClass(parameter.getParameterType());
		MutablePropertyValues mpvs = new MutablePropertyValues(nativeRequest.getParameterMap());
		WebDataBinder binder = binderFactory.createBinder(nativeRequest, target, name);
		if (binder.getTarget() != null) {
			binder.bind(mpvs);
			if (binder.getValidator()!= null)
				binder.validate();
			if (binder.getBindingResult().hasErrors()) {
				throw new BindException(binder.getBindingResult());
			}			

			// if target extends BaseEntity and for update, link target to database record
			if (BaseEntity.class.isAssignableFrom(parameter.getParameterType()) && 
					!StringUtil.isEmpty(id) && !"0".equals(id)) {	
				// retrieve from database
				Method updateForm = CacheUtil.getUpdateFormBindMethod(clazz);
				BaseEntity record = null;
				if (updateForm==null) {
					// no annotation, invoke from service
					Method getService = controller.getClass().getMethod("getService");
					BaseCrudService<? extends BaseEntity> service = 
							(BaseCrudService<? extends BaseEntity>) getService.invoke(controller);
					record = (BaseEntity) service.load(id);					
				} else {
					// with annotation, invoke annotation
					record = (BaseEntity) updateForm.invoke(controller, request);
				}
				
				if (record != null) {
					WebDataBinder updateBinder = binderFactory.createBinder(nativeRequest, record, name);
					updateBinder.bind(mpvs);
					mavContainer.addAllAttributes(updateBinder.getBindingResult().getModel());
					return updateBinder.getTarget();
				} else {
					String message = "Cannot find method with @FormBind with update mode. " +
							   "Also, unable to find service associated to controller." +
							   "Please specify one that retrieves record from database."; 
					throw new InvalidImplementationException(message);
				}				
			}
		}

		mavContainer.addAllAttributes(binder.getBindingResult().getModel());
		return binder.getTarget();
	}

	protected final String getName(FormBind annot, MethodParameter parameter) {
		String name = (annot != null) ? annot.name() : null;
		if (StringUtil.isEmpty(name)) 
			name = NamingUtil.toAttributeName(parameter.getParameterType().getSimpleName());
		return name;
	}	
}
