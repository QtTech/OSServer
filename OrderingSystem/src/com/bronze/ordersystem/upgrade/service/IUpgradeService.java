package com.bronze.ordersystem.upgrade.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.bronze.ordersystem.upgrade.model.UpgradeCheckModel;

public interface IUpgradeService {

	Map<String, Object> checkUpgrade(UpgradeCheckModel ucm);

	void download(HttpServletResponse response, String filepath);

	Map<String, Object> getVersion();

}
