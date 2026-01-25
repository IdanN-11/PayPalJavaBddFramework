package com.example.paypal.runners;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class Ruunnnnrrr{
    // JUnit 5 Suite configuration via annotations - Cucumber will execute all features
}