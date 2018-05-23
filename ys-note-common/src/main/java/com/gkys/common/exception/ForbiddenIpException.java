package com.gkys.common.exception;

/**
 * @author Administrator
 * ip禁用异常
 */
public class ForbiddenIpException extends RuntimeException{
	private String ip;
	
	public ForbiddenIpException(){
	}
	
	public ForbiddenIpException(String ip) {
		super(ip+"被禁用！！！");
		this.ip=ip;
	}

}
