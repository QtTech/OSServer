package com.bronze.ordersystem.food.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FoodRecommendWithName")
public class FoodRecommendWithName implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String foodid;
	private String foodname;
	private String recommendid;
	private String recommendname;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFoodid() {
		return foodid;
	}
	public void setFoodid(String foodid) {
		this.foodid = foodid;
	}
	public String getRecommendid() {
		return recommendid;
	}
	public void setRecommendid(String recommendid) {
		this.recommendid = recommendid;
	}
	public String getFoodname() {
		return foodname;
	}
	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}
	public String getRecommendname() {
		return recommendname;
	}
	public void setRecommendname(String recommendname) {
		this.recommendname = recommendname;
	}
}

