package com.bronze.ordersystem.statistics.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DailySaleStatistics")
public class DailySaleStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	private String saledate;
	
	private double sales;
	
	private double customers;
	
	private double average;

	public String getSaledate() {
		return saledate;
	}

	public void setSaledate(String saledate) {
		this.saledate = saledate;
	}

	public double getSales() {
		return sales;
	}

	public void setSales(double sales) {
		this.sales = sales;
	}

	public double getCustomers() {
		return customers;
	}

	public void setCustomers(double customers) {
		this.customers = customers;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}
	
}
