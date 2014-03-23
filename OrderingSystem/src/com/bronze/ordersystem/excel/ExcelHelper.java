package com.bronze.ordersystem.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bronze.ordersystem.excel.model.ExcelData;
import com.bronze.ordersystem.statistics.model.DailySaleStatistics;
import com.bronze.ordersystem.statistics.model.DishStatistics;
import com.bronze.ordersystem.statistics.model.TotalSaleStatistics;

public class ExcelHelper {

	public static final String DISH_STATISTICS_TEMPLETE_EN_US = File.separator + "document" + File.separator + "template" + File.separator + "DishStatistics_en_US.xlsx";
	public static final String DISH_STATISTICS_TEMPLETE_ZH_CN = File.separator + "document" + File.separator + "template" + File.separator + "DishStatistics_zh_CN.xlsx";
	
	public static final String SALE_STATISTICS_TEMPLETE_EN_US = File.separator + "document" + File.separator + "template" + File.separator + "SaleStatistics_en_US.xlsx";
	public static final String SALE_STATISTICS_TEMPLETE_ZH_CN = File.separator + "document" + File.separator + "template" + File.separator + "SaleStatistics_zh_CN.xlsx";
	
	public static final int DISH_START_ROW = 2;
	public static final int DISH_COLUMNS = 2;
	
	public static final int SALE_START_ROW = 2;
	public static final int SALE_COLUMNS = 4;
	
	public static final int DISH_TYPE = 0;
	public static final int SALE_TYPE = 1;
	
	public static final String TEMP_EXCEL_PATH = File.separator + "document" + File.separator + "temporary" + File.separator;
	
	private ExcelHelper() {
		
	}

	public static Workbook createExcel(ExcelData excelData, int type, String lang) {
		Workbook workbook = null;
		InputStream templeteIn = null;
		Sheet workSheet = null;
		
		try {
			if (type == DISH_TYPE) {
				if (lang.equals("zh_CN")) {
					templeteIn = new FileInputStream(System.getProperty("webapp.root") + DISH_STATISTICS_TEMPLETE_ZH_CN);
				} else {
					templeteIn = new FileInputStream(System.getProperty("webapp.root") + DISH_STATISTICS_TEMPLETE_EN_US);
				}
				
				try {
					workbook = new XSSFWorkbook(templeteIn);
				} catch (Exception e) {
					workbook = new HSSFWorkbook(templeteIn);
				}
				workSheet = workbook.getSheet("Dish Statistics");
		
				writeDishStatistics(workSheet, excelData);
			} else if (type == SALE_TYPE) {
				if (lang.equals("zh_CN")) {
					templeteIn = new FileInputStream(System.getProperty("webapp.root") + SALE_STATISTICS_TEMPLETE_ZH_CN);
				} else {
					templeteIn = new FileInputStream(System.getProperty("webapp.root") + SALE_STATISTICS_TEMPLETE_EN_US);
				}
				
				try {
					workbook = new XSSFWorkbook(templeteIn);
				} catch (Exception e) {
					workbook = new HSSFWorkbook(templeteIn);
				}
				workSheet = workbook.getSheet("Sale Statistics");
				
				writeSaleStatistics(workSheet, excelData);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return workbook;
	}

	private static void writeSaleStatistics(Sheet workSheet, ExcelData excelData) {
		Row row = null;
		Cell cell = null;
		
		// generate all empty rows that with style same as the first row
		List<DailySaleStatistics> dailySales = excelData.getDailySales();
		List<TotalSaleStatistics> totalSales = excelData.getTotalSales();
		if (dailySales == null || dailySales.size() == 0) {
			return;
		}
		
		cell = workSheet.getRow(0).getCell(0);
		StringBuffer time = new StringBuffer();
		if (excelData.getStarttime() != null && !excelData.getStarttime().equals("")) {
			time.append(excelData.getStarttime());
			time.append(" ~ ");
		} else {
			time.append("           ~ ");
		}
		
		if (excelData.getEndtime() != null && !excelData.getEndtime().equals("")) {
			time.append(excelData.getEndtime());
		}
		
		cell.setCellValue(cell.getStringCellValue() + time.toString());
		
		copyRows(workSheet, SALE_START_ROW + 1, SALE_START_ROW + 1, SALE_START_ROW + dailySales.size());
		row = workSheet.getRow(SALE_START_ROW + dailySales.size());
		for (int j = 1; j < SALE_COLUMNS; j++) {
			cell = row.getCell(j);
			switch (j) {
			case 1:
				cell.setCellValue(totalSales.get(0).getTotalsales());
				break;
			case 2:
				cell.setCellValue(totalSales.get(0).getTotalcustomers());
				break;
			case 3:
				cell.setCellValue(totalSales.get(0).getTotalaverage());
				break;
			default:
				break;
			}
		}
		
		for (int i = 0; i < dailySales.size(); i++) {
			if (i != 0) {
				copyRows(workSheet, SALE_START_ROW, SALE_START_ROW, SALE_START_ROW + i);
			}
		}
		
		// write data
		for (int i = 0; i < dailySales.size(); i++) {
			DailySaleStatistics ds = dailySales.get(i);
			row = workSheet.getRow(SALE_START_ROW + i);
			for (int j = 0; j < SALE_COLUMNS; j++) {
				cell = row.getCell(j);
				switch (j) {
				case 0:
					cell.setCellValue(ds.getSaledate());
					break;
				case 1:
					cell.setCellValue(ds.getSales());
					break;
				case 2:
					cell.setCellValue(ds.getCustomers());
					break;
				case 3:
					cell.setCellValue(ds.getAverage());
					break;
				default:
					break;
				}
			}
		}
	}

	private static void writeDishStatistics(Sheet workSheet, ExcelData excelData) {
		Row row = null;
		Cell cell = null;
		
		// generate all empty rows that with style same as the first row
		List<DishStatistics> list = excelData.getDsList();
		if (list == null) {
			return;
		}
		
		cell = workSheet.getRow(0).getCell(0);
		StringBuffer time = new StringBuffer();
		if (excelData.getStarttime() != null && !excelData.getStarttime().equals("")) {
			time.append(excelData.getStarttime());
			time.append(" ~ ");
		} else {
			time.append("           ~ ");
		}
		
		if (excelData.getEndtime() != null && !excelData.getEndtime().equals("")) {
			time.append(excelData.getEndtime());
		}
		
		cell.setCellValue(cell.getStringCellValue() + time.toString());
		
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				copyRows(workSheet, DISH_START_ROW, DISH_START_ROW, DISH_START_ROW + i);
			}
		}
		
		// write data
		for (int i = 0; i < list.size(); i++) {
			DishStatistics ds = list.get(i);
			row = workSheet.getRow(DISH_START_ROW + i);
			for (int j = 0; j < DISH_COLUMNS; j++) {
				cell = row.getCell(j);
				switch (j) {
				case 0:
					cell.setCellValue(ds.getFoodname());
					break;
				case 1:
					cell.setCellValue(ds.getAmount());
					break;
				default:
					break;
				}
			}
		}
	}

