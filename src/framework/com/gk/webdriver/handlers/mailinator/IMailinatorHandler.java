/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 23.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.handlers.mailinator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gk.webdriver.exceptions.AutomationException;

/**
 * Handles custom actions in the mailiantor inbox and acts as a callback handler
 */
public interface IMailinatorHandler
{

    /**
     * Checks for specific element (email) on each polling interval
     * 
     * @param driver
     * @return the desired element or NULL if it's not found
     */
    public WebElement searchElement(final WebDriver driver)
            throws AutomationException;

    /**
     * Handles the desired element (email) in a manner suitable for the
     * programmer
     * 
     * @param driver
     *            the active webdriver
     * @param element
     *            the link of the desired email in the mailbox
     */
    public void handleElement(final WebDriver driver, final WebElement element)
            throws AutomationException;

}
