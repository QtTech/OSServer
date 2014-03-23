package com.bronze.ordersystem.food.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bronze.ordersystem.attachment.dao.IAttachmentDao;
import com.bronze.ordersystem.attachment.model.BillAttachment;
import com.bronze.ordersystem.bill.dao.IBillDao;
import com.bronze.ordersystem.bill.model.BillSpecialRemark;
import com.bronze.ordersystem.common.exception.OSException;
import com.bronze.ordersystem.common.util.CommonUtils;
import com.bronze.ordersystem.common.util.OSConfiguration;
import com.bronze.ordersystem.common.versiongenerator.BillRemarkWriter;
import com.bronze.ordersystem.common.versiongenerator.CategoryWriter;
import com.bronze.ordersystem.common.versiongenerator.ConfigWriter;
import com.bronze.ordersystem.common.versiongenerator.DataWriter;
import com.bronze.ordersystem.common.versiongenerator.MenuWriter;
import com.bronze.ordersystem.common.versiongenerator.PrinterWriter;
import com.bronze.ordersystem.food.dao.ICategoryListDao;
import com.bronze.ordersystem.food.dao.IFoodCategoryDao;
import com.bronze.ordersystem.food.dao.IFoodInfoDao;
import com.bronze.ordersystem.food.dao.IFoodOrderableDao;
import com.bronze.ordersystem.food.dao.IFoodRecommendDao;
import com.bronze.ordersystem.food.dao.IFoodSpecialRemarkDao;
import com.bronze.ordersystem.food.model.CategoryList;
import com.bronze.ordersystem.food.model.FoodCategory;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodOrderable;
import com.bronze.ordersystem.food.model.FoodRecommend;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;
import com.bronze.ordersystem.food.model.InsertFoodInfo;
import com.bronze.ordersystem.food.model.SelectedDishes;
import com.bronze.ordersystem.food.service.IMenuService;


@Service
public class MenuService implements IMenuService {

	protected final static Logger logger = Logger.getLogger(MenuService.class);
	
	private final static Object LOCKOBJ = new Object();
	
	@Resource
	private IFoodInfoDao foodInfoDao;
	@Resource
	private ICategoryListDao categoryListDao;
	@Resource
	private IBillDao billDao;
	
	@Resource
	private IFoodCategoryDao foodCategoryDao;
	@Resource
	private IFoodRecommendDao foodRecommendDao;
	@Resource
	private IFoodSpecialRemarkDao foodSpecialRemarkDao;
	@Resource
	private IFoodOrderableDao foodOrderableDao;
	@Resource
	private IAttachmentDao attachmentDao;

