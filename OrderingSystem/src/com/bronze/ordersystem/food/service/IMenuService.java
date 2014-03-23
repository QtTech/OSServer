package com.bronze.ordersystem.food.service;

import java.util.Map;

import com.bronze.ordersystem.bill.model.BillSpecialRemark;
import com.bronze.ordersystem.food.model.CategoryList;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodRecommend;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;
import com.bronze.ordersystem.food.model.InsertFoodInfo;
import com.bronze.ordersystem.food.model.SelectedDishes;



public interface IMenuService {

	Map<String, Object> generateConfiguration();

	Map<String, Object> insertCategory(CategoryList cl);

	Map<String, Object> updateCategory(CategoryList cl);

	Map<String, Object> deleteCategory(CategoryList cl);

	Map<String, Object> insertBillRemark(BillSpecialRemark bsr);

	Map<String, Object> deleteBillRemark(BillSpecialRemark bsr);

	Map<String, Object> updateBillRemark(BillSpecialRemark bsr);

	Map<String, Object> insertFoodRemark(FoodSpecialRemark fsr);

	Map<String, Object> deleteFoodRemark(FoodSpecialRemark fsr);

	Map<String, Object> insertFoodRecommend(FoodRecommend fr);

	Map<String, Object> deleteFoodRecommend(FoodRecommend fr);

	Map<String, Object> insertFood(InsertFoodInfo ifi);

	Map<String, Object> deleteFood(InsertFoodInfo ifi);

	Map<String, Object> updateFoodPhoto(FoodInfo ifi);

	Map<String, Object> updateFood(InsertFoodInfo ifi);

	Map<String, Object> updateFoodRecommend(FoodRecommend fr);

	Map<String, Object> updateFoodRemark(FoodSpecialRemark fsr);

	Map<String, Object> deleteSelectedDishes(SelectedDishes sds);

	Map<String, Object> updateDishesCategory(SelectedDishes sds);

	Map<String, Object> updateMenuOrders(SelectedDishes sds);

}
