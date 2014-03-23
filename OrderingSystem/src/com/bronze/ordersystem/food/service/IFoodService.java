package com.bronze.ordersystem.food.service;

import java.util.List;
import java.util.Map;

import com.bronze.ordersystem.bill.model.BillSpecialRemark;
import com.bronze.ordersystem.food.model.CategoryList;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodOrderable;
import com.bronze.ordersystem.food.model.FoodRecommend;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;
import com.bronze.ordersystem.food.model.SimpleFoodInfo;


public interface IFoodService {

	Map<String, Object> queryCategoryList();

	Map<String, Object> updateOrderable(List<FoodOrderable> foodlist);

	Map<String, Object> queryOrdered(List<FoodOrderable> foodlist);

	Map<String, Object> queryBillmarkList();

	Map<String, Object> queryMenuList();

	Map<String, Object> queryFoodmarkList();

	Map<String, Object> querySimpleFoodList();

	Map<String, Object> queryFoodrecommendList();

	Map<String, Object> queryFoodByID(FoodInfo fi);

	Map<String, Object> queryFoodAllInfoByID(FoodInfo fi);

	Map<String, Object> queryCategoryListByID(CategoryList cl);

	Map<String, Object> queryFoodrecommendListByName(FoodRecommend fr);

	Map<String, Object> queryBillmarkListByID(BillSpecialRemark bsr);

	Map<String, Object> queryMenuListByName(FoodInfo fi);

	Map<String, Object> queryFoodmarkListByName(FoodSpecialRemark fsr);

	Map<String, Object> queryFoodRecommendName(FoodRecommend fr);

	Map<String, Object> queryFoodRemarkName(FoodSpecialRemark fsr);

	Map<String, Object> queryMenuListByCID(SimpleFoodInfo sfi);

	Map<String, Object> queryFoodNumbers();

}
