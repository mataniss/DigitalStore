package com.matan.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Utils {

    public static String getCurrentDateTime() {
        // Prepare the SQL statement
        LocalDateTime now = LocalDateTime.now();
        // Create a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Format the current date and time
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }

    public static String encryptPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    public static boolean validatePassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    private static final String SECRET_KEY = "javainuse-secret-key";
    public static String generateToken(Long userID) {
        return Jwts.builder()
                .setSubject(String.valueOf(userID))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 900000)) // Token expires in 15 minutes
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Integer validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Token details: ");
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Issued at: " + claims.getIssuedAt());
            System.out.println("Expiration: " + claims.getExpiration());

            return Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
            return null;
        }
    }
}
