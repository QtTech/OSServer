package com.bronze.ordersystem.bill.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BillSpecialRemark")
public class BillSpecialRemark implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	
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
}
