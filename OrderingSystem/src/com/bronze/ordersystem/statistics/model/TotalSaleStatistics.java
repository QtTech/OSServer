package com.bronze.ordersystem.statistics.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TotalSaleStatistics")
public class TotalSaleStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	private double totalsales;
	
	private double totalcustomers;
	
	private double totalaverage;

	public double getTotalsales() {
		return totalsales;
	}

	public void setTotalsales(double totalsales) {
		this.totalsales = totalsales;
	}

	public double getTotalcustomers() {
		return totalcustomers;
	}

	public void setTotalcustomers(double totalcustomers) {
		this.totalcustomers = totalcustomers;
	}

	public double getTotalaverage() {
		return totalaverage;
	}

	public void setTotalaverage(double totalaverage) {
		this.totalaverage = totalaverage;
	}
	
}
