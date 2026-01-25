package com.example.paypal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PayPal configuration class for managing sandbox API credentials and
 * endpoints.
 * Credentials are loaded from environment variables for security best
 * practices.
 */
public class PayPalConfig {

        private static final Logger logger = LoggerFactory.getLogger(PayPalConfig.class);

        /** PayPal Sandbox API base URL */
        public static final String BASE_URL = "https://api-m.sandbox.paypal.com";

        /** PayPal Client ID loaded from environment variable */
        public static final String CLIENT_ID = initClientId();

        /** PayPal Client Secret loaded from environment variable */
        public static final String CLIENT_SECRET = initClientSecret();

        /** Sandbox buyer email for testing */
        public static final String BUYER_EMAIL = "sandbox-buyer@example.com";

        /** Sandbox buyer password for testing */
        public static final String BUYER_PASSWORD = "sandbox-password";

        /**
         * Initialize Client ID from environment variable with fallback
         */
        private static String initClientId() {
                String clientId = System.getenv("PAYPAL_CLIENT_ID");
                if (clientId == null || clientId.isEmpty()) {
                        logger.warn("PAYPAL_CLIENT_ID environment variable not set. Using placeholder.");
                        clientId = "PUT_SANDBOX_CLIENT_ID";
                } else {
                        logger.info("PayPal Client ID loaded from environment");
                }
                return clientId;
        }

        /**
         * Initialize Client Secret from environment variable with fallback
         */
        private static String initClientSecret() {
                String clientSecret = System.getenv("PAYPAL_CLIENT_SECRET");
                if (clientSecret == null || clientSecret.isEmpty()) {
                        logger.warn("PAYPAL_CLIENT_SECRET environment variable not set. Using placeholder.");
                        clientSecret = "PUT_SANDBOX_CLIENT_SECRET";
                } else {
                        logger.info("PayPal Client Secret loaded from environment");
                }
                return clientSecret;
        }
}
