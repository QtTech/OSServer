package com.bronze.ordersystem.attachment.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BillAttachment")
public class BillAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private int type;
	private double price;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if (id != null) {
			id = id.trim();
		}
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name != null) {
			name = name.trim();
		}
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
