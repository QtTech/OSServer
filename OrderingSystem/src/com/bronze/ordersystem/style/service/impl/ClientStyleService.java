package com.bronze.ordersystem.style.service.impl;

import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.OSConfiguration;
import com.bronze.ordersystem.style.model.SkinStyle;
import com.bronze.ordersystem.style.service.IClientStyleService;

@Service
public class ClientStyleService implements IClientStyleService {

	private static final Logger logger = Logger.getLogger(ClientStyleService.class);
	
	@Override
	public Map<String, Object> getSkinStyle() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int style = CommonUtils.getSkinStyle();
			map.put(OSException.STATUS, OSException.SUCCESS);
			map.put(OSException.DATA, style);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> setSkinStyle(SkinStyle ss) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int style = ss.getSkin();
			if (style > OSConfiguration.SKIN_MIN && style < OSConfiguration.SKIN_MAX) {
				CommonUtils.setSkinStyle(style);
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
