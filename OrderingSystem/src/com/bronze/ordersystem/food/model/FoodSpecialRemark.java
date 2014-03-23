package com.bronze.ordersystem.food.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FoodSpecialRemark")
public class FoodSpecialRemark implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String foodid;
	
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
	public String getFoodid() {
		return foodid;
	}
	public void setFoodid(String foodid) {
		if (foodid != null) {
			foodid = foodid.trim();
		}
		this.foodid = foodid;
	}
}
