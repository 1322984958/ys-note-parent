package com.gkys.common.exception;

import java.util.Map;

public class BaseMyValidateException extends RuntimeException {
	private Map<String,Object> result;
	
	public BaseMyValidateException(Map<String,Object> result) {
		this.result=result;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	
	public String printlnResult(){
		StringBuffer sb=new StringBuffer();
		for (String key:this.result.keySet()) {
			sb.append("key:").append(key).append(",value:").append(this.result.get(key));
		}
		return sb.toString();
	}
	
}
