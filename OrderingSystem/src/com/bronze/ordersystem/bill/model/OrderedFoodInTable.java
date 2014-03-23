package com.bronze.ordersystem.bill.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OrderedFoodInTable")
public class OrderedFoodInTable implements Serializable {

	private static final long serialVersionUID = 1L;

	private UncheckedBill tempbill;
	private List<UncheckedBillDetails> tempbilldetails;
	
	public UncheckedBill getTempbill() {
		return tempbill;
	}
	public void setTempbill(UncheckedBill tempbill) {
		this.tempbill = tempbill;
	}
	public List<UncheckedBillDetails> getTempbilldetails() {
		return tempbilldetails;
	}
	public void setTempbilldetails(List<UncheckedBillDetails> tempbilldetails) {
		this.tempbilldetails = tempbilldetails;
	}

}
