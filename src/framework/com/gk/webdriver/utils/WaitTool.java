/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 15.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Wait tool class. Provides Wait methods for an elements, and AJAX elements to
 * load. It uses WebDriverWait (explicit wait) for waiting an element or
 * javaScript.
 * 
 * To use implicitlyWait() and WebDriverWait() in the same test, we would have to
 * nullify implicitlyWait() before calling WebDriverWait(), and reset after it.
 * This class takes care of it.
 * 
 * Generally relying on implicitlyWait slows things down so use WaitTool’s
 * explicit wait methods as much as possible. Also, consider (DEFAULT_WAIT_4_PAGE
 * = 0) for not using implicitlyWait for a certain test.
 * 
 * 
 */

public abstract class WaitTool {
    private static final Logger LOGGER = Logger.getLogger(WaitTool.class);

    /** Default wait time for an element. 7 seconds. */
    public static final int DEFAULT_WAIT_4_ELEMENT = Integer.parseInt(
            System.getProperty("default.wait.4.element"));
    /**
     * Default wait time for a page to be displayed. 12 seconds. "0" will nullify
     * implicitlyWait and speed up a test.
     */
    public static final int DEFAULT_WAIT_4_PAGE = Integer.parseInt(
            System.getProperty("default.wait.4.page"));
    /**
     * Default polling interval for fluent wait in milliseconds. 500
     * milliseconds.
     */
    private static final long DEFAULT_POLLING_INTERVAL = Long.parseLong(
            System.getProperty("default.polling.interval"));

    /** Long wait time for an element. 12 seconds. */
    public static final int LONG_WAIT_4_ELEMENT = Integer.parseInt(
            System.getProperty("long.wait.4.element"));

    /** Short wait time for an element. 3 seconds. */
    public static final int SHORT_WAIT_4_ELEMENT = Integer.parseInt(System
            .getProperty("short.wait.4.element"));

