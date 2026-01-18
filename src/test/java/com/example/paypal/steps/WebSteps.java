package com.example.paypal.steps;
import com.example.paypal.util.TestContext;

import com.example.paypal.config.PayPalConfig;
import com.example.paypal.hooks.Hooks;
import com.example.paypal.pageobject.LoginPage;
import com.example.paypal.util.DriverFactory;
import com.example.paypal.util.RestHelper;

import io.cucumber.java.en.And;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WebSteps {
	private RestHelper resrtHelper =Hooks.resrtHelper;
	private TestContext testContext = Hooks.testContext ;


//    @And("I attempt to approve the order based on test case true")
//    public void approve() throws InterruptedException {
//        WebDriver driver = DriverFactory.getDriver();
//        String url = testContext.getredirectUrl();
//        driver.get(url);
//        LoginPage loginpage = new LoginPage(driver);
//        loginpage.enterEmail(System.getProperty(""));
//        Thread.sleep(700000);
//    }
}
