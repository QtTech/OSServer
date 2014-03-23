package com.bronze.ordersystem.common.util;

public abstract class SerialNumber {
	
	public synchronized String getSerialNumber() {
        return process();
    }

	protected abstract String process();
	
}
