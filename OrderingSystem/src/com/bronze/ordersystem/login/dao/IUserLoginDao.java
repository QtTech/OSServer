package com.bronze.ordersystem.login.dao;

import java.util.List;

import com.bronze.ordersystem.login.model.UserInfo;


public interface IUserLoginDao {

	List<UserInfo> userLogin(UserInfo ui);

	List<UserInfo> queryUserInfo();

	int addUserInfo(UserInfo ui);

	int deleteUserInfo(UserInfo ui);

	int updateUserInfo(UserInfo ui);

	List<UserInfo> queryUserInfoByID(UserInfo ui);

	List<UserInfo> queryUserInfoByName(UserInfo ui);
	
}
