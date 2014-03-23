package com.bronze.ordersystem.bill.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TableInfo")
public class TableInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private int cookby;
	
	private String billid;

	private List<String> ids;
	
	private String newid;
	
	private String waitername;

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCookby() {
		return cookby;
	}

	public void setCookby(int cookby) {
		this.cookby = cookby;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getNewid() {
		return newid;
	}

	public void setNewid(String newid) {
		this.newid = newid;
	}

	public String getWaitername() {
		return waitername;
	}

	public void setWaitername(String waitername) {
		this.waitername = waitername;
	}
	
}
