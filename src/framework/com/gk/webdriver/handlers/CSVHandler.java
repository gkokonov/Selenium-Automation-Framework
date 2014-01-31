/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 22.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.webdriver.handlers;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CSVHandler {
    private final String FILE_PATH;

    /**
     * Pass csv file relative location.
     */
    public CSVHandler(String filepath) {
        FILE_PATH = filepath;
    }

    public String getFilePath() {
        return FILE_PATH;
    }

    public String[][] readCsvData() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(FILE_PATH));
        List<String[]> csvAsList;

        //Skip first line, description line
        reader.readNext();
        csvAsList = reader.readAll();

        // Convert to 2D array
        String[][] dataArr = new String[csvAsList.size()][];
        dataArr = csvAsList.toArray(dataArr);
        reader.close();

        return dataArr;
    }

}
