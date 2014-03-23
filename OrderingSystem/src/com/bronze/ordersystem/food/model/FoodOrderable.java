package com.bronze.ordersystem.food.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FoodOrderable")
public class FoodOrderable implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String foodid;
	private int orderable;
	
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
	public int getOrderable() {
		return orderable;
	}
	public void setOrderable(int orderable) {
		this.orderable = orderable;
	}
}
