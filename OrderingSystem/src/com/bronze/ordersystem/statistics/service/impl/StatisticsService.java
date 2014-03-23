package com.bronze.ordersystem.statistics.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.bill.dao.IBillDao;
import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.excel.ExcelHelper;
import com.bronze.ordersystem.excel.model.ExcelData;
import com.bronze.ordersystem.statistics.model.DishStatistics;
import com.bronze.ordersystem.statistics.model.DailySaleStatistics;
import com.bronze.ordersystem.statistics.model.StatisticsCondition;
import com.bronze.ordersystem.statistics.model.TotalSaleStatistics;
import com.bronze.ordersystem.statistics.service.IStatisticsService;

@Service
public class StatisticsService implements IStatisticsService {

	private static final Logger logger = Logger.getLogger(StatisticsService.class);
	
	@Resource
	private IBillDao billDao;
	
	@Override
	public Map<String, Object> queryDishStatistics(StatisticsCondition sc) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<DishStatistics> list = billDao.queryDishStatistics(sc);
			map.put(OSException.DATA, list);
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public int exportStatistics(HttpServletResponse response,
			StatisticsCondition sc, String lang, int type) {
		if (type == ExcelHelper.DISH_TYPE) {
			List<DishStatistics> list = billDao.queryDishStatistics(sc);
			ExcelData excelData = new ExcelData(list , sc.getStarttime(), sc.getEndtime());
			
			response.setContentType("application/octet-stream");
			SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss"); 
			response.setHeader("Content-Disposition", String.format("attachment;filename=Dish_Statistics_%s.xlsx", time.format(new Date())));
			Workbook excel = ExcelHelper.createExcel(excelData, ExcelHelper.DISH_TYPE, lang);

			try {
				excel.write(response.getOutputStream());
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		} else if (type == ExcelHelper.SALE_TYPE) {
			List<DailySaleStatistics> dailySales = billDao.querySaleStatistics(sc);
			List<TotalSaleStatistics> totalSales = billDao.queryTotalSaleStatistics(sc);
			
			ExcelData excelData = new ExcelData(dailySales, totalSales, sc.getStarttime(), sc.getEndtime());
			
			response.setContentType("application/octet-stream");
			SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss"); 
			response.setHeader("Content-Disposition", String.format("attachment;filename=Sale_Statistics_%s.xlsx", time.format(new Date())));
			Workbook excel = ExcelHelper.createExcel(excelData, ExcelHelper.SALE_TYPE, lang);

			try {
				excel.write(response.getOutputStream());
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public Map<String, Object> querySaleStatistics(StatisticsCondition sc) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<DailySaleStatistics> dailySales = billDao.querySaleStatistics(sc);
			List<TotalSaleStatistics> totalSales = billDao.queryTotalSaleStatistics(sc);
			map.put(OSException.STATUS, OSException.SUCCESS);
			map.put(OSException.OTHERS, ((totalSales != null && totalSales.size() > 0) ? totalSales.get(0) : new TotalSaleStatistics()));
			map.put(OSException.DATA, dailySales);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

}
