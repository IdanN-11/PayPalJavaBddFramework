package com.example.paypal.util;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private   ThreadLocal <String> token = new ThreadLocal<>();
    private   ThreadLocal <String> orderId=new ThreadLocal<>();;
    private   ThreadLocal <String> redirectUrl=new ThreadLocal<>();;
    public void setToken(String token) {
    	this.token.set(token);
    }
    public String getToken() {
    	return this.token.get();
    }
	public void setorderId(String orderId) {
		// TODO Auto-generated method stub
		this.orderId.set(orderId);
		
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
	
    
    
}
