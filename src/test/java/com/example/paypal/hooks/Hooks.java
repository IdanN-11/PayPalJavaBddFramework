package com.example.paypal.hooks;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.paypal.util.ConfigLoader;
import com.example.paypal.util.DriverFactory;
import com.example.paypal.util.ExtentReportsManager;
import com.example.paypal.util.RestHelper;
import com.example.paypal.util.ScreenshotUtil;
import com.example.paypal.util.TestContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    public static TestContext testContext = new TestContext();
    public static RestHelper restHelper = new RestHelper();

    @BeforeAll
    public static void loadData() {
        logger.info("Loading test configuration and data...");
        logger.info("Initializing Extent Reports...");
        ExtentReportsManager.initializeExtentReports();
        // allData = ExcelReader.read(
        // "src/test/resources/testdata/PayPalTestData.xlsx",
        // "CheckoutTests"
        // );
        ConfigLoader.load();
        logger.info("Test configuration loaded successfully");
    }

    @Before
    public void before(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());

        // Create test in Extent Report
        ExtentReportsManager.createTest(scenario.getName(), "PayPal BDD Test Scenario");
        ExtentReportsManager.logInfo("Scenario Started", "Scenario: " + scenario.getName());

        // TestContext.data.set(allData.get(0));
    }

    @After
    public void after(Scenario scenario) {
        logger.info("Finishing scenario: {} with status: {}", scenario.getName(),
                scenario.isFailed() ? "FAILED" : "PASSED");

        WebDriver driver = null;
        try {
            driver = DriverFactory.getDriver();
        } catch (Exception e) {
            logger.debug("WebDriver not available for screenshot capture");
        }

        if (scenario.isFailed()) {
            logger.error("Scenario FAILED: {}", scenario.getName());

            // Capture failure screenshot
            if (driver != null) {
                String screenshotPath = ScreenshotUtil.captureFailureScreenshot(
                        driver,
                        scenario.getName(),
                        "failure");
                ExtentReportsManager.logFailWithScreenshot(
                        "Scenario Failed",
                        "Failure screenshot captured",
                        screenshotPath);
            } else {
                ExtentReportsManager.logFailWithScreenshot(
                        "Scenario Failed",
                        "WebDriver not available for screenshot",
                        null);
            }
        } else {
            logger.info("Scenario PASSED: {}", scenario.getName());
            ExtentReportsManager.logInfo("Scenario Passed", "All steps executed successfully");
        }

        DriverFactory.quitDriver();
        // TestContext.data.remove();
    }

    @After(order = Integer.MAX_VALUE)
    public void flushReports() {
        logger.info("Flushing Extent Reports...");
        ExtentReportsManager.flushExtentReports();
        logger.info("Extent Reports flushed successfully");
    }
}
