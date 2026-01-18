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
import com.example.paypal.util.InvoiceIdGenerator;
import com.example.paypal.util.JwtGeneratorRS256;
import com.example.paypal.util.OAuthTokenProvider;
import com.example.paypal.util.ResponseHandler;
import com.example.paypal.util.RestHelper;
import com.example.paypal.util.TestContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class ApiSteps {
	private static final Logger log =
	        LoggerFactory.getLogger(ApiSteps.class);
	private RestHelper resrtHelper =Hooks.resrtHelper;
	private TestContext testContext = Hooks.testContext ;
	


    

    @Given("I have a valid PayPal access token")
    public void token() {
        System.out.println("GGGGGGGGG.");
        
        String token = OAuthTokenProvider.getToken();
        System.out.println(token);
        testContext.setToken(token);
        
        log.info("Token  : "+token);
        System.out.println("GGGGGGGGG.");
        
    }

    @When("I create a checkout order using data from excel {string}")
    public void createOrder(String TestId) {
    	String path = System.getProperty("user.dir")+"/src/test/resources/paypal_multi_order_with_items.xlsx";
    	ObjectMapperPayload obm=new ObjectMapperPayload();
    	String body = obm.CreatePayload(path,TestId);
    	String invoinceId=InvoiceIdGenerator.generate();
    	body = body.replace("invoice_id", invoinceId);
    	System.out.println(body);
    	//String body = "{\"intent\":\"AUTHORIZE\",\"purchase_units\":[{\"amount\":{\"currency_code\":\"USD\",\"value\":\"10.00\"}}]}";
    	String fullUrl = System.getProperty("paypal.base.url")+System.getProperty("paypal.createO.url");
    	HashMap<String,String> Headers= new HashMap<>();
    	String token = "Bearer "+ testContext.getToken();
    	Headers.put("Authorization", token);
    	Headers.put("Content-Type", "application/json");
    	System.out.println(token);
    	
    	Response res = resrtHelper.postMessage(body, Headers,fullUrl);
    	ResponseHandler responseHandler = new ResponseHandler(res);
    	String orderId = res.jsonPath().get("id");
    	testContext.setorderId(orderId);
    	String rel = "payer-action";
    	testContext.setredirectUrl(responseHandler.getRedirectUrlByRel(rel));
    	System.out.println(responseHandler.getRedirectUrlByRel(rel));
    	
    }
     
    @And("I confirm a checkout order {string}")
    public void confirmOrder(String orderId) {
    	String path = System.getProperty("user.dir")+"/src/test/resources/paypal_multi_order_with_items.xlsx";
    	ObjectMapperPayload obm=new ObjectMapperPayload();
    	String body = obm.CreatePayload(path,orderId);

    	
    	String fullUrl = System.getProperty("paypal.base.url")+System.getProperty("paypal.createO.url")+"/"+testContext.getorderId()+"/confirm-payment-source";
    	HashMap<String,String> Headers= new HashMap<>();
    	String token = "Bearer "+ testContext.getToken();
    	Headers.put("Authorization", token);
    	Headers.put("Content-Type", "application/json");
    	System.out.println(token);
    	
    	resrtHelper.postMessage(body,Headers,fullUrl);
//    	ResponseHandler responseHandler = new ResponseHandler(res);
//    	String orderId = res.jsonPath().get("id");
//    	testContext.setorderId(orderId);
//    	String rel = "approve";
//    	testContext.setredirectUrl(responseHandler.getRedirectUrlByRel(rel));
//    	System.out.println(responseHandler.getRedirectUrlByRel(rel));
    	
    }
        
    @And("I attempt to approve the order based on test case true")
    public void approve() throws InterruptedException {
        WebDriver driver = DriverFactory.getDriver();
        
        String url = testContext.getredirectUrl();
        Thread.sleep(700);
        driver.get(url);
        Thread.sleep(7000);
        LoginPage loginpage= new LoginPage(driver);
        loginpage.loginWithEmail(System.getProperty("paypal.user.email"));
//        loginpage.clickChangeEmailLink();
        Thread.sleep(70);
        //loginpage.loginWithEmail(System.getProperty("paypal.user.email"));
        Thread.sleep(70);
        //loginpage.loginWithPasswordInstead();
        Thread.sleep(70);
        //loginpage.loginWithEmail(System.getProperty("paypal.user.email"));
        Thread.sleep(70);
        loginpage.loginWithPassword(System.getProperty("paypal.user.password"));
        Thread.sleep(700);
        AccountsPage accountsPage = new AccountsPage(driver);
        
        //accountsPage.waitForAccountsPage();
        accountsPage.selectAccountByName("Visa");
        accountsPage.clickCompletePurchase();
        //accountsPage.selectAccountByName("CREDIT UNION 1 (AK)");
        Thread.sleep(7000);

    }
    
    @Then("I authorize order after user approval")
    public void authorizeOrder() throws NoSuchAlgorithmException, InvalidKeySpecException {
    	String body = "";

    	
    	String fullUrl = System.getProperty("paypal.base.url")+System.getProperty("paypal.createO.url")+"/"+testContext.getorderId()+"/authorize";
    	HashMap<String,String> Headers= new HashMap<>();
    	String jwt = JwtGeneratorRS256.generateJwt();
    	String token = "Bearer "+ testContext.getToken();
    	Headers.put("Authorization", token);
    	Headers.put("Content-Type", "application/json");
    	System.out.println(token);
    	
    	resrtHelper.postMessage(body,Headers,fullUrl);
//    	ResponseHandler responseHandler = new ResponseHandler(res);
//    	String orderId = res.jsonPath().get("id");
//    	testContext.setorderId(orderId);
//    	String rel = "approve";
//    	testContext.setredirectUrl(responseHandler.getRedirectUrlByRel(rel));
//    	System.out.println(responseHandler.getRedirectUrlByRel(rel));
    	
    }
    
    }
    












