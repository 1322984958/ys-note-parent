package com.gkys.web.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.gkys.common.annotation.MethodParaValidate;
import com.gkys.common.base.StringKeyHashMap;
import com.gkys.common.exception.BaseMyValidateException;
import com.gkys.common.validator.FluentHibernateSupport;

@Component
@Aspect
@Order(2)
public class ValidateAspect {
	private static Logger logger = LoggerFactory.getLogger(ValidateAspect.class);
	
	@Pointcut(value="execution(* com.gkys..*Service*.insert*(..))")
	public void pointcut1(){} 
	@Pointcut(value="execution(* com.gkys..*Service*.update*(..))")
	public void pointcut2(){} 
	@Pointcut(value="execution(* com.gkys..*Service*.save*(..))")
	public void pointcut3(){} 
	
	@Before(value="pointcut1()||pointcut2()||pointcut3()")
    public void doBeforeInService(JoinPoint joinPoint) {
        logger.debug("ValidateAspect");
		Object target = joinPoint.getTarget();
		Method method=((MethodSignature)joinPoint.getSignature()).getMethod();
		Annotation parameterAnnotations[][] = method.getParameterAnnotations();
		List<Integer> needValidateParaNum=new ArrayList<>();
        for (int i = 0; i < parameterAnnotations.length; i++) {  
            for (Annotation annotation : parameterAnnotations[i]) {  
                if (MethodParaValidate.class.equals(annotation.annotationType())) {  
                	needValidateParaNum.add(i);
                }  
            }  
        }
        Object[] params=joinPoint.getArgs();
        for(Integer num:needValidateParaNum){
        	validate(params[num], new HibernateSupportedValidator<>());
        }
		//拦截的放参数类型  
		/*Class[] parameterTypes = method.getParameterTypes();
		method.getAnnotation(MethodParaValidate.class);
        for (int j = 0; j < parameterTypes.length; j++) {
        	Class parameterType = parameterTypes[j];
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (int k = 0; k < annotations.length; k++) {
                Annotation annotation = annotations[k];
                System.out.println(annotation.annotationType());
            }
        }*/
    }
	
	public static void validate(Object target,HibernateSupportedValidator t){
    	ComplexResult result = FluentValidator.checkAll().failOver()
    		    .on(target, t.setHiberanteValidator(FluentHibernateSupport.getValidator()))
    		    .doValidate()
    		    .result(ResultCollectors.toComplex());
		if (!result.isSuccess()) {
			StringKeyHashMap stringKeyHashMap=new StringKeyHashMap();
			stringKeyHashMap.put("error", result.getErrors());
			throw new BaseMyValidateException(stringKeyHashMap);
		}
    }

}
