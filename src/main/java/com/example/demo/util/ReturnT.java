package com.example.demo.util;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;


/**
 * common return
 *
 * @author xuxueli 2015-12-4 16:32:31
 * @param <T>
 */
public class ReturnT<T> implements Serializable {
	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;
	public static final int FAIL_CODE = 500;
	public static final ReturnT<Object> SUCCESS = new ReturnT<Object>(null);
	public static final ReturnT<Object> FAIL = new ReturnT<Object>(FAIL_CODE, null);
	
	private int code;
	private String msg;
	private T data;
	
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ReturnT(int code, String msg,T data) {
		this.code = code;
		this.msg = msg;
		this.data=data;
	}
	public ReturnT(T data) {
		this.code = SUCCESS_CODE;
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
