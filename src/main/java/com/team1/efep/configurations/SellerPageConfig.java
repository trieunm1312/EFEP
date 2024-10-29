package com.team1.efep.configurations;

import com.team1.efep.services.SellerService;
import org.springframework.ui.Model;

public class SellerPageConfig {

    public static void config(Model model, SellerService sellerService){
        sellerService.getSoldQuantityCategory(model);
        sellerService.getRevenue(model);
        sellerService.getTotalNumberFlower(model);
        sellerService.getTotalNumberOfCanceledOrder(model);
        sellerService.getOrderInDaily(model);
        sellerService.getTotalNumberOfOrder(model);

    }

}
