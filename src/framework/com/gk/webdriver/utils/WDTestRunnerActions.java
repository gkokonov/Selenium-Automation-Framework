/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 18.11.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.os.WindowsRegistryException;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.gk.webdriver.exceptions.AutomationException;
import com.opera.core.systems.OperaDriver;

/**
 * Utilities used by WebDriver Test runner class.
 * 
 */
public abstract class WDTestRunnerActions {

    private static final Logger LOGGER =
            Logger.getLogger(WDTestRunnerActions.class);

    /**
     * Clears all files in specified directory.
     * 
     * @param dirToClean
     *            Specify directory to clear
     * @throws SecurityException
     */
    public static void clearDirs(String[] dirsToClean) throws SecurityException,
            IOException {
        for (String dir : dirsToClean) {
            File outputDir = new File(dir);
            if (outputDir.exists()) {
                String[] list = outputDir.list();
                for (String element : list) {
                    File file = new File(outputDir, element);
                    FileUtils.forceDelete(file);
                }
            }
        }
    }

    /**
     * Takes screenshot.
     * 
     * @param driver
     *            WebDriver instance
     * @return Returns HTML string that needs to be logged in the TestNG
     *         Reporter.
     */
    public static String captureScreenshot(WebDriver driver) {
        try {
            File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            String imgName = "scrnShot_"
                    + (new SimpleDateFormat("yyyyMMdd-HHmmss")
                    .format(new Date())) + ".png";
            FileUtils.copyFile(scrFile, new File("test-output\\Screenshots\\"
                    + imgName));
            return ("<a href=\"../../Screenshots/" + imgName
                    + "\"><p align=\"left\">View Screenshot</p></a>");
            //this.logMessage("...<a href=" + screenShotLink + " target='_blank'>view screenshot</a>");
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return ("<font color='orange'>Failed to capture image.</font>");
        }
    }

    /**
     * Takes screenshot with specified file name prefix.
     * 
     * @param driver
     *            WebDriver instance
     * @param fileNamePrefix
     *            File name prefix
     * @return Returns full image name. If a problem occurs, returns null.
     */
    public static String captureScreenshot(WebDriver driver,
            String fileNamePrefix) {
        try {
            File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            String imgName = fileNamePrefix + "_"
                    + (new SimpleDateFormat("yyyyMMdd-HHmmss")
                    .format(new Date())) + ".png";
            FileUtils.copyFile(scrFile, new File("test-output\\Screenshots\\"
                    + imgName));
            return imgName;
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * @param browser
     * @param ffDownloadPath
     * @param ieDriver
     * @param chromeDriver
     * @param operaBinaryPath
     * @param chromeBinaryPath
     * @return WebDriver instance for a specific browser profile
     * @throws AutomationException
     * @throws IOException
     */
    public static WebDriver browserProfileConfiguration(String browser,
            String ffDownloadPath, String ieDriver, String chromeDriver,
            String operaBinaryPath, String chromeBinaryPath)
                    throws AutomationException, IOException {

        WebDriver driver = null;

        switch (browser) {
        case "Firefox": {
            FirefoxProfile fProfile = new FirefoxProfile();
            fProfile.setEnableNativeEvents(true);
            fProfile.setPreference("browser.cache.disk.enable", false);
            fProfile.setAcceptUntrustedCertificates(true);
            fProfile.setPreference("browser.download.folderList", 2);
            fProfile.setPreference("browser.download.manager.showWhenStarting",
                    false);
            fProfile.setPreference("browser.download.dir", ffDownloadPath);
            fProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                    "application/zip");
            fProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                    "application/octet-stream");
            fProfile.setPreference("privacy.popups.disable_from_plugins", 3);
            fProfile.addExtension(new File(
                    "resources/ffExtensions/firebug.xpi").getCanonicalFile());
            fProfile.addExtension(new File(
                    "resources/ffExtensions/FireXPath.xpi").getCanonicalFile());
            fProfile.setPreference("extensions.firebug.currentVersion", "1.12.5");
            driver = new FirefoxDriver(fProfile);
            LOGGER.info("New Firefox driver instance created.");
            break;
        }
        case "IE": {
            System.setProperty("webdriver.ie.driver", ieDriver);
            DesiredCapabilities ieCapabilities = DesiredCapabilities
                    .internetExplorer();
            ieCapabilities
            .setCapability(
                    InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                    true);
            ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            ieCapabilities.setCapability("ie.ensureCleanSession", true);
            driver = new InternetExplorerDriver(ieCapabilities);
            LOGGER.info("New IE driver instance created.");
            break;
        }
        case "Chrome": {
            System.setProperty("webdriver.chrome.driver", chromeDriver);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            options.setBinary(chromeBinaryPath);
            driver = new ChromeDriver(options);
            LOGGER.info("New Chrome driver instance created.");
            break;
        }
        case "Opera": {
            System.setProperty("os.name", "windows");
            DesiredCapabilities capabilities = DesiredCapabilities.opera();
            capabilities.setCapability("opera.binary", operaBinaryPath);
            capabilities.setCapability(
                    "opera.arguments", "-nowin -nomail -fullscreen");
            driver = new OperaDriver(capabilities);
            LOGGER.info("New Opera driver instance created.");
            break;
        }
        case "Safari":
            DesiredCapabilities capabilities = DesiredCapabilities.safari();
            capabilities.setCapability("cleanSession", true);
            capabilities.setJavascriptEnabled(true);
            capabilities.setPlatform(Platform.WINDOWS);
            driver = new SafariDriver(capabilities);
            LOGGER.info("New Safari driver instance created.");
            break;
        default:
            LOGGER.error("Fail to start any browser, please check config file or browser installation. Current browser param value: "
                    + browser);
            throw new AutomationException(
                    "Fail to start browser, please check browser parameter.");
        }
        return driver;
    }

    /**
     * Kill specified running apps, if possible
     * 
     * @param runningApps
     */
    public static void killRunningApps(String[] runningApps) {
        for (String browserExe : runningApps) {
            try {
                WindowsUtils.killByName(browserExe);
            } catch (WindowsRegistryException e) {
                LOGGER.info("The process " + browserExe + " is not found.");
            }
        }
    }

    /**
     * @param driver
     *            WebDriver instance
     * @param br
     *            Currently used browser
     * @return Browser version
     */
    public static String extractBrowserVersion(WebDriver driver, String br) {
        String brVersion = "";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userAgent = js.executeScript("return window.navigator.userAgent")
                .toString();
        Pattern p;
        if (br.contains("Firefox")) {
            p = Pattern.compile("Firefox/\\d+.\\d+");
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                brVersion = m.group(0);
            }
        } else if (br.contains("IE")) {
            p = Pattern.compile("MSIE \\d+.\\d+");
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                brVersion = m.group(0);
            }
        } else if (br.contains("Chrome")) {
            p = Pattern.compile("Chrome/\\d+(.\\d+)*");
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                brVersion = m.group(0);
            }
        }
        return brVersion;
    }

    /**
     * @param fileDir
     *            File location dir
     * @return True if deletion is successful, otherwise return false.
     */
    public static boolean deleteLastModifiedFile(String fileDir) {
        File dir = new File(fileDir);
        File[] files = dir.listFiles();
        LOGGER.info("Video Files: " + Arrays.toString(files));
        if (files != null && files.length != 0) {
            Arrays.sort(
                    files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
            return files[files.length - 1].delete();
        }
        return false;
    }

}
