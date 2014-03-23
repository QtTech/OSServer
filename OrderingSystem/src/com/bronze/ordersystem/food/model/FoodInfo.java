package com.bronze.ordersystem.food.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FoodInfo")
public class FoodInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int COOKBYKITCHEN = 0;
	public static final int COOKBYWATERBAR = 1;
	
	private String id;
	private String name;
	private String number;
	private String description;
	private String details;
	private int default_count;
	private double price;
	private String largepic;
	private String smallpic;
	private int cookby;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		if (description != null) {
			description = description.trim();
		}
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getLargepic() {
		return largepic;
	}
	public void setLargepic(String largepic) {
		if (largepic != null) {
			largepic = largepic.trim();
		}
		this.largepic = largepic;
	}
	public String getSmallpic() {
		return smallpic;
	}
	public void setSmallpic(String smallpic) {
		if (smallpic != null) {
			smallpic = smallpic.trim();
		}
		this.smallpic = smallpic;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		if (number != null) {
			number = number.trim();
		}
		this.number = number;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		if (details != null) {
			details = details.trim();
		}
		this.details = details;
	}
	public int getDefault_count() {
		return default_count;
	}
	public void setDefault_count(int default_count) {
		this.default_count = default_count;
	}
	public int getCookby() {
		return cookby;
	}
	public void setCookby(int cookby) {
		this.cookby = cookby;
	}
}
