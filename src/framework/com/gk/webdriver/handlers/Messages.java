/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 23.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.handlers;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Static resource bundle for all localized messages
 * 
 */
public abstract class Messages {
    private static final String msgsFileName = "Messages";

    /**
     * Gets a message by its key
     * 
     * @param message
     *            Message key
     * @return Returns message by its key
     */
    public static String getMessage(String message) {
        String localeString = System.getProperty("env.locale");

        Locale locale = Locale.getDefault();

        if (localeString != null && !"".equals(localeString)) {
            locale = new Locale(localeString);
        }
        ResourceBundle boundle = ResourceBundle.getBundle(msgsFileName, locale);
        return boundle.getString(message);
    }

    /**
     * Gets a message by its key
     * 
     * @param message
     *            Message key
     * @return Returns message by its key, or null if localeStr is null
     */
    public static String getMessage(String message, String localeStr) {
        Locale locale = null;

        if (localeStr != null) {
            locale = new Locale(localeStr);
            ResourceBundle boundle = ResourceBundle.getBundle(
                    msgsFileName, locale);
            return boundle.getString(message);
        }
        return null;
    }
}
