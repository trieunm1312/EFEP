package com.team1.efep.utils;

import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.entity_models.OrderDetail;
import com.team1.efep.models.entity_models.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

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

//    public static String readFile(User user, BusinessPlan businessPlan) {
//        String result = "";
//        try {
//            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/business.txt"));
//            String line;
//            while ((line = br.readLine()) != null) {
//                result += line;
//            }
//
//            LocalDate purchaseDate = user.getSeller().getPlanPurchaseDate().toLocalDate();
//            LocalDate expiryDate = purchaseDate.plusDays(businessPlan.getDuration());
//            long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
//
//            result = result.replaceAll("@@###", user.getName());
//            result = result.replaceAll("@@@##", businessPlan.getName());
//            result = result.replaceAll("@@##", String.valueOf(remainingDays));
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    public static String readFile(Order order, User user) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/order.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

            String[] splitResult = result.split("##loop");

            result = splitResult[0];

            List<OrderDetail> orderDetailList = order.getOrderDetailList();

            for (OrderDetail orderDetail : orderDetailList) {
                String data = replaceData(order, orderDetail, splitResult[1]);
                result += data;
            }

            result += splitResult[2];

            result = result.replaceAll("##orderId", String.valueOf(order.getId()));
            result = result.replaceAll("##createDate", String.valueOf(order.getCreatedDate().toLocalDate()));
            result = result.replaceAll("##totalPrice", String.valueOf(order.getTotalPrice()));

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String replaceData(Order order, OrderDetail orderDetail, String data) {
        data = data.replaceAll("##flowerName", orderDetail.getFlowerName());
        data = data.replaceAll("##description", orderDetail.getFlower().getDescription());
        data = data.replaceAll("##quantity", String.valueOf(orderDetail.getQuantity()));
        data = data.replaceAll("##price", String.valueOf(orderDetail.getPrice()));
        return data;
    }


    //psvm = ham test main
    public static void main(String[] args) {
        System.out.println(readFile("L09ve"));
    }
}
