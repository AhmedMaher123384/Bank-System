package com.App.BankingSystem.SpringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Random;

public class Utils {

    @Autowired
    private static Random RANDOM = new Random();

    public static String generateCardNumber() {
        return generateRandomNumber(16);
    }

    public static String generateCVV() {
        return generateRandomNumber(3);
    }

    private static String generateRandomNumber(int len) {
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < len; ++i) {
            int digit = RANDOM.nextInt(10);
            number.append(digit);
        }

        return number.toString();
    }

}


