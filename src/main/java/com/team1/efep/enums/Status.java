package com.team1.efep.enums;

import com.team1.efep.models.entity_models.*;
import com.team1.efep.repositories.*;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Status {
    public static String ACCOUNT_STATUS_ACTIVE = "active";
    public static String ACCOUNT_STATUS_BANNED = "banned";
    public static String ORDER_STATUS_PROCESSING = "processing";
    public static String ORDER_STATUS_PACKED = "packed";
    public static String ORDER_STATUS_COMPLETED = "completed";
    public static String ORDER_STATUS_CANCELLED = "cancelled";
    public static String FLOWER_STATUS_AVAILABLE = "available";
    public static String FLOWER_STATUS_OUT_OF_STOCK = "out-of-stock";
    public static String PAYMENT_METHOD_COD = "cod";
    public static String PAYMENT_METHOD_VN_PAY = "vn-pay";
    public static String SELLER_APPLICATION_STATUS_PENDING = "pending";
    public static String SELLER_APPLICATION_STATUS_APPROVED = "approved";
    public static String SELLER_APPLICATION_STATUS_REJECTED = "rejected";

    public static void changeAccountStatus(Account account, String status, AccountRepo accountRepo) {
        account.setStatus(status);
        accountRepo.save(account);
    }

    public static void changeOrderStatus(Order order, String status, OrderRepo orderRepo) {
        order.setStatus(status);
        orderRepo.save(order);
    }

    public static void changeFlowerStatus(Flower flower, String status, FlowerRepo flowerRepo) {
        flower.setStatus(status);
        flowerRepo.save(flower);
    }

    public static List<String> getFlowerStatusList() {
        List<String> flowerStatuses = new ArrayList<>();

        Field[] fields = Status.class.getFields();

        for(Field field : fields) {
            if(field.getName().contains("FLOWER_STATUS")) {
                try {
                    flowerStatuses.add((String) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return flowerStatuses;
    }

}
