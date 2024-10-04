package com.team1.efep.enums;

import com.team1.efep.models.entity_models.*;
import com.team1.efep.repositories.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Status {
    public static String ACCOUNT_STATUS_ACTIVE = "active";
    public static String ACCOUNT_STATUS_BANNED = "banned";
    public static String ORDER_STATUS_PROCESSING = "processing";
    public static String ORDER_STATUS_PACKED = "packed";
    public static String ORDER_STATUS_COMPLETED = "completed";
    public static String ORDER_STATUS_CANCELLED = "cancelled";
    public static String PURCHASED_PLAN_STATUS_PURCHASED = "purchased";
    public static String PURCHASED_PLAN_STATUS_CANCELLED = "cancelled";
    public static String PURCHASED_PLAN_STATUS_DISABLED = "disabled";
    public static String FLOWER_STATUS_AVAILABLE = "available";
    public static String FLOWER_STATUS_OUT_OF_STOCK = "out of stock";
    public static String FLOWER_STATUS_DELETED = "deleted";
    public static String BUSINESS_PLAN_STATUS_DISABLED = "disabled";
    public static String BUSINESS_PLAN_STATUS_ACTIVE = "active";
    public static String PAYMENT_METHOD_COD = "cod";
    public static String PAYMENT_METHOD_VN_PAY = "vn-pay";




    public static void changeAccountStatus(Account account, String status, AccountRepo accountRepo) {
        account.setStatus(status);
        accountRepo.save(account);
    }

    public static void changeOrderStatus(Order order, String status, OrderRepo orderRepo) {
        order.setStatus(status);
        orderRepo.save(order);
    }

    public static void changePurchasedPlanStatus(PurchasedPlan purchasedPlan, String status, PurchasedPlanRepo purchasedPlanRepo) {
        purchasedPlan.setStatus(status);
        purchasedPlanRepo.save(purchasedPlan);
    }

    public static void changeFlowerStatus(Flower flower, String status, FlowerRepo flowerRepo) {
        flower.setStatus(status);
        flowerRepo.save(flower);
    }

    public static void getRoleStatus(BusinessPlan businessPlan, String status, BusinessPlanRepo businessPlanRepo) {
        businessPlan.setStatus(status);
        businessPlanRepo.save(businessPlan);
    }

}
