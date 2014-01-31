/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 07.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.magento.pageobjects.customer_ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gk.helpers.WebElementInteractions;
import com.gk.magento.pageobjects.BasePage;
import com.gk.webdriver.utils.WDCommonActions;
import com.gk.webdriver.utils.WaitTool;

public class MagentoCustomerDashboardPage extends BasePage {
    WebElementInteractions webElementInteractions = new WebElementInteractions();
    private final static String URL = "http://demo.magentocommerce.com/customer/account/index/";
    private final static String TITLE = "My Account - Magento Commerce Demo Store";

    private static final By REGISTR_SUCCESS_MSG = By
            .xpath(".//span[text()=\"Thank you for registering with Main Store.\"]");
    private static final By VERIFY_PAGE = By
            .xpath(".//h1[text()=\"My Dashboard\"]");
    private static final By LOGOUT_BTN = By.linkText("Log Out");
    private static final By MANAGE_ADDRESS_BTN = By.linkText("Manage Addresses");
    private static final By ADD_NEW_ADDRESS_BTN = By
            .xpath(".//*[@title='Add New Address']");

    //Address form elements
    private static final By COMPANY_FIELD = By.id("company");
    private static final By TELEPHONE_FIELD = By.id("telephone");
    private static final By STREET_ADDR_FIELD = By.id("street_1");
    private static final By CITY_FIELD = By.id("city");
    private static final By ZIP_FIELD = By.id("zip");
    private static final By COUNTRY_FIELD = By.id("country");
    private static final By SAVE_ADDRESS_FORM_BTN = By
            .xpath(".//*[@title='Save Address']");

    private static final By SUCCESSFUL_ADDR_SAVE_MSG = By
            .xpath(".//span[text()=\"The address has been saved.\"]");
    private static final By VALIDATION_EXCEPTION = By
            .className("validation-advice");
    private static final By DELETE_ADDRESS_BTN = By.linkText("Delete Address");
    private static final String NO_ADDITIONAL_ADDRESS_MSG = "You have no additional address entries in your address book.";

    /**
     * @param driver
     * @param pageTitle
     * @param url
     */
    public MagentoCustomerDashboardPage(WebDriver driver) {
        super(driver, TITLE, URL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gk.magento.pageobjects.BasePage#isPageLoaded()
     */
    @Override
    public boolean isPageLoaded() {
        if (super.isPageLoaded() && super.isPageLoaded(VERIFY_PAGE)) {
            return true;
        }
        return false;
    }

    public void logout() {
        driver.findElement(LOGOUT_BTN);
    }

    public boolean fillNewAddress(boolean positive, String company,
            String telephone, String street, String city, String zipCode,
            String country) {
        boolean result = false;

        driver.findElement(MANAGE_ADDRESS_BTN).click();
        driver.findElement(ADD_NEW_ADDRESS_BTN).click();

        webElementInteractions.sendTextToElement(driver, COMPANY_FIELD, company);
        webElementInteractions.sendTextToElement(driver, TELEPHONE_FIELD,
                telephone);
        webElementInteractions.sendTextToElement(driver, STREET_ADDR_FIELD,
                street);
        webElementInteractions.sendTextToElement(driver, CITY_FIELD, city);
        webElementInteractions.sendTextToElement(driver, ZIP_FIELD, zipCode);
        webElementInteractions.selectByTextFromSelectableElement(driver,
                COUNTRY_FIELD, country);

        driver.findElement(SAVE_ADDRESS_FORM_BTN).click();
        if (positive) {
            result = webElementInteractions.isElementDisplayed(driver,
                    SUCCESSFUL_ADDR_SAVE_MSG);
        } else {
            result = webElementInteractions.isElementDisplayed(driver,
                    VALIDATION_EXCEPTION);
        }
        return result;
    }

    public boolean deleteAddress() {
        boolean result = false;
        driver.findElement(MANAGE_ADDRESS_BTN).click();
        if (WaitTool.fluentWaitForElement(driver, DELETE_ADDRESS_BTN, 3) != null) {
            driver.findElement(DELETE_ADDRESS_BTN).click();

            //This will fail
            driver.findElement(By.className("NOT_FOUND_CLASSNAME"));
            WDCommonActions.handleAlert(driver, true);
            result = WaitTool.waitForTextPresent(driver,
                    NO_ADDITIONAL_ADDRESS_MSG, WaitTool.DEFAULT_WAIT_4_ELEMENT);
        }
        return result;
    }

}
