package com.airtel.cs.commonutils.dataproviders.beantoexcel;

import com.airtel.cs.commonutils.dataproviders.databeans.TicketTransferRuleDataBean;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.airtel.cs.driver.Driver.commonLib;

public class TicketTransferRuleDateToExcel {

    static DataFormatter dataFormatter;
    static FormulaEvaluator evaluator;
    private static final String XLSX_FILE_EXTENSION="xlsx";

    /**
     * This method is use to get cell value
     * @param cell The Cell object
     * @return String The value
     */
    private String fetchValue(Cell cell) {
        evaluator.evaluate(cell);
        return dataFormatter.formatCellValue(cell, evaluator);
    }

    /**
     * This method is use to get all the  Ticket transfer rule based on file path and sheet name
     * @param path The file path of .xlsx file name
     * @param sheetName The sheet name
     * @return List The Ticket transfer rule
     */
    public List<TicketTransferRuleDataBean> getData(String path, String sheetName) {

        List<TicketTransferRuleDataBean> ruleData = new ArrayList<>();
        FileInputStream file;
        try {
            file = new FileInputStream(new File(path));
            Workbook workbook;
            if (path.contains(XLSX_FILE_EXTENSION)) {
                workbook = new XSSFWorkbook(file);
                dataFormatter = new DataFormatter();
                evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            } else {
                workbook = new HSSFWorkbook(file);
                dataFormatter = new DataFormatter();
                evaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            }

            Sheet sheet = workbook.getSheet(sheetName);

            for (Row cells : sheet) {
                TicketTransferRuleDataBean testDatabean = new TicketTransferRuleDataBean();
                Iterator<Cell> cellIterator = cells.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cells.getRowNum() == 0) {
                    } else {
                        int columnIndex = cell.getColumnIndex();
                        String cellValue = fetchValue(cell);

                        switch (columnIndex) {
                            case 0:
                                testDatabean.setIssueCode(cellValue);
                                break;
                            case 1:
                                testDatabean.setTicketFromState(cellValue);
                                break;
                            case 2:
                                testDatabean.setTicketToState(cellValue);
                                break;
                            case 3:
                                testDatabean.setFromQueue(cellValue);
                                break;
                            case 4:
                                testDatabean.setToQueue(cellValue);
                                break;
                            default:
                                break;
                        }
                    }
                }


                if (cells.getRowNum() != 0) {
                    ruleData.add(testDatabean);
                }
            }
        } catch (IOException e) {
            commonLib.fail("Exception found while reading the test data excel sheet with sheet name "+sheetName+". Error Log: "+e.fillInStackTrace(),false);
        }
        return ruleData;
    }
}
