package com.bronze.ordersystem.food.controller;


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
import com.bronze.ordersystem.food.model.FoodRecommend;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;
import com.bronze.ordersystem.food.model.InsertFoodInfo;
import com.bronze.ordersystem.food.model.SelectedDishes;
import com.bronze.ordersystem.food.service.IMenuService;

@Controller
@RequestMapping(value="menuitem")
public class MenuController {
	
	@Resource
	private IMenuService menuService;

	@RequestMapping(value="generatenewversion", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> generateConfiguration() {
		return menuService.generateConfiguration();
	}
	
	@RequestMapping(value="insertcategory", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertCategory(@RequestBody CategoryList cl) {
		return menuService.insertCategory(cl);
	}
	
	@RequestMapping(value="deletecategory", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteCategory(@RequestBody CategoryList cl) {
		return menuService.deleteCategory(cl);
	}
	
	@RequestMapping(value="updatecategory", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateCategory(@RequestBody CategoryList cl) {
		return menuService.updateCategory(cl);
	}
	
	@RequestMapping(value="insertbillremark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertBillRemark(@RequestBody BillSpecialRemark bsr) {
		return menuService.insertBillRemark(bsr);
	}
	
	@RequestMapping(value="deletebillremark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteBillRemark(@RequestBody BillSpecialRemark bsr) {
		return menuService.deleteBillRemark(bsr);
	}
	
	@RequestMapping(value="updatebillremark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateBillRemark(@RequestBody BillSpecialRemark bsr) {
		return menuService.updateBillRemark(bsr);
	}
	
	@RequestMapping(value="insertfoodremark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertFoodRemark(@RequestBody FoodSpecialRemark fsr) {
		return menuService.insertFoodRemark(fsr);
	}
	
	@RequestMapping(value="updatefoodremark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateFoodRemark(@RequestBody FoodSpecialRemark fsr) {
		return menuService.updateFoodRemark(fsr);
	}
	
	@RequestMapping(value="deletefoodremark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteFoodRemark(@RequestBody FoodSpecialRemark fsr) {
		return menuService.deleteFoodRemark(fsr);
	}
	
	@RequestMapping(value="insertfoodrecommend", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertFoodRecommend(@RequestBody FoodRecommend fr) {
		return menuService.insertFoodRecommend(fr);
	}
	
	@RequestMapping(value="deletefoodrecommend", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteFoodRecommend(@RequestBody FoodRecommend fr) {
		return menuService.deleteFoodRecommend(fr);
	}
	
	@RequestMapping(value="updatefoodrecommend", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateFoodRecommend(@RequestBody FoodRecommend fr) {
		return menuService.updateFoodRecommend(fr);
	}
	
	@RequestMapping(value="insertfood", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertFood(@RequestBody InsertFoodInfo ifi) {
		return menuService.insertFood(ifi);
	}
	
	@RequestMapping(value="updatefoodphoto", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateFoodPhoto(@RequestBody FoodInfo fi) {
		return menuService.updateFoodPhoto(fi);
	}
	
	@RequestMapping(value="deletefood", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteFood(@RequestBody InsertFoodInfo ifi) {
		return menuService.deleteFood(ifi);
	}

	@RequestMapping(value="updatefood", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateFood(@RequestBody InsertFoodInfo ifi) {
		return menuService.updateFood(ifi);
	}
	
	@RequestMapping(value="updateDishesCategory", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateDishesCategory(@RequestBody SelectedDishes sds) {
		return menuService.updateDishesCategory(sds);
	}
	
	@RequestMapping(value="deleteSelectedDishes", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteSelectedDishes(@RequestBody SelectedDishes sds) {
		return menuService.deleteSelectedDishes(sds);
	}
	
	@RequestMapping(value="updatemenuorders", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateMenuOrders(@RequestBody SelectedDishes sds) {
		return menuService.updateMenuOrders(sds);
	}
}
