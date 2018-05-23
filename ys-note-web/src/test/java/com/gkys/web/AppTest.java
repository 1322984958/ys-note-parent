package com.gkys.web;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.Base64;
import com.gkys.common.util.HttpUtils;  

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	private String MD5(String s) {
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
	 * Rigourous Test :-)
	 */
	public void testApp() throws Exception{
		assertTrue(true);
		String uri="http://api.xfyun.cn/v1/service/v1/tts";
		//创建默认的httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建post请求对象
        HttpPost httpPost = new HttpPost(uri);
        BASE64Encoder encoder = new BASE64Encoder();
        //装填请求参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("text", encoder.encode("科大讯飞是中国最大的智能语音技术提供商".getBytes("UTF-8")));
        for (Map.Entry<String, String> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        OrderHead o=new OrderHead();
        o.setAuf("audio/L16;rate=16000");
        o.setAue("raw");
        o.setVoice_name("xiaoyan");
        o.setSpeed("50");
        o.setVolume("50");
        o.setPitch("50");
        o.setEngine_type("intp65");
        o.setText_type("text");
        JSONObject headerJson=new JSONObject(false);
        headerJson.put("auf", "audio/L16;rate=16000");
        headerJson.put("aue", "raw");
        headerJson.put("voice_name", "xiaoyan");
        headerJson.put("speed", "50");
        headerJson.put("volume", "50");
        headerJson.put("pitch", "50");
        headerJson.put("engine_type", "intp65");
        headerJson.put("text_type", "text");
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
        System.out.println(fxParamJsonStr);
        String xParam = encoder.encode(JSONObject.toJSONString(headerJson).getBytes());
        System.out.println(xParam);
        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(list,"utf-8"));
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        httpPost.setHeader("X-Param", "eyJhdWYiOiAiYXVkaW8vTDE2O3JhdGU9MTYwMDAiLCJhdWUiOiAicmF3Iiwidm9pY2VfbmFtZSI6ICJ4aWFveWFuIiwic3BlZWQiOiAiNTAiLCJ2b2x1bWUiOiAiNTAiLCJwaXRjaCI6ICI1MCIsImVuZ2luZV90eXBlIjogImludHA2NSIsInRleHRfdHlwZSI6ICJ0ZXh0In0=");
        String appid="5ade9a17";
        httpPost.setHeader("X-Appid", appid);
        String curTime=String.valueOf(System.currentTimeMillis());
        httpPost.setHeader("X-CurTime", curTime);
        String checkSum=MD5(appid+curTime+xParam);
        httpPost.setHeader("X-CheckSum", checkSum);
//        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
      //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取所有的请求头信息
        Header[] allHeaders = response.getAllHeaders();
        for (Header allHeader : allHeaders) {
        	if("Content-Type:text/plain".equals(allHeader)){
        		System.out.println(allHeader.toString());
        	}
        }
        //获取结果实体
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            System.out.println(EntityUtils.toString(entity,"utf-8"));
        }
    	//关流
        EntityUtils.consume(entity);
        response.close();
	}
	
	
	public void test1() throws Exception{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = decoder.decodeBuffer("eyJhdWYiOiAiYXVkaW8vTDE2O3JhdGU9MTYwMDAiLCJhdWUiOiAicmF3Iiwidm9pY2VfbmFtZSI6ICJ4aWFveWFuIiwic3BlZWQiOiAiNTAiLCJ2b2x1bWUiOiAiNTAiLCJwaXRjaCI6ICI1MCIsImVuZ2luZV90eXBlIjogImludHA2NSIsInRleHRfdHlwZSI6ICJ0ZXh0In0=");
		System.out.println(new String(bytes, "UTF-8"));
		BASE64Encoder encoder = new BASE64Encoder();
//		System.out.println(encoder.encode(new String(bytes, "UTF-8").getBytes()));
		OrderHead o=new OrderHead();
        o.setAuf("audio/L16;rate=16000");
        o.setAue("raw");
        o.setVoice_name("xiaoyan");
        o.setSpeed("50");
        o.setVolume("50");
        o.setPitch("50");
        o.setEngine_type("intp65");
        o.setText_type("text");
        System.out.println(JSONObject.toJSONString(o));
        String x="{\"auf\": \"audio/L16;rate=16000\",\"aue\": \"raw\",\"voice_name\": \"xiaoyan\",\"speed\": \"50\",\"volume\": \"50\",\"pitch\": \"50\",\"engine_type\": \"intp65\",\"text_type\": \"text\"}";
        System.out.println(x);
        String xParam = encoder.encode(x.getBytes());
        System.out.println(xParam);
	}
	
	public void test2(){
		String xx="http://www.xfyun.cn/herapi/solution/synthesis?vcn=aisxping&vol=7&spd=medium&textPut=我是谁啊。&textType=cn";
		String yy=HttpUtils.doGet(xx);
		System.out.println(yy);
	}
	
	@JSONType(orders = { "auf", "aue", "voice_name","speed","volume","pitch","engine_type","text_type"})  
	public class OrderHead{
		private String auf;
		private String aue;
		private String voice_name;
		private String speed;
		private String volume;
		private String pitch;
		private String engine_type;
		private String text_type;
		public String getAuf() {
			return auf;
		}
		public void setAuf(String auf) {
			this.auf = auf;
		}
		public String getAue() {
			return aue;
		}
		public void setAue(String aue) {
			this.aue = aue;
		}
		public String getVoice_name() {
			return voice_name;
		}
		public void setVoice_name(String voice_name) {
			this.voice_name = voice_name;
		}
		public String getSpeed() {
			return speed;
		}
		public void setSpeed(String speed) {
			this.speed = speed;
		}
		public String getVolume() {
			return volume;
		}
		public void setVolume(String volume) {
			this.volume = volume;
		}
		public String getPitch() {
			return pitch;
		}
		public void setPitch(String pitch) {
			this.pitch = pitch;
		}
		public String getEngine_type() {
			return engine_type;
		}
		public void setEngine_type(String engine_type) {
			this.engine_type = engine_type;
		}
		public String getText_type() {
			return text_type;
		}
		public void setText_type(String text_type) {
			this.text_type = text_type;
		}
		
	}
}
