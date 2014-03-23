package com.bronze.ordersystem.bill.service.impl;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bronze.ordersystem.bill.dao.IBillDao;
import com.bronze.ordersystem.bill.model.AccountReconciliation;
import com.bronze.ordersystem.bill.model.AllCookingInfo;
import com.bronze.ordersystem.bill.model.BillPayment;
import com.bronze.ordersystem.bill.model.CheckedBill;
import com.bronze.ordersystem.bill.model.CheckedBillDetails;
import com.bronze.ordersystem.bill.model.Discount;
import com.bronze.ordersystem.bill.model.DiscountResult;
import com.bronze.ordersystem.bill.model.OrderedFoodInTable;
import com.bronze.ordersystem.bill.model.PlaceTakeOutOrders;
import com.bronze.ordersystem.bill.model.PlacedOrders;
import com.bronze.ordersystem.bill.model.ReconciliationBill;
import com.bronze.ordersystem.bill.model.TableInfo;
import com.bronze.ordersystem.bill.model.UncheckedBill;
import com.bronze.ordersystem.bill.model.UncheckedBillDetails;
import com.bronze.ordersystem.bill.model.UpdatedTempBillDetails;
import com.bronze.ordersystem.bill.service.IBillService;
import com.bronze.ordersystem.common.exception.BusinessException;
import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.MQTTNotificationThread;
import com.bronze.ordersystem.food.dao.IFoodInfoDao;
import com.bronze.ordersystem.food.dao.IFoodOrderableDao;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodOrderable;

@Service
@Transactional
public class BillService implements IBillService {
	
	protected final static Logger logger = Logger.getLogger(BillService.class);
	
	private final static Object objLock_TOPIC_KITCHEN = new Object();
	private final static Object objLock_TOPIC_WATERBAR = new Object();
	
	private final static Object objLock_TOPIC_KITCHEN_TAKE_OUT = new Object();
	private final static Object objLock_TOPIC_WATERBAR_TAKE_OUT = new Object();
	
	public final static Object objLock_TOPIC_CASH = new Object();
	
	@Resource
	private IBillDao billDao;
	@Resource
	private IFoodInfoDao foodInfoDao;
	@Resource
	private IFoodOrderableDao foodOrderableDao;

	@Override
	public Map<String, Object> placeOrder(PlacedOrders pos) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			//check ordered
			List<UncheckedBillDetails> ubd = pos.getDetailedorders();
			List<String> unorderedlist = new ArrayList<String>();
			boolean haswater = false;
			boolean hasfood = false;
			
			for (int i = 0; ubd != null && i < ubd.size(); i++) {
				List<FoodOrderable> list = foodOrderableDao.queryFoodOrderableByFoodId(ubd.get(i).getFoodid());
				if (list.size() == 1 && list.get(0).getOrderable() == 0) {
					unorderedlist.add(list.get(0).getFoodid());
				}
			}
			
			if (unorderedlist.size() > 0) {
				map.put(OSException.DATA, unorderedlist);
				map.put(OSException.STATUS, OSException.FAILURE);
				return map;
			}
			
			UncheckedBill ub = new UncheckedBill();
			ub.setTableid(pos.getTableid());
			List<UncheckedBill> list = billDao.queryBillByTableId(ub);
			if (list != null && list.size() == 1) {
				ub = list.get(0);
				double price = list.get(0).getOriginalprice();
				for (int i = 0; ubd != null && i < ubd.size(); i++) {
					FoodInfo fi = new FoodInfo();
					fi.setId(ubd.get(i).getFoodid());
					fi = foodInfoDao.queryFood(fi).get(0);
					
					if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
						hasfood = true;
					} else {
						haswater = true;
					}
					
					UncheckedBillDetails temp = ubd.get(i);
					temp.setId(CommonUtils.generateUUID());
					temp.setBilltempid(ub.getId());
					if (temp.getState() != UncheckedBill.STANDBY) {
						temp.setState(UncheckedBill.WAITING);
					}
					temp.setType(ub.getType());
					temp.setPrice(fi.getPrice() * temp.getAmount());
					temp.setFoodname(fi.getName());
					
					int ret = billDao.addBillDetailsTemp(temp);
					
					while (ret < 1) {
						temp.setId(CommonUtils.generateUUID());
						ret = billDao.addBillDetailsTemp(temp);
					}
					
					price += temp.getPrice();
				}
				
