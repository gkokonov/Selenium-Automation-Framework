/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 15.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.exceptions;

import org.apache.log4j.Logger;

public class AutomationException extends RuntimeException {
    private static final Logger LOGGER = Logger
            .getLogger(AutomationException.class);

    /**
     * Automation framework exception handler
     */
    private static final long serialVersionUID = 1000L;

    public AutomationException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.error("Automation exception with message: " + message
                + "and cause: " + cause.toString());
    }

    public AutomationException(String message) {
        super(message);
        LOGGER.error("Automation exception with message: " + message);
    }

    public AutomationException() {
        super();
        LOGGER.error("Automation exception!");
    }

}
