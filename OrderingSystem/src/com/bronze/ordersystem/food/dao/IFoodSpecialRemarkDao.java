package com.bronze.ordersystem.food.dao;

import java.util.List;

import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodRemarkWithName;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;

public interface IFoodSpecialRemarkDao {

	List<FoodRemarkWithName> queryFoodSpecialRemark();
	
	List<FoodSpecialRemark> queryFoodSpecialRemarkByFoodId(FoodInfo fi);

	int deleteFoodRemark(FoodSpecialRemark fsr);

	int insertFoodRemark(FoodSpecialRemark fsr);

	List<FoodRemarkWithName> queryFoodmarkListByName(FoodSpecialRemark fsr);

	List<FoodRemarkWithName> queryFoodRemarkName(FoodSpecialRemark fsr);

	int updateFoodRemark(FoodSpecialRemark fsr);
	
}
