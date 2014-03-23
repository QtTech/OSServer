package com.bronze.ordersystem.photo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface IPhotoService {

	String photoPut(HttpServletRequest request, MultipartFile multipartFile, MultipartFile multipartFile2);

	void photoGet(HttpServletResponse response, String path);

}
