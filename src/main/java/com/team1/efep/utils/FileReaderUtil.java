package com.team1.efep.utils;

import com.team1.efep.models.entity_models.BusinessPlan;
import com.team1.efep.models.entity_models.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.temporal.ChronoUnit;

public class FileReaderUtil {
    public static String readFile(String verificationLink) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/email.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }
            result = result.replaceAll("@@@###", verificationLink);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readFile(User user, BusinessPlan businessPlan) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/email.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }
            result = result.replaceAll("@@###", user.getName());
            result = result.replaceAll("@@@##", businessPlan.getName());
            result = result.replaceAll("@@##", String.valueOf(businessPlan.getDuration()));
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //psvm = ham test main
    public static void main(String[] args) {
        System.out.println(readFile("L09ve"));
    }
}
