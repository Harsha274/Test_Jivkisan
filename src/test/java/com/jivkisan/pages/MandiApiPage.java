package com.jivkisan.pages;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class MandiApiPage {
    private static final Logger logger = LoggerFactory.getLogger(MandiApiPage.class);
    private Response response;
    private final String endpoint = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001515b2d5f65fd476c5fdac1c67542d705&format=json&filters[state]=Karnataka";

    public void sendGetRequest() {
        logger.info("Sending GET request to Mandi API...");
        response = RestAssured.given()
                             .relaxedHTTPSValidation()
                             .get(endpoint);
        System.out.println("API RESPONSE: " + response.asPrettyString());
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    // THIS WAS THE MISSING METHOD
    public List<Map<String, Object>> getRecords() {
        return response.jsonPath().getList("records");
    }

    public Response getResponse() {
        return response;
    }
}