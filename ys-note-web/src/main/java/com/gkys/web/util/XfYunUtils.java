package com.gkys.web.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import sun.misc.BASE64Encoder;

/**
 * @author Administrator
 * 讯飞工具类
 */
public class XfYunUtils {
	private static final Logger logger= LoggerFactory.getLogger(XfYunUtils.class);
	
	public static final String APPID= "5ae1fd94";
	public static final String APIKey= "82a68adce3159b06d59275e4f62a5d6c";
	public static final String TTS_URI= "http://api.xfyun.cn/v1/service/v1/tts";
	
	private static String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	private static String toHex(byte[] bytes) {
	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	/**
	 * 语音合成
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject tts(String text) throws Exception{
		//创建默认的httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建post请求对象
        HttpPost httpPost = new HttpPost(TTS_URI);
        BASE64Encoder encoder = new BASE64Encoder();
        //装填请求参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("text",text));
        String fxParamJsonStr="{\n"+
    			"    \"auf\": \"audio/L16;rate=16000\",\n"+
				"    \"aue\": \"raw\",\n"+
				"    \"voice_name\": \"xiaoyan\",\n"+
				"    \"speed\": \"50\",\n"+
			    "    \"volume\": \"50\",\n"+
			    "    \"pitch\": \"50\",\n"+
			    "    \"engine_type\": \"intp65\",\n"+
			    "    \"text_type\": \"text\"\n"+
		    	"}";
        String xParam = encoder.encode(JSONObject.toJSONString(fxParamJsonStr).getBytes());
        
      //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(list,"utf-8"));
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        httpPost.setHeader("X-Param", xParam);
        httpPost.setHeader("X-Appid", APPID);
        String curTime=String.valueOf(System.currentTimeMillis());
        httpPost.setHeader("X-CurTime", curTime);
        String checkSum=MD5(APPID+curTime+xParam);
        httpPost.setHeader("X-CheckSum", checkSum);
//        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
      //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取所有的请求头信息
        Header[] allHeaders = response.getAllHeaders();
        for (Header allHeader : allHeaders) {
        	if("Content-Type:text/plain".equals(allHeader)){
        		logger.debug(allHeader.toString());
        	}
        }
        //获取结果实体
        HttpEntity entity = response.getEntity();
        JSONObject result = new JSONObject();
        if (entity != null) {
        	result.parseObject(EntityUtils.toString(entity,"utf-8"));
        }
    	//关流
        EntityUtils.consume(entity);
        response.close();
		return result;
	}
}
