package com.bronze.ordersystem.food.dao;

import java.util.List;

import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.SimpleFoodInfo;


public interface IFoodInfoDao {

	int addNewFood(FoodInfo foodinfo);

	int removeFood(FoodInfo foodinfo);

	List<FoodInfo> queryFood(FoodInfo foodinfo);

	int updateFood(FoodInfo foodinfo);

	List<SimpleFoodInfo> querySimpleFoodList();

	int updateFoodPhoto(FoodInfo fi);

	List<SimpleFoodInfo> queryFoodNameMap(List<String> idlist);

	List<FoodInfo> queryMenuListByName(FoodInfo fi);

	List<FoodInfo> queryMenuListByCID(SimpleFoodInfo sfi);

	List<String> queryFoodNumber();

	List<FoodInfo> queryFoodWithoutCategory();

}
