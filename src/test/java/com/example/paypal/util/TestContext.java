package com.example.paypal.util;

import java.util.HashMap;
import java.util.Map;

import io.restassured.response.Response;

public class TestContext {
    private   ThreadLocal <String> token = new ThreadLocal<>();
    private   ThreadLocal <Response> createOrderResponse = new ThreadLocal<>();
    private   ThreadLocal <Response> authorizeOrderResponse = new ThreadLocal<>();
    private   ThreadLocal <Response> updateOrderResponse = new ThreadLocal<>();
    private   ThreadLocal <String> orderId=new ThreadLocal<>();;
    private   ThreadLocal <String> redirectUrl=new ThreadLocal<>();;
    public void setToken(String token) {
    	this.token.set(token);
    }
    public String getToken() {
    	return this.token.get();
    }
    public Response getcreateOrderResponse() {
    	return this.createOrderResponse.get();
    }
    public Response getAuthorizeOrderResponse() {
    	return this.authorizeOrderResponse.get();
    }
	public void setorderId(String orderId) {
		// TODO Auto-generated method stub
		this.orderId.set(orderId);
		
	}
	public void setcreateOrderResponse(Response createOrderResponse) {
		// TODO Auto-generated method stub
		this.createOrderResponse.set(createOrderResponse);
		
	}
	public void setauthorizeOrderResponse(Response authorizeOrderResponse) {
		// TODO Auto-generated method stub
		this.authorizeOrderResponse.set(authorizeOrderResponse);
		
	}
	
	public String getorderId() {
    	return this.orderId.get();
    }
	
	public void setredirectUrl(String redirectUrl) {
		// TODO Auto-generated method stub
		this.redirectUrl.set(redirectUrl);
		
	}
	public String getredirectUrl() {
    	return this.redirectUrl.get();
    }
	public Response getupdateOrderResponse() {
		
		return this.updateOrderResponse.get();
	}
public void setupdateOrderResponse(Response updateOrderResponse) {
		
		this.updateOrderResponse.set(updateOrderResponse);
	}
	
    
    
}
