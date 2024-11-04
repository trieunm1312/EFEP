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

    public static String readFile(Order order) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/seller.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

            result = result.replaceAll("##orderId", String.valueOf(order.getId()));
            result = result.replaceAll("##createDate", String.valueOf(order.getCreatedDate().toLocalDate()));

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readFile1(Order order) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/cancelOrderToSeller.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

            result = result.replaceAll("##orderId", String.valueOf(order.getId()));
            result = result.replaceAll("##createDate", String.valueOf(order.getCreatedDate().toLocalDate()));

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readFile2(Order order) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/packedOrder.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

            result = result.replaceAll("##orderId", String.valueOf(order.getId()));
            result = result.replaceAll("##createDate", String.valueOf(order.getCreatedDate().toLocalDate()));

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readFile3(Order order) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/team1/efep/email_file/cancelOrderToBuyer.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

            result = result.replaceAll("##orderId", String.valueOf(order.getId()));
            result = result.replaceAll("##createDate", String.valueOf(order.getCreatedDate().toLocalDate()));

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
