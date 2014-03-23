package com.bronze.ordersystem.upgrade.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CheckUpdateResultInfo")
public class CheckUpdateResultInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String latestVersion;
	private List<String> filenames;
	
	public String getLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}
	public List<String> getFilenames() {
		return filenames;
	}
	public void setFilenames(List<String> filenames) {
		this.filenames = filenames;
	}
	
}
