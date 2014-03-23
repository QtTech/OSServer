package com.bronze.ordersystem.photo.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.OSConfiguration;
import com.bronze.ordersystem.food.dao.IFoodInfoDao;
import com.bronze.ordersystem.photo.service.IPhotoService;

@Service
public class PhotoService implements IPhotoService {
	private final static Logger logger = Logger.getLogger(PhotoService.class);

	@Resource
	private IFoodInfoDao foodInfoDao;

	@Override
	public String photoPut(HttpServletRequest request,
			MultipartFile multipartFile, MultipartFile multipartFile2) {
		String originalPath = "";
		String originalPath2 = "";
		long currentTime = 0;
		long currentTime2 = 0;
		
		String result = "";
		
		try {
			if (multipartFile != null) {
				currentTime = System.currentTimeMillis();
				originalPath = OSConfiguration.WebAppRoot + File.separator + OSConfiguration.TMPDirectory
						+ File.separator + String.valueOf(currentTime);
				FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(originalPath));
			}
			
			if (multipartFile2 != null) {
				currentTime2 = System.currentTimeMillis();
				originalPath2 = OSConfiguration.WebAppRoot + File.separator + OSConfiguration.TMPDirectory
						+ File.separator + String.valueOf(currentTime2);
				FileUtils.copyInputStreamToFile(multipartFile2.getInputStream(), new File(originalPath2));
			}
			
			if (multipartFile == null && multipartFile2 == null) {
				result = "{\"status\":1}";
			} else {
				if (multipartFile == null) {
					result = "{\"status\":0,\"GeneralPhotos\":\"" + 
					CommonUtils.BASE64URLEncoder(OSConfiguration.TMPDirectory + File.separator + String.valueOf(currentTime2)) +"\"}";
				} else if (multipartFile2 == null) {
					result = "{\"status\":0,\"DetailPhotos\":\"" + 
					CommonUtils.BASE64URLEncoder(OSConfiguration.TMPDirectory + File.separator + String.valueOf(currentTime)) +"\"}";
				} else {
					result = "{\"status\":0,\"DetailPhotos\":\"" + 
							CommonUtils.BASE64URLEncoder(OSConfiguration.TMPDirectory + File.separator + String.valueOf(currentTime)) +"\",\"GeneralPhotos\":\"" +
							CommonUtils.BASE64URLEncoder(OSConfiguration.TMPDirectory + File.separator + String.valueOf(currentTime2)) + "\"}";
				}
			}
		} catch (IllegalStateException e) {
			logger.error(e);
			FileUtils.deleteQuietly(new File(originalPath));
			FileUtils.deleteQuietly(new File(originalPath2));
			result = "{\"status\":3}";
		} catch (IOException e) {
			logger.error(e);
			FileUtils.deleteQuietly(new File(originalPath));
			FileUtils.deleteQuietly(new File(originalPath2));
			result = "{\"status\":3}";
		}
		return result;
	}

	@Override
	public void photoGet(HttpServletResponse response, String path) {
		logger.info("Path : " + path);
		File file = null;
		if (path.startsWith(OSConfiguration.TMPDirectory)) {
			file = new File(OSConfiguration.WebAppRoot + File.separator + path);
		} else {
			file = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator + path);
		}
		
		logger.info("File path : " + file.getPath());
		if (file.exists() && file.isFile()) {
			byte[] data = CommonUtils.getBytes(file);
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");  
			response.addHeader("Content-Length", "" + data.length);  
			response.setContentType("application/octet-stream;charset=UTF-8");  
			OutputStream outputStream;
			try {
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

}
