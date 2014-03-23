package com.bronze.ordersystem.statistics.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.statistics.model.StatisticsCondition;
import com.bronze.ordersystem.statistics.service.IStatisticsService;

@Controller
@RequestMapping(value="statistics")
public class StatisticsController {

	@Resource
	private IStatisticsService statisticsService;
	
	@RequestMapping(value="dishstatistics", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryDishStatistics(@RequestBody StatisticsCondition sc) {
		return statisticsService.queryDishStatistics(sc);
	}
	
	@RequestMapping(value="salestatistics", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querySaleStatistics(@RequestBody StatisticsCondition sc) {
		return statisticsService.querySaleStatistics(sc);
	}
	
	@RequestMapping(value="exportstatistics", method=RequestMethod.GET)
	@ResponseBody
	public int exportStatistics(HttpServletResponse response, 
			@RequestParam(value="lang") String lang,
			@RequestParam(value="type") int type,
			@RequestParam(value="starttime") String starttime,
			@RequestParam(value="endtime") String endtime) {
		
		return statisticsService.exportStatistics(response, new StatisticsCondition(starttime, endtime), lang, type);
	}
	
}
