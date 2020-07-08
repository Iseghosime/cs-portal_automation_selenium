package utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadData {
	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static DataFormatter formatter = new DataFormatter();
	
	public static Object[][] getData(String excelPath,String sheetName) throws IOException {
		FileInputStream input = new FileInputStream(excelPath);
		workbook = new XSSFWorkbook(input);
		sheet = workbook.getSheet(sheetName);
		XSSFRow row = sheet.getRow(0);
		int row_count = sheet.getPhysicalNumberOfRows();
		int col_count = row.getLastCellNum();
		Object data[][] = new Object[row_count - 1][col_count];
		for (int i = 0; i < row_count - 1; i++) {
			XSSFRow rows = sheet.getRow(i + 1);
			for (int j = 0; j < col_count; j++) {
				XSSFCell cell = rows.getCell(j);
				if (cell == null) {
					data[i][j] = "";
				} else {
					String value = formatter.formatCellValue(cell);
					data[i][j] = value;

				}
			}
		}
		return data;
	}
	
}
