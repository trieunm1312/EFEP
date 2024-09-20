package com.team1.efep.enums;

import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Flower;
import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.entity_models.PurchasedPlan;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.FlowerRepo;
import com.team1.efep.repositories.OrderRepo;
import com.team1.efep.repositories.PurchasedPlanRepo;
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
    public static String PURCHASED_PLAN_STATUS_EXPIRED = "expired";
    public static String FLOWER_STATUS_AVAILABLE = "available";
    public static String FLOWER_STATUS_OUT_OF_STOCK = "out of stock";
    public static String FLOWER_STATUS_DELETED = "deleted";

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

}
