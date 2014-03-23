package com.bronze.ordersystem.food.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="InsertFoodInfo")
public class InsertFoodInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String number;
	private String description;
	private String details;
	private int default_count;
	private double price;
	private int cookby;
	private int orderable;
	private List<String> categorylist;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public int getDefault_count() {
		return default_count;
	}
	public void setDefault_count(int default_count) {
		this.default_count = default_count;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCookby() {
		return cookby;
	}
	public void setCookby(int cookby) {
		this.cookby = cookby;
	}
	public int getOrderable() {
		return orderable;
	}
	public void setOrderable(int orderable) {
		this.orderable = orderable;
	}
	public List<String> getCategorylist() {
		return categorylist;
	}
	public void setCategorylist(List<String> categorylist) {
		this.categorylist = categorylist;
	}
}
