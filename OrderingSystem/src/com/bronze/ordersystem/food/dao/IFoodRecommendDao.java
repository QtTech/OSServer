package com.bronze.ordersystem.food.dao;

import java.util.List;

import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodRecommend;

public interface IFoodRecommendDao {

	List<FoodRecommend> queryFoodRecommend();
	
	List<FoodRecommend> queryFoodRecommendByFoodId(FoodInfo fi);

	List<FoodRecommend> queryFoodrecommendList();

	int insertFoodRemark(FoodRecommend fr);

	int deleteFoodRemark(FoodRecommend fr);

	List<FoodRecommend> queryFoodrecommendListByName(FoodRecommend fr);

	List<FoodRecommend> queryFoodrecommendListById(FoodRecommend fr);

	int updateFoodRecommend(FoodRecommend fr);
}
