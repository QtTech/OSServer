package com.bronze.ordersystem.login.service.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.login.dao.IUserLoginDao;
import com.bronze.ordersystem.login.model.UserInfo;
import com.bronze.ordersystem.login.service.IUserInfoService;

@Service
public class UserInfoService implements IUserInfoService {

	private static final Logger logger = Logger.getLogger(UserInfoService.class);
	
	@Resource
	private IUserLoginDao userLoginDao;
	
	@Override
	public Map<String, Object> queryUserInfo() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UserInfo> list = userLoginDao.queryUserInfo();
			if (list != null) {
				map.put(OSException.DATA, list);
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> addUserInfo(UserInfo ui) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = userLoginDao.addUserInfo(ui);
			if (ret > 0) {
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> deleteUserInfo(UserInfo ui) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = userLoginDao.deleteUserInfo(ui);
			if (ret > 0) {
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateUserInfo(UserInfo ui) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = userLoginDao.updateUserInfo(ui);
			if (ret > 0) {
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryUserInfoByID(UserInfo ui) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UserInfo> list = userLoginDao.queryUserInfoByID(ui);
			if (list != null && list.size() == 1) {
				map.put(OSException.DATA, list.get(0));
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryUserInfoByName(UserInfo ui) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UserInfo> list = userLoginDao.queryUserInfoByName(ui);
			if (list != null) {
				map.put(OSException.DATA, list);
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

}
