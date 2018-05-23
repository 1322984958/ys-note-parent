package com.gkys.common.util;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.gkys.common.constant.BaseConstants;
import com.gkys.common.token.TokenCheckResult;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.Base64Codec;

public class JwtsUtils {
	/**
	 * 由字符串生成加密key
	 * @return
	 */
	private static Key generalKey(String key) {
		byte[] encodedKey = Base64Codec.BASE64.encode(key.getBytes())
				.getBytes();
		return new SecretKeySpec(encodedKey, "AES");
	}

	/**
	 * 创建token
	 * @param userId
	 * @param ttlMillis
	 * @return
	 * @throws Exception
	 */
	public static String createToken(String userId,String key,Date expireDate) {
		String token = Jwts.builder().setSubject(userId)
				.signWith(SignatureAlgorithm.HS256, generalKey(key))
				.setIssuedAt(new Date()).setExpiration(expireDate).compact();
		return token;
	}

	/**
	 * 解析token
	 * @param token
	 * @return
	 * @throws Exception
	 */
	protected static Claims parseToken(String token, String key)
			throws Exception {
		if (token == null) {
			throw new NullPointerException("token不能为null");
		}
		return Jwts.parser().setSigningKey(generalKey(key))
				.parseClaimsJws(token).getBody();
	}
	
	public static TokenCheckResult parseTokenGetResult(String jwtStr, String key) {
		TokenCheckResult checkResult = new TokenCheckResult();
		Claims claims = null;
		try {
			claims = parseToken(jwtStr,key);
			checkResult.setSuccess(true);
			checkResult.setClaims(claims);
		} catch (ExpiredJwtException e) {
			checkResult.setErrCode(BaseConstants.JWT_ERRCODE_EXPIRE);
			checkResult.setSuccess(false);
		} catch (SignatureException e) {
			checkResult.setErrCode(BaseConstants.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
		} catch (Exception e) {
			checkResult.setErrCode(BaseConstants.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
		}
		return checkResult;
	}
	
	public static void main(String[] args) throws Exception {
//		String token=createToken(String.valueOf(4L), "ys", DateUtils.addMilliseconds(new Date(), 1));
		String token = Jwts.builder().setSubject("4")
				.signWith(SignatureAlgorithm.HS256, generalKey("ys"))
				.setIssuedAt(new Date()).setExpiration(DateUtils.addDays(new Date(), 1))
				.claim("ys", "asdf").claim("afewwe", "faewew").compact();
		System.out.println(token);
		token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNTIyMzgxNTQzLCJleHAiOjE1MjI0Njc5NDMsInlzIjoiYXNkZiIsImFmZXd3ZSI6ImZhZXdldyJ9.UhzCZofWqLCFk4eZSSNzcOJ58SDlFzUCDDFZgGbWCl4";
		System.out.println(Jwts.parser().setSigningKey(generalKey("ys"))
				.parseClaimsJws(token).getSignature());
		System.out.println(Jwts.parser().setSigningKey(generalKey("ys"))
				.parseClaimsJws(token).getBody());
//		Claims claims=parseToken(token, "ys");
//		System.out.println(DateFormatUtils.format(claims.getExpiration(),"yyyy-MM-dd HH:mm:ss"));
//		System.out.println();
//		Key key = MacProvider.generateKey();
//
//		String compactJws = Jwts.builder()
//		  .setSubject("Joe")
////		  .setClaims(claims)
//          .setExpiration(DateUtils.addHours(new Date(), 5))
//		  .signWith(SignatureAlgorithm.HS512, key)
//		  .compact();
//		System.out.println(compactJws);
//		
//		try {
//		    Jws<Claims> claims = Jwts.parser()
//		        .requireSubject("Joe")
//		        .require("hasMotorcycle", true)
//		        .setSigningKey(key)
//		        .parseClaimsJws(compactJws);
//		    System.out.println("+++++++++++++++++");
//		    System.out.println(claims.getBody().toString());
//		} catch (MissingClaimException e) {
//		} catch (IncorrectClaimException e) {
//		}
	}

}
