/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 10.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.magento.pageobjects.customer_ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gk.helpers.WebElementInteractions;
import com.gk.magento.pageobjects.BasePage;

public class MagentoLoginPage extends BasePage {
    WebElementInteractions webElementInteractions = new WebElementInteractions();
    private final static String URL = "http://demo.magentocommerce.com/customer/account/login/";
    private final static String TITLE = "Customer Login - Magento Commerce Demo Store";

    private final static By CREATE_ACCOUNT_BUTTON = By.cssSelector(
            "button[title='Create an Account']");
    private final static By LOGIN_BUTTON = By.cssSelector(
            "button[title='Login']");
    private final static By EMAIL_FIELD = By.id("email");
    private final static By PASSWORD_FIELD = By.id("pass");
    private final static By FORGOT_PASS_LINK = By.cssSelector(
            "a[href*='/account/forgotpassword/']");

    private final static By VALIDATION_ERROR_MSG = By
            .className("validation-advice");

    /**
     * @param driver
     * @param pageTitle
     * @param url
     */
    public MagentoLoginPage(WebDriver driver) {
        super(driver, TITLE, URL);
    }

    public MagentoRegistrationPage clickCreateAccount() {
        driver.findElement(CREATE_ACCOUNT_BUTTON).click();
        return new MagentoRegistrationPage(driver);
    }

    public MagentoCustomerDashboardPage loginAsCutomer(Boolean positive,
            String email, String pass) {
        webElementInteractions.sendTextToElement(driver, EMAIL_FIELD, email);
        webElementInteractions.sendTextToElement(driver, PASSWORD_FIELD, pass);
        if (positive) {
            driver.findElement(LOGIN_BUTTON).click();
            return new MagentoCustomerDashboardPage(driver);
        } else {
            driver.findElement(LOGIN_BUTTON).click();
            webElementInteractions.isElementDisplayed(driver,
                    VALIDATION_ERROR_MSG);
        }
        return null;
    }
}
