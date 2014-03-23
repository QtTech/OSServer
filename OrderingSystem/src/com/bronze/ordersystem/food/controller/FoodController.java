package com.bronze.ordersystem.food.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.bill.model.BillSpecialRemark;
import com.bronze.ordersystem.food.model.CategoryList;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodOrderable;
import com.bronze.ordersystem.food.model.FoodRecommend;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;
import com.bronze.ordersystem.food.model.SimpleFoodInfo;
import com.bronze.ordersystem.food.service.IFoodService;

@Controller
@RequestMapping(value="food")
public class FoodController {
	@Resource
	private IFoodService menuSettingService;
	
	@RequestMapping(value="querycategorylist", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryCategoryList() {
		return menuSettingService.queryCategoryList();
	}
	
	@RequestMapping(value="querycategorylistbyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryCategoryListByID(@RequestBody CategoryList cl) {
		return menuSettingService.queryCategoryListByID(cl);
	}
	
	@RequestMapping(value="querybillmarklist", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryBillmarkList() {
		return menuSettingService.queryBillmarkList();
	}
	
	@RequestMapping(value="querybillmarklistbyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryBillmarkListByID(@RequestBody BillSpecialRemark bsr) {
		return menuSettingService.queryBillmarkListByID(bsr);
	}
	
	@RequestMapping(value="querymenulist", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryMenuList() {
		return menuSettingService.queryMenuList();
	}
	
	@RequestMapping(value="querymenulistbyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryMenuListByName(@RequestBody FoodInfo fi) {
		return menuSettingService.queryMenuListByName(fi);
	}
	
	@RequestMapping(value="querymenulistbycid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryMenuListByCID(@RequestBody SimpleFoodInfo sfi) {
		return menuSettingService.queryMenuListByCID(sfi);
	}
	
	@RequestMapping(value="querysimplefoodlist", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querySimpleFoodList() {
		return menuSettingService.querySimpleFoodList();
	}
	
	@RequestMapping(value="queryfoodrecommendlist", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodrecommendList() {
		return menuSettingService.queryFoodrecommendList();
	}
	
	@RequestMapping(value="queryfoodrecommendlistbyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodrecommendListByName(@RequestBody FoodRecommend fr) {
		return menuSettingService.queryFoodrecommendListByName(fr);
	}
	
	@RequestMapping(value="queryordered", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryOrdered(@RequestBody List<FoodOrderable> foodlist) {
		return menuSettingService.queryOrdered(foodlist);
	}
	
	@RequestMapping(value="queryfoodmarklist", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodmarkList() {
		return menuSettingService.queryFoodmarkList();
	}
	
	@RequestMapping(value="queryfoodmarklistbyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodmarkListByName(@RequestBody FoodSpecialRemark fsr) {
		return menuSettingService.queryFoodmarkListByName(fsr);
	}
	
	@RequestMapping(value="ordered", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> foodOrdered(@RequestBody List<FoodOrderable> foodlist) {
		return menuSettingService.updateOrderable(foodlist);
	}
	
	@RequestMapping(value="queryfoodbyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodByID(@RequestBody FoodInfo fi) {
		return menuSettingService.queryFoodByID(fi);
	}
	
	@RequestMapping(value="queryfoodallinfobyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodAllInfoByID(@RequestBody FoodInfo fi) {
		return menuSettingService.queryFoodAllInfoByID(fi);
	}
	
	@RequestMapping(value="queryfoodrecommendname", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodRecommendName(@RequestBody FoodRecommend fr) {
		return menuSettingService.queryFoodRecommendName(fr);
	}
	
	@RequestMapping(value="queryfoodremarkname", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodRemarkName(@RequestBody FoodSpecialRemark fsr) {
		return menuSettingService.queryFoodRemarkName(fsr);
	}
	
	@RequestMapping(value="queryfoodnumbers", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodNumbers() {
		return menuSettingService.queryFoodNumbers();
	}
}
