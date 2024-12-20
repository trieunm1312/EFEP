package com.team1.efep.configurations;

import com.team1.efep.models.entity_models.Account;
import com.team1.efep.services.SellerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public class SellerPageConfig {

    public static void config(Model model, SellerService sellerService, HttpSession session){
        sellerService.getSoldQuantityCategory(model, session);
        sellerService.getRevenue(model,session);
        sellerService.getTotalSoldFlowers(model, session);
        sellerService.getTotalNumberOfCanceledOrder(model, session);
        sellerService.getOrderInDaily(model, session);
        sellerService.getTotalNumberOfOrder(model, session);

    }

}
