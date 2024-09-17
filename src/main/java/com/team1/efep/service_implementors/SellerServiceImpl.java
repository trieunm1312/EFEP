package com.team1.efep.service_implementors;

import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.ChangeOrderStatusRequest;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.ChangeOrderStatusResponse;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.models.response_models.ViewOrderListResponse;
import com.team1.efep.repositories.*;
import com.team1.efep.services.SellerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.ChangeOrderStatusValidation;
import com.team1.efep.validations.CreateFlowerValidation;
import com.team1.efep.validations.ViewOrderListValidation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final FlowerRepo flowerRepo;

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
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFlowerResponse.class)) {
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
                .status(Status.FLOWER_STATUS_AVAILABLE)
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

    //---------------------------------------------VIEW ORDER LIST--------------------------------------------------------//

    @Override
    public String viewOrderList(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ViewOrderListResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderListLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderListResponse.class)) {
            model.addAttribute("response", (ViewOrderListResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "seller";
    }

    @Override
    public ViewOrderListResponse viewOrderListAPI(int id) {
        Account account = Role.getCurrentLoggedAccount(id, accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return ViewOrderListResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = viewOrderListLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderListResponse.class)) {
            return (ViewOrderListResponse) output;
        }
        return ViewOrderListResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object viewOrderListLogic(int accountId) {
        List<Order> orderList = orderRepo.findAllByUser_Seller_Id(accountId);
        if (!orderList.isEmpty()) {
            List<ViewOrderListResponse.OrderBill> orderBills = orderList.stream()
                    .map(this::viewOrderList)
                    .collect(Collectors.toList());
            return ViewOrderListResponse.builder()
                    .status("200")
                    .message("Orders found")
                    .orderList(orderBills)
                    .build();
        }
        return ViewOrderListValidation.orderListValidation();
    }

    private ViewOrderListResponse.OrderBill viewOrderList(Order order) {
        return ViewOrderListResponse.OrderBill.builder()
                .orderId(order.getId())
                .buyerName(order.getBuyerName())
                .createDate(order.getCreatedDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentType(order.getPaymentType().getType())
                .paymentMethod(order.getPaymentMethod().getMethod())
                .orderDetailList(viewOrderDetailList(order.getOrderDetailList()))
                .build();
    }
    private List<ViewOrderListResponse.Item> viewOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderListResponse.Item.builder()
                        .name(detail.getFlower().getName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //-----------------------------------CHANGE ORDER STATUS----------------------------------------//
    @Override
    public String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = changeOrderStatusLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangeOrderStatusResponse.class)) {
            model.addAttribute("response", (ChangeOrderStatusResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "seller";
    }

    @Override
    public ChangeOrderStatusResponse changeOrderStatusAPI(ChangeOrderStatusRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = changeOrderStatusLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangeOrderStatusResponse.class)) {
            return (ChangeOrderStatusResponse) output;
        }
        return ChangeOrderStatusResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object changeOrderStatusLogic(ChangeOrderStatusRequest request) {
        Map<String, String> errors = ChangeOrderStatusValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        order.setStatus(request.getStatus());
        orderRepo.save(order);
        return ChangeOrderStatusResponse.builder()
                .status("200")
                .message("Change order status successful")
                .order(ChangeOrderStatusResponse.ChangedStatus.builder()
                        .id(order.getId())
                        .status(order.getStatus())
                        .build())
                .build();
    }

}
