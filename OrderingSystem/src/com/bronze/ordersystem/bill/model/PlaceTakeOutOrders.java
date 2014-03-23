package com.bronze.ordersystem.bill.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PlaceTakeOffOrders")
public class PlaceTakeOutOrders implements Serializable {

	private static final long serialVersionUID = 1L;

	private String billremark;
	private String waitername;
	private List<UncheckedBillDetails> detailedorders;

	public String getWaitername() {
		return waitername;
	}
	public void setWaitername(String waitername) {
		this.waitername = waitername;
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
