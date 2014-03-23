package com.bronze.ordersystem.common.versiongenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class DataWriter {

	protected String m_Error_Extra = null;
	protected String m_ExceptionMsg = null;
	protected int m_Error_Code = -1;
	protected static final String ENTRY_SPLITOR = "\r\n";
	protected abstract boolean writeContent(FileOutputStream fOut);
	protected abstract String getFilePath();
	
	public DataWriter() {
		
	}
	
	public boolean write() {
		String path = getFilePath();
		if (!prepareFile(path)) {
			m_Error_Code = ErrorCode.ERROR_CREATE_FILE;
			return false;
		} else {
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(path);
			} catch (FileNotFoundException e1) {
				m_Error_Code = ErrorCode.ERROR_CREATE_FILE;
				m_ExceptionMsg = e1.getMessage();
				e1.printStackTrace();
				return false;
			}
			
			if (!writeContent(fOut)) {
				close(fOut);
				m_Error_Code = ErrorCode.ERROR_WRITE_FILE;
				return false;
			} else {
				close(fOut);
				return true;
			}
		}
	}
	
	public int getLastErrorCode() {
		return m_Error_Code;
	}
	
	public String getLastExtra() {
		return m_Error_Extra;
	}
	
	public String getLastExceptionMsg() {
		return m_ExceptionMsg;
	}
	
	protected boolean prepareFolder(String dir) {
		File folder = new File(dir);
		folder.mkdirs();
		return folder.exists();
	}
	
	protected void close(FileOutputStream fout) {
		if(fout != null) {
			try {
				fout.close();
			} catch(Exception e) {
				m_ExceptionMsg = e.getMessage();
			}
		}
	}
	
	protected boolean prepareFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (!file.delete()) {
				return false;
			}
		}
		
		try {
			if (!file.createNewFile()) {
				return false;
			}
		} catch (IOException e) {
			m_ExceptionMsg = e.getMessage();
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected byte[] getByte(String tag, boolean newLine) {
		byte[] result = null;
		if(newLine) {
			tag += ENTRY_SPLITOR;
		}
		
		try {
			result = tag.getBytes("utf-8");
		} catch(Exception e) {
			m_ExceptionMsg = e.getMessage();
			e.printStackTrace();
		}
		return result;
	}

	protected byte[] getByte(String tag, String value, boolean newLine) {
		byte[] result = null;
		try {
			result = getString(tag, value, newLine).getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected String getString(String tag, String value, boolean newLine) {
		String result = tag + DataFileProperty.VALUE_CONNECTOR + value;
		if(newLine) {
			result += ENTRY_SPLITOR;
		}
		return result;
	}

}
