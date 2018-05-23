package com.gkys.common.exception;

public class RepeatedSubmitFormException extends BaseException {

	private static final long serialVersionUID = 5341547962077784610L;

	public RepeatedSubmitFormException(String msg) {
		super(msg);
	}

	public RepeatedSubmitFormException(int code, String msg) {
		super(code, msg);
	}

	public RepeatedSubmitFormException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

	public RepeatedSubmitFormException() {
		super(2075, "当前页面已过期，请刷新页面重试");
	}
}