    /**
     * Wait for the element to be present in the DOM, and displayed on the page.
     * And returns the first WebElement using the given method.
     * 
     * @param driver
     *            The driver object to be used
     * @param by
     *            selector to find the element
     * @param timeOutInSeconds
     *            The time in seconds to wait until returning a failure
     * 
     * @return The first WebElement using the given method, or null (if the
     *         timeout is reached)
     */
    public static WebElement waitForElementVisibility(WebDriver driver,
            final By by, int timeOutInSeconds) {
        WebElement element;
        try {
            // To use WebDriverWait(), we would have to nullify implicitlyWait().
            // Because implicitlyWait time also set "driver.findElement()" wait time.
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(by));
            return element; // return the element
        } catch (Exception e) {
            LOGGER.error("WaitForElementVisibility failed for element: " + by
                    + " error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return null;
    }

    /**
     * Wait for the element to disappear from the page.
     * 
     * @param driver
     *            The driver object to be used
     * @param by
     *            selector to find the element
     * @param timeOutInSeconds
     *            The time in seconds to wait until returning a failure
     * 
     * @return True if the element disappear, otherwise return false.
     */
    public static Boolean waitForElementInvisibility(WebDriver driver,
            final By by, int timeOutInSeconds) {
        Boolean temp;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            temp = wait.until(ExpectedConditions
                    .invisibilityOfElementLocated(by));
            return temp;
        } catch (Exception e) {
            LOGGER.error("WaitForElementInvisibility failed for element: " + by
                    + " error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return false;
    }

    /**
     * Wait for the element to be present in the DOM, regardless of being
     * displayed or not. And returns the first WebElement using the given method.
     * 
     * @param driver
     *            The driver object to be used
     * @param by
     *            selector to find the element
     * @param timeOutInSeconds
     *            The time in seconds to wait until returning a failure
     * 
     * @return The first WebElement using the given method, or null (if the
     *         timeout is reached)
     */
    public static WebElement waitForElementPresentDOM(WebDriver driver,
            final By by, int timeOutInSeconds) {
        WebElement element;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait
                    .until(ExpectedConditions.presenceOfElementLocated(by));
            return element; // return the element
        } catch (Exception e) {
            LOGGER.error("WaitForElementPresentDOM failed for element: " + by
                    + " error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return null;
    }

    /**
     * Wait for the List<WebElement> to be present in the DOM, regardless of
     * being displayed or not. Returns all elements within the current page DOM.
     * 
     * @param WebDriver
     *            The driver object to be used
     * @param By
     *            selector to find the element
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return List<WebElement> all elements within the current page DOM, or null
     *         (if the timeout is reached)
     */
    public static List<WebElement> waitForListElementsPresent(WebDriver driver,
            final By by, int timeOutInSeconds) {
        List<WebElement> elements;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.until((new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return WaitTool.areElementsPresent(driverObject, by);
                }
            }));

            elements = driver.findElements(by);
            return elements; // return list of elements
        } catch (Exception e) {
            LOGGER.error("WaitForListElementsPresent failed for element: " + by
                    + " error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return null;
    }

    /**
     * Wait for an element to appear on the refreshed web-page. And returns the
     * first WebElement using the given method.
     * 
     * This method is to deal with dynamic pages.
     * 
     * @param WebDriver
     *            The driver object to use to perform this element search
     * @param locator
     *            selector to find the element
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return WebElement the first WebElement using the given method, or null(if
     *         the timeout is reached)
     * 
     */
    public static WebElement waitForElementAfterRefresh(WebDriver driver,
            final By by, int timeOutInSeconds) {
        WebElement element;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                @Override
                public Boolean apply(WebDriver driverObject) {
                    driverObject.navigate().refresh(); // refresh the page
                    return WaitTool.isElementPresentAndDisplay(driverObject, by);
                }
            });
            element = driver.findElement(by);
            return element; // return the element
        } catch (Exception e) {
            LOGGER.error("WaitForElementAfterRefresh failed for element: " + by
                    + " error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return null;
    }

    /**
     * Wait for the Text to be present in the given element, regardless of being
     * displayed or not.
     * 
     * @param WebDriver
     *            The driver object to be used to wait and find the element
     * @param locator
     *            selector of the given element, which should contain the text
     * @param String
     *            The text we are looking
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return boolean true or false
     */
    public static boolean waitForTextPresent(WebDriver driver, final By by,
            final String text, int timeOutInSeconds) {
        boolean isPresent = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                @Override
                public Boolean apply(WebDriver driverObject) {
                    return WaitTool.isTextPresent(driverObject, by, text); // is the Text in the DOM
                }
            });
            isPresent = WaitTool.isTextPresent(driver, by, text);
            return isPresent;
        } catch (Exception e) {
            LOGGER.error("WaitForTextPresent failed for element: " + by
                    + ", text:" + text + " error:" + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return false;
    }

    /**
     * Wait for the Text to be present in the page source, regardless of being
     * displayed or not.
     * 
     * @param WebDriver
     *            The driver object to be used to wait and find the element
     * @param String
     *            The text we are looking
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return boolean true or false
     */
    public static boolean waitForTextPresent(WebDriver driver,
            final String text, int timeOutInSeconds) {
        boolean isPresent = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                @Override
                public Boolean apply(WebDriver driverObject) {
                    return WaitTool.isTextPresent(driverObject, text); // is the Text in the DOM
                }
            });
            isPresent = WaitTool.isTextPresent(driver, text);
            return isPresent;
        } catch (Exception e) {
            LOGGER.error("WaitForTextPresent failed for text: " + text
                    + ", error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset implicitlyWait()
        }
        return false;
    }

    /**
     * Waits for the Condition of JavaScript.
     * 
     * 
     * @param WebDriver
     *            The driver object to be used to wait and find the element
     * @param String
     *            The javaScript condition we are waiting. e.g.
     *            "return (xmlhttp.readyState >= 2 && xmlhttp.status == 200)"
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return boolean true or false(condition fail, or if the timeout is
     *         reached)
     **/
    public static boolean waitForJavaScriptCondition(WebDriver driver,
            final String javaScript, int timeOutInSeconds) {
        boolean jscondition = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify implicitlyWait()

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject)
                            .executeScript(javaScript);
                }
            });
            jscondition = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript(javaScript);
            return jscondition;
        } catch (Exception e) {
            LOGGER.error("WaitForJavaScriptCondition failed for javascript: "
                    + javaScript + " error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return false;
    }

    /**
     * Waits for the completion of Ajax jQuery processing by checking
     * "return jQuery.active == 0" condition.
     * 
     * @param WebDriver
     *            - The driver object to be used to wait and find the element
     * @param int - The time in seconds to wait until returning a failure
     * 
     * @return boolean true or false(condition fail, or if the timeout is
     *         reached)
     * */
    public static boolean waitForJQueryProcessing(WebDriver driver,
            int timeOutInSeconds) {
        boolean jQcondition = false;
        try {
            // nullify implicitlyWait()
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject)
                            .executeScript("return jQuery.active == 0");
                }
            });
            jQcondition = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0");
            return jQcondition;
        } catch (Exception e) {
            LOGGER.error("waitForJQueryProcessing failed: "
                    + " error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return jQcondition;
    }

    /**
     * Coming to implicit wait, If you have set it once then you would have to
     * explicitly set it to zero to nullify it -
     */
    public static void nullifyImplicitWait(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    /**
     * Set driver implicitlyWait() time.
     */
    public static void setImplicitWait(WebDriver driver, int waitTime_InSeconds) {
        driver.manage().timeouts()
        .implicitlyWait(waitTime_InSeconds, TimeUnit.SECONDS);
    }

    /**
     * Reset ImplicitWait. To reset ImplicitWait time you would have to
     * explicitly set it to zero to nullify it before setting it with a new time
     * value.
     */
    public static void resetImplicitWait(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(
                WaitTool.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS);
    }

    /**
     * Reset ImplicitWait.
     * 
     * @param int - a new wait time in seconds
     */
    public static void resetImplicitWait(WebDriver driver,
            int newWaitTime_InSeconds) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(
                newWaitTime_InSeconds, TimeUnit.SECONDS);
    }

    /**
     * @param driver
     *            WebDriver
     * @param elementLocator
     *            By locator.
     * @return Returns the element if it is displayed in time or null.
     */
    public static WebElement fluentWaitForElement(final WebDriver driver,
            By elementLocator, int timeOutInSeconds) {
        try {
            // nullify implicitlyWait()
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            FluentWait<By> fluentWait = new FluentWait<By>(elementLocator);
            fluentWait.pollingEvery(
                    DEFAULT_POLLING_INTERVAL, TimeUnit.MILLISECONDS);
            fluentWait.withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
            fluentWait.until(new Predicate<By>() {
                @Override
                public boolean apply(By by) {
                    try {
                        return driver.findElement(by).isDisplayed();
                    } catch (NoSuchElementException ex) {
                        return false;
                    }
                }
            });
            return driver.findElement(elementLocator);
        } catch (Exception e) {
            LOGGER.error("FluentWaitForElement failed for element: "
                    + elementLocator + ", error: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS);
        }
        return null;
    }

    /**
     * @param driver
     *            WebDriver
     * @param elementLocator
     *            By locator
     * @param elementAttributeName
     *            An attribute name of the element
     * @return Returns the web element when the specified attribute value is not
     *         null.
     */
    public static WebElement fluentWaitForElementAttribute(
            final WebDriver driver, final By elementLocator,
            final String elementAttributeName, int timeOutInSeconds) {
        try {
            // nullify implicitlyWait()
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                    .pollingEvery(DEFAULT_POLLING_INTERVAL,
                            TimeUnit.MILLISECONDS)
                            .ignoring(NoSuchElementException.class);
            WebElement element = wait.until(
                    new Function<WebDriver, WebElement>() {
                        @Override
                        public WebElement apply(WebDriver driver) {
                            if (driver.findElement(elementLocator).getAttribute(
                                    elementAttributeName) != null) {
                                return driver.findElement(elementLocator);
                            } else {
                                return driver.findElement(By.id("Foo Error"));
                            }
                        }
                    });
            return element;
        } catch (Exception e) {
            LOGGER.error("FluentWaitForElementAttribute failed for element: "
                    + elementLocator + ", attribute:" + elementAttributeName
                    + ", error:" + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(
                    DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
        }
        return null;
    }

    /**
     * Checks if the text is present in the element.
     * 
     * @param driver
     *            - The driver object to use to perform this element search
     * @param by
     *            - selector to find the element that should contain text
     * @param text
     *            - The Text element you are looking for
     * @return true or false
     */
    private static boolean isTextPresent(WebDriver driver, By by, String text) {
        try {
            return fluentWaitForElement(
                    driver, by, DEFAULT_WAIT_4_ELEMENT).getText().contains(text);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /** Is the text present in page. Look inside page source. */
    private static boolean isTextPresent(WebDriver driver, String text) {
        return driver.getPageSource().contains(text);
    }

    /**
     * Checks if the element is in the DOM, regardless of being displayed or not.
     * 
     * @param driver
     *            - The driver object to use to perform this element search
     * @param by
     *            - selector to find the element
     * @return boolean
     */
    @SuppressWarnings("unused")
    private static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by); // if it does not find the element throw
            // NoSuchElementException, which calls
            // "catch(Exception)" and returns false;
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if the List<WebElement> are in the DOM, regardless of being
     * displayed or not.
     * 
     * @param driver
     *            - The driver object to use to perform this element search
     * @param by
     *            - selector to find the element
     * @return boolean
     */
    private static boolean areElementsPresent(WebDriver driver, By by) {
        try {
            driver.findElements(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if the element is in the DOM and displayed.
     * 
     * @param driver
     *            - The driver object to use to perform this element search
     * @param by
     *            - selector to find the element
     * @return boolean
     */
    private static boolean isElementPresentAndDisplay(WebDriver driver, By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /** Is the Element in the page, using WaitTool. */
    public static boolean waitForElementPresentDOM(WebDriver driver, By locator) {
        if (WaitTool.waitForElementPresentDOM(driver, locator,
                WaitTool.DEFAULT_WAIT_4_ELEMENT) != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the element is in the DOM and displayed, using WaitTool.
     * 
     * @param locator
     *            - selector to find the element
     * @return true or false
     */
    public static boolean waitForElementVisibility(WebDriver driver,
            By locator) {
        if (WaitTool.waitForElementVisibility(driver, locator,
                WaitTool.DEFAULT_WAIT_4_ELEMENT) != null) {
            return true;
        }
        return false;
    }

}