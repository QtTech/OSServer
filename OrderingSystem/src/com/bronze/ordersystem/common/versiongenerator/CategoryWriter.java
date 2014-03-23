package com.bronze.ordersystem.common.versiongenerator;

import java.io.FileOutputStream;
import java.util.List;

import com.bronze.ordersystem.common.util.OSConfiguration;
import com.bronze.ordersystem.food.model.CategoryList;

public class CategoryWriter extends DataWriter {

	private List<CategoryList> items;
	
	public CategoryWriter(List<CategoryList> items) {
		super();
		this.items = items;
		m_Error_Extra = ErrorCode.EXTRA_CATEGORY;
	}

	@Override
	protected boolean writeContent(FileOutputStream fOut) {
		if(items != null && items.size() > 0) {
			try {
				fOut.write(getByte(
						DataFileProperty.TAG_TOTAL,
						String.valueOf(items.size()),
						true));
				for(int i = 0 ; i < items.size() ; i++) {
					fOut.write(getByte(
							DataFileProperty.TAG_START,
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_CATEGORY_ID,
							String.valueOf(items.get(i).getId()),
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_CATEGORY_INDEX,
							String.valueOf("-1"),
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_CATEGORY_NAME,
							String.valueOf(items.get(i).getName()),
							true));					
					fOut.write(getByte(
							DataFileProperty.TAG_END,
							true));
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

	@Override
	protected String getFilePath() {
		return OSConfiguration.WebAppRoot + DataFileProperty.FILE_CONNECTOR + OSConfiguration.VersionDirectory + 
			   DataFileProperty.FILE_CONNECTOR + DataFileProperty.FILE_NAME_CATEGORY;
	}
}
