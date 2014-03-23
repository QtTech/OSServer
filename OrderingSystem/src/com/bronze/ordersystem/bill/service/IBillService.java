package com.bronze.ordersystem.bill.service;

import java.util.List;
import java.util.Map;

import com.bronze.ordersystem.bill.model.AccountReconciliation;
import com.bronze.ordersystem.bill.model.Discount;
import com.bronze.ordersystem.bill.model.PlaceTakeOutOrders;
import com.bronze.ordersystem.bill.model.PlacedOrders;
import com.bronze.ordersystem.bill.model.TableInfo;
import com.bronze.ordersystem.bill.model.UncheckedBill;
import com.bronze.ordersystem.bill.model.UncheckedBillDetails;
import com.bronze.ordersystem.bill.model.UpdatedTempBillDetails;
import com.bronze.ordersystem.food.model.FoodInfo;


public interface IBillService {

	Map<String, Object> placeOrder(PlacedOrders pos);

	Map<String, Object> queryByTableId(TableInfo ti);

	Map<String, Object> queryFoodStates(List<FoodInfo> foodlist);

	Map<String, Object> checkOrder(TableInfo ti);

	Map<String, Object> confirmOrder(TableInfo ti);

	Map<String, Object> queryNotFinished(TableInfo ti);

	Map<String, Object> updateTempDetailState(List<UncheckedBillDetails> ub);

	Map<String, Object> queryAllCooking(FoodInfo fi);

	Map<String, Object> queryByBillId(TableInfo ti);

	Map<String, Object> getAllBills();

	Map<String, Object> getMaxDiscount();

	Map<String, Object> checkOut(Discount discount);

	Map<String, Object> getReconciliationBills();

	Map<String, Object> confirmReconciliationBills(AccountReconciliation ar);

	Map<String, Object> getAllTakeOut();

	Map<String, Object> placeTakeOut(PlaceTakeOutOrders ptoos);

	Map<String, Object> updateBillPeople(UncheckedBill ub);

	Map<String, Object> updateBillTable(TableInfo ti);

	Map<String, Object> cancelTempBill(UncheckedBill ub);

	Map<String, Object> mergeBillTable(TableInfo ti);

	Map<String, Object> updateBillDetails(UpdatedTempBillDetails utbd);

	Map<String, Object> updateMaxDiscount(Discount discount);

	Map<String, Object> cleanTable(TableInfo tables);

}
