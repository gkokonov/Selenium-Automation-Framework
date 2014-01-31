/* ***********************************************************************
 * Copyright 2013 ScoreTwice, All rights reserved. ScoreTwice Confidential
 * ***********************************************************************
 * Date: 21.07.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.gk.webdriver.handlers.ConfigFileHandler;

public class DBManager {
    private static ConfigFileHandler handleConfFile;
    private static Connection conn = null;
    private static String url;
    private static String className;
    private static String user;
    private static String pass;
    private static final String dbConfigFile = "config/dbConfig.properties";

    private static final Logger LOGGER = Logger.getLogger(DBManager.class);

    static {
        try {
            handleConfFile = new ConfigFileHandler(dbConfigFile);
            url = handleConfFile.getPropertyAsString("url");
            className = handleConfFile.getPropertyAsString("className");
            user = handleConfFile.getPropertyAsString("user");
            pass = handleConfFile.getPropertyAsString("pass");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        try {
            Class.forName(className);

        } catch (java.lang.ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException: " + e.getMessage());
        }

        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public static ResultSet getResult(String sqlQuery) throws SQLException {
        ResultSet resultSet = null;
        Connection connection = getConnection();
        try {
            resultSet = connection.prepareStatement(sqlQuery).executeQuery();
            connection.close();
        } catch (SQLException e) {
            connection.close();
            throw new SQLException();
        }
        return resultSet;
    }

    private static Connection getConnection() {
        return conn;
    }

    public static List<String> getSqlColumnValues(String statement,
            String columnName) throws SQLException {
        ResultSet rs = getResult(statement);
        String value = null;
        List<String> rows = new ArrayList<String>();
        while (rs.next()) {
            value = rs.getString(columnName);
            rows.add(value);
        }
        rs.close();
        return rows;
    }
}