				ub.setRemark(pos.getBillremark());
				ub.setOriginalprice(price);
				ub.setRealprice(price);
				billDao.updateBillTemp(ub);
				map.put(OSException.STATUS, OSException.SUCCESS);
				
				final String tableid = ub.getTableid();
				new MQTTNotificationThread(tableid, CommonUtils.TOPIC_CUSTOMER + tableid, tableid).start();
				new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, tableid).start();

				if (hasfood) {
					new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, ub.getTableid()).start();
				}
				
				if (haswater) {
					new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, ub.getTableid()).start();
				}
				
			} else {
				map.put(OSException.STATUS, OSException.PARAM_FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryByTableId(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBillDetails> list = billDao.queryBDTViaTableIdAndCookBy(ti);
			UncheckedBill ub = new UncheckedBill();
			ub.setTableid(ti.getId());
			List<UncheckedBill> _list = billDao.queryBillByTableId(ub);
			OrderedFoodInTable ofit = new OrderedFoodInTable();
			
			if (_list != null && _list.size() > 0) {
				ofit.setTempbill(_list.get(0));
			}
			
			if (list != null) {
				ofit.setTempbilldetails(list);
			}
			
			map.put(OSException.DATA, ofit);
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryFoodStates(List<FoodInfo> foodlist) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBillDetails> list = billDao.queryFoodStates(foodlist);
			if (list != null) {
				map.put(OSException.STATUS, OSException.SUCCESS);
				map.put(OSException.DATA, list);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.FAILURE);
		}
		return map;
	}


	@Override
	public Map<String, Object> checkOrder(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBillDetails> list = billDao.queryBillDetailsTempByTableId(ti);
			UncheckedBill ub = new UncheckedBill();
			ub.setTableid(ti.getId());
			List<UncheckedBill> _list = billDao.queryBillByTableId(ub);
			OrderedFoodInTable ofit = new OrderedFoodInTable();
			
			if (_list != null && _list.size() > 0) {
				ofit.setTempbill(_list.get(0));
			}
			
			if (list != null) {
				ofit.setTempbilldetails(list);
			}
			
			map.put(OSException.DATA, ofit);
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> confirmOrder(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBillDetails> list = billDao.queryBillDetailsTempByTableId(ti);
			UncheckedBill ub = new UncheckedBill();
			ub.setTableid(ti.getId());
			List<UncheckedBill> _list = billDao.queryBillByTableId(ub);
			if (list != null && list.size() > 0 && _list != null && _list.size() > 0) {
				CheckedBill cb = new CheckedBill();
				cb.copy(_list.get(0));
				cb.setId(CommonUtils.generateUUID());
				
				while(billDao.generateNewBill(cb) != 1) {
					cb.setId(CommonUtils.generateUUID());
				}
				
				for (int i = 0; i < list.size(); i++) {
					CheckedBillDetails cbd = new CheckedBillDetails();
					cbd.setId(CommonUtils.generateUUID());
					cbd.setBillid(cb.getId());
					cbd.copy(list.get(i));
					while(billDao.generateNewBillDetails(cbd) != 1) {
						cbd.setId(CommonUtils.generateUUID());
					}
				}
				
				billDao.deleteBillTemp(ub);
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
	public Map<String, Object> queryNotFinished(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBillDetails> list = billDao.queryNotFinished(ti);
			
			UncheckedBill ub = new UncheckedBill();
			ub.setTableid(ti.getId());
			List<UncheckedBill> _list = billDao.queryBillByTableId(ub);
			
			OrderedFoodInTable ofit = new OrderedFoodInTable();
			if (_list != null && _list.size() > 0) {
				ofit.setTempbill(_list.get(0));
			}
			
			if (list != null) {
				ofit.setTempbilldetails(list);
			}
			
			map.put(OSException.STATUS, OSException.SUCCESS);
			map.put(OSException.DATA, ofit);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateTempDetailState(List<UncheckedBillDetails> ublist) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			for (int i = 0; ublist != null && i < ublist.size(); i++) {
				UncheckedBillDetails ub = ublist.get(i);
				List<UncheckedBill> list = billDao.queryBillByDetailID(ub);
				if (list != null && list.size() > 0) {
					List<UncheckedBillDetails> ubdList = billDao.queryBillDetailsTempByID(ub);
					if (ub.getState() == UncheckedBill.URGENT && ubdList.get(0).getState() != UncheckedBill.WAITING) {
						
					} else if ((ub.getState() == UncheckedBill.STANDBY && ubdList.get(0).getState() != UncheckedBill.WAITING) ||
								(ub.getState() == UncheckedBill.WAITING && ubdList.get(0).getState() != UncheckedBill.STANDBY) ||
								(ub.getState() == UncheckedBill.SERVED && ubdList.get(0).getState() != UncheckedBill.COOKED)) {
						map.put(OSException.STATUS, OSException.FAILURE);
						map.put(OSException.DATA, ubdList.get(0).getFoodname());
						throw new BusinessException("updateTempDetailState");
					} else {
						if (billDao.updateTempDetailState(ub) > 0) {
							map.put(OSException.STATUS, OSException.SUCCESS);
							final String tableid = list.get(0).getTableid();
							if (tableid != null && !tableid.equals("")) {
								new MQTTNotificationThread(tableid, CommonUtils.TOPIC_CUSTOMER + tableid, tableid).start();
								new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, tableid).start();
							}
							
							ub = ubdList.get(0);
							FoodInfo fi = new FoodInfo();
							fi.setId(ub.getFoodid());
							fi = foodInfoDao.queryFood(fi).get(0);
							
							if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
								if (ub.getType() == UncheckedBill.NORMAL) {
									new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, tableid).start();
								} else {
									new MQTTNotificationThread(objLock_TOPIC_KITCHEN_TAKE_OUT, CommonUtils.TOPIC_KITCHEN_TAKE_OUT, ub.getBilltempid()).start();
								}
							} else {
								if (ub.getType() == UncheckedBill.NORMAL) {
									new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, tableid).start();
								} else {
									new MQTTNotificationThread(objLock_TOPIC_WATERBAR_TAKE_OUT, CommonUtils.TOPIC_WATERBAR_TAKE_OUT, ub.getBilltempid()).start();
								}
							}
							
						} else {
							map.put(OSException.STATUS, OSException.FAILURE);
							throw new BusinessException("updateTempDetailState");
						}
					}
				} else {
					map.put(OSException.STATUS, OSException.FAILURE);
					throw new BusinessException("updateTempDetailState");
				}
			}
		} catch (Exception e) {
			logger.error(e);
			if (!map.containsKey(OSException.DATA) && ((int)map.get(OSException.STATUS)) != OSException.FAILURE) {
				map.clear();
				map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
			}
		} finally {
			if ((int)map.get(OSException.STATUS) == OSException.FAILURE) {
				return map;
			}
		}
		
		return map;
	}

	@Override
	public Map<String, Object> queryAllCooking(FoodInfo fi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<AllCookingInfo> list = billDao.queryAllCooking(fi);
			map.put(OSException.STATUS, OSException.SUCCESS);
			map.put(OSException.DATA, list);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryByBillId(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBillDetails> list = billDao.queryBDTViaTableIdAndCookBy(ti);
			UncheckedBill ub = new UncheckedBill();
			ub.setId(ti.getBillid());
			List<UncheckedBill> _list = billDao.queryBillById(ub);
			OrderedFoodInTable ofit = new OrderedFoodInTable();
			
			if (_list != null && _list.size() > 0) {
				ofit.setTempbill(_list.get(0));
			}
			
			if (list != null) {
				ofit.setTempbilldetails(list);
			}
			
			map.put(OSException.DATA, ofit);
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> getAllBills() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBill> list = billDao.queryBillById(new UncheckedBill());
			List<OrderedFoodInTable> ofitList = new ArrayList<OrderedFoodInTable>();
			for (int i = 0; i < list.size(); i++) {
				OrderedFoodInTable ofit = new OrderedFoodInTable();
				ofit.setTempbill(list.get(i));
				List<UncheckedBillDetails> listdetals = billDao.queryBDTViaBillID(list.get(i).getId());
				ofit.setTempbilldetails(listdetals);
				ofitList.add(ofit);
			}
			
			map.put(OSException.DATA, ofitList);
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		
		return map;
	}

	@Override
	public Map<String, Object> getMaxDiscount() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<Discount> list = billDao.queryDiscount();
			if (list != null && list.size() > 0) {
				DiscountResult dr = new DiscountResult();
				dr.setDeduction(list.get(0).getDeduction());
				dr.setDiscount(list.get(0).getDiscount());
				map.put(OSException.DATA, dr);
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
			}
		} catch (Exception e) {
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> checkOut(Discount discount) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			//check bill states are all in served
			UncheckedBillDetails tempubd = new UncheckedBillDetails();
			tempubd.setBilltempid(discount.getId());
			tempubd.setState(UncheckedBill.SERVED);
			List<UncheckedBillDetails> checklist = billDao.checkBillDetailsCheckoutState(tempubd);
			if (checklist != null && checklist.size() > 0) {
				map.put(OSException.STATUS, OSException.PARAM_FAILURE);
				return map;
			}
			
			UncheckedBill ub = new UncheckedBill();
			ub.setId(discount.getId());
			List<UncheckedBill> _list = billDao.queryBillById(ub);
			List<UncheckedBillDetails> list = null;
			
			if (_list != null && _list.size() > 0) {
				list = billDao.queryBDTViaBillID(_list.get(0).getId());
			}
			
			if (list != null && list.size() > 0 && _list != null && _list.size() > 0) {
				CheckedBill cb = new CheckedBill();
				cb.copy(_list.get(0));
				cb.setId(CommonUtils.generateUUID());
				cb.setOriginalprice(discount.getOriginalprice());
				cb.setDeduction(discount.getDeduction());
				cb.setDiscount(discount.getDiscount());
				cb.setRealprice(discount.getRealprice());
				
				while(billDao.generateNewBill(cb) != 1) {
					cb.setId(CommonUtils.generateUUID());
				}
				
				BillPayment bp = new BillPayment();
				bp.setId(CommonUtils.generateUUID());
				bp.setBillid(cb.getId());
				bp.setCard(discount.getCard());
				bp.setCash(discount.getCash());
				
				while(billDao.insertBillPayment(bp) != 1) {
					bp.setId(CommonUtils.generateUUID());
				}
				
				for (int i = 0; i < list.size(); i++) {
					CheckedBillDetails cbd = new CheckedBillDetails();
					cbd.setId(CommonUtils.generateUUID());
					cbd.setBillid(cb.getId());
					cbd.copy(list.get(i));
					while(billDao.generateNewBillDetails(cbd) != 1) {
						cbd.setId(CommonUtils.generateUUID());
					}
				}
				
				billDao.deleteBillTemp(ub);
				
				if (_list.get(0).getTableid() != null && !_list.get(0).getTableid().equals("")) {
					final String tableid = _list.get(0).getTableid();
					new MQTTNotificationThread(tableid, CommonUtils.TOPIC_CUSTOMER + tableid, tableid).start();
					new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, tableid).start();
				}
				
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
	public Map<String, Object> getReconciliationBills() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<AccountReconciliation> list = billDao.queryCurrentReconciliations();
			
			if (list == null || list.size() == 0) {
				CheckedBill cb = new CheckedBill();
				List<ReconciliationBill> billList = billDao.queryCheckBillByTime(cb);
				map.put(OSException.DATA, billList);
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else if (list.size() == 1) {
				CheckedBill cb = new CheckedBill();
				cb.setStarttime(list.get(0).getTime());
				List<ReconciliationBill> billList = billDao.queryCheckBillByTime(cb);
				map.put(OSException.DATA, billList);
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
	public Map<String, Object> confirmReconciliationBills(
			AccountReconciliation ar) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<AccountReconciliation> list = billDao.queryCurrentReconciliations();
			if (list != null && list.size() > 1) {
				map.put(OSException.STATUS, OSException.FAILURE);
				return map;
			} else if (list != null && list.size() == 1) {
				list.get(0).setCurrentEdit(0);
				billDao.updateReconciliations(list.get(0));
			}
			
			ar.setId(CommonUtils.generateUUID());
			ar.setCurrentEdit(1);
			while (billDao.insertReconciliations(ar) != 1) {
				ar.setId(CommonUtils.generateUUID());
			}
			
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> getAllTakeOut() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBill> list = billDao.queryTakeOutBills();
			List<OrderedFoodInTable> ofitList = new ArrayList<OrderedFoodInTable>();
			for (int i = 0; i < list.size(); i++) {
				OrderedFoodInTable ofit = new OrderedFoodInTable();
				ofit.setTempbill(list.get(i));
				List<UncheckedBillDetails> listdetals = billDao.queryBDTViaBillID(list.get(i).getId());
				ofit.setTempbilldetails(listdetals);
				ofitList.add(ofit);
			}
			
			map.put(OSException.DATA, ofitList);
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		
		return map;
	}

	@Override
	public Map<String, Object> placeTakeOut(PlaceTakeOutOrders ptoos) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBillDetails> ubd = ptoos.getDetailedorders();
			List<String> unorderedlist = new ArrayList<String>();
			boolean haswater = false;
			boolean hasfood = false;
			
			for (int i = 0; ubd != null && i < ubd.size(); i++) {
				List<FoodOrderable> list = foodOrderableDao.queryFoodOrderableByFoodId(ubd.get(i).getFoodid());
				if (list.size() == 1 && list.get(0).getOrderable() == 0) {
					unorderedlist.add(list.get(0).getFoodid());
				}
			}
			
			if (unorderedlist.size() > 0) {
				map.put(OSException.DATA, unorderedlist);
				map.put(OSException.STATUS, OSException.FAILURE);
				return map;
			}
			
			UncheckedBill ub = new UncheckedBill();
			ub.setId(CommonUtils.generateUUID());
			ub.setRunningnumber(CommonUtils.generateRunningNumber());
			ub.setRemark(ptoos.getBillremark());
			ub.setType(UncheckedBill.TAKEOUT);
			ub.setPeople(0);
			ub.setWaitername(ptoos.getWaitername());
			while(billDao.createNewBillTemp(ub) < 1) {
				ub.setId(CommonUtils.generateUUID());
			}
			
			double price = 0;
			for (int i = 0; ubd != null && i < ubd.size(); i++) {
				FoodInfo fi = new FoodInfo();
				fi.setId(ubd.get(i).getFoodid());
				fi = foodInfoDao.queryFood(fi).get(0);
				
				if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
					hasfood = true;
				} else {
					haswater = true;
				}
				
				UncheckedBillDetails temp = ubd.get(i);
				temp.setId(CommonUtils.generateUUID());
				temp.setBilltempid(ub.getId());
				temp.setState(UncheckedBill.WAITING);
				temp.setType(ub.getType());
				temp.setPrice(fi.getPrice() * temp.getAmount());
				temp.setFoodname(fi.getName());
				
				int ret = billDao.addBillDetailsTemp(temp);
				
				while (ret < 1) {
					temp.setId(CommonUtils.generateUUID());
					ret = billDao.addBillDetailsTemp(temp);
				}
				
				price += temp.getPrice();
			}
			
			ub.setRemark(ptoos.getBillremark());
			ub.setOriginalprice(price);
			ub.setRealprice(price);
			billDao.updateBillTemp(ub);
			map.put(OSException.STATUS, OSException.SUCCESS);
			
			if (hasfood) {
				new MQTTNotificationThread(objLock_TOPIC_KITCHEN_TAKE_OUT, CommonUtils.TOPIC_KITCHEN_TAKE_OUT, ub.getId()).start();
			}
			
			if (haswater) {
				new MQTTNotificationThread(objLock_TOPIC_WATERBAR_TAKE_OUT, CommonUtils.TOPIC_WATERBAR_TAKE_OUT, ub.getId()).start();
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateBillPeople(UncheckedBill ub) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBill> list = billDao.queryBillByTableId(ub);
			if (list == null || list.size() != 1 || ub.getPeople() < 1) {
				map.put(OSException.STATUS, OSException.FAILURE);
			} else {
				ub.setId(list.get(0).getId());
				if (billDao.updateBillTempPeople(ub) == 1) {
					map.put(OSException.STATUS, OSException.SUCCESS);
					
					final String tableid = list.get(0).getTableid();
					new MQTTNotificationThread(tableid, CommonUtils.TOPIC_CUSTOMER + tableid, tableid).start();
					new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, tableid).start();
				} else {
					map.put(OSException.STATUS, OSException.FAILURE);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateBillTable(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			String org_id = ti.getId();
			String new_id = ti.getNewid();			
			boolean haswater = false;
			boolean hasfood = false;
			
			UncheckedBill ub = new UncheckedBill();
			ub.setTableid(org_id);
			List<UncheckedBill> list = billDao.queryBillByTableId(ub);
			
			UncheckedBill nub = new UncheckedBill();
			nub.setTableid(new_id);
			List<UncheckedBill> newlist = billDao.queryBillByTableId(nub);
			
			if (newlist != null && newlist.size() > 0) {
				map.put(OSException.STATUS, OSException.PARAM_FAILURE);
			} else {
				if (list != null && list.size() == 1) {
					ub = list.get(0);
					ub.setTableid(new_id);
					if (billDao.updateBillTempTable(ub) == 1) {
						map.put(OSException.STATUS, OSException.SUCCESS);
						
						List<UncheckedBillDetails> ubd = billDao.queryBDTViaBillID(ub.getId());
						for (int i = 0; ubd != null && i < ubd.size(); i++) {
							FoodInfo fi = new FoodInfo();
							fi.setId(ubd.get(i).getFoodid());
							fi = foodInfoDao.queryFood(fi).get(0);
							if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
								hasfood = true;
							} else {
								haswater = true;
							}
						}
						
						if (ub.getType() == UncheckedBill.NORMAL) {
							if (hasfood) {
								new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, org_id).start();
								new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, new_id).start();
							}
							
							if (haswater) {
								new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, org_id).start();
								new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, new_id).start();
							}
							
							new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, org_id).start();
							new MQTTNotificationThread(org_id, CommonUtils.TOPIC_CUSTOMER + org_id, org_id).start();
							
							new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, new_id).start();
							new MQTTNotificationThread(new_id, CommonUtils.TOPIC_CUSTOMER + new_id, new_id).start();
						}
					} else {
						map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
					}
				} else {
					map.put(OSException.STATUS, OSException.FAILURE);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> cancelTempBill(UncheckedBill ub) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<UncheckedBill> list = billDao.queryBillById(ub);
			List<UncheckedBillDetails> resultList;
			if (list != null && list.size() == 1) {
				resultList = billDao.cancelTempBill(ub);
				if (resultList == null || resultList.size() == 0) {
					ub = list.get(0);
					List<UncheckedBillDetails> _list = billDao.queryBDTViaBillID(ub.getId());
					
					boolean hasfood = false;
					boolean haswater = false;
					for (int i = 0; _list != null && i < _list.size(); i++) {
						FoodInfo fi = new FoodInfo();
						fi.setId(_list.get(i).getFoodid());
						fi = foodInfoDao.queryFood(fi).get(0);
						if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
							hasfood = true;
						} else {
							haswater = true;
						}
					}
					
					billDao.deleteBillTemp(ub);
					
					if (ub.getType() == UncheckedBill.NORMAL) {
						final String tableid = ub.getTableid();

						if (hasfood) {
							new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, tableid).start();
						}

						if (haswater) {
							new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, tableid).start();
						}
						new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, tableid).start();
						new MQTTNotificationThread(tableid, CommonUtils.TOPIC_CUSTOMER + tableid, tableid).start();
					} else {
						if (hasfood) {
							new MQTTNotificationThread(objLock_TOPIC_KITCHEN_TAKE_OUT, CommonUtils.TOPIC_KITCHEN_TAKE_OUT, ub.getId()).start();
						}

						if (haswater) {
							new MQTTNotificationThread(objLock_TOPIC_WATERBAR_TAKE_OUT, CommonUtils.TOPIC_WATERBAR_TAKE_OUT, ub.getId()).start();
						}
					}
					map.put(OSException.STATUS, OSException.SUCCESS);
				} else {
					map.put(OSException.STATUS, OSException.FAILURE);
				}
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
	public Map<String, Object> mergeBillTable(TableInfo ti) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			UncheckedBill ub = new UncheckedBill();
			ub.setTableid(ti.getNewid());
			List<String> params = ti.getIds();

			if (params != null && params.size() > 0) {
				for (int i = 0; i < params.size(); i++) {
					UncheckedBill temp = new UncheckedBill();
					temp.setTableid(params.get(i));
					List<UncheckedBill> templist = billDao.queryBillByTableId(temp);
					if (templist == null || templist.size() == 0) {
						map.put(OSException.STATUS, OSException.FAILURE);
						return map;
					}
				}
			} else {
				map.put(OSException.STATUS, OSException.FAILURE);
				return map;
			}
			
			List<UncheckedBill> ublist = billDao.queryBillByTableId(ub);
			
			if (ublist != null && ublist.size() == 1) {
				if (!params.contains(ti.getNewid())) {
					map.put(OSException.STATUS, OSException.PARAM_FAILURE);
					return map;
				} else {
					ub = ublist.get(0);
					params.remove(ti.getNewid());
					
					TableInfo tiparams = new TableInfo();
					tiparams.setBillid(ub.getId());
					tiparams.setIds(params);
					// update details' bill ID
					billDao.mergeExistedBillDetails(tiparams);
					
					List<UncheckedBill> ubs = billDao.queryBillTempByTables(params);
					double orgprice = ub.getOriginalprice();
					double realprice = ub.getRealprice();
					int people = ub.getPeople();
					for (int i = 0; i < ubs.size(); i++) {
						orgprice += ubs.get(i).getOriginalprice();
						realprice += ubs.get(i).getRealprice();
						people += ubs.get(i).getPeople();
					}
					// set data
					ub.setOriginalprice(orgprice);
					ub.setRealprice(realprice);
					ub.setPeople(people);
					// merge prices
					billDao.updateBillTemp(ub);
					// merge people
					billDao.updateBillTempPeople(ub);
					// remove
					billDao.deleteBillTempByTables(params);
					
					map.put(OSException.STATUS, OSException.SUCCESS);
				}
			} else {
				ub.setId(CommonUtils.generateUUID());
				ub.setRunningnumber(CommonUtils.generateRunningNumber());
				ub.setType(UncheckedBill.NORMAL);
				ub.setPeople(0);
				ub.setWaitername(ti.getWaitername());
				// create new bill for new table
				while(billDao.createNewBillTemp(ub) < 1) {
					ub.setId(CommonUtils.generateUUID());
				}
				
				TableInfo tiparams = new TableInfo();
				tiparams.setBillid(ub.getId());
				tiparams.setIds(params);
				// update details' bill ID
				billDao.mergeExistedBillDetails(tiparams);
				
				List<UncheckedBill> ubs = billDao.queryBillTempByTables(params);
				double orgprice = 0;
				double realprice = 0;
				int people = 0;
				for (int i = 0; i < ubs.size(); i++) {
					orgprice += ubs.get(i).getOriginalprice();
					realprice += ubs.get(i).getRealprice();
					people += ubs.get(i).getPeople();
				}
				// set data
				ub.setOriginalprice(orgprice);
				ub.setRealprice(realprice);
				ub.setPeople(people);
				// merge prices
				billDao.updateBillTemp(ub);
				// merge people
				billDao.updateBillTempPeople(ub);
				// remove
				billDao.deleteBillTempByTables(params);
				
				map.put(OSException.STATUS, OSException.SUCCESS);
			}
			
			List<UncheckedBillDetails> ubd = billDao.queryBDTViaBillID(ub.getId());
			boolean hasfood = false;
			boolean haswater = false;
			final String new_id = ti.getNewid();
			for (int i = 0; ubd != null && i < ubd.size(); i++) {
				FoodInfo fi = new FoodInfo();
				fi.setId(ubd.get(i).getFoodid());
				fi = foodInfoDao.queryFood(fi).get(0);
				if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
					hasfood = true;
				} else {
					haswater = true;
				}
			}
			
			if (hasfood) {
				for (int i = 0; i < params.size(); i ++) {
					new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, params.get(i)).start();
				}
				new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, new_id).start();
			}

			if (haswater) {
				for (int i = 0; i < params.size(); i ++) {
					new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, params.get(i)).start();
				}
				new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, new_id).start();
			}
			
			for (int i = 0; i < params.size(); i ++) {
				new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, params.get(i)).start();
				new MQTTNotificationThread(params.get(i), CommonUtils.TOPIC_CUSTOMER + params.get(i), params.get(i)).start();
			}
			new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, new_id).start();
			new MQTTNotificationThread(new_id, CommonUtils.TOPIC_CUSTOMER + new_id, new_id).start();
			
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateBillDetails(UpdatedTempBillDetails utbd) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			UncheckedBill ub = utbd.getTempbill();
			List<UncheckedBill> list = billDao.queryBillById(ub);
			
			List<UncheckedBillDetails> _list = new ArrayList<UncheckedBillDetails>();
			List<String> foodnames = new ArrayList<String>();
			if (utbd.getRemovedbills() != null) {
				_list.addAll(utbd.getRemovedbills());
			}
			
			if (utbd.getUpdatedbills() != null) {
				_list.addAll(utbd.getUpdatedbills());
			}
			
			for (int i = 0; i < _list.size(); i++) {
				List<UncheckedBillDetails> result = billDao.queryBillDetailsTempByID( _list.get(i));
				if (result != null && result.size() == 1 && result.get(0).getState() == UncheckedBill.WAITING) {
					continue;
				} else {
					if (!foodnames.contains(result.get(0).getFoodname())) {
						foodnames.add(result.get(0).getFoodname());
					}
				}
			}
			
			if (foodnames.size() > 0) {
				map.put(OSException.STATUS, OSException.FAILURE);
				map.put(OSException.DATA, foodnames);
				return map;
			}
			
			if (list != null && list.size() == 1) {
				ub = list.get(0);
				
				if (utbd.getRemovedbills() != null) {
					billDao.deleteBillTempDetails(utbd.getRemovedbills());
				}
				
				if (utbd.getUpdatedbills() != null) {
					billDao.updateBillTempDetails(utbd.getUpdatedbills());
				}
				
				List<UncheckedBillDetails> ubs = billDao.queryBDTViaBillID(ub.getId());
				double price = 0;
				String billremark = utbd.getTempbill().getRemark();
				
				boolean hasfood = false;
				boolean haswater = false;
				
				for (int i = 0; i < ubs.size(); i++) {
					price += ubs.get(i).getPrice() * ubs.get(i).getAmount();
					
					FoodInfo fi = new FoodInfo();
					fi.setId(ubs.get(i).getFoodid());
					fi = foodInfoDao.queryFood(fi).get(0);
					if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
						hasfood = true;
					} else {
						haswater = true;
					}
				}
				
				// set data
				ub.setOriginalprice(price);
				ub.setRealprice(price);
				ub.setRemark(billremark);
				// merge prices
				billDao.updateBillTemp(ub);
				
				if (ub.getType() == UncheckedBill.NORMAL) {
					final String tableid = ub.getTableid();

					if (hasfood) {
						new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, tableid).start();
					}

					if (haswater) {
						new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, tableid).start();
					}
					new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, tableid).start();
					new MQTTNotificationThread(tableid, CommonUtils.TOPIC_CUSTOMER + tableid, tableid).start();
				} else {
					if (hasfood) {
						new MQTTNotificationThread(objLock_TOPIC_KITCHEN_TAKE_OUT, CommonUtils.TOPIC_KITCHEN_TAKE_OUT, ub.getId()).start();
					}

					if (haswater) {
						new MQTTNotificationThread(objLock_TOPIC_WATERBAR_TAKE_OUT, CommonUtils.TOPIC_WATERBAR_TAKE_OUT, ub.getId()).start();
					}
				}
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
	public Map<String, Object> updateMaxDiscount(Discount discount) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<Discount> list = billDao.queryDiscount();
			if (list != null && list.size() > 0) {
				Discount dr = list.get(0);
				dr.setDeduction(discount.getDeduction());
				dr.setDiscount(discount.getDiscount());
				billDao.updateMaxDiscount(dr);
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				Discount dr = new Discount();
				dr.setId(CommonUtils.generateUUID());
				dr.setType(0);
				
				if (discount.getDeduction() > 0) {
					dr.setDeduction(discount.getDeduction());
				} else {
					dr.setDeduction(0);
				}
				
				if (discount.getDiscount() > 0) {
					dr.setDiscount(discount.getDiscount());
				} else {
					dr.setDiscount(0);
				}
				
				while(billDao.createMaxDiscount(dr) < 1) {
					dr.setId(CommonUtils.generateUUID());
				}
				map.put(OSException.STATUS, OSException.SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> cleanTable(TableInfo table) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<String> params = new ArrayList<String>();
			params.add(table.getId());
			List<UncheckedBill> list = billDao.queryBillTempByTables(params);
			if (list == null || list.size() == 0) {
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else {
				List<UncheckedBillDetails> ulist = billDao.queryWorkingDetailsViaTable(table);
				if (ulist == null || ulist.size() == 0) {
					List<UncheckedBillDetails> uglist = billDao.queryUrgentDetailsViaTable(table);
					boolean hasfood = false;
					boolean haswater = false;
					for (int i = 0; uglist != null && i < uglist.size(); i++) {
						FoodInfo fi = new FoodInfo();
						fi.setId(uglist.get(i).getFoodid());
						fi = foodInfoDao.queryFood(fi).get(0);
						if (fi.getCookby() == FoodInfo.COOKBYKITCHEN) {
							hasfood = true;
						} else {
							haswater = true;
						}
					}
					
					if (billDao.deleteBillTempByTables(params) > 0) {
						map.put(OSException.STATUS, OSException.SUCCESS);

						final String tableid = table.getId();

						if (hasfood) {
							new MQTTNotificationThread(objLock_TOPIC_KITCHEN, CommonUtils.TOPIC_KITCHEN, tableid).start();
						}

						if (haswater) {
							new MQTTNotificationThread(objLock_TOPIC_WATERBAR, CommonUtils.TOPIC_WATERBAR, tableid).start();
						}
						new MQTTNotificationThread(objLock_TOPIC_CASH, CommonUtils.TOPIC_CASH, tableid).start();
						new MQTTNotificationThread(tableid, CommonUtils.TOPIC_CUSTOMER + tableid, tableid).start();
					
					} else {
						map.put(OSException.STATUS, OSException.FAILURE);
					}
				} else {
					map.put(OSException.STATUS, OSException.PARAM_FAILURE);
				}
			}
			
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

}
