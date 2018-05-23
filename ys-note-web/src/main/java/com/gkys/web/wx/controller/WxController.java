package com.gkys.web.wx.controller;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gkys.common.base.BaseResult;
import com.gkys.common.util.HttpUtils;
import com.gkys.common.util.PropertiesFileUtil;
import com.gkys.web.util.XfYunUtils;

import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.user.User;
import weixin.popular.support.TicketManager;
import weixin.popular.support.TokenManager;
import weixin.popular.util.JsUtil;
import weixin.popular.util.SignatureUtil;

@Controller
@RequestMapping(value = "wx/")
public class WxController {
	private Logger logger=LoggerFactory.getLogger(getClass());
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	
	@RequestMapping(value = "/bind.do",method = RequestMethod.GET)
	public void bindGet(Model model, HttpServletRequest request) {
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		String signature = request.getParameter("signature");
		String token = "ysysys";
		String content=timestamp+nonce+token;
		try {
			String result=DigestUtils.shaHex(content);
            if(signature.equals(result)){
            	PrintWriter printWriter = response.getWriter();
    			printWriter.print(echostr);
    			printWriter.flush();
    			printWriter.close();
            	return;
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	/**
	 * 签名
	 * @param content
	 * @return
	 *//*
	public String getSign(String content){ 
		try {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");  
	        crypt.reset();  
	        crypt.update(content.getBytes("UTF-8"));  
	        String result = byteToHex(crypt.digest());
	        return result;
	    } catch (Exception e) {  
	        logger.error("签名错误：",e);
	    }
		return "";
	}
	
	private String byteToHex(final byte[] hash) {  
        Formatter formatter = new Formatter();  
        for (byte b : hash) {  
            formatter.format("%02x", b);  
        }  
        String result = formatter.toString();  
        formatter.close();  
        return result;  
    }*/
	
	@RequestMapping(value = "/index.html")
	public String indexHtml(String code,Model model, HttpServletRequest request) throws Exception {
		//测试注释 上线前放开
		User wxUser=null;
		String appid=PropertiesFileUtil.getInstance("properties/application").get("wx.appid");
		String appsecret=PropertiesFileUtil.getInstance("properties/application").get("wx.appsecret");
		if(StringUtils.isBlank(code)){
			String query = "";
		     if(request.getQueryString()!=null && !"".equals(request.getQueryString())){
		    	 query = "?" + request.getQueryString();
		     }
		     String url = (request.getRequestURL() + query).toString();
		     String fullurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+URLEncoder.encode(url,"UTF-8")+"&response_type=code&scope=snsapi_userinfo&state=state#wechat_redirect";
		     response.sendRedirect(fullurl);
			 return "";
		}else{
			SnsToken token = SnsAPI.oauth2AccessToken(appid, appsecret, code);
			logger.error("微信获得----accesstoken：{},openid：{}",token.getAccess_token(),token.getOpenid());
			wxUser = SnsAPI.userinfo(token.getAccess_token(), token.getOpenid(), "zh_CN");
			String indexUrl="http://127.0.0.1:8080/wcManagementWx?id="+wxUser.getOpenid();
			response.sendRedirect(indexUrl);
			return "";
		}
		//测试放开 上线前注释
//		return "";
	}
	
	@RequestMapping(value = "/jsConfig")
	@ResponseBody
	public BaseResult jsConfig(String url){
//		String jsapi_ticket="ticket";
//		String url=request.getRequestURL().toString();
//		String access_token="";
//		Ticket t=TicketAPI.ticketGetticket(TokenManager.getDefaultToken());
//		logger.error("Ticket:{}",t.getTicket());
		logger.error("token:"+TokenManager.getDefaultToken());
		String jsapi_ticket=TicketManager.getDefaultTicket();
		logger.error("jsapi_ticket:"+jsapi_ticket);
		Map<String, String> params = new HashMap<String, String>();
		Long timestamp=new Date().getTime();
		String timestampStr=timestamp.toString().substring(0,10);
		String noncestr=getWeiXinRandom();
        params.put("jsapi_ticket", jsapi_ticket);
        params.put("noncestr", noncestr);
        params.put("timestamp", timestampStr);
        params.put("url", url);
        String sign=JsUtil.generateConfigSignature(noncestr, jsapi_ticket, timestampStr, url);
		params.put("sign", sign);
		System.out.println(sign);
		System.out.println(params);
		return BaseResult.success(params);
	}
	
	public static String getWeiXinRandom(){
		StringBuffer random=new StringBuffer();
		for (int i = 0; i < 10; i++) {
			char c=(char)(int)(Math.random()*26+97);
			random.append(c);
		}
		return random.toString();
	}
	
	@PostMapping(value = "/getVoiceSrc")
	@ResponseBody
	public JSONObject getVoiceSrc(@RequestParam(required=true) String text){
		try {
			JSONObject jSONObject = XfYunUtils.tts(text);
			return jSONObject;
		} catch (Exception e) {
			logger.error("获取接口出错", e);
		}
//		String url="http://www.xfyun.cn/herapi/solution/synthesis?vcn=aisxping&vol=7&spd=medium&textPut="+text+"&textType=cn";
//		String result=HttpUtils.doGet(url);
		return JSONObject.parseObject("");
	}
	
	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		Long timestamp=new Date().getTime();
		String timestampStr=timestamp.toString().substring(0,10);
		String noncestr=getWeiXinRandom();
        params.put("jsapi_ticket", "kgt8ON7yVITDhtdwci0qefhCFZnc1er3BlulKD4-kUpsiQWHK7dP78-voxz_ZKIgVKPrATi2oGDvukxVRhqZuw");
        params.put("noncestr", "grhnbnispa");
        params.put("timestamp", timestampStr);
        params.put("url", "http://192.168.1.88:8090/wcmanagementwx/");
        String content="jsapi_ticket=kgt8ON7yVITDhtdwci0qefhCFZnc1er3BlulKD4-kUpsiQWHK7dP78-voxz_ZKIgVKPrATi2oGDvukxVRhqZuw&noncestr=grhnbnispa&timestamp="+timestampStr+"&url=http://192.168.1.88:8090/wcmanagementwx/";
        try {
        	System.out.println(content);
            String result = DigestUtils.shaHex(content);
            System.out.println(result);
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		String sign=SignatureUtil.generateSign(params, "");
		params.put("sign", sign);
		System.out.println(sign);
		System.out.println(params);
		
//		WxController wx=new WxController();
//		System.out.println(wx.jsConfig());
		
		/*String signature="f5ae2aa103120a34699f2062cd522c7eadc18d07";
		String echostr="9573509493868920831";
		String timestamp="1524641527";
		String nonce="294243903";
		String token="ysysys";
		try {
			String content=timestamp+nonce+token;
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");  
            crypt.reset();  
            crypt.update(content.getBytes("UTF-8"));  
            String result = byteToHex(crypt.digest());
            System.out.println(result);
        } catch (Exception e) {  
            e.printStackTrace();  
        }*/
	}
}
