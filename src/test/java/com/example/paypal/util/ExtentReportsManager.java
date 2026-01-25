package com.example.paypal.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extent Reports manager for generating comprehensive HTML test reports.
 * Provides methods to create and manage test reports with screenshots.
 */
public class ExtentReportsManager {

    private static final Logger logger = LoggerFactory.getLogger(ExtentReportsManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final String REPORT_DIR = "target/extent-reports";
    private static final String REPORT_FILE = REPORT_DIR + "/ExtentReport.html";

    static {
        // Create reports directory if it doesn't exist
        File dir = new File(REPORT_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                logger.info("Extent Reports directory created: {}", REPORT_DIR);
            }
        }
    }

    /**
     * Initialize Extent Reports
     */
    public static void initializeExtentReports() {
        if (extent == null) {
            try {
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_FILE);
                sparkReporter.config().setTheme(Theme.DARK);
                sparkReporter.config().setDocumentTitle("PayPal BDD Test Report");
                sparkReporter.config().setReportName("PayPal Automation Report");
                sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);

                // Add system information
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                extent.setSystemInfo("User", System.getProperty("user.name"));
                extent.setSystemInfo("Report Date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                logger.info("Extent Reports initialized successfully at: {}", new File(REPORT_FILE).getAbsolutePath());
            } catch (Exception e) {
                logger.error("Failed to initialize Extent Reports", e);
            }
        }
    }

    /**
     * Create a new test in the report
     * 
     * @param testName    Name of the test
     * @param description Description of the test
     */
    public static void createTest(String testName, String description) {
        if (extent != null) {
            ExtentTest extentTest = extent.createTest(testName, description);
            test.set(extentTest);
            logger.info("Test created in Extent Report: {}", testName);
        }
    }

    /**
     * Get the current ExtentTest instance
     * 
     * @return Current ExtentTest
     */
    public static ExtentTest getTest() {
        return test.get();
    }

    /**
     * Log a step with PASS status and screenshot
     * 
     * @param stepName       Name of the step
     * @param details        Details of the step
     * @param screenshotPath Path to screenshot file
     */
    public static void logPassWithScreenshot(String stepName, String details, String screenshotPath) {
        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.PASS, stepName + ": " + details);
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                try {
                    extentTest.addScreenCaptureFromPath(screenshotPath);
                    logger.info("Screenshot attached to PASS step: {}", screenshotPath);
                } catch (Exception e) {
                    logger.error("Failed to attach screenshot: {}", screenshotPath, e);
                }
            }
        }
    }

    /**
     * Log a step with FAIL status and screenshot
     * 
     * @param stepName       Name of the step
     * @param details        Details of the step
     * @param screenshotPath Path to screenshot file
     */
    public static void logFailWithScreenshot(String stepName, String details, String screenshotPath) {
        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.FAIL, stepName + ": " + details);
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                try {
                    extentTest.addScreenCaptureFromPath(screenshotPath);
                    logger.info("Screenshot attached to FAIL step: {}", screenshotPath);
                } catch (Exception e) {
                    logger.error("Failed to attach screenshot: {}", screenshotPath, e);
                }
            }
        }
    }

    /**
     * Log an INFO step
     * 
     * @param stepName Name of the step
     * @param details  Details of the step
     */
    public static void logInfo(String stepName, String details) {
        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.INFO, stepName + ": " + details);
        }
    }

    /**
     * Log a WARNING step
     * 
     * @param stepName Name of the step
     * @param details  Details of the step
     */
    public static void logWarning(String stepName, String details) {
        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.WARNING, stepName + ": " + details);
        }
    }

    /**
     * Flush and close the Extent Reports
     */
    public static void flushExtentReports() {
        if (extent != null) {
            extent.flush();
            logger.info("Extent Report generated at: {}", new File(REPORT_FILE).getAbsolutePath());
        }
    }

    /**
     * Get the Extent Reports instance
     * 
     * @return ExtentReports instance
     */
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            initializeExtentReports();
        }
        return extent;
    }

    /**
     * Get report file path
     * 
     * @return Path to the generated report
     */
    public static String getReportPath() {
        return new File(REPORT_FILE).getAbsolutePath();
    }
}
