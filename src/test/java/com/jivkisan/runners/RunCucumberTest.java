package com.jivkisan.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.jivkisan.stepDefinitions", "com.jivkisan.hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-html-report.html",
        "json:target/cucumber-reports/cucumber.json"
    },
    monochrome = true
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true) // This enables parallel execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}