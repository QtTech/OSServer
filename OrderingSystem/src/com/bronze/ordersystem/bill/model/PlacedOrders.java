package com.bronze.ordersystem.bill.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PlacedOrders")
public class PlacedOrders implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tableid;
	private String billremark;
	private List<UncheckedBillDetails> detailedorders;

	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	public String getBillremark() {
		return billremark;
	}
	public void setBillremark(String billremark) {
		this.billremark = billremark;
	}
	public List<UncheckedBillDetails> getDetailedorders() {
		return detailedorders;
	}
	public void setDetailedorders(List<UncheckedBillDetails> detailedorders) {
		this.detailedorders = detailedorders;
	}
}
