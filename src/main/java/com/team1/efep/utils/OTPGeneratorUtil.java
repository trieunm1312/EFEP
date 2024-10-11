package com.team1.efep.utils;

import java.util.Random;

public class OTPGeneratorUtil {
    public static String generateOTP(int length) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(characters.charAt(random.nextInt(characters.length())));
        }
        return otp.toString();
    }
}
