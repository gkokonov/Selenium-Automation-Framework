/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 24.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.webdriver.interfaces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface IWebElementInteractions {
    /**
     * @param driver
     * @param locator
     * @return Returns true if element exists on the page.
     */
    boolean doesElementExist(WebDriver driver, By locator);

    /**
     * @param driver
     * @param elementLocator
     * @return Returns true if successful, otherwise returns false.
     */
    boolean doubleClickElement(WebDriver driver, By elementLocator);

    /**
     * @param driver
     * @param locator
     * @return Number of found web elements on the page.
     */
    int getElementCount(WebDriver driver, By locator);

    /**
     * @param driver
     * @param element
     * @return Returns XPath of a specified web element.
     */
    String getElementXPath(WebDriver driver, WebElement element);

    /**
     * An extension for WebElement that provides the highlight Elements() method
     * at runtime.
     * 
     * @param element
     */
    void highlightElement(WebElement element);

    /**
     * @param driver
     * @param element
     * @return Returns true if the hover action is performed, otherwise returns
     *         false.
     */
    boolean hoverElement(WebDriver driver, WebElement element);

    /**
     * @param driver
     * @param locator
     * @return Returns true only if element exists and it is displayed on the page.
     */
    boolean isElementDisplayed(WebDriver driver, By locator);

    /**
     * @param driver
     * @param element
     * @return Returns true if successful, otherwise returns false.
     */
    boolean moveMouseOverElement(WebDriver driver, WebElement element);

    /**
     * Select by visible text form drop down menu.
     * 
     * @param driver
     *            Web driver instance
     * @param selectElementLocator
     *            Locator for SELECT html element
     * @param visibleText
     *            Visible text to select
     */
    void selectByTextFromSelectableElement(WebDriver driver,
            By selectElementLocator, String visibleText);

    /**
     * Send text keys to the element that is found by Locator.
     * 
     * @param locator
     *            By locator to use
     * @param text
     *            Text to fill
     * @return True if the field is filled with text, otherwise return false.
     */
    boolean sendTextToElement(WebDriver driver, By locator, String text);

    /**
     * An extension for WebElement and provide a method to set the attribute
     * value of an element at runtime.
     * 
     * @param element
     * @param attributeName
     * @param value
     */
    void setElementAttributeValue(
            WebElement element, String attributeName, String value);

    /**
     * @param locator
     * @param text
     * @return Returns true if element value contains the specified text.
     */
    boolean isElementValueFilledWithText(WebDriver driver, By locator,
            String text);

}
