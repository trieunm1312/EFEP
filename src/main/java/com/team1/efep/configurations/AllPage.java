package com.team1.efep.configurations;

import com.team1.efep.services.BuyerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public class AllPage {
    public static void allConfig(Model model, BuyerService buyerService){
        buyerService.viewCategory(model);
        buyerService.viewSellerTopList(5, model);
    }
}


