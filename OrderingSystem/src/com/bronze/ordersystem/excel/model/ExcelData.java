package com.bronze.ordersystem.excel.model;

import java.util.List;

import com.bronze.ordersystem.statistics.model.DailySaleStatistics;
import com.bronze.ordersystem.statistics.model.DishStatistics;
import com.bronze.ordersystem.statistics.model.TotalSaleStatistics;

public class ExcelData {

	public ExcelData(List<DishStatistics> dsList, String starttime,
			String endtime) {
		this.dsList = dsList;
		this.starttime = starttime;
		this.endtime = endtime;
	}

	public ExcelData(List<DailySaleStatistics> dailySales,
			List<TotalSaleStatistics> totalSales, String starttime,
			String endtime) {
		this.dailySales = dailySales;
		this.totalSales = totalSales;
		this.starttime = starttime;
		this.endtime = endtime;
	}

	private List<DishStatistics> dsList;
	private String starttime;
	private String endtime;
	
	private List<DailySaleStatistics> dailySales;
	private List<TotalSaleStatistics> totalSales;
	
	public List<DishStatistics> getDsList() {
		return dsList;
	}

	public void setDsList(List<DishStatistics> dsList) {
		this.dsList = dsList;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public List<DailySaleStatistics> getDailySales() {
		return dailySales;
	}

	public void setDailySales(List<DailySaleStatistics> dailySales) {
		this.dailySales = dailySales;
	}

	public List<TotalSaleStatistics> getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(List<TotalSaleStatistics> totalSales) {
		this.totalSales = totalSales;
	}
	
}
