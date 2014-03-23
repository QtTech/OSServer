package com.bronze.ordersystem.statistics.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.bronze.ordersystem.statistics.model.StatisticsCondition;

public interface IStatisticsService {

	Map<String, Object> queryDishStatistics(StatisticsCondition sc);

	int exportStatistics(HttpServletResponse response,
			StatisticsCondition sc, String lang, int type);

	Map<String, Object> querySaleStatistics(StatisticsCondition sc);

}
