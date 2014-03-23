package com.bronze.ordersystem.attachment.dao;

import java.util.List;

import com.bronze.ordersystem.attachment.model.BillAttachment;

public interface IAttachmentDao {

	List<BillAttachment> queryAttachments();

	List<BillAttachment> queryAttachmentsByName(BillAttachment ba);

	int deleteAttachment(BillAttachment ba);

	int insertAttachment(BillAttachment ba);

	List<BillAttachment> queryAttachmentByID(BillAttachment ba);

	int updateAttachment(BillAttachment ba);

}
