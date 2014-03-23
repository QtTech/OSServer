package com.bronze.ordersystem.food.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SelectedDishes implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_COPY = 0;
	public static final int TYPE_MOVE = 1;
	
	private int type;
	
	private int categoryid;
	
	private int orgcategoryid;
	
	private List<String> dishes;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public List<String> getDishes() {
		return dishes;
	}

	public void setDishes(List<String> dishes) {
		this.dishes = dishes;
	}

	public int getOrgcategoryid() {
		return orgcategoryid;
	}

	public void setOrgcategoryid(int orgcategoryid) {
		this.orgcategoryid = orgcategoryid;
	}
	
}
