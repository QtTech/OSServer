package com.bronze.ordersystem.food.dao;

import java.util.List;

import com.bronze.ordersystem.food.model.CategoryList;

public interface ICategoryListDao {

	List<CategoryList> queryCategoryList();

	int insertCategory(CategoryList cl);

	int updateCategory(CategoryList cl);

	int deleteCategory(CategoryList cl);

	List<CategoryList> queryCategoryListByID(CategoryList cl);
	
}
