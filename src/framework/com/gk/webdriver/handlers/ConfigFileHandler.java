/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 15.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.handlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.gk.webdriver.exceptions.AutomationException;

/**
 * Handle specific configuration properties file
 * 
 */
public class ConfigFileHandler {
    private final Properties config;
    private final FileInputStream inputStream;

    public ConfigFileHandler(String configFile) {
        config = new Properties();
        try {
            this.inputStream = new FileInputStream(configFile);
            config.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            throw new AutomationException("Wrong config file name!",
                    e.getCause());
        } catch (IOException e) {
            throw new AutomationException("Wrong config file name!",
                    e.getCause());
        }
    }

    public String getPropertyAsString(String propertyName) {
        String result;
        if (config.getProperty(propertyName) != null) {
            result = config.getProperty(propertyName);
        } else {
            throw new AutomationException("Poperty: " + propertyName
                    + " not found!");
        }
        return result;
    }

    public long getPropertyAsLong(String propertyName) {
        long result;
        try {
            if (config.getProperty(propertyName) != null) {
                result = Long.parseLong(config.getProperty(propertyName).trim());
            } else {
                throw new AutomationException("Poperty: " + propertyName
                        + " not found!");
            }
        } catch (NumberFormatException ex) {
            throw new AutomationException("Poperty: " + propertyName
                    + " cannot be parsed to Long!", ex.getCause());
        }
        return result;
    }

}
