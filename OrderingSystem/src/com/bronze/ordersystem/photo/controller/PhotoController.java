package com.bronze.ordersystem.photo.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.photo.service.IPhotoService;

@Controller
@RequestMapping(value="photo")
public class PhotoController {
	
	@Resource
	private IPhotoService photoService;
	
	@RequestMapping(value="put", method=RequestMethod.POST, produces="text/html")
	@ResponseBody
	public String photoPut(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		MultipartFile multipartFile = request.getFile("detailphotos");
		MultipartFile multipartFile2 = request.getFile("generalphotos");
		return photoService.photoPut(request, multipartFile, multipartFile2);
	}
	
	@RequestMapping(value="get", method=RequestMethod.GET)
	@ResponseBody
	public void photoGet(HttpServletResponse response, @RequestParam(value="path") String path) {
		photoService.photoGet(response, CommonUtils.BASE64URLDecoder(path));
	}
}
