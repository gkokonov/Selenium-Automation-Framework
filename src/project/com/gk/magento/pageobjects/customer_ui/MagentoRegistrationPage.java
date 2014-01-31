/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 11.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.magento.pageobjects.customer_ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gk.helpers.WebElementInteractions;
import com.gk.magento.pageobjects.BasePage;

public class MagentoRegistrationPage extends BasePage {
    WebElementInteractions webElementInteractions = new WebElementInteractions();
    private final static String URL = "http://demo.magentocommerce.com/customer/account/create/";
    private final static String TITLE = "Create New Customer Account - Magento Commerce Demo Store";

    private final static By FIRSTNAME_FIELD = By.id("firstname");
    private final static By LASTNAME_FIELD = By.id("lastname");
    private final static By EMAIL_ADDRESS_FIELD = By.id("email_address");
    private final static By PASSWORD_FIELD = By.id("password");
    private final static By CONFIRM_PASSWORD_FIELD = By.id("confirmation");
    private final static By SUBMIT_BUTTON = By
            .xpath(".//*[@id='form-validate']/div[3]/button");

    private final static By VALIDATION_ERROR_MSG = By
            .className("validation-advice");

    /**
     * @param driver
     * @param pageTitle
     * @param url
     */
    public MagentoRegistrationPage(WebDriver driver) {
        super(driver, TITLE, URL);

    }

    public MagentoCustomerDashboardPage registerNewCustomer(
            boolean positive, String firstname, String lastname,
            String email, String pass) {
        webElementInteractions.sendTextToElement(
                driver, FIRSTNAME_FIELD, firstname);
        webElementInteractions.sendTextToElement(driver, LASTNAME_FIELD, lastname);
        webElementInteractions.sendTextToElement(
                driver, EMAIL_ADDRESS_FIELD, email);
        webElementInteractions.sendTextToElement(driver, PASSWORD_FIELD, pass);
        webElementInteractions.sendTextToElement(
                driver, CONFIRM_PASSWORD_FIELD, pass);

        if (positive) {
            driver.findElement(SUBMIT_BUTTON).click();
            return new MagentoCustomerDashboardPage(driver);
        } else {
            driver.findElement(SUBMIT_BUTTON).click();
            webElementInteractions.isElementDisplayed(driver, VALIDATION_ERROR_MSG);
        }

        return null;
    }

}
