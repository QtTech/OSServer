package com.bronze.ordersystem.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AccessKeyInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(AccessKeyInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			logger.info("AccessKeyInterceptor preHandle URL : " + request.getRequestURI());
		} catch (Exception e) {
			logger.error("AccessKeyInterceptor preHandle Error URL");
		}
		return super.preHandle(request, response, handler);
	}

}
