package com.example.paypal.steps;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.paypal.POJO.ObjectMapperPayload;
import com.example.paypal.hooks.Hooks;
import com.example.paypal.pageobject.AccountsPage;
import com.example.paypal.pageobject.LoginPage;
import com.example.paypal.util.DriverFactory;
import com.example.paypal.util.ExtentReportsManager;
import com.example.paypal.util.InvoiceIdGenerator;
import com.example.paypal.util.JwtGeneratorRS256;
import com.example.paypal.util.ResponseHandler;
import com.example.paypal.util.RestHelper;
import com.example.paypal.util.ScreenshotUtil;
import com.example.paypal.util.TestContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiSteps {
    private static final Logger log = LoggerFactory.getLogger(ApiSteps.class);
    private RestHelper restHelper = Hooks.restHelper;
    private TestContext testContext = Hooks.testContext;

    @Given("TPP have a valid PayPal access token")
    public void token() {
        log.info("Retrieving PayPal access token...");
        try {
            String token = RestHelper.PostCCToken();
            if (token == null || token.isEmpty()) {
                log.error("Failed to retrieve PayPal access token - token is empty");
                throw new RuntimeException("Failed to retrieve PayPal access token");
            }
            testContext.setToken(token);
            log.info("PayPal access token retrieved successfully: {}",
                    token.substring(0, Math.min(20, token.length())) + "...");
        } catch (Exception e) {
            log.error("Exception while retrieving PayPal access token", e);
            throw new RuntimeException("Token retrieval failed", e);
        }
    }

    @When("TPP create a checkout order using data from excel for {string}")
    public void createOrder(String TestId) {
        log.info("Creating PayPal checkout order with test data: {}", TestId);
        try {
            String path = System.getProperty("user.dir") + "/src/test/resources/paypal_multi_order_with_items.xlsx";
            log.debug("Loading test data from path: {}", path);

            ObjectMapperPayload obm = new ObjectMapperPayload();
            String body = obm.CreatePayload(path, TestId);
            String invoinceId = InvoiceIdGenerator.generate();
            body = body.replace("INV_ID", invoinceId);
            log.debug("Generated invoice ID: {}", invoinceId);

            String fullUrl = System.getProperty("paypal.base.url") + System.getProperty("paypal.createO.url");
            HashMap<String, String> Headers = new HashMap<>();
            String token = "Bearer " + testContext.getToken();
            Headers.put("Authorization", token);
            Headers.put("Content-Type", "application/json");

            log.info("Sending order creation request to PayPal API: {}", fullUrl);
            Response res = restHelper.postMessage(body, Headers);

            ResponseHandler responseHandler = new ResponseHandler(res);
            String orderId = res.jsonPath().get("id");
            testContext.setorderId(orderId);
            testContext.setcreateOrderResponse(res);
            String rel = "payer-action";
            testContext.setredirectUrl(responseHandler.getRedirectUrlByRel(rel));
            log.info("redirect url  : {}", responseHandler.getRedirectUrlByRel(rel));
            log.info("Order created successfully with ID: {}", orderId);
            log.info("Redirect URL obtained for payer action");

            // Log to extent report
            ExtentReportsManager.logPassWithScreenshot("Order Created",
                    "Order ID: " + orderId + " | Invoice ID: " + invoinceId, null);
        } catch (Exception e) {
            log.error("Failed to create PayPal order for test: {}", TestId, e);
            ExtentReportsManager.logFailWithScreenshot("Order Creation Failed",
                    "Error: " + e.getMessage(), null);
            throw new RuntimeException("Order creation failed", e);
        }
    }

    @Then("TPP check order is succesfully created with {string}")
    public void checkCreateOrderStatus(String createOrderStatusCode) {
        log.info("Verifying order creation status code from feature: {}", createOrderStatusCode);
        try {
            Response res = testContext.getcreateOrderResponse();
            String statusCode = String.valueOf(res.getStatusCode());
            log.info("Order creation response status code from response:", statusCode);
            log.info("Order creation response :", res);
            assertEquals(statusCode, createOrderStatusCode);
            log.info("Order creation status validation passed");
        } catch (AssertionError e) {
            log.error("Order creation status mismatch. Expected: {}, but was: {}", createOrderStatusCode, e);
            throw e;
        } catch (Exception e) {
            log.error("Error checking order creation status", e);
            throw new RuntimeException("Status validation failed", e);
        }
    }

    // @Then(" TPP update the order with {string} details")
    // public void updateOrder(String updatePayload) {
    //
    // }

    // @And("TPP confirm the order is update with update order resopnse code
    // {string}")
    // public void checkUpdateOrderStatus(String updateOrderstatusCode) {
    // Response res = testContext.getupdateOrderResponse();
    // int statusCode = res.getStatusCode();
    // assertEquals(statusCode,updateOrderstatusCode);
    // }

    @And("TPP confirm the checkout order and response code should be {string} and {string}")
    public void confirmOrder(String confirmOrderStatusCode, String payload) {
        log.info("Confirming checkout order with payload: {}", payload);
        try {
            String path = System.getProperty("user.dir") + "/src/test/resources/paypal_multi_order_with_items.xlsx";
            ObjectMapperPayload obm = new ObjectMapperPayload();
            String orderId = testContext.getorderId();
            String body = obm.CreatePayload(path, payload);
            log.debug("Order ID for confirmation: {}", orderId);

            String fullUrl = System.getProperty("paypal.base.url") + System.getProperty("paypal.createO.url") + "/"
                    + testContext.getorderId() + "/confirm-payment-source";
            HashMap<String, String> Headers = new HashMap<>();
            String token = "Bearer " + testContext.getToken();
            Headers.put("Authorization", token);
            Headers.put("Content-Type", "application/json");

            log.info("Sending order confirmation request to: {}", fullUrl);
            Response res = restHelper.postMessage(body, Headers);

            String statusCode = String.valueOf(res.getStatusCode());
            log.info("Order confirmation response status code: {}", statusCode);
            assertEquals(confirmOrderStatusCode, statusCode);
            log.info("Order confirmation status validation passed");
        } catch (AssertionError e) {
            log.error("Order confirmation status mismatch. Expected: {}", confirmOrderStatusCode, e);
            throw e;
        } catch (Exception e) {
            log.error("Error confirming order", e);
            throw new RuntimeException("Order confirmation failed", e);
        }
    }

    @Then("TPP redirect PSU to login on paypal  to approve the order with {string} and {string}")
    public void approve(String user_email, String user_password) throws InterruptedException {
        log.info("Redirecting to PayPal login page for order approval");
        try {
            String url = testContext.getredirectUrl();

            if (url == null || url.isEmpty()) {
                log.error("Redirect URL is null or empty. Cannot navigate to PayPal login");
                throw new RuntimeException("Redirect URL is null or empty");
            }

            log.info("Redirect URL obtained: {}", url);

            WebDriver driver = DriverFactory.getDriver();

            if (driver == null) {
                log.error("WebDriver initialization failed - driver is null");
                throw new RuntimeException("Failed to initialize WebDriver");
            }

            log.info("WebDriver initialized successfully");
            log.info("Navigating to PayPal login with credentials: email={}", user_email);

            Thread.sleep(70);
            log.info("Executing driver.get() with URL: {}", url);
            driver.get(url);
            log.info("Navigation to PayPal login page successful");
            Thread.sleep(70);

            // Capture screenshot of login page
            String loginPageScreenshot = ScreenshotUtil.takeScreenshot(driver, "01_PayPal_Login_Page");
            ExtentReportsManager.logPassWithScreenshot("PayPal Login Page Loaded",
                    "Login page displayed successfully", loginPageScreenshot);

            LoginPage loginpage = new LoginPage(driver);
            loginpage.loginWithEmail(user_email);
            log.info("Entered email on PayPal login page");

            // Capture screenshot after email entry
            String afterEmailScreenshot = ScreenshotUtil.takeScreenshot(driver, "02_After_Email_Entry");
            ExtentReportsManager.logPassWithScreenshot("Email Entered",
                    "User email entered successfully", afterEmailScreenshot);

            Thread.sleep(70);
            Thread.sleep(70);

            loginpage.loginWithPassword(user_password);
            log.info("Entered password on PayPal login page");

            // Capture screenshot after password entry
            String afterPasswordScreenshot = ScreenshotUtil.takeScreenshot(driver, "03_After_Password_Entry");
            ExtentReportsManager.logPassWithScreenshot("Password Entered",
                    "User password entered successfully", afterPasswordScreenshot);

            Thread.sleep(700);
            log.info("PayPal login completed successfully");

            // Final screenshot after login
            String loginCompleteScreenshot = ScreenshotUtil.takeScreenshot(driver, "04_Login_Completed");
            ExtentReportsManager.logPassWithScreenshot("Login Completed",
                    "PayPal login completed successfully", loginCompleteScreenshot);
        } catch (Exception e) {
            log.error("Error during PayPal login and approval process", e);
            WebDriver driver = null;
            try {
                driver = DriverFactory.getDriver();
                String errorScreenshot = ScreenshotUtil.takeScreenshot(driver, "ERROR_Login_Failed");
                ExtentReportsManager.logFailWithScreenshot("Login Failed",
                        "Error during login: " + e.getMessage(), errorScreenshot);
            } catch (Exception ex) {
                log.debug("WebDriver not available for error screenshot");
                ExtentReportsManager.logFailWithScreenshot("Login Failed",
                        "Error during login: " + e.getMessage(), null);
            }
            throw new RuntimeException("PayPal login failed", e);
        }
    }

    @And("PSU select account {string} on accounts page and submit the payment")
    public void selectAccountandSubmit(String payer_account) {
        log.info("Selecting payer account: {} and submitting payment", payer_account);
        try {
            WebDriver driver = DriverFactory.getDriver();
            AccountsPage accountsPage = new AccountsPage(driver);

            log.debug("Selecting account by name: {}", payer_account);

            // Capture screenshot of accounts page before selection
            String accountsPageScreenshot = ScreenshotUtil.takeScreenshot(driver, "05_Accounts_Page_Before_Selection");
            ExtentReportsManager.logPassWithScreenshot("Accounts Page Displayed",
                    "Accounts page loaded successfully", accountsPageScreenshot);

            accountsPage.selectAccountByName(payer_account);
            Thread.sleep(400);

            // Capture screenshot after account selection
            String afterSelectionScreenshot = ScreenshotUtil.takeScreenshot(driver, "06_After_Account_Selection");
            ExtentReportsManager.logPassWithScreenshot("Account Selected",
                    "Account '" + payer_account + "' selected successfully", afterSelectionScreenshot);

            log.info("Clicking complete purchase button");
            accountsPage.clickCompletePurchase();
            Thread.sleep(500);

            // Capture screenshot after completing purchase
            String completePurchaseScreenshot = ScreenshotUtil.takeScreenshot(driver, "07_Complete_Purchase_Clicked");
            ExtentReportsManager.logPassWithScreenshot("Purchase Completed",
                    "Complete purchase button clicked successfully", completePurchaseScreenshot);

            log.info("Payment submission completed successfully");
        } catch (Exception e) {
            log.error("Error selecting account or submitting payment for account: {}", payer_account, e);
            WebDriver driver = null;
            try {
                driver = DriverFactory.getDriver();
                String errorScreenshot = ScreenshotUtil.takeScreenshot(driver, "ERROR_Account_Selection_Failed");
                ExtentReportsManager.logFailWithScreenshot("Account Selection Failed",
                        "Error: " + e.getMessage(), errorScreenshot);
            } catch (Exception ex) {
                log.debug("WebDriver not available for error screenshot");
                ExtentReportsManager.logFailWithScreenshot("Account Selection Failed",
                        "Error: " + e.getMessage(), null);
            }
            throw new RuntimeException("Payment submission failed", e);
        }
    }

    @Then("TPP authorize order after user approval through paypal with payload {string} and response code should be {string}")
    public void authorizeOrder(String authpayload, String authorizatioresCode)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Authorizing PayPal order with payload: {}", authpayload);
        try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            String body = "";
            String orderId = testContext.getorderId();
            log.debug("Order ID for authorization: {}", orderId);

            String fullUrl = System.getProperty("paypal.base.url") + System.getProperty("paypal.createO.url") + "/"
                    + orderId + "/authorize";
            log.debug("Authorization endpoint URL: {}", fullUrl);

            HashMap<String, String> Headers = new HashMap<>();
            JwtGeneratorRS256.generateJwt();
            String token = "Bearer " + testContext.getToken();
            Headers.put("Authorization", token);
            Headers.put("Content-Type", "application/json");

            log.info("Sending authorization request to PayPal API");
            Response res = restHelper.postMessageAuth(body, Headers, fullUrl);
            testContext.setauthorizeOrderResponse(res);

            String statusCode = String.valueOf(res.getStatusCode());
            log.info("Authorization response status code: {}", statusCode);
            assertEquals(authorizatioresCode, statusCode);
            log.info("Order authorization status validation passed");

            ExtentReportsManager.logPassWithScreenshot("Order Authorized",
                    "Authorization successful | Status Code: " + statusCode, null);
        } catch (AssertionError e) {
            log.error("Order authorization status mismatch. Expected: {}", authorizatioresCode, e);
            ExtentReportsManager.logFailWithScreenshot("Authorization Failed",
                    "Expected status: " + authorizatioresCode, null);
            throw e;
        } catch (Exception e) {
            log.error("Error authorizing PayPal order", e);
            ExtentReportsManager.logFailWithScreenshot("Authorization Error",
                    "Error: " + e.getMessage(), null);
            throw new RuntimeException("Order authorization failed", e);
        }
    }

    @And("the order status should be {string}")
    public void checkOrderStatusafterAuth(String orderStatus) {
        log.info("Verifying final order status: {}", orderStatus);
        try {
            Response res = testContext.getAuthorizeOrderResponse();
            String status = res.jsonPath().getString("status");
            log.info("Order status from API response: {}", status);
            assertEquals(orderStatus, status);
            log.info("Order status validation passed");

            // Capture screenshot on successful status verification
            WebDriver driver = null;
            try {
                driver = DriverFactory.getDriver();
                String finalScreenshot = ScreenshotUtil.takeScreenshot(driver, "08_Final_Order_Status_Verified");
                ExtentReportsManager.logPassWithScreenshot("Order Status Verified",
                        "Order status is: " + status + " (Expected: " + orderStatus + ")", finalScreenshot);
            } catch (Exception ex) {
                log.debug("WebDriver not available for final screenshot");
                ExtentReportsManager.logPassWithScreenshot("Order Status Verified",
                        "Order status is: " + status + " (Expected: " + orderStatus + ")", null);
            }
        } catch (AssertionError e) {
            log.error("Order status mismatch. Expected: {}", orderStatus, e);
            WebDriver driver = null;
            try {
                driver = DriverFactory.getDriver();
                String errorScreenshot = ScreenshotUtil.takeScreenshot(driver, "ERROR_Order_Status_Mismatch");
                ExtentReportsManager.logFailWithScreenshot("Order Status Verification Failed",
                        "Expected: " + orderStatus + " | Error: " + e.getMessage(), errorScreenshot);
            } catch (Exception ex) {
                log.debug("WebDriver not available for error screenshot");
                ExtentReportsManager.logFailWithScreenshot("Order Status Verification Failed",
                        "Expected: " + orderStatus + " | Error: " + e.getMessage(), null);
            }
            throw e;
        } catch (Exception e) {
            log.error("Error checking order status", e);
            WebDriver driver = null;
            try {
                driver = DriverFactory.getDriver();
                String errorScreenshot = ScreenshotUtil.takeScreenshot(driver, "ERROR_Order_Status_Check");
                ExtentReportsManager.logFailWithScreenshot("Order Status Check Failed",
                        "Error: " + e.getMessage(), errorScreenshot);
            } catch (Exception ex) {
                log.debug("WebDriver not available for error screenshot");
                ExtentReportsManager.logFailWithScreenshot("Order Status Check Failed",
                        "Error: " + e.getMessage(), null);
            }
            throw new RuntimeException("Order status validation failed", e);
        }
    }
}
