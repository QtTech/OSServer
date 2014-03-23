package com.bronze.ordersystem.upgrade.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.OSConfiguration;
import com.bronze.ordersystem.upgrade.model.CheckUpdateResultInfo;
import com.bronze.ordersystem.upgrade.model.UpgradeCheckModel;
import com.bronze.ordersystem.upgrade.service.IUpgradeService;

@Service
public class UpgradeService implements IUpgradeService {
	
	protected final static Logger logger = Logger.getLogger(UpgradeService.class);

	@Override
	public Map<String, Object> checkUpgrade(UpgradeCheckModel ucm) {
		logger.info("Latest version : " + CommonUtils.getLatestVersion());
		Map<String, Object> map = new Hashtable<String, Object>();
		boolean islatest = CommonUtils.needUpgrade(ucm.getCurrentVersion());
		if (!islatest) {
			map.put(OSException.STATUS, OSException.SUCCESS);
		} else {
			List<String> list = new ArrayList<String>();
			list = CommonUtils.refreshFileList(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.VersionDirectory);
			CheckUpdateResultInfo curi = new CheckUpdateResultInfo();
			curi.setFilenames(list);
			curi.setLatestVersion(CommonUtils.getLatestVersion());
			map.put(OSException.DATA, curi);
			map.put(OSException.STATUS, OSException.FAILURE);
		}
		return map;
	}

	@Override
	public void download(HttpServletResponse response, String filepath) {
		File file = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.VersionDirectory + File.separator + filepath);
		logger.info("File path : " + file.getPath());
		if (file.exists() && file.isFile()) {
			byte[] data = CommonUtils.getBytes(file);
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");  
			response.addHeader("Content-Length", "" + data.length);  
			response.setContentType("application/octet-stream;charset=UTF-8");  
			OutputStream outputStream;
			try {
				logger.info("Downloading File " + file.getName());
				outputStream = new BufferedOutputStream(response.getOutputStream());
				outputStream.write(data);
				outputStream.flush();  
				outputStream.close();  
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}  
		}
	}

	@Override
	public Map<String, Object> getVersion() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			logger.info("Latest version : " + CommonUtils.getLatestVersion());
			map.put(OSException.DATA, CommonUtils.getLatestVersion());
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.FAILURE);
		}
		return map;
	}

}
