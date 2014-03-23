package com.bronze.ordersystem.bill.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="UpdatedTempBillDetails")
public class UpdatedTempBillDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private UncheckedBill tempbill;
	
	private List<UncheckedBillDetails> removedbills;
	
	private List<UncheckedBillDetails> updatedbills;

	public UncheckedBill getTempbill() {
		return tempbill;
	}

	public void setTempbill(UncheckedBill tempbill) {
		this.tempbill = tempbill;
	}

	public List<UncheckedBillDetails> getRemovedbills() {
		return removedbills;
	}

	public void setRemovedbills(List<UncheckedBillDetails> removedbills) {
		this.removedbills = removedbills;
	}

	public List<UncheckedBillDetails> getUpdatedbills() {
		return updatedbills;
	}

	public void setUpdatedbills(List<UncheckedBillDetails> updatedbills) {
		this.updatedbills = updatedbills;
	}
	
}
