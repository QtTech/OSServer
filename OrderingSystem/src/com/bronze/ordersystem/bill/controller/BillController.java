package com.bronze.ordersystem.bill.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bronze.ordersystem.bill.model.AccountReconciliation;
import com.bronze.ordersystem.bill.model.Discount;
import com.bronze.ordersystem.bill.model.PlaceTakeOutOrders;
import com.bronze.ordersystem.bill.model.PlacedOrders;
import com.bronze.ordersystem.bill.model.TableInfo;
import com.bronze.ordersystem.bill.model.UncheckedBill;
import com.bronze.ordersystem.bill.model.UncheckedBillDetails;
import com.bronze.ordersystem.bill.model.UpdatedTempBillDetails;
import com.bronze.ordersystem.bill.service.IBillService;
import com.bronze.ordersystem.food.model.FoodInfo;

@Controller
@RequestMapping(value="bill")
public class BillController {

	@Resource
	private IBillService billService;
	
	@RequestMapping(value="placeorder", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> placeOrder(@RequestBody PlacedOrders pos) {
		return billService.placeOrder(pos);
	}
	
	@RequestMapping(value="querybytableid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryByTableId(@RequestBody TableInfo ti) {
		return billService.queryByTableId(ti);
	}

	@RequestMapping(value="querybybillid", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryByBillId(@RequestBody TableInfo ti) {
		return billService.queryByBillId(ti);
	}

	
	@RequestMapping(value="queryfoodstates", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryFoodStates(@RequestBody List<FoodInfo> foodlist) {
		return billService.queryFoodStates(foodlist);
	}
	
	@RequestMapping(value="updatetempdetailstate", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateTempDetailState(@RequestBody List<UncheckedBillDetails> ub) {
		return billService.updateTempDetailState(ub);
	}
	
	@RequestMapping(value="querynotfinished", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryNotFinished(@RequestBody TableInfo ti) {
		return billService.queryNotFinished(ti);
	}
	
	@RequestMapping(value="queryallcooking", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryAllCooking(@RequestBody FoodInfo fi) {
		return billService.queryAllCooking(fi);
	}
	
	@RequestMapping(value="checkorder", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkOrder(@RequestBody TableInfo ti) {
		return billService.checkOrder(ti);
	}
	
	@RequestMapping(value="confirmorder", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> confirmOrder(@RequestBody TableInfo ti) {
		return billService.confirmOrder(ti);
	}
	
	@RequestMapping(value="getallbills", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllBills() {
		return billService.getAllBills();
	}
	
	@RequestMapping(value="updatemaxdiscount", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateMaxDiscount(@RequestBody Discount discount) {
		return billService.updateMaxDiscount(discount);
	}
	
	@RequestMapping(value="getmaxdiscount", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getMaxDiscount() {
		return billService.getMaxDiscount();
	}
	
	@RequestMapping(value="checkout", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkOut(@RequestBody Discount discount) {
		return billService.checkOut(discount);
	}
	
	@RequestMapping(value="getreconciliations", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getReconciliationBills() {
		return billService.getReconciliationBills();
	}
	
	@RequestMapping(value="confirmreconciliations", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> confirmReconciliationBills(@RequestBody AccountReconciliation ar) {
		return billService.confirmReconciliationBills(ar);
	}
	
	@RequestMapping(value="getalltakeout", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllTakeOut() {
		return billService.getAllTakeOut();
	}
	
	@RequestMapping(value="placetakeout", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> placeTakeOut(@RequestBody PlaceTakeOutOrders ptoos) {
		return billService.placeTakeOut(ptoos);
	}
	
	@RequestMapping(value="updatebillpeople", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateBillPeople(@RequestBody UncheckedBill ub) {
		return billService.updateBillPeople(ub);
	}
	
	@RequestMapping(value="updatebilltable", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateBillTable(@RequestBody TableInfo ti) {
		return billService.updateBillTable(ti);
	}
	
	@RequestMapping(value="canceltempbill", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cancelTempBill(@RequestBody UncheckedBill ub) {
		return billService.cancelTempBill(ub);
	}
	
	@RequestMapping(value="mergebilltable", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> mergeBillTable(@RequestBody TableInfo ti) {
		return billService.mergeBillTable(ti);
	}
	
	@RequestMapping(value="updatebilldetails", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateBillDetails(@RequestBody UpdatedTempBillDetails utbd) {
		return billService.updateBillDetails(utbd);
	}
	
	@RequestMapping(value="cleantable", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cleanTable(@RequestBody TableInfo table) {
		return billService.cleanTable(table);
	}
}
