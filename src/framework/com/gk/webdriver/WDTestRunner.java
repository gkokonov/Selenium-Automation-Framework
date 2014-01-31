/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 15.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.gk.webdriver.exceptions.AutomationException;
import com.gk.webdriver.handlers.ConfigFileHandler;
import com.gk.webdriver.utils.WDCommonActions;
import com.gk.webdriver.utils.WDTestRunnerActions;
import com.gk.webdriver.utils.video_recorder.VideoRecorder;

/**
 * Defines the test cases flow and framework behavior.
 * 
 */
public abstract class WDTestRunner extends WDTestRunnerActions {
    private ConfigFileHandler handleConfFile;
    private String browser;
    // Separate test result instance for each thread
    private final ThreadLocal<String> result = new ThreadLocal<String>();
    private String browserVersion;

    protected WebDriver driver;

    // Separate driver instance for each thread
    protected final ThreadLocal<WebDriver> driverThread = new ThreadLocal<WebDriver>();
    // Separate video recorder instance for each thread
    protected final ThreadLocal<VideoRecorder> videoRecorder = new ThreadLocal<VideoRecorder>();

    private static final Logger LOGGER = Logger.getLogger(WDTestRunner.class);

    AtomicInteger counter = new AtomicInteger(10);

    /**
     * @return the result
     */
    public String getResult() {
        return result.get();
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(String result) {
        this.result.set(result);
    }

    public void removeResult() {
        this.result.remove();
    }

    /**
     * @return the browserVersion
     */
    public String getBrowserVersion() {
        return browserVersion;
    }

    /**
     * @param browserVersion
     *            the browserVersion to set
     */
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    /**
     * @param browser
     *            the browser to set
     */
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    /**
     * @return the browser
     */
    public String getBrowser() {
        return browser;
    }

    @BeforeSuite(alwaysRun = true)
    @Parameters({ "configFile", "cleanDirs" })
    public void init(@Optional("config/config.properties") String configFile,
            @Optional("true") String cleanDirs) {
        try {

            System.setProperty("org.uncommons.reportng.escape-output", "false");
            //Reporter.log("Loading config files... ");
            LOGGER.info("Loading config files... ");
            LOGGER.info("Config File location: " + configFile + " Clean Dirs: "
                    + cleanDirs);

            handleConfFile = new ConfigFileHandler(configFile);

            //            System.setProperty("env.locale",
            //                    handleConfFile.getPropertyAsString("locale"));

            System.setProperty("default.wait.4.page",
                    handleConfFile.getPropertyAsString("DEFAULT_WAIT_4_PAGE"));
            System.setProperty("default.wait.4.element",
                    handleConfFile.getPropertyAsString("DEFAULT_WAIT_4_ELEMENT"));
            System.setProperty("long.wait.4.element",
                    handleConfFile.getPropertyAsString("LONG_WAIT_4_ELEMENT"));
            System.setProperty("short.wait.4.element",
                    handleConfFile.getPropertyAsString("SHORT_WAIT_4_ELEMENT"));
            System.setProperty("default.polling.interval", handleConfFile
                    .getPropertyAsString("DEFAULT_POLLING_INTERVAL"));

            //Cleaning directories
            if (cleanDirs.equalsIgnoreCase("true")) {
                WDTestRunnerActions.clearDirs(
                        handleConfFile.getPropertyAsString(
                                "dirsToClean").split(","));
            }


        } catch (Exception e) {
            Reporter.log("Critical error during initialization phase! Check logs for more info. ");
            LOGGER.error("Initialization phase fails with message: "
                    + e.toString());
            throw new RuntimeException();
        }
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({ "specifyBrowser", "killRunningBrowsers" })
    public void setUp(@Optional("Firefox") String specifyBrowser, @Optional("true") String killRunningBrowsers) {
        try {
            LOGGER.info("Get browser type from TestNG .xml file and kill running browser processes.");
            this.setBrowser(specifyBrowser);
            if (killRunningBrowsers.equalsIgnoreCase("true")) {
                String[] browsersToKill = handleConfFile.getPropertyAsString(
                        "exeToKill").split(",");
                WDTestRunnerActions.killRunningApps(browsersToKill);
            }

        } catch (Exception e) {
            Reporter.log("Initialization phase failed. Check log file for more info.");
            LOGGER.error("Initialization phase exception", e.getCause());
            throw new RuntimeException();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        LOGGER.info("Tear Down... ");
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        LOGGER.info("Open browser and manage timeouts.");

        try {
            String ffDownloadPath = handleConfFile.getPropertyAsString(
                    "FirefoxDownloadPath").trim();
            String ieDriver = handleConfFile.getPropertyAsString("ieDriver");
            String chromeDriver = handleConfFile.getPropertyAsString(
                    "chromeDriver");
            String chromeBinaryPath = handleConfFile.getPropertyAsString(
                    "chrome.binary");
            String operaBinaryPath = handleConfFile.getPropertyAsString(
                    "opera.binary");

            driverThread.set(WDTestRunnerActions.browserProfileConfiguration(
                    this.getBrowser(), ffDownloadPath, ieDriver, chromeDriver,
                    operaBinaryPath, chromeBinaryPath));

            if (!this.getBrowser().equals("Safari")) {
                driverThread.get().manage().timeouts().pageLoadTimeout(
                        Long.parseLong(System.getProperty(
                                "default.wait.4.page")), TimeUnit.SECONDS);
                driverThread.get().manage().timeouts().implicitlyWait(
                        Long.parseLong(System.getProperty(
                                "default.wait.4.element")), TimeUnit.SECONDS);
            }

            driver = driverThread.get();
        } catch (Exception e) {
            LOGGER.error("Exception during browser startup: " + e.getMessage());
            throw new RuntimeException("Exception during browser startup: ",
                    e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
            LOGGER.info("Close browser.");
        }
    }

    protected void startTest(String urlNavigation) {
        if (urlNavigation != null) {
            driver.get(urlNavigation);
            startTest();
        } else {
            throw new AutomationException("Navigation URL is Null!");
        }
    }

    protected void startTest() {
        LOGGER.info("Initialize and start recording.");

        //Initialize and start recording
        videoRecorder.set(new VideoRecorder());
        videoRecorder.get().startRecording(
                handleConfFile.getPropertyAsString("video.rec.folder"), "video");
        videoRecorder.get().getScreenRecorder().setMaxRecordingTime(600000);

        // Opera driver does not support maximize opearion (1.5 version)
        if (!this.getBrowser().equals("Opera")) {
            WDCommonActions.changeBrowserWindowSize(driver);
        }

        this.setBrowserVersion(WDTestRunnerActions.extractBrowserVersion(
                driver,
                this.getBrowser()));

        Reporter.log("<p>Browser: <font size=2 color='blue'>"
                + this.getBrowserVersion() + "</font></p>");
        Reporter.log("<ol type='1'>");

    }

    protected void endTest() {
        Reporter.log("</ol>");

        // Stop video recording
        LOGGER.info("Stop current recording.");
        videoRecorder.get().stopRecording();
        videoRecorder.remove();

        if (this.getResult() != null) {
            this.removeResult();
            Assert.fail("Error occurs during test execution.");
        } else {
            LOGGER.info("Delete latest recorded video: "
                    + WDTestRunnerActions.deleteLastModifiedFile(handleConfFile
                            .getPropertyAsString("video.rec.folder")));
        }
    }

    protected void startStep(String stepDescription) {
        Reporter.log("<li>Step Name: " + stepDescription);
    }

    protected void endStep() {
        endStep(true, "Completed");
    }

    protected void endStep(boolean condition, String errorMessage) {
        StringBuilder logLine = new StringBuilder(64);
        int id = counter.incrementAndGet();

        if (condition) {
            logLine.append(" - <b><font color='green'>OK</font></b>");
        } else {
            logLine.append(" - <b><font color='red'>FAILED!</font></b><br>");
            //logLine.append("<font color='red'>" + errorMessage + "</font><br>");
            logLine.append("<a title=\"Error Stacktrace\" href=\"javascript:toggleElement('error-"
                    + id
                    + "', 'block')\"><b>Click to expand/collapse error stacktrace.</b></a><br>");
            logLine.append("<div id='error-"
                    + id
                    + "' class='errorStackTrace' style='display: none;'><font color='red'>"
                    + errorMessage + "</font></div>");
            logLine.append(WDTestRunnerActions.captureScreenshot(driver));
            this.setResult(errorMessage);
        }

        logLine.append("</li>");
        Reporter.log(logLine.toString());
    }

    public static void logWarning(String msg) {
        Reporter.log("<b><font color='orange'>" + msg + "</font></b>");
    }

    public static void logError(String msg) {
        Reporter.log("<b><font color='red'>" + msg + "</font></b>");
        Assert.fail();
    }

    public static void logMsg(String msg) {
        Reporter.log("_" + msg);
    }
}
