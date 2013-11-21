package com.ideyatech.ninja.web.validator;

import com.ideyatech.ninja.bean.Ninja;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/13/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class NinjaValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Ninja.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
