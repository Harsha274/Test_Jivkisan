package com.jivkisan.stepDefinitions;

import com.jivkisan.pages.MandiApiPage;
import io.cucumber.java.en.*;
import org.testng.Assert;
import java.util.List;
import java.util.Map;

public class MandiApiSteps {
    MandiApiPage apiPage = new MandiApiPage();

    @Given("the Mandi Price API is available")
    public void api_is_available() {
        // Setup logic if needed
    }

    @When("I send a GET request to the data.gov.in resource")
    public void i_send_get_request() {
        apiPage.sendGetRequest();
    }

    @Then("the response status code should be 200")
    public void verify_status_code() {
        Assert.assertEquals(apiPage.getStatusCode(), 200);
    }

    @Then("each record should contain {string}, {string}, {string}, and {string}")
    public void verify_schema(String col1, String col2, String col3, String col4) {
        // This line calls the method we just added
        List<Map<String, Object>> records = apiPage.getRecords(); 
        
        for (Map<String, Object> record : records) {
            Assert.assertTrue(record.containsKey(col1), "Missing field: " + col1);
            Assert.assertTrue(record.containsKey(col2), "Missing field: " + col2);
            Assert.assertTrue(record.containsKey(col3), "Missing field: " + col3);
            Assert.assertTrue(record.containsKey(col4), "Missing field: " + col4);
        }
    }

    @Then("the {string} should be a valid numeric string")
    public void verify_data_type(String field) {
        // 1. Get the list as Objects instead of Strings to avoid ClassCastException
        List<Object> prices = apiPage.getResponse().jsonPath().getList("records." + field);
        
        for (Object price : prices) {
            Assert.assertNotNull(price, "Field " + field + " is null!");
            
            // 2. Convert whatever it is (Integer, Double, or String) into a String
            String priceValue = String.valueOf(price);
            
            try {
                // 3. Verify it can be parsed as a number
                Double.parseDouble(priceValue); 
            } catch (NumberFormatException e) {
                Assert.fail("Field " + field + " value [" + priceValue + "] is not a valid number.");
            }
        }
    }
}