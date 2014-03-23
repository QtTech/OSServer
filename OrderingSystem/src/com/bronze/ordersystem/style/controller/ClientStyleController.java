package com.bronze.ordersystem.style.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.style.model.SkinStyle;
import com.bronze.ordersystem.style.service.IClientStyleService;

@Controller
@RequestMapping(value="clientstyle")
public class ClientStyleController {
	
	@Resource
	private IClientStyleService clientStyleService;
	
	@RequestMapping(value="getskinstyle", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getSkinStyle() {
		return clientStyleService.getSkinStyle();
	}
	
	@RequestMapping(value="setskinstyle", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setSkinStyle(@RequestBody SkinStyle ss) {
		return clientStyleService.setSkinStyle(ss);
	}
}
