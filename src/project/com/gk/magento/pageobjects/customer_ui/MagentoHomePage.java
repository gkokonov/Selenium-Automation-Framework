/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 08.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.magento.pageobjects.customer_ui;

import org.openqa.selenium.WebDriver;

import com.gk.magento.pageobjects.BasePage;

public class MagentoHomePage extends BasePage {
    private final static String URL = "http://demo.magentocommerce.com/";
    private final static String TITLE = "Home page - Magento Commerce Demo Store";

    public MagentoHomePage(WebDriver driver) {
        super(driver, TITLE, URL);
    }

}
