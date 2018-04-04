package com.nonobank.group.component.exception;


import com.nonobank.group.component.result.ResultCode;

public class GroupException extends RuntimeException {

	private static final long serialVersionUID = 2495559366495945814L;
	private int code;

	public GroupException(ResultCode resultCode) {
		super(resultCode.getMsg());
		this.code = resultCode.getCode();
	}
	
	public GroupException(int code, String msg){
		super(msg);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
