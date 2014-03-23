package com.bronze.ordersystem.login.service;

import java.util.Map;

import com.bronze.ordersystem.login.model.UserInfo;

public interface IUserInfoService {

	Map<String, Object> queryUserInfo();

	Map<String, Object> addUserInfo(UserInfo ui);

	Map<String, Object> deleteUserInfo(UserInfo ui);

	Map<String, Object> updateUserInfo(UserInfo ui);

	Map<String, Object> queryUserInfoByID(UserInfo ui);

	Map<String, Object> queryUserInfoByName(UserInfo ui);

}
