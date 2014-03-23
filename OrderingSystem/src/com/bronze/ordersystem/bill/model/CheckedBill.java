package com.bronze.ordersystem.bill.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.bronze.ordersystem.common.util.OSConfiguration;

@XmlRootElement(name="CheckedBill")
public class CheckedBill implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String runningnumber;
	private int type;
	private double originalprice;
	private double discount;
	private double deduction;
	private double realprice;
	private String starttime;
	private String endtime;
	private String tableid;
	private String waitername;
	private int people;
	private String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRunningnumber() {
		return runningnumber;
	}
	public void setRunningnumber(String runningnumber) {
		this.runningnumber = runningnumber;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getOriginalprice() {
		return originalprice;
	}
	public void setOriginalprice(double originalprice) {
		this.originalprice = originalprice;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getDeduction() {
		return deduction;
	}
	public void setDeduction(double deduction) {
		this.deduction = deduction;
	}
	public double getRealprice() {
		return realprice;
	}
	public void setRealprice(double realprice) {
		this.realprice = realprice;
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
	public String getEndtime() {
		if (endtime != null && endtime.length() > OSConfiguration.MAX_TIMER_LENGTH) {
			return endtime.substring(0, OSConfiguration.MAX_TIMER_LENGTH);
		} else {
			return endtime;
		}
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	public String getWaitername() {
		return waitername;
	}
	public void setWaitername(String waitername) {
		this.waitername = waitername;
	}
	public int getPeople() {
		return people;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void copy(UncheckedBill ucb) {
		runningnumber = ucb.getRunningnumber();
		type = ucb.getType();
		originalprice = ucb.getOriginalprice();
		discount = ucb.getDiscount();
		deduction = ucb.getDeduction();
		realprice = ucb.getRealprice();
		starttime = ucb.getStarttime();
		tableid = ucb.getTableid();
		waitername = ucb.getWaitername();
		people = ucb.getPeople();
		remark = ucb.getRemark();
	}
}
