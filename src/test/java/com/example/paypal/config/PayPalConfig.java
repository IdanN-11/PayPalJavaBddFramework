package com.example.paypal.config;

public class PayPalConfig {

    public static final String BASE_URL = "https://api-m.sandbox.paypal.com";

    public static final String CLIENT_ID =
            System.getenv("PAYPAL_CLIENT_ID") != null ?
                    System.getenv("PAYPAL_CLIENT_ID") : "PUT_SANDBOX_CLIENT_ID";

    public static final String CLIENT_SECRET =
            System.getenv("PAYPAL_CLIENT_SECRET") != null ?
                    System.getenv("PAYPAL_CLIENT_SECRET") : "PUT_SANDBOX_CLIENT_SECRET";

    public static final String BUYER_EMAIL = "sandbox-buyer@example.com";
    public static final String BUYER_PASSWORD = "sandbox-password";
}
