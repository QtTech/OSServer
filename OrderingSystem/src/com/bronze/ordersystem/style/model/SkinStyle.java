package com.bronze.ordersystem.style.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SkinStyle")
public class SkinStyle implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int skin;

	public int getSkin() {
		return skin;
	}

	public void setSkin(int skin) {
		this.skin = skin;
	}
	
}
