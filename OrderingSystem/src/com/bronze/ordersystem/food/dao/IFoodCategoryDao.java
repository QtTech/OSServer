package com.bronze.ordersystem.food.dao;

import java.util.List;

import com.bronze.ordersystem.food.model.FoodCategory;
import com.bronze.ordersystem.food.model.FoodInfo;

public interface IFoodCategoryDao {

	List<FoodCategory> queryFoodCategory();
	
	List<FoodCategory> queryFoodCategoryByFoodId(FoodInfo fi);

	int addCategoryList(List<FoodCategory> list);

	int deleteCategoryByFoodID(FoodInfo fi);
	
	List<String> queryFoodCategoryByFoodID(FoodInfo fi);

	List<FoodCategory> queryCategoryByCID(FoodCategory fc);
	
	int deleteCategoryByCID(FoodCategory fc);

	int updateIndex(FoodCategory fc);

	List<String> queryCategoryIndex(FoodCategory fc);
}
