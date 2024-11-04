package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.entity_models.Seller;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.BanUserRequest;
import com.team1.efep.models.request_models.CreateAccountForSellerRequest;
import com.team1.efep.models.request_models.SearchUserListRequest;
import com.team1.efep.models.request_models.UnBanUserRequest;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.AdminService;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.BanUserValidation;
import com.team1.efep.validations.CreateAccountForSellerValidation;
import com.team1.efep.validations.UnBanUserValidation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AccountRepo accountRepo;

    private final UserRepo userRepo;

    private final SellerRepo sellerRepo;

    private final JavaMailSenderImpl mailSender;

    private final OrderRepo orderRepo;

    private final FlowerRepo flowerRepo;


    //-------------------------------------VIEW USER LIST----------------------------//

    @Override
    public String viewUserList(HttpSession session, Model model) {
        model.addAttribute("msg1", viewUserListLogic());
        return "manageUser";
    }

    private ViewUserListResponse viewUserListLogic() {
        return ViewUserListResponse.builder()
                .status("200")
                .message("")
                .userList(
                        userRepo.findAll().stream()
                                .map(
                                        user -> ViewUserListResponse.User.builder()
                                                .id(user.getId())
                                                .name(user.getName())
                                                .phone(user.getPhone())
                                                .avatar(user.getAvatar())
                                                .createdDate(user.getCreatedDate())
                                                .accountUser(
                                                        ViewUserListResponse.Account.builder()
                                                                .id(user.getAccount().getId())
                                                                .status(user.getAccount().getStatus())
                                                                .role(user.getAccount().getRole())
                                                                .email(user.getAccount().getEmail())
                                                                .build()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();

    }


    //-------------------------------------SEARCH USER LIST----------------------------//
    @Override
    public String searchUserList(HttpSession session, SearchUserListRequest request, Model model) {
        model.addAttribute("msg1", searchUserListLogic(request));
        return "manageUser";
    }

    private SearchUserListResponse searchUserListLogic(SearchUserListRequest request) {
        return SearchUserListResponse.builder()
                .status("200")
                .message("")
                .keyword(request.getKeyword())
                .userList(
                        userRepo.findAll().stream()
                                .filter(user -> user.getName().toLowerCase().contains(request.getKeyword()))
                                .map(
                                        user -> SearchUserListResponse.User.builder()
                                                .id(user.getId())
                                                .name(user.getName())
                                                .phone(user.getPhone())
                                                .createdDate(user.getCreatedDate())
                                                .avatar(user.getAvatar())
                                                .accountUser(
                                                        ViewUserListResponse.Account.builder()
                                                                .id(user.getAccount().getId())
                                                                .status(user.getAccount().getStatus())
                                                                .role(user.getAccount().getRole())
                                                                .email(user.getAccount().getEmail())
                                                                .build()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }

    //-------------------------------------BAN USER----------------------------//

    @Override
    public String banUser(BanUserRequest request, Model model, RedirectAttributes redirectAttributes) {
        Object output = banUserLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, BanUserResponse.class)) {
            redirectAttributes.addFlashAttribute("msg", (BanUserResponse) output);
            return "redirect:/admin/user/list";
        }
        redirectAttributes.addFlashAttribute("error", (output));
        return "redirect:/admin/user/list";
    }

    private Object banUserLogic(BanUserRequest request) {
        Map<String, String> error = BanUserValidation.validate(request, userRepo);
        if (error.isEmpty()) {
            User user = userRepo.findById(request.getId()).orElse(null);
            assert user != null;
            Account account = user.getAccount();
            account.setStatus(Status.ACCOUNT_STATUS_BANNED);
            accountRepo.save(account);
            return BanUserResponse.builder()
                    .status("200")
                    .message("Ban user successfully")
                    .build();
        }
        return error;
    }

    //-------------------------------------UNBAN USER----------------------------//

    @Override
    public String unBanUser(UnBanUserRequest request, Model model, RedirectAttributes redirectAttributes) {
        Object output = unBanUserLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UnBanUserResponse.class)) {
            redirectAttributes.addFlashAttribute("msg", (UnBanUserResponse) output);
            return "redirect:/admin/user/list";
        }
        redirectAttributes.addFlashAttribute("error", (output));
        return "redirect:/admin/user/list";
    }

    private Object unBanUserLogic(UnBanUserRequest request) {
        Map<String, String> error = UnBanUserValidation.validate(request, userRepo);
        if (error.isEmpty()) {
            User user = userRepo.findById(request.getId()).orElse(null);
            assert user != null;
            Account account = user.getAccount();
            account.setStatus(Status.ACCOUNT_STATUS_ACTIVE);
            accountRepo.save(account);
            return UnBanUserResponse.builder()
                    .status("200")
                    .message("Unban user successfully")
                    .build();
        }
        return error;
    }

    //-----------------------------------------CREATE ACCOUNT FOR SELLER------------------------------------//

    @Override
    public String createAccountForSeller(CreateAccountForSellerRequest request, Model model, RedirectAttributes redirectAttributes) {
        Object output = createAccountForSellerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateAccountForSellerResponse.class)) {
            redirectAttributes.addFlashAttribute("msg", (CreateAccountForSellerResponse) output);
            return "redirect:/admin/user/list";
        }
        redirectAttributes.addFlashAttribute("error", (output));
        redirectAttributes.addFlashAttribute("userInput", request);
        return "redirect:/admin/user/list";
    }

    private Object createAccountForSellerLogic(CreateAccountForSellerRequest request) {
        Map<String, String> error = CreateAccountForSellerValidation.validate(request, accountRepo);

        if (!error.isEmpty()) {
            return error;
        }
        createNewSeller(request);
        return CreateAccountForSellerResponse.builder()
                .status("200")
                .message("Create account for seller successfully")
                .email(request.getEmail())
                .build();
    }

    private void createNewSeller(CreateAccountForSellerRequest request) {

        sellerRepo.save(Seller.builder()
                .user(userRepo.save(User.builder()
                        .account(createNewAccount(request))
                        .name(request.getName())
                        .phone(request.getPhone())
                        .createdDate(LocalDate.now())
                        .build()))
                .build()
        );
    }

    private Account createNewAccount(CreateAccountForSellerRequest request) {
        return accountRepo.save(
                Account.builder()
                        .status("200")
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .role(Role.SELLER)
                        .status(Status.ACCOUNT_STATUS_ACTIVE)
                        .build()
        );
    }

    //---------------------------------------------DASHBOARD------------------------------------------------//

    @Override
    public void getTotalUser(Model model) {
        model.addAttribute("totalUser", userRepo.count());
    }

    @Override
    public void getTotalSeller(Model model) {
        model.addAttribute("totalSeller", sellerRepo.count());
    }

    @Override
    public void getTotalBuyer(Model model) {
        model.addAttribute("totalBuyer", userRepo.count() - sellerRepo.count());
    }

    @Override
    public void getTotalFlower(Model model) {
        model.addAttribute("totalFlower", flowerRepo.count());
    }

    @Override
    public void getTotalSale(Model model) {
        model.addAttribute("totalSale", orderRepo.countByStatus(Status.ORDER_STATUS_COMPLETED));
    }

    @Override
    public void getTotalRevenue(Model model) {
        List<Order> orders = orderRepo.findAll().stream()
                .filter(order -> order.getCreatedDate().getMonth() == LocalDateTime.now().getMonth() && order.getStatus().equals(Status.ORDER_STATUS_COMPLETED))
                .toList();

        Map<Seller, Double> revenueMap = orders.stream()
                .flatMap(order -> order.getOrderDetailList().stream())
                .collect(Collectors.groupingBy(
                        orderDetail -> orderDetail.getFlower().getSeller(),
                        Collectors.summingDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity() * 0.09)
                ));

        double totalRevenue = revenueMap.values().stream().mapToDouble(Double::doubleValue).sum();
        model.addAttribute("totalRevenue", totalRevenue);
    }

    @Override
    public void getOrdersInMonth(Model model) {
        model.addAttribute("OrderMonthly", getOrdersInMonthLogic());
    }

    private OrdersInMonthResponse getOrdersInMonthLogic() {
        List<Order> orders = orderRepo.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

        Map<String, Long> orderMap = orders.stream()
                .filter(order -> order.getStatus().equals(Status.ORDER_STATUS_COMPLETED))
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedDate().format(formatter),
                        Collectors.counting()
                ));

        List<OrdersInMonthResponse.OrderCount> orderCounts = orderMap.entrySet().stream()
                .map(entry -> OrdersInMonthResponse.OrderCount.builder()
                        .month(entry.getKey())
                        .count(entry.getValue().intValue())
                        .build())
                .toList();

        return OrdersInMonthResponse.builder()
                .status("200")
                .message("Get orders in month successfully")
                .orderCounts(orderCounts)
                .build();
    }

    @Override
    public void getTop3SellerInMonth(Model model) {
        model.addAttribute("topSellers", getTop3SellerInMonthLogic());
    }

    private TopSellersResponse getTop3SellerInMonthLogic() {
        List<Order> orders = orderRepo.findAll().stream()
                .filter(order -> order.getCreatedDate().getMonth() == LocalDateTime.now().getMonth() && order.getStatus().equals(Status.ORDER_STATUS_COMPLETED))
                .toList();

        Map<Seller, Double> revenueMap = orders.stream()
                .flatMap(order -> order.getOrderDetailList().stream())
                .collect(Collectors.groupingBy(
                        orderDetail -> orderDetail.getFlower().getSeller(),
                        Collectors.summingDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity() * 0.09)
                ));

        List<TopSellersResponse.SellerRevenue> topSellers = revenueMap.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .sorted(Map.Entry.<Seller, Double>comparingByValue().reversed())
                .limit(3)
                .map(entry -> TopSellersResponse.SellerRevenue.builder()
                        .image(entry.getKey().getUser().getAvatar())
                        .sellerName(entry.getKey().getUser().getName())
                        .revenue(entry.getValue().floatValue())
                        .build())
                .collect(Collectors.toList());

        return TopSellersResponse.builder()
                .status("200")
                .message("Top 3 sellers retrieved successfully")
                .sellers(topSellers)
                .build();
    }

}

