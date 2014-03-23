package com.bronze.ordersystem.common.versiongenerator;

import java.io.File;

import com.bronze.ordersystem.common.util.OSConfiguration;

public class DataFileProperty {

	public static final String TAG_START = "Start";
	public static final String TAG_END = "End";
	public static final String TAG_TOTAL = "Total";
	
	public static final String TAG_MENU_DETAIL = "details";
	public static final String TAG_MENU_DESCRIP = "Descrip";
	public static final String TAG_MENU_GENERAL_PHOTO = "General_Photo";
	public static final String TAG_MENU_DETAIL_PHOTO = "Detail_Photo";
	public static final String TAG_MENU_CATEGORY = "Category";
	public static final String TAG_MENU_INDEX = "Index";

	public static final String TAG_MENU_PRICE = "Price";
	public static final String TAG_MENU_NAME = "Name";
	public static final String TAG_MENU_RECOMMEND = "Recommend";
	public static final String TAG_MENU_ID = "Id";
	public static final String TAG_MENU_NUMBER = "Number";
	public static final String TAG_MENU_DEFAULT_COUNT = "count";

	
	public static final String TAG_USER_NAME = "Name";
	public static final String TAG_USER_PASSWORD = "Password";
	public static final String TAG_USER_CODE = "Code";

	public static final String TAG_REMARK_ALL = "Remark";
	public static final String TAG_REMARK_DISH = "Dish_Remark";
	
	public static final String TAG_CATEGORY_INDEX = "Index";
	public static final String TAG_CATEGORY_NAME = "Name";
	public static final String TAG_CATEGORY_ID = "Id";
	
	public static final String TAG_PRINTER_PRICE = "price";
	public static final String TAG_PRINTER_TYPE = "type";
	public static final String TAG_PRINTER_NAME = "name";
	
	public static final String TAG_CONFIG_CATEGORY_FILE = "Category_File_Path";
	public static final String TAG_CONFIG_MENU_FILE = "Menu_File_Path";
	public static final String TAG_CONFIG_DETAIL_DIR = "Detail_Photo_Directory";
	public static final String TAG_CONFIG_GENERAL_DIR = "General_Photo_Directory";
	public static final String TAG_CONFIG_REMARK_FILE = "Remark_File_Path";
	public static final String TAG_CONFIG_PRINTER_FILE = "Printer_File_Path";
	
	public static final String VALUE_CONNECTOR = ":";
	public static final String FILE_CONNECTOR = File.separator;
	
	public static final String DIR_NAME_DETAIL = "DetailPhotos";
	public static final String DIR_NAME_GENERAL = "GeneralPhotos";
	public static final String FILE_NAME_MENU = "Menu.odt";
	public static final String FILE_NAME_CATEGORY = "Category.odt";
	public static final String FILE_NAME_CONFIG = "Config.odt";
	public static final String FILE_NAME_REMARK = "Remark.odt";
	public static final String FILE_NAME_PRINTER = "Printer.odt";
	
	public static String getRootDir() {
		return OSConfiguration.WebAppRoot;
	}

}
