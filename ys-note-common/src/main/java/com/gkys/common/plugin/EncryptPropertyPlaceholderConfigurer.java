package com.gkys.common.plugin;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.gkys.common.util.AESUtil;


/**
 * 支持加密配置文件插件
 * Created by ys on 2017/2/4.
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	/**
	 * 解密指定propertyName的加密属性值
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
//		System.out.println("属性名："+propertyName+"属性值："+propertyValue);
	    if(isEncryptPropertyVal(propertyName)){
	    	return AESUtil.AESDecode(propertyValue);
	    }
		return super.convertProperty(propertyName, propertyValue);
	}
	
	/** 
     * 判断属性值是否需要解密，这里约定需要解密的属性名用encrypt开头 
     * @param propertyName 
     * @return 
     */  
    private boolean isEncryptPropertyVal(String propertyName){  
        if(propertyName.startsWith("encrypt.")){  
            return true;  
        }else{  
            return false;  
        }  
    }  
}
