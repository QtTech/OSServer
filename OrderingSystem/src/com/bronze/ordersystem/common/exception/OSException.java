package com.bronze.ordersystem.common.exception;

public class OSException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int PARAM_FAILURE = 2;
	public static final int CODE_EXCEPTION = 3;
	
	public static final String DATA = "data";
	public static final String STATUS = "status";
	public static final String OTHERS = "others";
}
