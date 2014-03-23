package com.bronze.ordersystem.statistics.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="AllSaleStatistics")
public class AllSaleStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<DailySaleStatistics> dailySales;
	
	private TotalSaleStatistics totalSales;

	public List<DailySaleStatistics> getDailySales() {
		return dailySales;
	}

	public void setDailySales(List<DailySaleStatistics> dailySales) {
		this.dailySales = dailySales;
	}

	public TotalSaleStatistics getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(TotalSaleStatistics totalSales) {
		this.totalSales = totalSales;
	}
	
}
