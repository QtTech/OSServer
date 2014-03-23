package com.bronze.ordersystem.operate.service;

import java.util.Map;

import com.bronze.ordersystem.bill.model.TableInfo;

public interface IOperateService {

	Map<String, Object> callWaiter(TableInfo ti);

	Map<String, Object> answerCall(TableInfo ti);

}