	public static void copyRows(Sheet currentSheet, int srcStartRow, int srcEndRow, int distStartRow) {
		int targetRowFrom;
		int targetRowTo;
		int columnCount;
		CellRangeAddress region = null;
		int i;
		int j;
		
		if (srcStartRow == -1 || srcEndRow == -1) {
			return;
		}
		
		for (i = 0; i < currentSheet.getNumMergedRegions(); i++) {
			region = currentSheet.getMergedRegion(i);
			if ((region.getFirstRow() >= srcStartRow)
					&& (region.getLastRow() <= srcEndRow)) {
				targetRowFrom = region.getFirstRow() - srcStartRow
						+ distStartRow;
				targetRowTo = region.getLastRow() - srcStartRow + distStartRow;
				CellRangeAddress newRegion = region.copy();
				newRegion.setFirstRow(targetRowFrom);
				newRegion.setFirstColumn(region.getFirstColumn());
				newRegion.setLastRow(targetRowTo);
				newRegion.setLastColumn(region.getLastColumn());
				currentSheet.addMergedRegion(newRegion);
			}
		}
		
		for (i = srcStartRow; i <= srcEndRow; i++) {
			Row sourceRow = currentSheet.getRow(i);
			columnCount = sourceRow.getLastCellNum();
			if (sourceRow != null) {
				Row newRow = currentSheet.createRow(distStartRow - srcStartRow + i);
				newRow.setHeight(sourceRow.getHeight());
				for (j = 0; j < columnCount; j++) {
					Cell templateCell = sourceRow.getCell(j);
					if (templateCell != null) {
						Cell newCell = newRow.createCell(j);
						copyCell(templateCell, newCell);
					}
				}
			}
		}
	}

	public static void copyRows(HSSFSheet currentSheet, int srcStartRow, int srcEndRow, int distStartRow, int column, int width) {
		int targetRowFrom;
		int targetRowTo;
		CellRangeAddress region = null;
		int i;
		int j;
		
		if (srcStartRow == -1 || srcEndRow == -1) {
			return;
		}
		
		for (i = 0; i < currentSheet.getNumMergedRegions(); i++) {
			region = currentSheet.getMergedRegion(i);
			if ((region.getFirstRow() >= srcStartRow) && (region.getLastRow() <= srcEndRow)) {
				targetRowFrom = region.getFirstRow() - srcStartRow + distStartRow;
				targetRowTo = region.getLastRow() - srcStartRow + distStartRow;
				CellRangeAddress newRegion = region.copy();
				newRegion.setFirstRow(targetRowFrom);
				newRegion.setFirstColumn(region.getFirstColumn());
				newRegion.setLastRow(targetRowTo);
				newRegion.setLastColumn(region.getLastColumn());
				currentSheet.addMergedRegion(newRegion);
			}
		}
		
		for (i = srcStartRow; i <= srcEndRow; i++) {
			HSSFRow sourceRow = currentSheet.getRow(i);
			if (sourceRow != null) {
				HSSFRow newRow = currentSheet.createRow(distStartRow - srcStartRow + i);
				newRow.setHeight(sourceRow.getHeight());
				for (j = column; j < (width + column); j++) {
					HSSFCell templateCell = sourceRow.getCell(j);
					if (templateCell != null) {
						HSSFCell newCell = newRow.createCell(j);
						copyCell(templateCell, newCell);
					}
				}
			}
		}
	}
	
	private static void copyCell(Cell srcCell, Cell distCell) {
		distCell.setCellStyle(srcCell.getCellStyle());
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		
		if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
				distCell.setCellValue(srcCell.getDateCellValue());
			} else {
				distCell.setCellValue(srcCell.getNumericCellValue());
			}
		} else if (srcCellType == Cell.CELL_TYPE_STRING) {
			distCell.setCellValue(srcCell.getRichStringCellValue());
		} else if (srcCellType == Cell.CELL_TYPE_BLANK) {
			// nothing
		} else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
			distCell.setCellValue(srcCell.getBooleanCellValue());
		} else if (srcCellType == Cell.CELL_TYPE_ERROR) {
			distCell.setCellErrorValue(srcCell.getErrorCellValue());
		} else if (srcCellType == Cell.CELL_TYPE_FORMULA) {
			distCell.setCellFormula(srcCell.getCellFormula());
		}
	}
	
}
