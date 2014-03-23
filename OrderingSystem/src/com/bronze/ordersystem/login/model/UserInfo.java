package com.bronze.ordersystem.login.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="UserInfo")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int GUEST = 1;
	public static final int WAITER = 2;
	public static final int COOK = 3;
	public static final int BARTENDER = 4;
	public static final int CASHIER = 5;
	public static final int SUPERVISOR = 6;
	public static final int MANAGER = 7;
	
	private String username;
	private String password;
	private String code;
	private int category;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if (username != null) {
			username = username.trim();
		}
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if (password != null) {
			password = password.trim();
		}
		this.password = password;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		if (code != null) {
			code = code.trim();
		}
		this.code = code;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
}
