package com.example.paypal.util;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing screenshots during test execution.
 * Screenshots are saved to target/screenshots directory with timestamp.
 */
public class ScreenshotUtil {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";

    static {
        // Create screenshots directory if it doesn't exist
        File dir = new File(SCREENSHOT_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                logger.info("Screenshots directory created: {}", SCREENSHOT_DIR);
            }
        }
    }

    /**
     * Capture screenshot from the current WebDriver instance
     * 
     * @param driver         WebDriver instance
     * @param screenshotName Name for the screenshot file
     * @return Absolute path to the saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) {
            logger.warn("WebDriver is null. Cannot capture screenshot.");
            return null;
        }

        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            // Generate unique filename with timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
            String filename = screenshotName.replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + timestamp + ".png";
            String destinationPath = SCREENSHOT_DIR + File.separator + filename;

            File destinationFile = new File(destinationPath);
            FileUtils.copyFile(sourceFile, destinationFile);

            logger.info("Screenshot captured successfully: {}", destinationPath);
            return destinationPath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", screenshotName, e);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error while capturing screenshot", e);
            return null;
        }
    }

    /**
     * Capture screenshot with default naming convention
     * 
     * @param driver   WebDriver instance
     * @param stepName Name of the test step
     * @return Absolute path to the saved screenshot
     */
    public static String takeScreenshot(WebDriver driver, String stepName) {
        return captureScreenshot(driver, stepName);
    }

    /**
     * Capture screenshot on failure
     * 
     * @param driver       WebDriver instance
     * @param testName     Name of the test
     * @param scenarioName Name of the scenario
     * @return Absolute path to the saved screenshot
     */
    public static String captureFailureScreenshot(WebDriver driver, String testName, String scenarioName) {
        String screenshotName = "FAILURE_" + testName + "_" + scenarioName;
        return captureScreenshot(driver, screenshotName);
    }

    /**
     * Get absolute path to screenshots directory
     * 
     * @return Screenshots directory path
     */
    public static String getScreenshotDirectory() {
        return new File(SCREENSHOT_DIR).getAbsolutePath();
    }
}
