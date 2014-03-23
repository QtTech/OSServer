package com.bronze.ordersystem.login.service;

import java.util.Map;

import com.bronze.ordersystem.login.model.OpenTableInfo;
import com.bronze.ordersystem.login.model.UserInfo;

public interface IUserLoginService {

	Map<String, Object> userLogin(UserInfo ui);

	Map<String, Object> openTable(OpenTableInfo oti);

}
