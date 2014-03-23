package com.bronze.ordersystem.food.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bronze.ordersystem.food.model.FoodOrderable;

public interface IFoodOrderableDao {

	int updateOrderable(FoodOrderable fo);
	
	List<FoodOrderable> queryFoodOrderableByFoodId(@Param(value = "foodid") String foodid);

	List<FoodOrderable> queryOrdered(List<FoodOrderable> foodlist);
	
	List<FoodOrderable> queryOrderedAll();

	int addNewFood(FoodOrderable fo);
}
