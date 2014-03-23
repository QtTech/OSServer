package com.bronze.ordersystem.statistics.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="StatisticsCondition")
public class StatisticsCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	public StatisticsCondition() {

	}
	
	public StatisticsCondition(String starttime, String endtime) {
		if (starttime != null && starttime.equals("")) {
			starttime = null;
		}
		
		if (endtime != null && endtime.equals("")) {
			endtime = null;
		}
		
		this.starttime = starttime;
		this.endtime = endtime;
	}

	private String starttime;
	
	private String endtime;
	
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
	
}
