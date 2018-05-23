package com.gkys.common.constant;

/**
 * 全局常量
 */
public class BaseConstants {
	public static final String YES = "1";
	public static final String NO = "0";
	public static final Integer ONE = 1;
	public static final Integer ZERO = 0;
	public static final String ROOT_PATH="gkys-common.root"; 
	
	/**********JWT************/
	public static final String JWT_ID = "5236A";										//jwtid
	public static final String JWT_DEFAULT_SECERT = "7786df7fc3a34e26a61c034d5ec8245d";			//密匙
	public static final long JWT_TTL = 1 * 5 * 1000;									//token有效时间
	public static final int JWT_ERRCODE_EXPIRE = 4001;			//Token过期
	public static final int JWT_ERRCODE_FAIL = 4002;			//验证不通过
	
	/*** 表单token ***/
    public static final String TOKEN_FORM = "tokenForm";
}
