package com.team1.efep.configurations;

import com.team1.efep.services.AdminService;
import org.springframework.ui.Model;

public class AdminPageConfig {

    private static AdminService adminService;

    public static void config(Model model, AdminService adminService){
        adminService.getTotalUser(model);
        adminService.getTotalSeller(model);
        adminService.getTotalRevenue(model);
        adminService.getTotalBuyer(model);
        adminService.getTotalFlower(model);
        adminService.getTop3SellerInMonth(model);
        adminService.getTotalSale(model);
        adminService.getOrdersInMonth(model);
    }

}
