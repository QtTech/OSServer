package com.bronze.ordersystem.bill.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.bronze.ordersystem.common.util.OSConfiguration;

@XmlRootElement(name="AllCookingInfo")
public class AllCookingInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String billtempid;
	private int type;
	private String foodid;
	private String foodname;
	private int amount;
	private double price;
	private int state;
	private String remark;
	private String billremark;
	private String starttime;
	private String tableid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBilltempid() {
		return billtempid;
	}
	public void setBilltempid(String billtempid) {
		this.billtempid = billtempid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFoodid() {
		return foodid;
	}
	public void setFoodid(String foodid) {
		this.foodid = foodid;
	}
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBillremark() {
		return billremark;
	}
	public void setBillremark(String billremark) {
		this.billremark = billremark;
	}
	public String getStarttime() {
		if (starttime != null && starttime.length() > OSConfiguration.MAX_TIMER_LENGTH) {
			return starttime.substring(0, OSConfiguration.MAX_TIMER_LENGTH);
		} else {
			return starttime;
		}
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
}
