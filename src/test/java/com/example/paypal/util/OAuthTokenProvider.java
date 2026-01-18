package com.example.paypal.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OAuthTokenProvider {

    public static String getToken() {

        String clientId = System.getProperty("paypal.client.id");
        String clientSecret = System.getProperty("paypal.client.secret");

        // Step 1: Base64 encode
        String auth = clientId + ":" + clientSecret;
        String clientId1 = Base64.getEncoder()
                .encodeToString(clientId.getBytes(StandardCharsets.UTF_8));
        String clientSecret1 = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        
        System.out.println(clientId1);

        Response response =
                RestAssured
                        .given()
                        .baseUri(System.getProperty("paypal.base.url"))
                        .header("Authorization", "Basic " + clientSecret1)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .formParam("grant_type", "client_credentials")
                        .post("/v1/oauth2/token");
        
        System.out.println(response.asPrettyString());
        System.out.println(response.jsonPath().getString("access_token"));

        return response.jsonPath().getString("access_token");
        
        
        
    }
}
