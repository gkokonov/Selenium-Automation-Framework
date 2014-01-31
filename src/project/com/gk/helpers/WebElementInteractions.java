/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 24.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.helpers;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.Select;

import com.gk.webdriver.interfaces.IWebElementInteractions;
import com.gk.webdriver.utils.WaitTool;

public class WebElementInteractions implements IWebElementInteractions {
    private static final Logger LOGGER = Logger.getLogger(
            WebElementInteractions.class);

    /**
     * 
     */
    public WebElementInteractions() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#doesElementExist(org
     * .openqa.selenium.WebDriver, org.openqa.selenium.By)
     */
    @Override
    public boolean doesElementExist(WebDriver driver, By locator) {
        if (driver.findElements(locator).size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#doubleClickElement
     * (org.openqa.selenium.WebDriver, org.openqa.selenium.By)
     */
    @Override
    public boolean doubleClickElement(WebDriver driver, By elementLocator) {
        try {
            Actions builder = new Actions(driver);
            builder.doubleClick(WaitTool.waitForElementVisibility(driver,
                    elementLocator, WaitTool.DEFAULT_WAIT_4_ELEMENT));
            builder.build().perform();
            return true;
        } catch (Exception e) {
            LOGGER.error("Double click was not successful for element: "
                    + elementLocator.toString(), e);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#getElementCount(org
     * .openqa.selenium.WebDriver, java.lang.String)
     */
    @Override
    public int getElementCount(WebDriver driver, By locator) {
        List<WebElement> elementsFound = driver.findElements(locator);
        return elementsFound.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#getElementXPath(org
     * .openqa.selenium.WebDriver, org.openqa.selenium.WebElement)
     */
    @Override
    public String getElementXPath(WebDriver driver, WebElement element) {
        String jscript = "function getPathTo(node) {" + "  var stack = [];"
                + "  while(node.parentNode !== null) {"
                + "    stack.unshift(node.tagName);"
                + "    node = node.parentNode;" + "  }"
                + "  return stack.join('/');" + "}"
                + "return getPathTo(arguments[0]);";
        return (String) ((JavascriptExecutor) driver).executeScript(jscript,
                element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#highlightElement(org
     * .openqa.selenium.WebElement)
     */
    @Override
    public void highlightElement(WebElement element) {
        for (int i = 0; i < 5; i++) {
            WrapsDriver wrappedElement = (WrapsDriver) element;
            JavascriptExecutor driver =
                    (JavascriptExecutor) wrappedElement.getWrappedDriver();
            driver.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element, "color: green; border: 2px solid yellow;");
            driver.executeScript(
                    "arguments[0].setAttribute('style',arguments[1]);",
                    element, "");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#hoverElement(org.openqa
     * .selenium.WebDriver, org.openqa.selenium.WebElement)
     */
    @Override
    public boolean hoverElement(WebDriver driver, WebElement element) {
        try {
            Actions builder = new Actions(driver);
            builder.moveToElement(element).build().perform();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            LOGGER.error(
                    "Hover was not successful for element: "
                            + element.toString(), e);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#isElementDisplayed
     * (org.openqa.selenium.WebDriver, org.openqa.selenium.By)
     */
    @Override
    public boolean isElementDisplayed(WebDriver driver, By locator) {
        if (doesElementExist(driver, locator)) {
            return driver.findElement(locator).isDisplayed();
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#moveMouseOverElement
     * (org.openqa.selenium.WebDriver, org.openqa.selenium.WebElement)
     */
    @Override
    public boolean moveMouseOverElement(WebDriver driver, WebElement element) {
        try {
            Locatable hoverItem = (Locatable) element;
            Mouse mouse = ((HasInputDevices) driver).getMouse();
            mouse.mouseMove(hoverItem.getCoordinates());
            return true;
        } catch (Exception e) {
            LOGGER.error("Mouse movement was not successful for element: "
                    + element.toString(), e);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gk.webdriver.interfaces.IWebElementInteractions#
     * selectByTextFromSelectableElement(org.openqa.selenium.WebDriver,
     * org.openqa.selenium.By, java.lang.String)
     */
    @Override
    public void selectByTextFromSelectableElement(WebDriver driver,
            By selectElementLocator, String visibleText) {
        Select select = new Select(driver.findElement(selectElementLocator));
        select.selectByVisibleText(visibleText);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#sendTextToElement(
     * org.openqa.selenium.WebDriver, org.openqa.selenium.By, java.lang.String)
     */
    @Override
    public boolean sendTextToElement(WebDriver driver, By locator, String text) {
        boolean result = false;
        if (WaitTool.fluentWaitForElement(driver, locator,
                WaitTool.DEFAULT_WAIT_4_ELEMENT) != null) {
            driver.findElement(locator).clear();
            driver.findElement(locator).sendKeys(text);

            if (driver.findElement(locator).getText().equals(text)) {
                result = true;
            } else {
                result = false;
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gk.webdriver.interfaces.IWebElementInteractions#setElementAttributeValue
     * (org.openqa.selenium.WebElement, java.lang.String, java.lang.String)
     */
    @Override
    public void setElementAttributeValue(
            WebElement element, String attributeName, String value) {

        WrapsDriver wrappedElement = (WrapsDriver) element;
        JavascriptExecutor driver =
                (JavascriptExecutor) wrappedElement.getWrappedDriver();
        driver.executeScript(
                "arguments[0].setAttribute(arguments[1], arguments[2])",
                element, attributeName, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gk.webdriver.interfaces.IWebElementInteractions#
     * isElementValueFilledWithText(org.openqa.selenium.By, java.lang.String)
     */
    @Override
    public boolean isElementValueFilledWithText(WebDriver driver, By locator,
            String text) {
        try {
            if (WaitTool.fluentWaitForElement(
                    driver, locator, WaitTool.DEFAULT_WAIT_4_ELEMENT)
                    .getAttribute("value").contains(text)) {
                return true;
            }
        } catch (NullPointerException ex) {
            LOGGER.warn("Field: " + locator.toString()
                    + " is not filled with text: " + text);
        }
        return false;
    }

}
