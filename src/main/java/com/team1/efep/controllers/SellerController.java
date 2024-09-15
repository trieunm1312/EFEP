package com.team1.efep.controllers;

import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/flower")
    @Operation(hidden = true)
    public String createFlower(@RequestBody CreateFlowerRequest request, HttpSession session, Model model) {
        return sellerService.createFlower(request, session, model);
    }

    @PostMapping("/flower/api")
    public CreateFlowerResponse createFlower(@RequestBody CreateFlowerRequest request, HttpSession session) {
        return sellerService.createFlowerAPI(request);
    }
}
