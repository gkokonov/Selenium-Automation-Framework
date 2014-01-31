/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 15.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.handlers;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Get Method reads data from Excel Spreadsheet (XLS - 2003 format) and transform
 * it to two dim array string.
 * 
 */
public class XLSHandler {
    private final String testDataFilePath;

    public XLSHandler(String filepath) {
        testDataFilePath = filepath;
    }

    public String[][] getTableDataArray(String sheetName, String testMethodName) {
        String[][] tabArray = null;
        try {
            Workbook workbook = Workbook.getWorkbook(new File(testDataFilePath));
            Sheet sheet = workbook.getSheet(sheetName);
            int startRow, startCol, endRow, endCol, ci, cj;
            Cell tableStart = sheet.findCell(testMethodName);
            startRow = tableStart.getRow();
            startCol = tableStart.getColumn();

            Cell tableEnd = sheet.findCell(testMethodName, startCol + 1,
                    startRow + 1, 100, 500, false);

            endRow = tableEnd.getRow();
            endCol = tableEnd.getColumn();
            tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
            ci = 0;

            for (int i = startRow + 1; i < endRow; i++, ci++) {
                cj = 0;
                for (int j = startCol + 1; j < endCol; j++, cj++) {
                    tabArray[ci][cj] = sheet.getCell(j, i).getContents();
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getTableDataArray()!");
            e.printStackTrace();
            tabArray = null;
        }

        return (tabArray);
    }

}
