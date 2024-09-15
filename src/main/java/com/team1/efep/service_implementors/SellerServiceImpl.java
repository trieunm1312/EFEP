package com.team1.efep.service_implementors;

import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.models.response_models.ViewOrderHistoryResponse;
import com.team1.efep.repositories.*;
import com.team1.efep.services.SellerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.CreateFlowerValidation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final FlowerRepo flowerRepo;

    private final FlowerStatusRepo flowerStatusRepo;

    private final FlowerImageRepo flowerImageRepo;

    private final AccountRepo accountRepo;

    private final OrderRepo orderRepo;

    @Override
    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", CreateFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        model.addAttribute("response", createNewFlower(request));
        return "flower";
    }

    @Override
    public CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return CreateFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = createFlowerLogic(request);
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFlowerResponse.class)){
            return (CreateFlowerResponse) output;
        }
        return CreateFlowerResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object createFlowerLogic(CreateFlowerRequest request) {
        Map<String, String> errors = CreateFlowerValidation.validateInput(request, flowerRepo);
        if (errors.isEmpty()) {
            //success
            Flower flower = createNewFlower(request);
            return CreateFlowerResponse.builder()
                    .status("200")
                    .message("Flower created successfully")
                    .flower(
                            CreateFlowerResponse.FlowerInfo.builder()
                                    .id(flower.getId())
                                    .name(flower.getName())
                                    .price(flower.getPrice())
                                    .rating(flower.getRating())
                                    .description(flower.getDescription())
                                    .flowerAmount(flower.getFlowerAmount())
                                    .quantity(flower.getQuantity())
                                    .soldQuantity(flower.getSoldQuantity())
                                    .imageList(
                                            addFlowerImages(request, flower).stream()
                                                    .map(image -> CreateFlowerResponse.FlowerInfo.Images.builder()
                                                            .link(image.getLink())
                                                            .build())
                                                    .toList()
                                    )
                            .build()
                    )
                    .build();
        }
        //failed
        return errors;
    }



    private Flower createNewFlower(CreateFlowerRequest request) {
        Flower flower = Flower.builder()
                .name(request.getName())
                .price(request.getPrice())
                .rating(0)
                .description(request.getDescription())
                .flowerAmount(request.getFlowerAmount())
                .quantity(request.getQuantity())
                .soldQuantity(0)
                .flowerStatus(flowerStatusRepo.findByStatus(Const.FLOWER_STATUS_AVAILABLE))
                .build();


        return flowerRepo.save(flower);
    }


    private List<FlowerImage> addFlowerImages(CreateFlowerRequest request, Flower flower) {
        List<FlowerImage> flowerImages = request.getImgList().stream()
                .map(link -> FlowerImage.builder()
                        .flower(flower)
                        .link(link)
                        .build())
                .collect(Collectors.toList());
        return flowerImageRepo.saveAll(flowerImages);
    }

    //---------------------------------------------VIEW ORDER HISTORY--------------------------------------------------------//

    @Override
    public String viewOrderHistoiry(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderHistoryLogic(account.getId());
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderHistoryResponse.class)){
            model.addAttribute("response", (ViewOrderHistoryResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "";
    }

    @Override
    public ViewOrderHistoryResponse viewOrderHistoryAPI(int id) {
        Account account = Role.getCurrentLoggedAccount(id, accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return  ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = viewOrderHistoryLogic(account.getId());
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderHistoryResponse.class)){
            return (ViewOrderHistoryResponse) output;
        }
        return ViewOrderHistoryResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object viewOrderHistoryLogic(int accountId) {
        Map<String, String> errors = new HashMap<>();
        List<Order> orderList = orderRepo.findAllByUser_Seller_Id(accountId);
        if (!orderList.isEmpty()) {
            List<ViewOrderHistoryResponse.OrderBill> orderBills = orderList.stream()
                    .map(this::viewOrderList)
                    .collect(Collectors.toList());
            return ViewOrderHistoryResponse.builder()
                    .status("200")
                    .message("Orders found")
                    .orderList(orderBills)
                    .build();
        }
        return errors;
    }

    private ViewOrderHistoryResponse.OrderBill viewOrderList(Order order) {
        return ViewOrderHistoryResponse.OrderBill.builder()
                .orderId(order.getId().toString())
                .buyerName(order.getBuyerName())
                .createDate(order.getCreatedDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getOrderStatus().getStatus())
                .paymentType(order.getPaymentType().getType())
                .paymentMethod(order.getPaymentMethod().getMethod())
                .orderDetailList(viewOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<ViewOrderHistoryResponse.Item> viewOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderHistoryResponse.Item.builder()
                        .name(detail.getFlower().getName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //-----------------------------------CHANGE ORDER STATUS----------------------------------------//



}
