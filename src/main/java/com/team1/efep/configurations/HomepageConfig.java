package com.team1.efep.configurations;

import com.team1.efep.services.BuyerService;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public class HomepageConfig {

    public static void config(Model model, BuyerService buyerService){
        buyerService.viewSlideBar(model);
        buyerService.viewFlowerTopList(5, model);
    }
}
