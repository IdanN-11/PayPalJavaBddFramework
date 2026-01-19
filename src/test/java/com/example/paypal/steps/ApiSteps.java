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
import com.example.paypal.util.ResponseHandler;
import com.example.paypal.util.RestHelper;
import com.example.paypal.util.TestContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ApiSteps {
	private static final Logger log =
	        LoggerFactory.getLogger(ApiSteps.class);
	private RestHelper restHelper =Hooks.restHelper;
	private TestContext testContext = Hooks.testContext ;
	


    

    @Given("TPP have a valid PayPal access token")
    public void token() {
        System.out.println("GGGGGGGGG.");
        
        String token = restHelper.PostCCToken();
        System.out.println(token);
        testContext.setToken(token);
        
        log.info("Token  : "+token);
        System.out.println("GGGGGGGGG.");
        
    }

    @When("TPP create a checkout order using data from excel for {string}")
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
    	
    	Response res = restHelper.postMessage(body, Headers);
    	ResponseHandler responseHandler = new ResponseHandler(res);
    	String orderId = res.jsonPath().get("id");
    	testContext.setorderId(orderId);
    	testContext.setcreateOrderResponse(res);
    	String rel = "payer-action";
    	testContext.setredirectUrl(responseHandler.getRedirectUrlByRel(rel));
    	System.out.println(responseHandler.getRedirectUrlByRel(rel));
    	
    }
    
    @Then("TPP check order is succesfully created with {string}")
    public void checkCreateOrderStatus(String createOrderStatusCode) {
         Response res = testContext.getcreateOrderResponse();
         String statusCode = String.valueOf(res.getStatusCode());
         assertEquals(statusCode,createOrderStatusCode);
    }
    

//    @Then(" TPP update the order with {string} details")
//    public void updateOrder(String updatePayload) {
//    
//    }
    
     
//    @And("TPP confirm the order is update with update order resopnse code {string}")
//    public void checkUpdateOrderStatus(String updateOrderstatusCode) {
//    	 Response res = testContext.getupdateOrderResponse();
//         int statusCode = res.getStatusCode();
//         assertEquals(statusCode,updateOrderstatusCode);
//    }
    
    @And("TPP confirm the checkout order and response code should be {string} and {string}")
    public void confirmOrder(String confirmOrderStatusCode , String payload) {
    	String path = System.getProperty("user.dir")+"/src/test/resources/paypal_multi_order_with_items.xlsx";
    	ObjectMapperPayload obm=new ObjectMapperPayload();
    	String orderId=testContext.getorderId();
    	String body = obm.CreatePayload(path,payload);

    	
    	String fullUrl = System.getProperty("paypal.base.url")+System.getProperty("paypal.createO.url")+"/"+testContext.getorderId()+"/confirm-payment-source";
    	HashMap<String,String> Headers= new HashMap<>();
    	String token = "Bearer "+ testContext.getToken();
    	Headers.put("Authorization", token);
    	Headers.put("Content-Type", "application/json");
    	System.out.println(token);
    	
    	Response res = restHelper.postMessage(body,Headers);
    	assertEquals(confirmOrderStatusCode,String.valueOf(res.getStatusCode()));
//    	ResponseHandler responseHandler = new ResponseHandler(res);
//    	String orderId = res.jsonPath().get("id");
//    	testContext.setorderId(orderId);
//    	String rel = "approve";
//    	testContext.setredirectUrl(responseHandler.getRedirectUrlByRel(rel));
//    	System.out.println(responseHandler.getRedirectUrlByRel(rel));
    	
    }
        
    @Then("TPP redirect PSU to login on paypal  to approve the order with {string} and {string}")
    public void approve(String user_email , String user_password) throws InterruptedException {
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
        

    }
    
    
    @And("PSU select account {string} on accounts page and submit the payment")
    public void selectAccountandSubmit(String payer_account) {
    	WebDriver driver = DriverFactory.getDriver();
       AccountsPage accountsPage = new AccountsPage(driver);
        
        //accountsPage.waitForAccountsPage();
        accountsPage.selectAccountByName(payer_account);
        accountsPage.clickCompletePurchase();
        //accountsPage.selectAccountByName("CREDIT UNION 1 (AK)");
        
    }

    
    
    
    
    
    @Then("TPP authorize order after user approval through paypal with payload {string} and response code should be {string}")
    public void authorizeOrder(String authpayload  , String authorizatioresCode) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	String body = "";

    	
    	String fullUrl = System.getProperty("paypal.base.url")+System.getProperty("paypal.createO.url")+"/"+testContext.getorderId()+"/authorize";
    	HashMap<String,String> Headers= new HashMap<>();
    	String jwt = JwtGeneratorRS256.generateJwt();
    	String token = "Bearer "+ testContext.getToken();
    	Headers.put("Authorization", token);
    	Headers.put("Content-Type", "application/json");
    	System.out.println(token);
    	
    	restHelper.postMessage(body,Headers);
    	
    	Response res = restHelper.postMessageAuth(body,Headers,fullUrl);
    	testContext.setauthorizeOrderResponse(res);
    
    	assertEquals(authorizatioresCode,String.valueOf(res.getStatusCode()));
//    	ResponseHandler responseHandler = new ResponseHandler(res);
//    	String orderId = res.jsonPath().get("id");
//    	testContext.setorderId(orderId);
//    	String rel = "approve";
//    	testContext.setredirectUrl(responseHandler.getRedirectUrlByRel(rel));
//    	System.out.println(responseHandler.getRedirectUrlByRel(rel));
    	
    }
    
    @And("the order status should be {string}")
    public void checkOrderStatusafterAuth(String orderStatus) {
         Response res = testContext.getAuthorizeOrderResponse();
         String status = res.jsonPath().getString("status");
     	assertEquals(orderStatus,status);

         
    }
    
    }
    












