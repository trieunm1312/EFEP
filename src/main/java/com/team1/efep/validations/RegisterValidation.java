package com.team1.efep.validations;

public class RegisterValidation {

    //email, phone, password
    // condition  ?  value 1 : value 2
    //==> if condition is correct then return value 1 or else return value true
    public static String validateRegisterInput(String email, String phone, String password) {
        String result = validateEmail(email) + validatePhone(phone) + validatePassword(password);
        return !result.isEmpty() ?
                result.substring(0, 1).toUpperCase() + result.trim().substring(1, result.length() - 2) + " invalid" :
                "";
    }

    private static String validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex) ? "" : "email, ";

    }

    private static String validatePhone(String phone) {
        String phoneRegex = "\\d{10}";
        return phone.matches(phoneRegex) ? "" : "phone, ";
    }

    private static String validatePassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$";

        return password.matches(passwordRegex) ? "" : "password, ";
    }


//    public static void main(String[] args) {
//        String target = "Phone, password invalid";
//        String actual = validateRegisterInput("email@email.com", "123thu456", "85690");
//        System.err.println("Act: " + actual + "   ||   String size: " + actual.length());
//        System.out.println("Exp: " + target + "   ||   String size: " + target.length());
//        System.out.println("Matched:" + actual.equals(target));
//    }
}
