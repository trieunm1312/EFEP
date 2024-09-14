package com.team1.efep.services;

import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.response_models.AddToCartResponse;
import com.team1.efep.models.response_models.ViewCartResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface BuyerService {

    String viewCart(HttpSession session, Model model);

    ViewCartResponse viewCartAPI(int id);

    String addToCart(HttpSession session, Model model);

    AddToCartResponse addToCartAPI(AddToCartRequest request);

}
