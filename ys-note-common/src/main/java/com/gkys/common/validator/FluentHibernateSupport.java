package com.gkys.common.validator;

import java.util.Locale;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by Administrator on 2017/4/5.
 */
public class FluentHibernateSupport {
    public static Validator getValidator() {
    	//Locale.setDefault(Locale.ENGLISH); // speicify language
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator;
    }
}
