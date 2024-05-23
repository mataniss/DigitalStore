package com.matan.api.utils;

import com.matan.api.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Utils {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final String SECRET_KEY = "javainuse-secret-key";
    /*
    The function retuns a string of the current date and time.
     */
    public static String getCurrentDateTime() {
        // Prepare the SQL statement
        LocalDateTime now = LocalDateTime.now();
        // Create a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Format the current date and time
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
    /*
    The function get a password and encrypts it.
     */
    public static String encryptPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    public static boolean validatePassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    /*
    The function generates a jwt token with the secret key for the user id that
    was sent in the argument.
     */
    public static String generateJWT(Long userID) {
        return Jwts.builder()
                .setSubject(userID+ "")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token expires in 1 hour
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    /*
    The function get a jwt and validates it using the secret key. If it's valid,
    the subject (the user id) for this key will be returned.
    otherwise false will be returned.
     */
    public static Long validateJWT(String token) {
        try {
            token = token.replaceFirst("^Bearer\\s+", ""); // Removes 'Bearer' and any whitespace after it
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Token details: ");
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Issued at: " + claims.getIssuedAt());
            System.out.println("Expiration: " + claims.getExpiration());

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
                System.out.println("Token validation error: " + e.getMessage());
            throw new UnauthorizedException("Invalid JWT");
        }
    }
    /*
    The function returns the extension for a filename.
     */
    public static String getExtension(String filename) {
        return filename.lastIndexOf(".") != -1 ? filename.substring(filename.lastIndexOf(".")) : "";
    }


    /**
    The function returns true if the email address has a valid patten.
     Otherwise, false will be returned.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
}
