package com.bronze.ordersystem.common.util;

import java.io.File;

public class OSConfiguration {
	public static final String DETAILPHOTOS = "DetailPhotos";
	
	public static final String GENERALPHOTOS = "GeneralPhotos";
	
	public static final String EveryDaySerialNumberFile = "EveryDaySerialNumber.dat";
	
	public static final String VersionSerialNumberFile = "VersionSerialNumberFile.dat";
	
	public static final String SkinStyleFile = "SkinStyleFile.dat";
	
	public static final String FoodIDFile = "FoodIDFile.dat";
	
	public static final String VersionDirectory = "backup";
	
	public static final String IMGDirectory = "img";
	
	public static final String TMPDirectory = "tmp";
	
	public static String WebAppRoot = new File(System.getProperty("webapp.root")).getParent() + File.separator
			+ "ROOT" + File.separator + "OrderingSystem";
	
	public static final String URL_SEPERATOR = "/";
	
	public static final String CONTENT_TYPE = "UTF-8";
	
	public static final int MAX_TIMER_LENGTH = 19;
	
	public static final int SKIN_MIN 	 = 0;
	public static final int SKIN_CHINESE = 1;
	public static final int SKIN_KOREAN  = 2;
	public static final int SKIN_WESTERN = 3;
	public static final int SKIN_MAX 	 = SKIN_WESTERN + 1;
}
