package com.bronze.ordersystem.common.versiongenerator;

import java.io.FileOutputStream;

import com.bronze.ordersystem.common.util.OSConfiguration;

public class ConfigWriter extends DataWriter {

	public ConfigWriter() {
		m_Error_Extra = ErrorCode.EXTRA_CONFIG;
	}

	protected boolean writeContent(FileOutputStream out) {
		try {
			out.write(getByte(
					DataFileProperty.TAG_CONFIG_CATEGORY_FILE,
					DataFileProperty.FILE_NAME_CATEGORY,
					true));
			out.write(getByte(
					DataFileProperty.TAG_CONFIG_GENERAL_DIR,
					DataFileProperty.DIR_NAME_GENERAL,
					true));
			out.write(getByte(
					DataFileProperty.TAG_CONFIG_DETAIL_DIR,
					DataFileProperty.DIR_NAME_DETAIL,
					true));
			out.write(getByte(
					DataFileProperty.TAG_CONFIG_MENU_FILE,
					DataFileProperty.FILE_NAME_MENU,
					true));
			out.write(getByte(
					DataFileProperty.TAG_CONFIG_REMARK_FILE,
					DataFileProperty.FILE_NAME_REMARK,
					true));
			out.write(getByte(
					DataFileProperty.TAG_CONFIG_PRINTER_FILE,
					DataFileProperty.FILE_NAME_PRINTER,
					true));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	protected String getFilePath() {
		return 	OSConfiguration.WebAppRoot + DataFileProperty.FILE_CONNECTOR + OSConfiguration.VersionDirectory + 
				DataFileProperty.FILE_CONNECTOR + DataFileProperty.FILE_NAME_CONFIG;
	}

}
