/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 08.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.magento;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.gk.magento.pageobjects.customer_ui.MagentoCustomerDashboardPage;
import com.gk.magento.pageobjects.customer_ui.MagentoHomePage;
import com.gk.magento.pageobjects.customer_ui.MagentoLoginPage;
import com.gk.magento.pageobjects.customer_ui.MagentoRegistrationPage;
import com.gk.webdriver.WDTestRunner;
import com.gk.webdriver.handlers.CSVHandler;
import com.gk.webdriver.handlers.XLSHandler;
import com.gk.webdriver.utils.WDCommonActions;

/**
 * All functional tests for Magneto web application
 * 
 */
public class MagentoTests extends WDTestRunner {
    private static final Logger LOGGER = Logger.getLogger(MagentoTests.class);
    private static final String CSV_FILE_PATH = "resources\\dataProviderFiles\\csv\\sample.csv";
    private static final String XLS_FILE_PATH = "resources\\dataProviderFiles\\excel\\sample.xls";

    @Test(groups = { "gr1" }, timeOut = 180000)
    public void verifyServerIsWorking() {
        try {
            startTest();
            startStep("Open Magento home page.");
            MagentoHomePage magentoHomePage = new MagentoHomePage(driver);
            magentoHomePage.open();
            magentoHomePage.isPageLoaded();
            endStep();

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            endStep(false, ExceptionUtils.getStackTrace(e));
        }
        endTest();
    }

    @Test(dataProvider = "dataPoolExcel", groups = { "gr1" }, timeOut = 180000, dependsOnMethods = "verifyServerIsWorking")
    public void registerNewCustomer(String firstname, String lastname,
            String pass) {
        try {
            startTest();
            startStep("Open Magento home page.");
            MagentoHomePage magentoHomePage = new MagentoHomePage(driver);
            magentoHomePage.open();
            magentoHomePage.isPageLoaded();
            endStep();

            startStep("Open Login page.");
            MagentoLoginPage magentoLoginPage = new MagentoLoginPage(driver);
            magentoLoginPage.open();
            magentoLoginPage.isPageLoaded();
            endStep();

            startStep("Open Create Account Page.");
            MagentoRegistrationPage magentoRegPage =
                    magentoLoginPage.clickCreateAccount();
            magentoRegPage.isPageLoaded();
            endStep();

            startStep("Register New Customer.");
            String email = WDCommonActions.generateRandomEmailAddress(
                    "mailinator.com");
            MagentoCustomerDashboardPage customerDasboardPage = magentoRegPage
                    .registerNewCustomer(true, firstname, lastname, email, pass);
            customerDasboardPage.isPageLoaded();
            endStep();
            Thread.sleep(3000);

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            endStep(false, ExceptionUtils.getStackTrace(e));
        }
        endTest();
    }

    @Test(dataProvider = "dataPoolCsv", groups = { "gr1" }, timeOut = 180000, dependsOnMethods = "verifyServerIsWorking")
    public void loginAndLogoutWithCustomer(String email, String pass) {
        try {
            startTest();
            MagentoCustomerDashboardPage customerDashboardPage = loginUserAction(
                    email, pass);

            startStep("Logout current user.");
            customerDashboardPage.logout();
            endStep();
            Thread.sleep(3000);

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            endStep(false, ExceptionUtils.getStackTrace(e));
        }
        endTest();
    }

    @Test(dataProvider = "dataPoolExcel_1", groups = { "gr1" }, timeOut = 180000, dependsOnMethods = "verifyServerIsWorking")
    public void addNewAddressToCustomer(String email, String pass,
            String company, String telephone, String street, String city,
            String zipCode, String country) {
        try {
            startTest();
            MagentoCustomerDashboardPage customerDashboardPage = loginUserAction(
                    email, pass);

            startStep("Fill new address to the customer.");
            Assert.assertTrue(customerDashboardPage.fillNewAddress(true,
                    company, telephone, street, city, zipCode, country),
                    "New address was not added successfully.");
            endStep();

            startStep("Logout current user.");
            customerDashboardPage.logout();
            endStep();
            Thread.sleep(3000);

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            endStep(false, ExceptionUtils.getStackTrace(e));
        }
        endTest();
    }

    @Test(dataProvider = "dataPoolCsv", groups = { "gr1" }, timeOut = 180000, dependsOnMethods = "verifyServerIsWorking")
    public void deleteExistingAddress(String email, String pass) {
        try {
            startTest();
            MagentoCustomerDashboardPage customerDashboardPage = loginUserAction(
                    email, pass);

            startStep("Delete existing Address.");
            Assert.assertTrue(customerDashboardPage.deleteAddress(),
                    "Failed to delete address");
            endStep();

            startStep("Logout current user.");
            customerDashboardPage.logout();
            endStep();
            Thread.sleep(3000);

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            endStep(false, ExceptionUtils.getStackTrace(e));
        }
        endTest();
    }

    @DataProvider(parallel = false)
    private Object[][] dataPoolCsv() throws Exception {
        CSVHandler csvHandler = new CSVHandler(CSV_FILE_PATH);
        return (csvHandler.readCsvData());
    }

    @DataProvider(parallel = false)
    private Object[][] dataPoolExcel() throws Exception {
        XLSHandler xlsData = new XLSHandler(XLS_FILE_PATH);
        Object[][] retObjArr = xlsData.getTableDataArray("Magento", "F_gr1");
        return (retObjArr);
    }

    @DataProvider(parallel = false)
    private Object[][] dataPoolExcel_1() throws Exception {
        XLSHandler xlsData = new XLSHandler(XLS_FILE_PATH);
        Object[][] retObjArr = xlsData.getTableDataArray("Magento", "F_gr2");
        return (retObjArr);
    }

    private MagentoCustomerDashboardPage loginUserAction(String email,
            String pass) {

        startStep("Open Magento home page.");
        MagentoHomePage magentoHomePage = new MagentoHomePage(driver);
        magentoHomePage.open();
        magentoHomePage.isPageLoaded();
        endStep();

        startStep("Open Login page.");
        MagentoLoginPage magentoLoginPage = new MagentoLoginPage(driver);
        magentoLoginPage.open();
        magentoLoginPage.isPageLoaded();
        endStep();

        startStep("Login with registered customer.");
        MagentoCustomerDashboardPage customerDashboardPage = magentoLoginPage
                .loginAsCutomer(true, email, pass);
        customerDashboardPage.isPageLoaded();
        endStep();

        return customerDashboardPage;
    }
}
