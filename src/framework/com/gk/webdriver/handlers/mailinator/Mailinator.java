/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 23.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.handlers.mailinator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gk.webdriver.exceptions.AutomationException;

/**
 * Mailinator wrapper withg basic mail check exposed. Everything else could be
 * made through custom
 * 
 * @see IMailinatorHandler implementation
 * 
 */
public class Mailinator {
    /** Default time to wait - 300 sec (5 min) */
    public static final int DEFAULT_TIME_TO_WAIT = 300;

    private final String email;

    private final int pollingTimeout;

    private final int pollingPeriod;

    private WebDriver driver = null;

    private String shortMailinatorMail;

    /**
     * Instantiate new Mailinator object
     * 
     * @param email
     *            of the mailinator test account
     * @param pollingPeriod
     *            - refresh interval (in seconds) for each check
     * @param pollingTimeout
     *            - the maximum amount (in seconds) of time for check.
     */
    public Mailinator(WebDriver driver, String email, int pollingPeriod,
            int pollingTimeout) {
        this.email = email;

        if (email.contains("@")) {
            shortMailinatorMail = email.substring(0, email.indexOf("@"));
        }

        this.pollingTimeout = pollingTimeout;
        this.pollingPeriod = pollingPeriod;
        this.driver = driver;
    }

    /**
     * Open the mailinator site and enter into the desired mailbox
     * 
     * @param handler
     * @throws AutomationException
     */
    public void handleMailBox(IMailinatorHandler handler)
            throws AutomationException {

        driver.get("http://mailinator.com");

        driver.findElement(By.id("inboxfield")).sendKeys(shortMailinatorMail);
        //        JavascriptExecutor js = (JavascriptExecutor) driver;
        //        js.executeScript("changeInbox();");
        driver.findElement(
                By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/div/btn"))
                .click();

        long beginTime = System.currentTimeMillis();

        //
        // Perform the real polling 
        //
        WebElement desiredElement = null;
        do {
            try {
                Thread.sleep(pollingPeriod * 1000);
            } catch (InterruptedException e) {
                break;
            }

            //
            // If the time exceed the timeout then RuntimeException
            //
            if (System.currentTimeMillis() - beginTime > pollingTimeout * 1000) {
                throw new AutomationException("Mailinator timeout!");
            }
            refreshMailBox();
        } while ((desiredElement = handler.searchElement(driver)) == null);

        handler.handleElement(driver, desiredElement);
    }

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Refreshed the mailbox
     */
    void refreshMailBox() {
        driver.navigate().refresh();
    }

    /**
     * Close the browser resources and free everything
     */
    public void close() {
        if (driver != null) {
            driver.close();
        }
    }
}
