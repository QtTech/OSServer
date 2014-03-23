package com.bronze.ordersystem.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MQTTConfig {

	private final static ApplicationContext AC = new ClassPathXmlApplicationContext("classpath:/config/applicationContext.xml");
	private final static MQTTConfig mqttConfig = (MQTTConfig) AC.getBean("mqttconfig");
	
	private String host;
	private String port;
	private String admin;
	private String password;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public static MQTTConfig getConfig() {
		return mqttConfig;
	}
}
