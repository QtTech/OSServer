package com.bronze.ordersystem.login.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ResultUserInfo")
public class ResultUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
