package com.team1.efep.configurations;

import com.team1.efep.services.BuyerService;
import org.springframework.ui.Model;

public class HomepageConfig {

    public static void config(Model model, BuyerService buyerService){
        buyerService.viewSlideBar(model);
        buyerService.viewFlowerTopList(5, model);
    }
}
