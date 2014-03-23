package com.bronze.ordersystem.food.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CategoryList")
public class CategoryList implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
