package com.bronze.ordersystem.food.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.bill.dao.IBillDao;
import com.bronze.ordersystem.bill.model.BillSpecialRemark;
import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.MQTTNotificationThread;
import com.bronze.ordersystem.food.dao.ICategoryListDao;
import com.bronze.ordersystem.food.dao.IFoodCategoryDao;
import com.bronze.ordersystem.food.dao.IFoodInfoDao;
import com.bronze.ordersystem.food.dao.IFoodOrderableDao;
import com.bronze.ordersystem.food.dao.IFoodRecommendDao;
import com.bronze.ordersystem.food.dao.IFoodSpecialRemarkDao;
import com.bronze.ordersystem.food.model.CategoryList;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodOrderable;
import com.bronze.ordersystem.food.model.FoodRecommend;
import com.bronze.ordersystem.food.model.FoodRecommendWithName;
import com.bronze.ordersystem.food.model.FoodRemarkWithName;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;
import com.bronze.ordersystem.food.model.InsertFoodInfo;
import com.bronze.ordersystem.food.model.SimpleFoodInfo;
import com.bronze.ordersystem.food.service.IFoodService;

@Service
public class FoodService implements IFoodService {
	
	protected final static Logger logger = Logger.getLogger(FoodService.class);
	
	@Resource
	private ICategoryListDao categoryListDao;
	@Resource
	private IFoodOrderableDao foodOrderableDao;
	@Resource
	private IBillDao billDao;
	@Resource
	private IFoodInfoDao foodInfoDao;
	@Resource
	private IFoodSpecialRemarkDao foodSpecialRemarkDao;
	@Resource
	private IFoodRecommendDao foodRecommendDao;
	@Resource
	private IFoodCategoryDao foodCategoryDao;

	@Override
	public Map<String, Object> queryCategoryList() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<CategoryList> list = categoryListDao.queryCategoryList();
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryOrdered(List<FoodOrderable> foodlist) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodOrderable> list = null;
			if (foodlist == null || foodlist.size() == 0) {
				list = foodOrderableDao.queryOrderedAll();
			} else {
				list = foodOrderableDao.queryOrdered(foodlist);
			}
			
