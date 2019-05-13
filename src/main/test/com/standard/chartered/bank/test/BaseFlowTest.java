package com.standard.chartered.bank.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/main/test/com/standard/chartered/bank/Scenario.feature"},
        tags = {"@test"},
        glue = "com.standard.chartered.bank.utils",
        plugin = {"pretty", "json:target/reports/report.json"})
public class BaseFlowTest {

}