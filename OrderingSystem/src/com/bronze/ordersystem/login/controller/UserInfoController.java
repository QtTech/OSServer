package com.bronze.ordersystem.login.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.login.model.UserInfo;
import com.bronze.ordersystem.login.service.IUserInfoService;

@Controller
@RequestMapping(value="user")
public class UserInfoController {
	
	@Resource
	private IUserInfoService userInfoService;
	
	@RequestMapping(value="query", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryUserInfo() {
		return userInfoService.queryUserInfo();
	}
	
	@RequestMapping(value="querybyname", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryUserInfoByName(@RequestBody UserInfo ui) {
		return userInfoService.queryUserInfoByName(ui);
	}
	
	@RequestMapping(value="add", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserInfo(@RequestBody UserInfo ui) {
		return userInfoService.addUserInfo(ui);
	}
	
	@RequestMapping(value="delete", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteUserInfo(@RequestBody UserInfo ui) {
		return userInfoService.deleteUserInfo(ui);
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateUserInfo(@RequestBody UserInfo ui) {
		return userInfoService.updateUserInfo(ui);
	}
	
	@RequestMapping(value="querybyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryUserInfoByID(@RequestBody UserInfo ui) {
		return userInfoService.queryUserInfoByID(ui);
	}
}