			if (list != null && list.size() > 0) {
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
	public Map<String, Object> updateOrderable(List<FoodOrderable> foodList) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = 0;
			for (int i = 0; i < foodList.size(); i++) {
				ret += foodOrderableDao.updateOrderable(foodList.get(i));
			}
			
			if (ret == foodList.size()) {
				map.put(OSException.STATUS, OSException.SUCCESS);
				new MQTTNotificationThread(CommonUtils.TOPIC_ORDERED, CommonUtils.TOPIC_ORDERED, String.valueOf(ret)).start();
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
	public Map<String, Object> queryBillmarkList() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<BillSpecialRemark> list = billDao.queryBillSpecialRemark();
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryMenuList() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodInfo> list = foodInfoDao.queryFood(new FoodInfo());
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryFoodmarkList() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodRemarkWithName> list = foodSpecialRemarkDao.queryFoodSpecialRemark();
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> querySimpleFoodList() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<SimpleFoodInfo> list = foodInfoDao.querySimpleFoodList();
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryFoodrecommendList() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			Map<String, String> nameMap = new Hashtable<String, String>();
			List<FoodRecommend> list = foodRecommendDao.queryFoodrecommendList();
			
			List<String> idlist = new ArrayList<String>();
			Set<String> idset = new HashSet<String>();

			for (int i = 0; i < list.size(); i++) {
				idlist.add(list.get(i).getFoodid());
				idlist.add(list.get(i).getRecommendid());
			}
			
			idset.addAll(idlist);
			
			List<SimpleFoodInfo> slist = foodInfoDao.queryFoodNameMap(Arrays.asList(idset.toArray(new String[0])));
			for (int i = 0; i < slist.size(); i++) {
				if (!nameMap.containsKey(slist.get(i).getId())) {
					nameMap.put(slist.get(i).getId(), slist.get(i).getName());
				}
			}
			
			List<FoodRecommendWithName> namelist = new ArrayList<FoodRecommendWithName>();
			for (int i = 0; i < list.size(); i++) {
				FoodRecommendWithName frwn = new FoodRecommendWithName();
				frwn.setId(list.get(i).getId());
				frwn.setFoodid(list.get(i).getFoodid());
				frwn.setFoodname(nameMap.get(list.get(i).getFoodid()));
				frwn.setRecommendid(list.get(i).getRecommendid());
				frwn.setRecommendname(nameMap.get(list.get(i).getRecommendid()));
				namelist.add(frwn);
			}
			
			if (namelist.size() > 0) {
				map.put(OSException.DATA, namelist);
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
	public Map<String, Object> queryFoodByID(FoodInfo fi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodInfo> list = foodInfoDao.queryFood(fi);
			if (list != null && list.size() > 0) {
				if (list.get(0).getLargepic() != null && !list.get(0).getLargepic().equals("")) {
					list.get(0).setLargepic(CommonUtils.BASE64URLEncoder(list.get(0).getLargepic()));
				}
				
				if (list.get(0).getSmallpic() != null && !list.get(0).getSmallpic().equals("")) {
					list.get(0).setSmallpic(CommonUtils.BASE64URLEncoder(list.get(0).getSmallpic()));
				}
				
				map.put(OSException.DATA, list.get(0));
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
	public Map<String, Object> queryFoodAllInfoByID(FoodInfo fi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodInfo> list = foodInfoDao.queryFood(fi);
			List<FoodOrderable> olist = foodOrderableDao.queryFoodOrderableByFoodId(fi.getId());
			if (list != null && list.size() > 0 && olist != null && olist.size() > 0) {
				List<String> cList = foodCategoryDao.queryFoodCategoryByFoodID(fi);
				InsertFoodInfo ifi = new InsertFoodInfo();
				ifi.setId(list.get(0).getId());
				ifi.setName(list.get(0).getName());
				ifi.setCookby(list.get(0).getCookby());
				ifi.setDefault_count(list.get(0).getDefault_count());
				ifi.setDescription(list.get(0).getDescription());
				ifi.setDetails(list.get(0).getDetails());
				ifi.setNumber(list.get(0).getNumber());
				ifi.setPrice(list.get(0).getPrice());
				ifi.setOrderable(olist.get(0).getOrderable());
				ifi.setCategorylist(cList);
				map.put(OSException.DATA, ifi);
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
	public Map<String, Object> queryCategoryListByID(CategoryList cl) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<CategoryList> list = categoryListDao.queryCategoryListByID(cl);
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryFoodrecommendListByName(FoodRecommend fr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			Map<String, String> nameMap = new Hashtable<String, String>();
			List<FoodRecommend> list = foodRecommendDao.queryFoodrecommendListByName(fr);
			
			List<String> idlist = new ArrayList<String>();
			Set<String> idset = new HashSet<String>();

			for (int i = 0; i < list.size(); i++) {
				idlist.add(list.get(i).getFoodid());
				idlist.add(list.get(i).getRecommendid());
			}
			
			idset.addAll(idlist);
			
			List<SimpleFoodInfo> slist = foodInfoDao.queryFoodNameMap(Arrays.asList(idset.toArray(new String[0])));
			for (int i = 0; i < slist.size(); i++) {
				if (!nameMap.containsKey(slist.get(i).getId())) {
					nameMap.put(slist.get(i).getId(), slist.get(i).getName());
				}
			}
			
			List<FoodRecommendWithName> namelist = new ArrayList<FoodRecommendWithName>();
			for (int i = 0; i < list.size(); i++) {
				FoodRecommendWithName frwn = new FoodRecommendWithName();
				frwn.setId(list.get(i).getId());
				frwn.setFoodid(list.get(i).getFoodid());
				frwn.setFoodname(nameMap.get(list.get(i).getFoodid()));
				frwn.setRecommendid(list.get(i).getRecommendid());
				frwn.setRecommendname(nameMap.get(list.get(i).getRecommendid()));
				namelist.add(frwn);
			}
			
			if (namelist.size() > 0) {
				map.put(OSException.DATA, namelist);
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
	public Map<String, Object> queryBillmarkListByID(BillSpecialRemark bsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<BillSpecialRemark> list = billDao.queryBillmarkListByID(bsr);
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryMenuListByName(FoodInfo fi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodInfo> list = foodInfoDao.queryMenuListByName(fi);
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryFoodmarkListByName(FoodSpecialRemark fsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodRemarkWithName> list = foodSpecialRemarkDao.queryFoodmarkListByName(fsr);
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryFoodRecommendName(FoodRecommend fr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			Map<String, String> nameMap = new Hashtable<String, String>();
			List<FoodRecommend> list = foodRecommendDao.queryFoodrecommendListById(fr);
			
			List<String> idlist = new ArrayList<String>();
			Set<String> idset = new HashSet<String>();

			for (int i = 0; i < list.size(); i++) {
				idlist.add(list.get(i).getFoodid());
				idlist.add(list.get(i).getRecommendid());
			}
			
			idset.addAll(idlist);
			
			List<SimpleFoodInfo> slist = foodInfoDao.queryFoodNameMap(Arrays.asList(idset.toArray(new String[0])));
			for (int i = 0; i < slist.size(); i++) {
				if (!nameMap.containsKey(slist.get(i).getId())) {
					nameMap.put(slist.get(i).getId(), slist.get(i).getName());
				}
			}
			
			FoodRecommendWithName frwn = new FoodRecommendWithName();
			frwn.setId(list.get(0).getId());
			frwn.setFoodid(list.get(0).getFoodid());
			frwn.setFoodname(nameMap.get(list.get(0).getFoodid()));
			frwn.setRecommendid(list.get(0).getRecommendid());
			frwn.setRecommendname(nameMap.get(list.get(0).getRecommendid()));
			
			map.put(OSException.DATA, frwn);
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryFoodRemarkName(FoodSpecialRemark fsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodRemarkWithName> list = foodSpecialRemarkDao.queryFoodRemarkName(fsr);
			if (list != null && list.size() == 1) {
				map.put(OSException.DATA, list.get(0));
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
	public Map<String, Object> queryMenuListByCID(SimpleFoodInfo sfi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodInfo> list = foodInfoDao.queryMenuListByCID(sfi);
			if (list != null) {
				map.put(OSException.DATA, list);
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
	public Map<String, Object> queryFoodNumbers() {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<String> list = foodInfoDao.queryFoodNumber();
			if (list != null) {
				map.put(OSException.DATA, CommonUtils.generateIndex(list));
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

}
