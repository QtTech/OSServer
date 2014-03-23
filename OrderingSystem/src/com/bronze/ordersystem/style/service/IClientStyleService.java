package com.bronze.ordersystem.style.service;

import java.util.Map;

import com.bronze.ordersystem.style.model.SkinStyle;

public interface IClientStyleService {

	Map<String, Object> getSkinStyle();

	Map<String, Object> setSkinStyle(SkinStyle ss);

}
