package com.bronze.ordersystem.login.service.impl;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.bill.dao.IBillDao;
import com.bronze.ordersystem.bill.model.UncheckedBill;
import com.bronze.ordersystem.bill.service.impl.BillService;
import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.MQTTNotificationThread;
import com.bronze.ordersystem.login.dao.IUserLoginDao;
import com.bronze.ordersystem.login.model.OpenTableInfo;
import com.bronze.ordersystem.login.model.ResultUserInfo;
import com.bronze.ordersystem.login.model.UserInfo;
import com.bronze.ordersystem.login.service.IUserLoginService;


@Service
public class UserLoginService implements IUserLoginService {

	protected static final Logger logger = Logger.getLogger(UserLoginService.class);
	
	@Resource
	private IUserLoginDao userLoginDao;
	@Resource
	private IBillDao billDao;

	@Override
	public Map<String, Object> userLogin(UserInfo ui) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UserInfo> list = userLoginDao.userLogin(ui);
			if (list != null && list.size() == 1) {
				logger.info("User " + list.get(0).getUsername() + " has logged in the system!!!");
				map.put(OSException.STATUS, OSException.SUCCESS);
				ResultUserInfo rui = new ResultUserInfo();
				rui.setUsername(list.get(0).getUsername());
				map.put(OSException.DATA, rui);
			} else {
				logger.info("Code " + ui.getCode() + " doesn't exist!!!");
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
	public Map<String, Object> openTable(OpenTableInfo oti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			if (oti.getUsername() == null || oti.getUsername().equals("")) {
				UserInfo ui = new UserInfo();
				ui.setCode(oti.getCode());
				ui.setCategory(oti.getCategory());
				List<UserInfo> list = userLoginDao.userLogin(ui);
				if (list != null && list.size() == 1) {
					logger.info("User " + list.get(0).getUsername() + " has logged in the system!!!");
					UncheckedBill ub = new UncheckedBill();
					ub.setTableid(oti.getTableid());
					List<UncheckedBill> billList = billDao.queryBillByTableId(ub);
					ResultUserInfo rui = new ResultUserInfo();
					rui.setUsername(list.get(0).getUsername());
					if (billList != null && billList.size() > 0) {
						logger.info("Table " + oti.getTableid() + " has been opened!!!");
						map.put(OSException.STATUS, OSException.PARAM_FAILURE);
						map.put(OSException.DATA, rui);
					} else {
						logger.info("Open Table " + oti.getTableid() + "!!!");
						ub.setId(CommonUtils.generateUUID());
						ub.setRunningnumber(CommonUtils.generateRunningNumber());
						ub.setType(UncheckedBill.NORMAL);
						ub.setPeople(oti.getCustomers());
						ub.setWaitername(list.get(0).getUsername());
						while(billDao.createNewBillTemp(ub) < 1) {
							ub.setId(CommonUtils.generateUUID());
						}
						map.put(OSException.STATUS, OSException.SUCCESS);
						map.put(OSException.DATA, rui);
						
						new MQTTNotificationThread(BillService.objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, oti.getTableid()).start();
					}
				} else {
					logger.info("Code " + ui.getCode() + " doesn't exist!!!");
					map.clear();
					map.put(OSException.STATUS, OSException.FAILURE);
				}
			} else {
				UncheckedBill ub = new UncheckedBill();
				ub.setTableid(oti.getTableid());
				List<UncheckedBill> billList = billDao.queryBillByTableId(ub);
				ResultUserInfo rui = new ResultUserInfo();
				rui.setUsername(oti.getUsername());
				if (billList != null && billList.size() > 0) {
					logger.info("Table " + oti.getTableid() + " has been opened!!!");
					map.put(OSException.STATUS, OSException.PARAM_FAILURE);
					map.put(OSException.DATA, rui);
				} else {
					logger.info("Open Table " + oti.getTableid() + "!!!");
					ub.setId(CommonUtils.generateUUID());
					ub.setRunningnumber(CommonUtils.generateRunningNumber());
					ub.setType(UncheckedBill.NORMAL);
					ub.setPeople(oti.getCustomers());
					ub.setWaitername(oti.getUsername());
					while(billDao.createNewBillTemp(ub) < 1) {
						ub.setId(CommonUtils.generateUUID());
					}
					map.put(OSException.STATUS, OSException.SUCCESS);
					map.put(OSException.DATA, rui);
					
					new MQTTNotificationThread(BillService.objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, oti.getTableid()).start();
				}
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		
		return map;
	}

}
