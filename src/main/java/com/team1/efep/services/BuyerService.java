package com.team1.efep.services;

import ch.qos.logback.core.model.Model;
import com.team1.efep.models.request_models.ForgotRequest;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.models.response_models.ForgotResponse;
import com.team1.efep.models.response_models.RegisterResponse;
import com.team1.efep.models.response_models.RenewPasswordResponse;

import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.response_models.AddToCartResponse;
import com.team1.efep.models.response_models.ViewCartResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface BuyerService {
    String sendEmail(ForgotRequest request, Model model);

    ForgotResponse sendEmailAPI(ForgotRequest request);

    String renewPass(RenewPasswordRequest request, Model model);

    RenewPasswordResponse renewPassAPI(RenewPasswordRequest request);

    String viewCart(HttpSession session, Model model);

    ViewCartResponse viewCartAPI(int id);

    String addToCart(HttpSession session, Model model);

    AddToCartResponse addToCartAPI(AddToCartRequest request);

}
