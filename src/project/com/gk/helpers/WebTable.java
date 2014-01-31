/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 01.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.helpers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Built-in class to support the web tables or html table elements.
 * 
 */
public class WebTable {
    private WebElement webTable;

    public WebTable(WebElement webTable) {
        setWebTable(webTable);
    }

    public WebElement get_webTable() {
        return webTable;
    }

    public void setWebTable(WebElement webTable) {
        this.webTable = webTable;
    }

    public int getRowCount() {
        List<WebElement> tableRows = webTable.findElements(By.tagName("tr"));
        return tableRows.size();
    }

    public int getColumnCount() {
        List<WebElement> tableRows = webTable.findElements(By.tagName("tr"));
        WebElement headerRow = tableRows.get(0);
        List<WebElement> tableCols = headerRow.findElements(By.tagName("td"));
        return tableCols.size();
    }

    /**
     * A method to retrieve the cell editor element; this is useful while working
     * with editable cells.
     * 
     * @param rowIdx
     * @param colIdx
     * @param editorIdx
     * @return
     * @throws NoSuchElementException
     */
    public WebElement getCellEditor(int rowIdx, int colIdx, int editorIdx)
            throws NoSuchElementException {
        try {
            List<WebElement> tableRows =
                    webTable.findElements(By.tagName("tr"));
            WebElement currentRow = tableRows.get(rowIdx - 1);
            List<WebElement> tableCols = currentRow.findElements(
                    By.tagName("td"));
            WebElement cell = tableCols.get(colIdx - 1);
            WebElement cellEditor = cell.findElements(By.tagName("input")).get(
                    editorIdx);
            return cellEditor;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Failed to get cell editor!");
        }
    }

    /**
     * A method to retrieve data from a specific cell of the table.
     * 
     * @param rowIdx
     * @param colIdx
     * @return
     * @throws NoSuchElementException
     */
    public WebElement getCellData(int rowIdx, int colIdx)
            throws NoSuchElementException {
        try {
            List<WebElement> tableRows = webTable.findElements(By.tagName("tr"));
            WebElement currentRow = tableRows.get(rowIdx - 1);
            List<WebElement> tableCols = currentRow.findElements(
                    By.tagName("td"));
            WebElement cell = tableCols.get(colIdx - 1);
            WebElement cellEditor = cell.findElements(
                    By.tagName("input")).get(0);
            return cellEditor;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Failed to get cell Data!");
        }
    }
}
