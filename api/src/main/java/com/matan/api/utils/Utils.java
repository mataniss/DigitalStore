package com.matan.api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
}
