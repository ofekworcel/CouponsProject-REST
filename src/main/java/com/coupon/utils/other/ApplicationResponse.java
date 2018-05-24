package com.coupon.utils.other;

public class ApplicationResponse {

	private int code;
	private String message;

	public ApplicationResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ApplicationResponse() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
