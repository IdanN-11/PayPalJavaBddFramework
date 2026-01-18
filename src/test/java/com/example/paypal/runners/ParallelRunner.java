package com.example.paypal.runners;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")  // tells JUnit 5 to use the Cucumber engine
@SelectClasspathResource("features")  // folder with .feature files (relative to src/test/resources)
public class ParallelRunner {
    // empty class body - con iguration via annotations or junit-platform.properties
}            