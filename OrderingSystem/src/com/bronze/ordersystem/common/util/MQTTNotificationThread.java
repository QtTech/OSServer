package com.bronze.ordersystem.common.util;

public class MQTTNotificationThread extends Thread {

	private Object lock;
	private String topic;
	private String message;
	
	public MQTTNotificationThread(Object lock, String topic, String message) {
		super();
		this.lock = lock;
		this.topic = topic;
		this.message = message;
	}

	@Override
	public void run() {
		synchronized (this.lock) {
			try {
				Thread.sleep(100);
				CommonUtils.sendMQTTNotification(this.topic, this.message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
}
