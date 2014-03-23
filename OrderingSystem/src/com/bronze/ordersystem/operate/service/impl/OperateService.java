package com.bronze.ordersystem.operate.service.impl;

import java.util.Hashtable;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bronze.ordersystem.bill.model.TableInfo;
import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.operate.service.IOperateService;

@Service
public class OperateService implements IOperateService {

	private static final Object callLock = new Object();
	private static final Object answerLock = new Object();
	
	private static final String TYPE_CALL = "call";
	private static final String TYPE_ANSWER = "answer";
	
	@Override
	public Map<String, Object> callWaiter(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			synchronized (callLock) {
				CommonUtils.sendMQTTNotification(CommonUtils.TOPIC_CALLING_WAITER, 
						transferMessage(ti.getId(), TYPE_CALL));
			}
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			map.put(OSException.STATUS, OSException.FAILURE);
		}
		return map;
	}

	@Override
	public Map<String, Object> answerCall(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			synchronized (answerLock) {
				CommonUtils.sendMQTTNotification(CommonUtils.TOPIC_CALLING_WAITER, 
						transferMessage(ti.getId(), TYPE_ANSWER));
			}
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			map.put(OSException.STATUS, OSException.FAILURE);
		}
		return map;
	}

	private String transferMessage(String tableid, String type) {
		String result = "";
		result = "type:" + type + ";" + "table:" + tableid;
		return result.trim();
	}
}
