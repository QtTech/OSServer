package com.bronze.ordersystem.common.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.OSConfiguration;

public class PhotoDirectoryListener implements ServletContextListener {

	private static Logger logger = Logger.getLogger(PhotoDirectoryListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		File rootdir = new File(OSConfiguration.WebAppRoot);
		if (rootdir.exists() && !rootdir.isDirectory()) {
			logger.info("Delete error Directory ROOT/OrderingSystem");
			rootdir.delete();
		} 
		
		if (!rootdir.exists()) {
			logger.info("Create Directory ROOT/OrderingSystem");
			rootdir.mkdirs();
		} else {
			logger.info("Directory ROOT/OrderingSystem exists");
		}
		
		if (!CommonUtils.SkinIsInitialized()) {
			CommonUtils.setSkinStyle(OSConfiguration.SKIN_CHINESE);
		}
		
		if (!CommonUtils.FoodIDIsInitialized()) {
			CommonUtils.setFoodID(0);
		}
	}

}
