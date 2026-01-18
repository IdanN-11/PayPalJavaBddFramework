package com.example.paypal.util;
import io.jsonwebtoken.Jwts;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtGeneratorRS256 {

    public static String generateJwt() throws NoSuchAlgorithmException, InvalidKeySpecException {
    	//String key= "ENVjN3YH3NYAlNc8PHDXMeNASsicoCwE8IaHADhrCnfPjQNx-cM7WOAJypAc3MqQ4FzVur6JxlAwnH3g";//secretkey
    	
    	//byte[] decodedKey = Base64.getDecoder().decode(key);

        // Generate PrivateKey
        //PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        //KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        //PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Instant now = Instant.now();

        // -------- HEADER --------
        Map<String, Object> header = new HashMap<>();
        //header.put("typ", "JWT");
          header.put("alg", "none");
        //header.put("kid", "my-key-id");

        // -------- PAYLOAD / CLAIMS --------
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "AZCJEFUh23ILAks14wm32o4f5jiyDrbqhSrNVc6bUcbOtYDFXvJplsZaYmf-Ig3tGCzEe3thxF3gmpy2");//client id
        //claims.put("sub", "AZCJEFUh23ILAks14wm32o4f5jiyDrbqhSrNVc6bUcbOtYDFXvJplsZaYmf-Ig3tGCzEe3thxF3gmpy2");
        //claims.put("aud", "https://api.paypal.com");
        claims.put("payer_id", "BWNV8FYMVYX24");
        //claims.put("iat", Date.from(now));
        //claims.put("nbf", Date.from(now.minusSeconds(5)));
        //claims.put("exp", Date.from(now.plusSeconds(3600)));
        //claims.put("scope", "payments");

        // -------- BUILD JWT --------
        return Jwts.builder()
                .header().add(header).and()   // ✅ non-deprecated
                .claims(claims)               // ✅ non-deprecated
                //.signWith(privateKey)         // ✅ RS256 inferred
                .compact();
    }
}
