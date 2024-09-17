package com.team1.efep.enums;

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

}
