package com.team1.efep.controllers;

import com.team1.efep.configurations.AllPage;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
@Tag(name = "Seller")
public class SellerController {

    private final SellerService sellerService;

    //--------------------------------------------Flower------------------------------------------------//

    @GetMapping("/view/flower")
    public String viewFlowerListForSeller(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        return sellerService.viewFlowerListForSeller(session, model, redirectAttributes);
    }

    @PutMapping("/flower")
    @Operation(hidden = true)
    public String updateFlower(UpdateFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return sellerService.updateFlower(request, session, model, redirectAttributes);
    }

    @PutMapping("/flower/api")
    public UpdateFlowerResponse updateFlowerAPI(UpdateFlowerRequest request) {
        return sellerService.updateFlowerAPI(request);
    }

    @DeleteMapping("/flower")
    @Operation(hidden = true)
    public String deleteFlower(DeleteFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return sellerService.deleteFlower(request, session, model,  redirectAttributes);
    }

    @GetMapping("/flower/image")
    @Operation(hidden = true)
    public String viewFlowerImage(ViewFlowerImageRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return sellerService.viewFlowerImage(request, session, model, redirectAttributes);
    }

    @PostMapping("/flower/image/add")
    @Operation(hidden = true)
    public String addFlowerImage(AddFlowerImageRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return sellerService.addFlowerImage(request, session, model, redirectAttributes);
    }

    @DeleteMapping("/flower/image")
    @Operation(hidden = true)
    public String deleteFlowerImage(DeleteFlowerImageRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        return sellerService.deleteFlowerImage(request, session, model,  redirectAttributes);
    }

    @GetMapping("/flower/status")
    public List<String> getAllFlowerStatus() {
        return sellerService.getAllFlowerStatus();
    }

    @PostMapping("/flower")
    @Operation(hidden = true)
    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return sellerService.createFlower(request, session, model, redirectAttributes);
    }

    @PostMapping("/flower/api")
    public CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request) {
        return sellerService.createFlowerAPI(request);
    }



    @PutMapping("/flower/category")
    @Operation(hidden = true)
    public String updateFlowerCategory(UpdateFlowerCategoryRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes){
        return sellerService.updateFlowerCategory(request, session, model, redirectAttributes);
    }

    @GetMapping("/flower/category")
    @Operation(hidden = true)
    public String getFlowerCategory(HttpSession session, Model model, int flowerId,  RedirectAttributes redirectAttributes){
        return sellerService.getFlowerCategory(session, model, flowerId, redirectAttributes);
    }

    @PutMapping("/flower/category/remove")
    @Operation(hidden = true)
    public String removeFlowerCategory(RemoveFlowerCategoryRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes){
        return sellerService.removeFlowerCategory(request, session, model, redirectAttributes);
    }

    //-------------------------------------------------------------------------------------------------//

    //--------------------------------------------Order------------------------------------------------//

    @PutMapping("/order/status")
    @Operation(hidden = true)
    public String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes) {
        return sellerService.changeOrderStatus(request, session, model, httpServletRequest,  redirectAttributes);
    }

    @GetMapping("/order/list")
    @Operation(hidden = true)
    public String viewOrderList(HttpSession session, Model model) {
        return sellerService.viewOrderList(session, model);
    }

    @GetMapping("/order/detail")
    @Operation(hidden = true)
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return sellerService.viewOrderDetail(request, session, model, redirectAttributes);
    }

    @PostMapping("/order/filter")
    @Operation(hidden = true)
    public String filterOrder(FilterOrderRequest request, HttpSession session, Model model) {
        return sellerService.filterOrder(request, session, model);
    }

    @GetMapping("/order/sort")
    @Operation(hidden = true)
    public String sortOrder(FilterOrderRequest filterOrderRequest, HttpSession session, Model model) {
        return sellerService.sortOrder(filterOrderRequest, session, model);
    }

    //-------------------------------------------------------------------------------------------------//

    @GetMapping("/buyer/list")
    public String viewBuyerList(HttpSession session, Model model) {
        return sellerService.viewBuyerList(session, model);
    }

    @PostMapping("/search/buyer")
    public String searchBuyerList(HttpSession session, SearchBuyerListRequest request, Model model) {
        return sellerService.searchBuyerList(session, request, model);
    }

    //---------------FEEDBACK----------------//

    @GetMapping("/feedback")
    @Operation(hidden = true)
    public String viewFeedback(Model model, HttpSession session) {
        return sellerService.viewFeedback(model, session);
    }

    @GetMapping("/feedback/api/{sellerId}")
    public ViewFeedbackResponse viewFeedback(@PathVariable int sellerId) {
        return sellerService.viewFeedbackAPI(sellerId);
    }

}
