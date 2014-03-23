package com.bronze.ordersystem.upgrade.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.upgrade.model.UpgradeCheckModel;
import com.bronze.ordersystem.upgrade.service.IUpgradeService;

@Controller
@RequestMapping(value="upgrade")
public class UpgradeController {

	@Resource
	private IUpgradeService upgradeService;
	
	@RequestMapping(value="check", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkUpgrade(@RequestBody UpgradeCheckModel ucm) {
		return upgradeService.checkUpgrade(ucm);
	}
	
	@RequestMapping(value="getversion", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getVersion() {
		return upgradeService.getVersion();
	}
	
	@RequestMapping(value="download", method=RequestMethod.POST)
	@ResponseBody
	public void download(HttpServletResponse response, @RequestParam(value="filepath", required=true) String filepath) {
		try {
			upgradeService.download(response, URLDecoder.decode(filepath, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
