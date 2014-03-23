package com.bronze.ordersystem.attachment.service.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.attachment.dao.IAttachmentDao;
import com.bronze.ordersystem.attachment.model.BillAttachment;
import com.bronze.ordersystem.attachment.service.IAttachmentService;
import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;

@Service
public class AttachmentService implements IAttachmentService {

	private static final Logger logger = Logger.getLogger(AttachmentService.class);
	
	@Resource
	private IAttachmentDao attachmentDao;

	@Override
	public Map<String, Object> queryAttachments() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<BillAttachment> list = attachmentDao.queryAttachments();
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
	public Map<String, Object> queryAttachmentsByName(BillAttachment ba) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<BillAttachment> list = attachmentDao.queryAttachmentsByName(ba);
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
	public Map<String, Object> deleteAttachment(BillAttachment ba) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = attachmentDao.deleteAttachment(ba);
			if (ret == 1) {
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
	public Map<String, Object> insertAttachment(BillAttachment ba) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			ba.setId(CommonUtils.generateUUID());
			int ret = attachmentDao.insertAttachment(ba);
			if (ret == 1) {
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
	public Map<String, Object> queryAttachmentByID(BillAttachment ba) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<BillAttachment> list = attachmentDao.queryAttachmentByID(ba);
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
	public Map<String, Object> updateAttachment(BillAttachment ba) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = attachmentDao.updateAttachment(ba);
			if (ret == 1) {
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
