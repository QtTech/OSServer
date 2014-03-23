package com.bronze.ordersystem.upgrade.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="UpgradeCheckModel")
public class UpgradeCheckModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String currentVersion;

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
}
