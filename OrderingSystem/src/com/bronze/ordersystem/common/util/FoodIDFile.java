package com.bronze.ordersystem.common.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FoodIDFile {
	private File file;

	public FoodIDFile(String filename) {
		super();
		this.file = new File(filename);
	}
	
	public int getFoodID() {
		int style = 0;
		if (file.exists() && file.isFile()) {
			List<String> list = FileUtil.readList(file);
			if (list != null && list.size() > 0) {
				style = Integer.parseInt(list.get(0).trim());
			}
		}
		return style;
	}
	
	public void setFoodID(int id) {
		if (!file.exists() || !file.isFile()) {
			if (file.exists()) {
				file.delete();
			}
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileUtil.rewrite(file, String.valueOf(id));
	}
	
	public boolean exists() {
		if (file == null || !file.exists() || !file.isFile()) {
			return false;
		} else {
			return true;
		}
 	}
}
