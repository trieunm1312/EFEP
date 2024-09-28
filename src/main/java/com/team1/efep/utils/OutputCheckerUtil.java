package com.team1.efep.utils;

public class OutputCheckerUtil {
    public static boolean checkIfThisIsAResponseObject(Object input, Class<?> classType) {
        return classType.isInstance(input);
    }
}
