package com.bronze.ordersystem.food.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FoodCategory")
public class FoodCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String foodid;
	private int categoryid;
	private int index;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if (id != null) {
			id = id.trim();
		}
		this.id = id;
	}
	public String getFoodid() {
		return foodid;
	}
	public void setFoodid(String foodid) {
		if (foodid != null) {
			foodid = foodid.trim();
		}
		this.foodid = foodid;
	}
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