	@Override
	public Map<String, Object> generateConfiguration() {
		synchronized (LOCKOBJ) {
			Map<String, Object> map = new Hashtable<String, Object>();
			try {
				List<DataWriter> writerlist = new ArrayList<DataWriter>();
				
				List<CategoryList> categorylist = categoryListDao.queryCategoryList();
				List<BillSpecialRemark> billremarklist = billDao.queryBillSpecialRemark();
				List<BillAttachment> billattachmentlist = attachmentDao.queryAttachments();
				
				List<FoodInfo> foodlist = foodInfoDao.queryFood(new FoodInfo());
				Map<String, List<FoodRecommend>> foodRecommondMap = new Hashtable<String, List<FoodRecommend>>();
				Map<String, List<FoodCategory>> foodcategoryMap = new Hashtable<String, List<FoodCategory>>();
				Map<String, List<FoodSpecialRemark>> foodSpecialRemark = new Hashtable<String, List<FoodSpecialRemark>>();

				for (int i = 0; i < foodlist.size(); i++) {
					FoodInfo fi = foodlist.get(i);
					if (fi.getLargepic() == null || fi.getLargepic().equals("") ||
							fi.getSmallpic() == null || fi.getSmallpic().equals("")) {
						map.put(OSException.STATUS, OSException.FAILURE);
						return map;
					}
					
					List<FoodRecommend> frlist = foodRecommendDao.queryFoodRecommendByFoodId(foodlist.get(i));
					List<FoodCategory> fclist = foodCategoryDao.queryFoodCategoryByFoodId(foodlist.get(i));
					List<FoodSpecialRemark> fsrlist = foodSpecialRemarkDao.queryFoodSpecialRemarkByFoodId(foodlist.get(i));
					
					if (frlist != null && frlist.size() > 0) {
						foodRecommondMap.put(foodlist.get(i).getId(), frlist);
					}
					
					if (fclist != null && fclist.size() > 0) {
						foodcategoryMap.put(foodlist.get(i).getId(), fclist);
					}
					
					if (fsrlist != null && fsrlist.size() > 0) {
						foodSpecialRemark.put(foodlist.get(i).getId(), fsrlist);
					}
				}
				
				File rootdir = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.VersionDirectory);
				if (rootdir.exists()) {
					logger.info("delete rootdir " + rootdir.getName());
					CommonUtils.deleteDir(rootdir);
				}
				
				rootdir.mkdirs();
				
				writerlist.add(new ConfigWriter());
				writerlist.add(new CategoryWriter(categorylist));
				writerlist.add(new BillRemarkWriter(billremarklist));
				writerlist.add(new MenuWriter(foodlist, foodRecommondMap, foodcategoryMap, foodSpecialRemark));
				writerlist.add(new PrinterWriter(billattachmentlist));
				
				for (int i = 0; i < writerlist.size(); i++) {
					if (!writerlist.get(i).write()) {
						logger.info(writerlist.get(i).getLastErrorCode());
						logger.info(writerlist.get(i).getLastExtra() + " , Exception Message : " + writerlist.get(i).getLastExceptionMsg());
					}
				}
				
				CommonUtils.copyDirectory(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator + OSConfiguration.DETAILPHOTOS, 
						OSConfiguration.WebAppRoot + File.separator + OSConfiguration.VersionDirectory + File.separator + OSConfiguration.DETAILPHOTOS, true);
				CommonUtils.copyDirectory(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator + OSConfiguration.GENERALPHOTOS, 
						OSConfiguration.WebAppRoot + File.separator + OSConfiguration.VersionDirectory + File.separator + OSConfiguration.GENERALPHOTOS, true);
				CommonUtils.generateVersionNumber();
				map.put(OSException.DATA, CommonUtils.getLatestVersion());
				map.put(OSException.STATUS, OSException.SUCCESS);
			} catch (Exception e) {
				logger.error(e);
				map.clear();
				map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
			}
			return map;
		}
	}

	@Override
	public Map<String, Object> insertCategory(CategoryList cl) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = categoryListDao.insertCategory(cl);
			if (ret > 0) {
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
	public Map<String, Object> updateCategory(CategoryList cl) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = categoryListDao.updateCategory(cl);
			if (ret > 0) {
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
	public Map<String, Object> deleteCategory(CategoryList cl) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = categoryListDao.deleteCategory(cl);
			if (ret > 0) {
				List<FoodInfo> flist = foodInfoDao.queryFoodWithoutCategory();
				if (flist != null && flist.size() > 0) {
					for (FoodInfo fi : flist) {
						if (foodInfoDao.removeFood(fi) > 0) {
							File largepic = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator +
									fi.getLargepic());
							File smallpic = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator + 
									fi.getSmallpic());
							CommonUtils.deleteDir(largepic);
							CommonUtils.deleteDir(smallpic);
						}
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
	public Map<String, Object> insertBillRemark(BillSpecialRemark bsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = billDao.insertBillRemark(bsr);
			if (ret > 0) {
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
	public Map<String, Object> deleteBillRemark(BillSpecialRemark bsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = billDao.deleteBillRemark(bsr);
			if (ret > 0) {
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
	public Map<String, Object> updateBillRemark(BillSpecialRemark bsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = billDao.updateBillRemark(bsr);
			if (ret > 0) {
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
	public Map<String, Object> insertFoodRemark(FoodSpecialRemark fsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			fsr.setId(CommonUtils.generateUUID());
			int ret = foodSpecialRemarkDao.insertFoodRemark(fsr);
			if (ret > 0) {
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
	public Map<String, Object> deleteFoodRemark(FoodSpecialRemark fsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = foodSpecialRemarkDao.deleteFoodRemark(fsr);
			if (ret > 0) {
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
	public Map<String, Object> insertFoodRecommend(FoodRecommend fr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			if (fr.getFoodid() != null && fr.getRecommendid() != null && 
				!fr.getFoodid().equals("") &&
				fr.getFoodid().equals(fr.getRecommendid())) {
				map.put(OSException.STATUS, OSException.PARAM_FAILURE);
				return map;
			}
			
			fr.setId(CommonUtils.generateUUID());
			int ret = foodRecommendDao.insertFoodRemark(fr);
			if (ret > 0) {
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
	public Map<String, Object> deleteFoodRecommend(FoodRecommend fr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = foodRecommendDao.deleteFoodRemark(fr);
			if (ret > 0) {
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
	public synchronized Map<String, Object> insertFood(InsertFoodInfo ifi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			FoodInfo foodinfo = new FoodInfo();
			if (ifi.getId() == null || ifi.getId().equals("")) {
				int id = CommonUtils.getFoodID() + 1;
				CommonUtils.setFoodID(id);
				foodinfo.setId(String.valueOf(id));
			} else {
				foodinfo.setId(ifi.getId());
			}
			
			List<String> numbers = foodInfoDao.queryFoodNumber();
			
			foodinfo.setName(ifi.getName());
			foodinfo.setNumber(CommonUtils.generateIndex(numbers));
			foodinfo.setPrice(ifi.getPrice());
			foodinfo.setDescription(ifi.getDescription());
			foodinfo.setDetails(ifi.getDetails());
			foodinfo.setDefault_count(ifi.getDefault_count());
			foodinfo.setCookby(ifi.getCookby());
			
			if (foodInfoDao.addNewFood(foodinfo) > 0) {
				FoodOrderable fo = new FoodOrderable();
				fo.setId(CommonUtils.generateUUID());
				fo.setFoodid(foodinfo.getId());
				fo.setOrderable(ifi.getOrderable());
				foodOrderableDao.addNewFood(fo);
				
				if (ifi.getCategorylist() != null && ifi.getCategorylist().size() > 0) {
					List<FoodCategory> list = new ArrayList<FoodCategory>();
					for (int i = 0; i < ifi.getCategorylist().size(); i++) {
						FoodCategory fc = new FoodCategory();
						fc.setId(CommonUtils.generateUUID());
						fc.setFoodid(foodinfo.getId());
						fc.setCategoryid(Integer.parseInt(ifi.getCategorylist().get(i)));
						List<String> clist = foodCategoryDao.queryCategoryIndex(fc);
						fc.setIndex(CommonUtils.generateCategoryIndex(clist));
						list.add(fc);
					}
					
					foodCategoryDao.addCategoryList(list);
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
	public Map<String, Object> deleteFood(InsertFoodInfo ifi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			FoodInfo foodinfo = new FoodInfo();
			foodinfo.setId(ifi.getId());
			List<FoodCategory> clist = foodCategoryDao.queryFoodCategoryByFoodId(foodinfo);
			if (clist != null && clist.size() == 1) {
				List<FoodInfo> list = foodInfoDao.queryFood(foodinfo);
				if (list != null && list.size() > 0) {
					foodinfo = list.get(0);
					int ret = foodInfoDao.removeFood(foodinfo);
					if (ret > 0) {
						map.put(OSException.STATUS, OSException.SUCCESS);
						File largepic = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator +
								foodinfo.getLargepic());
						File smallpic = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator + 
								foodinfo.getSmallpic());
						CommonUtils.deleteDir(largepic);
						CommonUtils.deleteDir(smallpic);
					} else {
						map.put(OSException.STATUS, OSException.FAILURE);
					}
				} else {
					map.put(OSException.STATUS, OSException.FAILURE);
				}
			} else if (clist != null && clist.size() > 1) {
				FoodCategory fc = new FoodCategory();
				fc.setFoodid(ifi.getId());
				fc.setCategoryid(Integer.parseInt(ifi.getCategorylist().get(0)));
				if (foodCategoryDao.deleteCategoryByCID(fc) > 0) {
					map.put(OSException.STATUS, OSException.SUCCESS);
				} else {
					map.put(OSException.STATUS, OSException.FAILURE);
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
	public Map<String, Object> updateFoodPhoto(FoodInfo fi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodInfo> list = foodInfoDao.queryFood(fi);
			if (list != null && list.size() > 0) {
				fi.setName(list.get(0).getName());
				if (fi.getLargepic() != null && !fi.getLargepic().equals("")) {
					fi.setLargepic(CommonUtils.BASE64URLDecoder(fi.getLargepic()));
				}
				
				if (fi.getSmallpic() != null && !fi.getSmallpic().equals("")) {
					fi.setSmallpic(CommonUtils.BASE64URLDecoder(fi.getSmallpic()));
				}
				
				if (fi.getLargepic().startsWith(OSConfiguration.TMPDirectory)) {
					CommonUtils.copyFile((OSConfiguration.WebAppRoot + File.separator + fi.getLargepic()), 
							(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator +
									OSConfiguration.DETAILPHOTOS + File.separator + fi.getName()), true);
					fi.setLargepic(OSConfiguration.DETAILPHOTOS + File.separator + fi.getName());
				} else if (fi.getLargepic().equals("")) {
					logger.info("Delete Detail Photo " + fi.getName());
					File file = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator +
							OSConfiguration.DETAILPHOTOS + File.separator + fi.getName());
					if (file != null && file.exists()) {
						CommonUtils.deleteDir(file);
					}
				} else {
					fi.setLargepic(null);
				}
				
				if (fi.getSmallpic().startsWith(OSConfiguration.TMPDirectory)) {
					CommonUtils.copyFile((OSConfiguration.WebAppRoot + File.separator + fi.getSmallpic()), 
							(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator +
									OSConfiguration.GENERALPHOTOS + File.separator + fi.getName()), true);
					fi.setSmallpic(OSConfiguration.GENERALPHOTOS + File.separator + fi.getName());
				} else if (fi.getSmallpic().equals("")) {
					logger.info("Delete General Photo " + fi.getName());
					File file = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator +
							OSConfiguration.GENERALPHOTOS + File.separator + fi.getName());
					if (file != null && file.exists()) {
						CommonUtils.deleteDir(file);
					}
				} else {
					fi.setSmallpic(null);
				}
				
				if (!(fi.getSmallpic() == null && fi.getLargepic() == null)) {
					int ret = foodInfoDao.updateFoodPhoto(fi);
					if (ret > 0) {
						map.put(OSException.STATUS, OSException.SUCCESS);
					} else {
						map.put(OSException.STATUS, OSException.FAILURE);
					}
				} else {
					map.put(OSException.STATUS, OSException.SUCCESS);
				}
			} else {
				logger.info("Food " + fi.getId() + " doesn't exist!");
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
	public Map<String, Object> updateFood(InsertFoodInfo ifi) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			FoodInfo fi = new FoodInfo();
			fi.setId(ifi.getId());
			fi.setName(ifi.getName());
			fi.setNumber(ifi.getNumber());
			fi.setPrice(ifi.getPrice());
			fi.setDefault_count(ifi.getDefault_count());
			fi.setCookby(ifi.getCookby());
			fi.setDescription(ifi.getDescription());
			fi.setDetails(ifi.getDetails());
			foodInfoDao.updateFood(fi);
			
			FoodOrderable fo = new FoodOrderable();
			fo.setFoodid(ifi.getId());
			fo.setOrderable(ifi.getOrderable());
			foodOrderableDao.updateOrderable(fo);
			
			foodCategoryDao.deleteCategoryByFoodID(fi);
			if (ifi.getCategorylist() != null && ifi.getCategorylist().size() > 0) {
				List<FoodCategory> list = new ArrayList<FoodCategory>();
				for (int i = 0; i < ifi.getCategorylist().size(); i++) {
					FoodCategory fc = new FoodCategory();
					fc.setId(CommonUtils.generateUUID());
					fc.setFoodid(ifi.getId());
					fc.setCategoryid(Integer.parseInt(ifi.getCategorylist().get(i)));
					List<String> clist = foodCategoryDao.queryCategoryIndex(fc);
					fc.setIndex(CommonUtils.generateCategoryIndex(clist));
					list.add(fc);
				}
				
				foodCategoryDao.addCategoryList(list);
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
	public Map<String, Object> updateFoodRecommend(FoodRecommend fr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<FoodRecommend> list = foodRecommendDao.queryFoodrecommendListById(fr);
			if (list == null || list.size() == 0) {
				map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
				return map;
			}
			
			fr.setFoodid(list.get(0).getFoodid());
			if (fr.getFoodid() != null && fr.getRecommendid() != null && 
				!fr.getFoodid().equals("") &&
				fr.getFoodid().equals(fr.getRecommendid())) {
				map.put(OSException.STATUS, OSException.PARAM_FAILURE);
				return map;
			}
			
			int ret = foodRecommendDao.updateFoodRecommend(fr);
			if (ret > 0) {
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
	public Map<String, Object> updateFoodRemark(FoodSpecialRemark fsr) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int ret = foodSpecialRemarkDao.updateFoodRemark(fsr);
			if (ret > 0) {
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
	public Map<String, Object> deleteSelectedDishes(SelectedDishes sds) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			List<String> disheids = sds.getDishes();
			for (String id : disheids) {
				FoodInfo foodinfo = new FoodInfo();
				foodinfo.setId(id);
				List<FoodCategory> clist = foodCategoryDao.queryFoodCategoryByFoodId(foodinfo);
				
				if (clist != null && clist.size() == 1) {
					List<FoodInfo> list = foodInfoDao.queryFood(foodinfo);
					if (list != null && list.size() > 0) {
						foodinfo = list.get(0);
						int ret = foodInfoDao.removeFood(foodinfo);
						if (ret > 0) {
							map.put(OSException.STATUS, OSException.SUCCESS);
							File largepic = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator +
									foodinfo.getLargepic());
							File smallpic = new File(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.IMGDirectory + File.separator + 
									foodinfo.getSmallpic());
							CommonUtils.deleteDir(largepic);
							CommonUtils.deleteDir(smallpic);
						} else {
							map.put(OSException.STATUS, OSException.FAILURE);
						}
					} else {
						map.put(OSException.STATUS, OSException.FAILURE);
					}
				} else if (clist != null && clist.size() > 1) {
					FoodCategory fc = new FoodCategory();
					fc.setFoodid(id);
					fc.setCategoryid(sds.getOrgcategoryid());
					if (foodCategoryDao.deleteCategoryByCID(fc) > 0) {
						map.put(OSException.STATUS, OSException.SUCCESS);
					} else {
						map.put(OSException.STATUS, OSException.FAILURE);
					}
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
	public Map<String, Object> updateDishesCategory(SelectedDishes sds) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			if (sds.getType() == SelectedDishes.TYPE_COPY) {
				List<FoodCategory> addlist = new ArrayList<FoodCategory>();
				FoodCategory fc = new FoodCategory();
				fc.setCategoryid(sds.getCategoryid());
				
				List<String> cilist = foodCategoryDao.queryCategoryIndex(fc);
				int index = CommonUtils.generateCategoryIndex(cilist);
				
				for (String id : sds.getDishes()) {
					FoodCategory fi = new FoodCategory();
					fi.setCategoryid(sds.getCategoryid());
					fi.setFoodid(id);
					List<FoodCategory> list = foodCategoryDao.queryCategoryByCID(fi);

					if (list != null && list.size() > 0) {
						continue;
					} else {
						FoodCategory fcs = new FoodCategory();
						fcs.setId(CommonUtils.generateUUID());
						fcs.setFoodid(id);
						fcs.setCategoryid(sds.getCategoryid());
						fcs.setIndex(index++);
						addlist.add(fcs);
					}
				} 
				
				foodCategoryDao.addCategoryList(addlist);
				map.put(OSException.STATUS, OSException.SUCCESS);
			} else if (sds.getType() == SelectedDishes.TYPE_MOVE) {
				List<FoodCategory> addlist = new ArrayList<FoodCategory>();
				FoodCategory fc = new FoodCategory();
				fc.setCategoryid(sds.getCategoryid());
				
				List<String> cilist = foodCategoryDao.queryCategoryIndex(fc);
				int index = CommonUtils.generateCategoryIndex(cilist);
				for (String id : sds.getDishes()) {
					FoodCategory fi = new FoodCategory();
					fi.setCategoryid(sds.getCategoryid());
					fi.setFoodid(id);
					List<FoodCategory> list = foodCategoryDao.queryCategoryByCID(fi);

					if (list != null && list.size() > 0) {
						continue;
					} else {
						FoodCategory fcs = new FoodCategory();
						fcs.setId(CommonUtils.generateUUID());
						fcs.setFoodid(id);
						fcs.setCategoryid(sds.getOrgcategoryid());
						foodCategoryDao.deleteCategoryByCID(fcs);
						fcs.setCategoryid(sds.getCategoryid());
						fcs.setIndex(index++);
						addlist.add(fcs);
					}
				} 
				
				foodCategoryDao.addCategoryList(addlist);
				map.put(OSException.STATUS, OSException.SUCCESS);
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
	public Map<String, Object> updateMenuOrders(SelectedDishes sds) {
		Map<String, Object> map = new Hashtable<String, Object>();
		try {
			int index = 0;
			for (String dishid : sds.getDishes()) {
				index++;
				FoodCategory fc = new FoodCategory();
				fc.setIndex(index);
				fc.setCategoryid(sds.getCategoryid());
				fc.setFoodid(dishid);
				foodCategoryDao.updateIndex(fc);
			}
			map.put(OSException.STATUS, OSException.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			map.clear();
			map.put(OSException.STATUS, OSException.CODE_EXCEPTION);
		}
		return map;
	}

}
