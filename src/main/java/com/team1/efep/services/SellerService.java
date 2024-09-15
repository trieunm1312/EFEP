package com.team1.efep.services;

import com.team1.efep.models.entity_models.Flower;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.models.response_models.ViewOrderHistoryResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface SellerService {

    String createFlower(CreateFlowerRequest request, HttpSession session, Model model);

    CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request);

    String viewOrderHistory(HttpSession session, Model model);

    ViewOrderHistoryResponse viewOrderHistoryAPI(int id);

}
