package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.AdminService;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.utils.PasswordEncryptUtil;
import com.team1.efep.validations.BanUserValidation;
import com.team1.efep.validations.CreateAccountForSellerValidation;
import com.team1.efep.validations.UnBanUserValidation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

    private final SellerApplicationRepo sellerApplicationRepo;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    //-------------------------------------VIEW USER LIST----------------------------//

    @Override
    public String viewUserList(HttpSession session, Model model) {
        if (session.getAttribute("acc") == null) {
            return "redirect:/login";
        }
        model.addAttribute("msg1", viewUserListLogic());
        return "manageUser";
    }

    private ViewUserListResponse viewUserListLogic() {
        return ViewUserListResponse.builder()
                .status("200")
                .message("")
                .userList(
                        userRepo.findAll().stream()
                                .filter(user -> user.getAccount().getRole().equals(Role.SELLER) || (user.getAccount().getRole().equals(Role.BUYER) && user.isSeller()))
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
        if (session.getAttribute("acc") == null) {
            return "redirect:/login";
        }
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
        String randomPassword = generateRandomPassword();
        System.out.println("Random Password: " + randomPassword);
        return accountRepo.save(
                Account.builder()
                        .status("200")
                        .email(request.getEmail())
                        .password(PasswordEncryptUtil.encrypt(randomPassword))
                        .role(Role.SELLER)
                        .status(Status.ACCOUNT_STATUS_ACTIVE)
                        .build()
        );
    }

    private String generateRandomPassword() {
        // Define the character sets to use
        final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        final String DIGITS = "0123456789";
        final String SPECIAL_CHARACTERS = "@$!%*#?&";
        final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);

        // Ensure the password meets the criteria
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length()))); // At least one uppercase letter
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length()))); // At least one lowercase letter
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length()))); // At least one digit
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length()))); // At least one special character

        // Fill the remaining length of the password with random characters
        while (password.length() < 8) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // Shuffle the characters to ensure randomness
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Swap characters
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
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
        totalRevenue = Math.floor(totalRevenue * 10) / 10.0;
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
    public void getSellersInMonth(Model model) {
        model.addAttribute("SellerMonthly", getSellersInMonthLogic());
    }

    private SellersInMonthResponse getSellersInMonthLogic() {
        List<Seller> sellers = sellerRepo.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

        Map<String, Long> sellerMap = sellers.stream()
                .collect(Collectors.groupingBy(
                        seller -> seller.getUser().getCreatedDate().format(formatter),
                        Collectors.counting()
                ));

        List<SellersInMonthResponse.SellerCount> sellerCounts = sellerMap.entrySet().stream()
                .map(entry -> SellersInMonthResponse.SellerCount.builder()
                        .month(entry.getKey())
                        .count(entry.getValue().intValue())
                        .build())
                .toList();
        System.out.println(sellerCounts);

        return SellersInMonthResponse.builder()
                .status("200")
                .message("Get sellers created in month successfully")
                .sellerCounts(sellerCounts)
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

    //-------------------------------------VIEW APPLICATION LIST----------------------------//

    @Override
    public String viewApplicationList(HttpSession session, Model model) {
        if (session.getAttribute("acc") == null) {
            return "redirect:/login";
        }
        model.addAttribute("msg", ViewSellerApplicationResponse.builder()
                .applicationList(viewSellerApplicationLogic())
                .build());
        return "buyerRequest";
    }

    private List<ViewSellerApplicationResponse.Application> viewSellerApplicationLogic() {
        List<SellerApplication> applications = sellerApplicationRepo.findAll();

        return applications.stream()
                .sorted(Comparator.comparing((SellerApplication app) -> app.getStatus().equalsIgnoreCase("PENDING") ? 0 : 1)
                        .thenComparing(SellerApplication::getCreatedDate, Comparator.reverseOrder()))
                .map(app -> ViewSellerApplicationResponse.Application.builder()
                        .id(app.getId())
                        .content(app.getContent())
                        .rejectionReason(app.getRejectionReason())
                        .status(app.getStatus())
                        .createdDate(app.getCreatedDate() != null ? formatLocalDateTime(app.getCreatedDate()) : null)
                        .approvedDate(app.getApprovedDate() != null ? formatLocalDateTime(app.getApprovedDate()) : null)
                        .accountId(app.getUser().getAccount().getId())
                        .buyerName(app.getUser().getName())
                        .buyerPhone(app.getUser().getPhone())
                        .build())
                .toList();
    }

    private String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : "";
    }

    //-------------------------------------ACCEPT APPLICATION----------------------------//

    @Override
    public String acceptApplication(ApproveApplicationRequest request, Model model, RedirectAttributes redirectAttributes) {
        Object output = approveSellerApplicationLogic(request.getApplicationId());
        model.addAttribute("msg", output);
        return "redirect:/admin/application/list";
    }

    private ApproveSellerApplicationResponse approveSellerApplicationLogic(int applicationId) {
        SellerApplication application = sellerApplicationRepo.findById(applicationId).orElse(null);
        assert application != null;
        User user = application.getUser();
        user.setSeller(true);
        userRepo.save(user);
        Seller seller = Seller.builder()
                .user(user)
                .rating(0)
                .build();
        sellerRepo.save(seller);

        application.setStatus(Status.SELLER_APPLICATION_STATUS_APPROVED);
        application.setApprovedDate(LocalDateTime.now());
        sellerApplicationRepo.save(application);

        return ApproveSellerApplicationResponse.builder()
                .status("200")
                .message("Application approved successfully!")
                .build();
    }

    //-------------------------------------REJECT APPLICATION----------------------------//

    @Override
    public String rejectApplication(RejectApplicationRequest request, Model model, RedirectAttributes redirectAttributes) {
        Object output = rejectSellerApplicationLogic(request.getApplicationId(), request.getRejectionReason());
        model.addAttribute("msg", output);
        return "redirect:/admin/application/list";
    }

    private RejectSellerApplicationResponse rejectSellerApplicationLogic(int applicationId, String reason) {
        SellerApplication application = sellerApplicationRepo.findById(applicationId).orElse(null);
        assert application != null;
        application.setStatus(Status.SELLER_APPLICATION_STATUS_REJECTED);
        application.setRejectionReason(reason);
        application.setApprovedDate(LocalDateTime.now());
        sellerApplicationRepo.save(application);

        return RejectSellerApplicationResponse.builder()
                .status("200")
                .message("Application rejected successfully!")
                .build();
    }

}

