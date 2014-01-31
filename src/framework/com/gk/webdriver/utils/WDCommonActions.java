/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 15.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

/**
 * This class contains frequently used global actions, independent for web app
 * implementation.
 * 
 */
public abstract class WDCommonActions {
    private static final Logger LOGGER = Logger.getLogger(WDCommonActions.class);

    /**
     * @param driver
     *            WebDriver
     * @param acceptAlert
     *            True - accept, False - dismiss
     * @return Returns Alert text if the alert is found, otherwise return Null.
     */
    public static String handleAlert(WebDriver driver, boolean acceptAlert) {
        try {
            Alert alert = driver.switchTo().alert();
            if (acceptAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alert.getText();
        } catch (NoAlertPresentException e) {
            LOGGER.warn("Alert not found!", e);
            return null;
        } finally {
            acceptAlert = true;
        }
    }

    /**
     * @param driver
     *            WebDriver
     * @param width
     *            Browser window width
     * @param height
     *            Browser window height
     */
    public static void changeBrowserWindowSize(WebDriver driver, int width,
            int height) {
        Dimension targetSize = new Dimension(width, height);
        driver.manage().window().setSize(targetSize);
    }

    /**
     * Maximize browser window.
     * 
     * @param driver
     */
    public static void changeBrowserWindowSize(WebDriver driver) {
        driver.manage().window().maximize();
    }

    /**
     * Generates random email address with specified domain
     * 
     * @param domain
     *            For example: google.com
     * @return Random email address as string
     */
    public static String generateRandomEmailAddress(String domain) {
        String emailAddress = "";
        // Generate random email address
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        while (emailAddress.length() < 5) {
            int character = (int) (Math.random() * 26);
            emailAddress += alphabet.substring(character, character + 1);
        }
        emailAddress += Integer.valueOf((int) (Math.random() * 99)).toString();
        emailAddress += "@" + domain;
        return emailAddress;
    }

    public static void sleep(long milliseconds) {
        try {
            if (milliseconds > 0) {
                Thread.sleep(milliseconds);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }
}
