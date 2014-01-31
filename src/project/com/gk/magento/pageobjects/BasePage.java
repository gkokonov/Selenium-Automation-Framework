/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 08.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.magento.pageobjects;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gk.webdriver.exceptions.AutomationException;
import com.gk.webdriver.utils.WaitTool;

/**
 * Main page-object class, every other page extends this class.
 * 
 */
public class BasePage {
    private static final Logger LOGGER = Logger.getLogger(BasePage.class);

    /** Default URL */
    private final String url;

    /** This page's WebDriver */
    protected WebDriver driver;

    /**
     * Expected BasePage Title. This will be used in isPageLoad() to check if
     * page is loaded.
     */
    private final String pageTitle;
    private static final By PAGE_TITLE_LOCATOR = new By.ByXPath(".//head/title");

    /**
     * Constructor for base page
     * 
     * @param driver
     *            Web driver instance
     * @param pageTitle
     *            String or null
     * @param url
     *            String or null
     */
    public BasePage(WebDriver driver, String pageTitle, String url) {
        this.driver = driver;

        if (pageTitle != null) {
            this.pageTitle = pageTitle;
        } else {
            this.pageTitle = "";
        }
        if (url != null) {
            this.url = url;
        } else {
            this.url = "";
        }
    }

    /**
     * Checks if the correct page is loaded.
     * 
     * @return True or false
     **/
    public boolean isPageLoaded() {
        WaitTool.waitForElementPresentDOM(driver, PAGE_TITLE_LOCATOR,
                WaitTool.DEFAULT_WAIT_4_ELEMENT);
        return (driver.getTitle().equalsIgnoreCase(pageTitle));
    }

    /**
     * Checks if the correct page is loaded by checking the specified element
     * visibility on the page.
     * 
     * @param locator
     * @return True or false
     */
    protected boolean isPageLoaded(By locator) {
        if (WaitTool.waitForElementVisibility(driver, locator,
                WaitTool.DEFAULT_WAIT_4_ELEMENT) != null) {
            return true;
        }
        return false;
    }

    /** Open the default page */
    public void open() {
        driver.get(url);
    }

    /** Returns the page title */
    public String getTitle() {
        return pageTitle;
    }

    /** Returns the default URL */
    public String getUrl() {
        return url;
    }

    /**
     * Go to specific page based on user input
     * 
     * @param type
     *            Expected page-object class
     * @return
     */
    public <T extends BasePage> T expectedLandingPage(Class<T> type) {
        return getPage(type);
    }

    private <T extends BasePage> T getPage(Class<T> type) {
        T page = null;

        try {
            page = type.getDeclaredConstructor(WebDriver.class).newInstance(
                    driver);
        } catch (InstantiationException e) {
            throw new AutomationException("Exception during page transition!",
                    e.getCause());
        } catch (IllegalAccessException e) {
            throw new AutomationException("Exception during page transition!",
                    e.getCause());
        } catch (InvocationTargetException e) {
            throw new AutomationException("Exception during page transition!",
                    e.getCause());
        } catch (NoSuchMethodException e) {
            throw new AutomationException("Exception during page transition!",
                    e.getCause());
        } catch (NullPointerException e) {
            throw new AutomationException("Exception during page transition!",
                    e.getCause());
        }

        return page;
    }
}
