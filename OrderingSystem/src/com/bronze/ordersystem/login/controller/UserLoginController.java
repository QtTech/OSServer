package com.bronze.ordersystem.login.controller;


import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.login.model.OpenTableInfo;
import com.bronze.ordersystem.login.model.UserInfo;
import com.bronze.ordersystem.login.service.IUserLoginService;


@Controller
@RequestMapping(value="user")
public class UserLoginController {
	
	@Resource
	private IUserLoginService userLoginService;

	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userLogin(@RequestBody UserInfo ui) {
		return userLoginService.userLogin(ui);
	}
	
	@RequestMapping(value="opentable", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> openTable(@RequestBody OpenTableInfo oti) {
		return userLoginService.openTable(oti);
	}
}
