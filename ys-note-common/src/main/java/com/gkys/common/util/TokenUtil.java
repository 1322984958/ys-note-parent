package com.gkys.common.util;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class TokenUtil {
	public static String userTokenKey="ysuser";
//	/**
//	 * 创建用户token
//	 * @return
//	 */
//	public static String createUserToken(Long userId){
//		return AESUtil.AESEncode(userId.toString(),userTokenKey)+";"+DateUtils.addHours(new Date(), 4).getTime();
//	}
	
	/**
	 * @return
	 */
	public static Boolean checkExpireToken(String token){
		String[] arr=token.split(";");
		String userId=AESUtil.AESDecode(arr[0],userTokenKey);
		return new Date(Long.parseLong(arr[1])).before(new Date());
	}
	
}
