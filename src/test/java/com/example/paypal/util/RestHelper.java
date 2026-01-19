package com.example.paypal.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
public class RestHelper {
	
	
		
		// Base setup
	    private RequestSpecification request() {
	        return RestAssured.given()
	        		.baseUri(System.getProperty("ui.base.url"));
	        		//.keyStore("certs", "password");
	    }
	    
	    //Post Message
	    
	    public Response postMessage(String body,HashMap<String,String> headers) {
	    	System.out.println(System.getProperty("paypal.client.secret"));
	  return request()
	    	.body(body)
	    	.headers(headers)
	    	.log().all()
            .post(System.getProperty("paypal.createO.url"))
            .then()
            .log().all()
            .extract().response();
	    }
	    
	    public Response postMessageAuth(String body,HashMap<String,String> headers, String fullurl) {
	    	System.out.println(System.getProperty("paypal.client.secret"));
	  return RestAssured.given()
	    	.body(body)
	    	.headers(headers)
	    	.log().all()
            .post(fullurl)
            .then()
            .log().all()
            .extract().response();
	    }
	    
	    
	    public static String PostCCToken() {
	        String clientId = System.getProperty("paypal.client.id");
	        String clientSecret = System.getProperty("paypal.client.secret");
	        // Step 1: Base64 encode
	        String auth = clientId + ":" + clientSecret;
	        String clientId1 = Base64.getEncoder()
	                .encodeToString(clientId.getBytes(StandardCharsets.UTF_8));
	        String clientSecret1 = Base64.getEncoder()
	                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
	        System.out.println(clientId1);
            //Create CC Token with basic auth
	        Response response =
	                RestAssured
	                        .given()
	                        .baseUri(System.getProperty("paypal.base.url"))
	                        .header("Authorization", "Basic " + clientSecret1)
	                        .header("Content-Type", "application/x-www-form-urlencoded")
	                        .formParam("grant_type", "client_credentials")
	                        .post(System.getProperty("paypal.CCtoken.url"));
	        
	        System.out.println(response.asPrettyString());
	        System.out.println(response.jsonPath().getString("access_token"));
            String ccToken = response.jsonPath().getString("access_token");
	        return ccToken;
	    }
	    
	    //Patch method
	    
	    public Response patchMessage(String body,HashMap<String,String> headers,String orderId) {
	    	System.out.println(System.getProperty("paypal.client.secret"));
	  return request()
	    	.body(body)
	    	.headers(headers)
	    	.pathParam("orderId",orderId)
	    	.log().all()
            .patch(System.getProperty("paypal.createO.url")+"/{orderId}")
            .then()
            .log().all()
            .extract().response();
	    }
	    
	    
	    //Get method
	    
	    public Response getMessage(HashMap<String,String> headers,String orderId) {
	    	System.out.println(System.getProperty("paypal.client.secret"));
	  return request()
	    	.headers(headers)
	    	.pathParam("orderId", orderId)
	    	.log().all()
            .get(System.getProperty("paypal.createO.url")+"/{orderId}")
            .then()
            .log().all()
            .extract().response();
	    }

	    
	    
	    
	}


