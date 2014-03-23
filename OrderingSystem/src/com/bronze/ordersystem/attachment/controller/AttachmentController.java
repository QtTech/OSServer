package com.bronze.ordersystem.attachment.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.attachment.model.BillAttachment;
import com.bronze.ordersystem.attachment.service.IAttachmentService;

@Controller
@RequestMapping(value="attachment")
public class AttachmentController {

	@Resource
	private IAttachmentService attachmentService;
	
	@RequestMapping(value="queryattachments", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryAttachments() {
		return attachmentService.queryAttachments();
	}
	
	@RequestMapping(value="queryattachmentsbyname", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryAttachmentsByName(@RequestBody BillAttachment ba) {
		return attachmentService.queryAttachmentsByName(ba);
	}
	
	@RequestMapping(value="queryattachmentbyid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryAttachmentByID(@RequestBody BillAttachment ba) {
		return attachmentService.queryAttachmentByID(ba);
	}
	
	@RequestMapping(value="deleteattachment", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteAttachment(@RequestBody BillAttachment ba) {
		return attachmentService.deleteAttachment(ba);
	}
	
	@RequestMapping(value="insertattachment", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertAttachment(@RequestBody BillAttachment ba) {
		return attachmentService.insertAttachment(ba);
	}
	
	@RequestMapping(value="updateattachment", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateAttachment(@RequestBody BillAttachment ba) {
		return attachmentService.updateAttachment(ba);
	}
}
