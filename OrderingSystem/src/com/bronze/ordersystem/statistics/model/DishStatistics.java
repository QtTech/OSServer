package com.bronze.ordersystem.statistics.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DishStatistics")
public class DishStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	private String foodname;
	
	private int amount;

	public String getFoodname() {
		return foodname;
	}

	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
