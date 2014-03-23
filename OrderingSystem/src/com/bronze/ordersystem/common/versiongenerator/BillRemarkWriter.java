package com.bronze.ordersystem.common.versiongenerator;

import java.io.FileOutputStream;
import java.util.List;

import com.bronze.ordersystem.bill.model.BillSpecialRemark;
import com.bronze.ordersystem.common.util.OSConfiguration;

public class BillRemarkWriter extends DataWriter {
	
	private List<BillSpecialRemark> rmks;
	
	public BillRemarkWriter(List<BillSpecialRemark> rmks) {
		super();
		this.rmks = rmks;
		m_Error_Extra = ErrorCode.EXTRA_CATEGORY;
	}

	@Override
	protected boolean writeContent(FileOutputStream fOut) {
		if(rmks != null && rmks.size() > 0) {
			try {
				fOut.write(getByte(
						DataFileProperty.TAG_TOTAL,
						String.valueOf(rmks.size()),
						true));
				for(int i = 0; i < rmks.size(); i++) {
					fOut.write(getByte(
							DataFileProperty.TAG_START,
							true));
					fOut.write(getByte(
							DataFileProperty.TAG_REMARK_ALL,
							rmks.get(i).getName(),
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
			   DataFileProperty.FILE_CONNECTOR + DataFileProperty.FILE_NAME_REMARK;
	}

}
