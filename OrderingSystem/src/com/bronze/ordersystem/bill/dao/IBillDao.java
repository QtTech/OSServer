package com.bronze.ordersystem.bill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bronze.ordersystem.bill.model.AccountReconciliation;
import com.bronze.ordersystem.bill.model.AllCookingInfo;
import com.bronze.ordersystem.bill.model.BillPayment;
import com.bronze.ordersystem.bill.model.BillSpecialRemark;
import com.bronze.ordersystem.bill.model.CheckedBill;
import com.bronze.ordersystem.bill.model.CheckedBillDetails;
import com.bronze.ordersystem.bill.model.Discount;
import com.bronze.ordersystem.bill.model.ReconciliationBill;
import com.bronze.ordersystem.bill.model.TableInfo;
import com.bronze.ordersystem.bill.model.UncheckedBill;
import com.bronze.ordersystem.bill.model.UncheckedBillDetails;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.statistics.model.DishStatistics;
import com.bronze.ordersystem.statistics.model.DailySaleStatistics;
import com.bronze.ordersystem.statistics.model.StatisticsCondition;
import com.bronze.ordersystem.statistics.model.TotalSaleStatistics;


public interface IBillDao {

	int createNewBillTemp(UncheckedBill ub);
	
	List<UncheckedBill> queryBillByTableId(UncheckedBill ub);
	
	List<UncheckedBill> queryBillById(UncheckedBill ub);
	
	List<UncheckedBill> queryBillByDetailID(UncheckedBillDetails ubd);

	int updateBillTemp(UncheckedBill ub);

	int addBillDetailsTemp(UncheckedBillDetails temp);

	List<UncheckedBillDetails> queryBillDetailsTempByTableId(TableInfo ti);
	
	List<UncheckedBillDetails> queryBDTViaBillID(@Param(value = "id") String id);
	
	List<UncheckedBillDetails> queryBillDetailsTempByID(UncheckedBillDetails ubd);
	
	List<UncheckedBillDetails> queryBDTViaTableIdAndCookBy(TableInfo ti);

	List<UncheckedBillDetails> queryFoodStates(List<FoodInfo> foodlist);

	List<UncheckedBillDetails> checkBillDetailsCheckoutState(UncheckedBillDetails ubd);
	
	List<UncheckedBillDetails> queryNotFinished(TableInfo ti);

	int updateTempDetailState(UncheckedBillDetails ub);
	
	List<BillSpecialRemark> queryBillSpecialRemark();

	int insertBillRemark(BillSpecialRemark bsr);

	int deleteBillRemark(BillSpecialRemark bsr);

	int updateBillRemark(BillSpecialRemark bsr);

	List<AllCookingInfo> queryAllCooking(FoodInfo fi);
	
	int generateNewBill(CheckedBill cb);

	int generateNewBillDetails(CheckedBillDetails cbd);

	int deleteBillTemp(UncheckedBill ub);

	List<BillSpecialRemark> queryBillmarkListByID(BillSpecialRemark bsr);

	List<Discount> queryDiscount();

	List<AccountReconciliation> queryCurrentReconciliations();

	int updateReconciliations(AccountReconciliation ar);

	int insertReconciliations(AccountReconciliation ar);

	List<ReconciliationBill> queryCheckBillByTime(CheckedBill cb);

	List<UncheckedBill> queryTakeOutBills();

	int updateBillTempPeople(UncheckedBill ub);

	int updateBillTempTable(UncheckedBill ub);

	List<UncheckedBillDetails> cancelTempBill(UncheckedBill ub);

	int mergeExistedBillDetails(TableInfo tiparams);

	int deleteBillTempByTables(List<String> params);

	List<UncheckedBill> queryBillTempByTables(List<String> params);

	int deleteBillTempDetails(List<UncheckedBillDetails> removedbills);

	int updateBillTempDetails(List<UncheckedBillDetails> updatedbills);

	int updateMaxDiscount(Discount discount);

	int createMaxDiscount(Discount discount);

	List<DishStatistics> queryDishStatistics(StatisticsCondition sc);

	List<DailySaleStatistics> querySaleStatistics(StatisticsCondition sc);

	List<TotalSaleStatistics> queryTotalSaleStatistics(StatisticsCondition sc);

	List<UncheckedBillDetails> queryWorkingDetailsViaTable(TableInfo ti);

	List<UncheckedBillDetails> queryUrgentDetailsViaTable(TableInfo ti);
	
	int insertBillPayment(BillPayment bp);
	
	List<BillPayment> queryBillPayment(BillPayment bp);
}
