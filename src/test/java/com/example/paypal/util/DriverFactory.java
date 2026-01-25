package com.example.paypal.util;

import java.net.URL;
import java.rmi.Remote;
import java.time.Duration;

import org.apache.http.client.utils.URIUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
    }

    // Default browser (safe for Windows)
    private static final String DEFAULT_BROWSER = "edge";

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            initDriver(DEFAULT_BROWSER);
        }
        return driver.get();
    }

    public static void initDriver(String browser) {
        try {
            log.info("Initializing WebDriver | Browser: {}", browser);

            WebDriver webDriver;
           // String hubUrlString = System.getProperty("SELENIUM_GRID_URL");
           // String hubUrlString = System.getenv("SELENIUM_GRID_URL");
            
            String hubUrlString = "http://localhost:4444/wd/hub";
           

            URL urihub = new URL(hubUrlString);
            switch (browser.toLowerCase()) {

                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");
                    webDriver = new RemoteWebDriver(urihub, chromeOptions);
                    //webDriver = new ChromeDriver( chromeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    webDriver = new RemoteWebDriver(urihub, firefoxOptions);
                    //webDriver = new FirefoxDriver( firefoxOptions);
                    break;

                case "edge":
                default:
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--start-maximized");
                    edgeOptions.addArguments("--disable-save-password-bubble");
                    edgeOptions.addArguments("--disable-autofill-keyboard-accessory-view");
                    edgeOptions.addArguments("--disable-autofill-credit-card-upload");
                    edgeOptions.addArguments("--disable-features=AutofillEnablePayments,AutofillEnableCardBenefits");
                    edgeOptions.addArguments("--disable-infobars");
                    edgeOptions.addArguments("--disable-notifications");
                    edgeOptions.addArguments("--disable-extensions");
                    edgeOptions.addArguments("--disable-popup-blocking");
                    //edgeOptions.addArguments("--headless");
                   
                    webDriver = new RemoteWebDriver(urihub, edgeOptions);
   //                 webDriver = new EdgeDriver(edgeOptions);
                    break;
            }

            webDriver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(5));
            webDriver.manage().timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(15));

            driver.set(webDriver);

            log.info("WebDriver initialized successfully");

        } catch (Exception e) {
            log.error("WebDriver initialization failed", e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                log.info("Closing WebDriver");
                driver.get().quit();
            } catch (Exception e) {
                log.warn("Error while closing WebDriver", e);
            } finally {
                driver.remove(); // 🔥 MUST for parallel runs
            }
        }
    }

    public static void main(String[] args) {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://www.example.com");
        System.out.println(driver.getTitle());
        DriverFactory.quitDriver();
    }

}
