package com.airtel.cs.commonutils.excelutils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteToExcel {

    public void writeTicketNumber(String filePath, String sheetName, String[] dataToWrite, int rowNum) throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook book = new XSSFWorkbook(inputStream);
        Sheet sheet = book.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        Cell cell;
        int ticketRow = 58;
        try {
            cell = row.getCell(ticketRow);
            cell.setCellValue(dataToWrite[0]);

        } catch (NullPointerException e) {
            cell = row.createCell(ticketRow);
            cell.setCellValue(dataToWrite[0]);
        }
        inputStream.close();
        //Create an object of FileOutputStream class to create write data in excel file
        FileOutputStream outputStream = new FileOutputStream(file);
        //write data in the excel file
        book.write(outputStream);
        //close output stream
        outputStream.close();
    }
}