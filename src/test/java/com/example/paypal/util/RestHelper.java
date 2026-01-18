package com.example.paypal.util;

import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
public class RestHelper {
	
	
		
		// Base setup
	    private RequestSpecification request() {
	        return RestAssured.given();
	        		//.keyStore("certs", "password");
	    }
	    
	    public Response postMessage(String body,HashMap<String,String> headers,String fullURL) {
	    	System.out.println(System.getProperty("paypal.client.secret"));
	  return request()
	    	.body(body)
	    	.headers(headers)
	    	.log().all()
            .post(fullURL)
            .then()
            .log().all()
            .extract().response();
	    }
	    
	    
	    
	}


