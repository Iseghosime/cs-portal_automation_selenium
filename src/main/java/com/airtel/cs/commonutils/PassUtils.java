package com.airtel.cs.commonutils;

import java.util.Base64;

public class PassUtils {
    public static String encodePassword(String originalInput) {
        return Base64.getEncoder().withoutPadding().encodeToString(originalInput.getBytes());
    }

    public static String decodePassword(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
<<<<<<< HEAD
=======

>>>>>>> 30d0262b61cf0afa5b11fad80020e50e8b6ba155
}
