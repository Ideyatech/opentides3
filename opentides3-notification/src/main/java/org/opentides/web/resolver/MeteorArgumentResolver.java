package org.opentides.web.resolver;

import org.atmosphere.cpr.Meteor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Auto-magically resolve a Meteor method parameter into an argument value for a given request.
 */
public class MeteorArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * Check that the parameter is of type Meteor.
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Meteor.class);
    }

    /**
     * Resolve the method parameter into an argument value.
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Meteor m = Meteor.build(webRequest.getNativeRequest(HttpServletRequest.class));
        return m;
    }
}
