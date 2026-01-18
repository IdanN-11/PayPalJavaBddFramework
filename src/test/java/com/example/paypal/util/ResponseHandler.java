package com.example.paypal.util;

import io.restassured.response.Response;

public class ResponseHandler {
	
	private Response res ;
	
	public  ResponseHandler(Response res) {
		this.res = res;
	}
      public String getRedirectUrl() {
    	  String redirectUrl=this.res.jsonPath().getString("links.href");
    	  return redirectUrl;
      }
      public String getRedirectUrlByRel(String rel) {
    	  String redirectUrl=this.res.jsonPath().getString("links.find { it.rel == '" + rel + "' }.href");
    	  return redirectUrl;
      }
}
