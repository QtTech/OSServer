package com.bronze.ordersystem.common.versiongenerator;

import java.io.FileOutputStream;
import java.util.List;

import com.bronze.ordersystem.attachment.model.BillAttachment;
import com.bronze.ordersystem.common.util.OSConfiguration;

public class PrinterWriter extends DataWriter {

	private List<BillAttachment> bas;
	
	public PrinterWriter(List<BillAttachment> bas) {
		super();
		this.bas = bas;
		m_Error_Extra = ErrorCode.EXTRA_PRINTER;
	}

	@Override
	protected boolean writeContent(FileOutputStream fOut) {
		if(bas != null && bas.size() > 0) {
			try {
				fOut.write(getByte(
						DataFileProperty.TAG_TOTAL,
						String.valueOf(bas.size()),
						true));
				for(int i = 0 ; i < bas.size() ; i++) {
					fOut.write(getByte(
							DataFileProperty.TAG_START,
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_PRINTER_PRICE,
							String.valueOf(bas.get(i).getPrice()),
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_PRINTER_TYPE,
							String.valueOf(bas.get(i).getType()),
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_PRINTER_NAME,
							bas.get(i).getName(),
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
				   DataFileProperty.FILE_CONNECTOR + DataFileProperty.FILE_NAME_PRINTER;
	}
	
}
