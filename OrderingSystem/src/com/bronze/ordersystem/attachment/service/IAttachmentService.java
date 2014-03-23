package com.bronze.ordersystem.attachment.service;

import java.util.Map;

import com.bronze.ordersystem.attachment.model.BillAttachment;

public interface IAttachmentService {

	Map<String, Object> queryAttachments();

	Map<String, Object> queryAttachmentsByName(BillAttachment ba);

	Map<String, Object> deleteAttachment(BillAttachment ba);

	Map<String, Object> insertAttachment(BillAttachment ba);

	Map<String, Object> queryAttachmentByID(BillAttachment ba);

	Map<String, Object> updateAttachment(BillAttachment ba);

}
