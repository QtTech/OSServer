package com.bronze.ordersystem.common.versiongenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.bronze.ordersystem.common.util.OSConfiguration;
import com.bronze.ordersystem.food.model.FoodCategory;
import com.bronze.ordersystem.food.model.FoodInfo;
import com.bronze.ordersystem.food.model.FoodRecommend;
import com.bronze.ordersystem.food.model.FoodSpecialRemark;

public class MenuWriter extends DataWriter {

	private List<FoodInfo> foodlist;
	
	private Map<String, List<FoodCategory>> foodcategoryMap;
	
	private Map<String, List<FoodRecommend>> foodRecommondMap;
	
	private Map<String, List<FoodSpecialRemark>> foodSpecialRemark;
	
	public MenuWriter(List<FoodInfo> foodlist, Map<String, List<FoodRecommend>> foodRecommondMap, 
			Map<String, List<FoodCategory>> foodcategoryMap, 
			Map<String, List<FoodSpecialRemark>> foodSpecialRemark) {
		super();
		m_Error_Extra = ErrorCode.EXTRA_MENU;
		this.foodlist = foodlist;
		this.foodRecommondMap = foodRecommondMap;
		this.foodcategoryMap = foodcategoryMap;
		this.foodSpecialRemark = foodSpecialRemark;
	}

	@Override
	protected boolean writeContent(FileOutputStream fOut) {
		if(foodlist != null && foodlist.size() > 0) {
			try {
				fOut.write(getByte(
						DataFileProperty.TAG_TOTAL,
						String.valueOf(foodlist.size()),
						true));
				for(int i = 0 ; i < foodlist.size() ; i++) {
					if(!writeDishItem(fOut,foodlist.get(i)))
					{
						return false;
					}
				}
			} catch(Exception e) {
				m_ExceptionMsg = e.getMessage();
				m_Error_Code = ErrorCode.ERROR_WRITE_FILE;
				return false;
			}
			return true;
		} else {
			return true;
		}
	}
	
	protected boolean writeDishItem(FileOutputStream fOut,FoodInfo dish) {
		try {
			fOut.write(getByte(
					DataFileProperty.TAG_START,
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_NAME,
					dish.getName(),
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_DESCRIP,
					dish.getDescription(),
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_DETAIL,
					dish.getDetails(),
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_NUMBER,
					dish.getNumber(),
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_DEFAULT_COUNT,
					String.valueOf(dish.getDefault_count()),
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_ID,
					String.valueOf(dish.getId()),
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_PRICE,
					String.valueOf(dish.getPrice()),
					true));
			if(!writeDishPhoto(fOut, dish))
					return false;
			if(!writeDishRecommends(fOut, foodRecommondMap.get(dish.getId())))
				return false;
			if(!writeDishRemarks(fOut, foodSpecialRemark.get(dish.getId())))
				return false;
			if(!writeDishCategories(fOut, foodcategoryMap.get(dish.getId())))
				return false;
			fOut.write(getByte(
					DataFileProperty.TAG_END,
					true));
		} catch (IOException e) {
			m_ExceptionMsg = e.getMessage();
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected boolean writeDishPhoto(FileOutputStream fOut,FoodInfo dish) {
		try {
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_DETAIL_PHOTO,
					dish.getLargepic().substring(OSConfiguration.DETAILPHOTOS.length() + 1),
					true));
			fOut.write(getByte(
					DataFileProperty.TAG_MENU_GENERAL_PHOTO,
					dish.getSmallpic().substring(OSConfiguration.GENERALPHOTOS.length() + 1),
					true));
		} catch (IOException e) {	
			m_ExceptionMsg = e.getMessage();
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	protected boolean writeDishRecommends(FileOutputStream fOut, List<FoodRecommend> recs) {
		if(recs != null) {
			for(int i = 0 ; i < recs.size() ; i++) {
				try {
					fOut.write(getByte(
							DataFileProperty.TAG_MENU_RECOMMEND,
							String.valueOf(recs.get(i).getRecommendid()),
							true));
				} catch (IOException e) {	
					m_ExceptionMsg = e.getMessage();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}

	protected boolean writeDishRemarks(FileOutputStream fOut,List<FoodSpecialRemark> rmks) {
		if(rmks != null) {
			for(int i = 0; i < rmks.size(); i++) {
				try {
					fOut.write(getByte(
							DataFileProperty.TAG_REMARK_DISH,
							rmks.get(i).getName(),
							true));
				} catch (IOException e) {	
					m_ExceptionMsg = e.getMessage();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}
	
	protected boolean writeDishCategories(FileOutputStream fOut, List<FoodCategory> ctgs) {
		if(ctgs != null) {
			for(int i = 0 ; i < ctgs.size() ; i++) {
				try {
					fOut.write(getByte(
							DataFileProperty.TAG_MENU_CATEGORY,
							String.valueOf(ctgs.get(i).getCategoryid()),
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_MENU_INDEX,
							String.valueOf(ctgs.get(i).getIndex()),
							true));
				} catch (IOException e) {	
					m_ExceptionMsg = e.getMessage();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}
	
	@Override
	protected String getFilePath() {
		return OSConfiguration.WebAppRoot + DataFileProperty.FILE_CONNECTOR + OSConfiguration.VersionDirectory + 
			   DataFileProperty.FILE_CONNECTOR + DataFileProperty.FILE_NAME_MENU;
	}

}
