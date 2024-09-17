package com.team1.efep.validations;

import java.util.ArrayList;
import java.util.List;

public class ForgotPasswordValidation {

    public static String validateForgotPasswordInput(String email) {
        String result = validateEmail(email);
        return !result.isEmpty() ?
                result.substring(0, 1).toUpperCase() + result.trim().substring(1, result.length() - 2) + " invalid" :
                "";
    }

    private static String validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex) ? "" : "email, ";
    }

//    public static void main(String[] args) {
//        List<String> list= new ArrayList<>();
//        list.add("A");
//        list.add("B");
//        list.add("C");
//        System.out.println(String.join(": ", list));
//    }
}
