package com.bronze.ordersystem.operate.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.bill.model.TableInfo;
import com.bronze.ordersystem.operate.service.IOperateService;

@Controller
@RequestMapping(value="operate")
public class OperateController {

	@Resource
	private IOperateService operateService;
	
	@RequestMapping(value="callwaiter", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> callWaiter(@RequestBody TableInfo ti) {
		return operateService.callWaiter(ti);
	}
	
	@RequestMapping(value="answercall", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> answerCall(@RequestBody TableInfo ti) {
		return operateService.answerCall(ti);
	}
}
