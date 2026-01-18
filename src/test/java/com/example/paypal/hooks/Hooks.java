package com.example.paypal.hooks;

import java.util.List;
import java.util.Map;

import com.example.paypal.util.ConfigLoader;
import com.example.paypal.util.DriverFactory;
import com.example.paypal.util.RestHelper;
import com.example.paypal.util.TestContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class Hooks {

    private static List<Map<String,String>> allData;
    public static TestContext testContext= new TestContext();
    public static RestHelper resrtHelper= new RestHelper();
    

    @BeforeAll
    public static void loadData() {
//        allData = ExcelReader.read(
//                "src/test/resources/testdata/PayPalTestData.xlsx",
//                "CheckoutTests"
//        );
        System.out.println("HIIII");
        ConfigLoader.load();
    }

    @Before
    public void before(Scenario scenario) {
//        TestContext.data.set(allData.get(0));
    }
    @Before
    
    @After
    public void after() {
        DriverFactory.quitDriver();
        //TestContext.data.remove();
    }
}
