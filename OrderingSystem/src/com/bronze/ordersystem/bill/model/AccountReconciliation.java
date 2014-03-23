package com.bronze.ordersystem.bill.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="AccountReconciliation")
public class AccountReconciliation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String pic;
	private String time;
	private double receivables;
	private double paidinCapital;
	private double balance;
	private int currentEdit;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if (id != null) {
			id = id.trim();
		}
		this.id = id;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		if (pic != null) {
			pic = pic.trim();
		}
		this.pic = pic;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getReceivables() {
		return receivables;
	}
	public void setReceivables(double receivables) {
		this.receivables = receivables;
	}
	public double getPaidinCapital() {
		return paidinCapital;
	}
	public void setPaidinCapital(double paidinCapital) {
		this.paidinCapital = paidinCapital;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getCurrentEdit() {
		return currentEdit;
	}
	public void setCurrentEdit(int currentEdit) {
		this.currentEdit = currentEdit;
	}
}
