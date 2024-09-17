package com.team1.efep.controllers;

import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.request_models.ViewFlowerListForSellerRequest;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.models.response_models.ViewFlowerListForSellerResponse;
import com.team1.efep.models.response_models.ViewOrderHistoryResponse;
import com.team1.efep.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/flower")
    @Operation(hidden = true)
    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model) {
        return sellerService.createFlower(request, session, model);
    }

    @PostMapping("/flower/api")
    public CreateFlowerResponse createFlower(@RequestBody CreateFlowerRequest request, HttpSession session) {
        return sellerService.createFlowerAPI(request);
    }

    @GetMapping("/order")
    @Operation(hidden = true)
    public String createFlower(HttpSession session, Model model) {
        return sellerService.viewOrderHistoiry(session, model);
    }

    @GetMapping("/order/api/{id}")
    public ViewOrderHistoryResponse createFlower(@PathVariable int id) {

        return sellerService.viewOrderHistoryAPI(id);
    }

    @PostMapping("/view/flower")
    public String viewFlowerListForSeller(ViewFlowerListForSellerRequest request,HttpSession session, Model model) {
        return sellerService.viewFlowerListForSeller(request, session, model);
    }

    @PostMapping("/view/flower/api")
    @Operation(hidden = true)
    public ViewFlowerListForSellerResponse viewFlowerListForSeller(@RequestBody ViewFlowerListForSellerRequest request) {
        return sellerService.viewFlowerListForSellerAPI(request);
    }

}
