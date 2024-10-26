package com.team1.efep.service_implementors;

import com.team1.efep.VNPay.VNPayConfig;
import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.FileReaderUtil;
import com.team1.efep.utils.OTPGeneratorUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final JavaMailSenderImpl mailSender;
    private final AccountRepo accountRepo;
    private final FlowerRepo flowerRepo;
    private final WishlistItemRepo wishlistItemRepo;
    private final OrderRepo orderRepo;
    private final WishlistRepo wishlistRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;

    //---------------------------------------VIEW WISHLIST------------------------------------------//
    @Override
    public String viewWishlist(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You must log in");
            return "redirect:/login";
        }
        Object output = viewWishlistLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewWishlistResponse.class)) {
            model.addAttribute("msg", (ViewWishlistResponse) output);
            return "viewWishlist";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewWishlist";
    }

    @Override
    public ViewWishlistResponse viewWishlistAPI(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        if (account == null) {
            return ViewWishlistResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        Object output = viewWishlistLogic(accountId);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewWishlistResponse.class)) {
            return (ViewWishlistResponse) output;
        }
        return ViewWishlistResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object viewWishlistLogic(int accountId) {
        Map<String, String> error = ViewWishlistValidation.validate(accountId, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Account account = accountRepo.findById(accountId).orElse(null);
        assert account != null;

        List<ViewWishlistResponse.WishlistItems> wishlistItems = viewWishlistItemList(accountId);

        float totalPrice = wishlistItems.stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        return ViewWishlistResponse.builder()
                .status("200")
                .message("View wishlist successfully")
                .id(account.getUser().getWishlist().getId())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .totalPrice(totalPrice)
                .wishlistItemList(viewWishlistItemList(accountId))
                .build();
    }

    private List<ViewWishlistResponse.WishlistItems> viewWishlistItemList(int accountId) {
        Account account = accountRepo.findById(accountId).orElse(null);
        assert account != null;
        return account.getUser().getWishlist().getWishlistItemList().stream()
                .map(item -> ViewWishlistResponse.WishlistItems.builder()
                        .id(item.getId())
                        .imgList(
                                viewImageList(
                                        item.getFlower().getFlowerImageList().stream()
                                                .map(FlowerImage::getLink).toList()
                                )
                        )
                        .name(item.getFlower().getName())
                        .quantity(item.getQuantity())
                        .price(item.getFlower().getPrice())
                        .build())
                .toList();
    }

    //-------------------------------------------------ADD TO WISHLIST-----------------------------------------------------//

    @Override
    public String addToWishlist(AddToWishlistRequest request, HttpServletRequest httpServletRequest, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = addToWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, AddToWishlistResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
            model.addAttribute("msg", (AddToWishlistResponse) output);
            return "redirect:" + httpServletRequest.getHeader("Referer");
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @Override
    public AddToWishlistResponse addToWishlistAPI(AddToWishlistRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return AddToWishlistResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        Object output = addToWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, AddToWishlistResponse.class)) {
            return (AddToWishlistResponse) output;
        }
        return AddToWishlistResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object addToWishlistLogic(AddToWishlistRequest request) {
        Map<String, String> error = AddToWishlistValidation.validate(request, accountRepo, flowerRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        assert flower != null;
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        Wishlist wishlist = account.getUser().getWishlist();
        if (checkExistedItem(request, wishlist)) {
            WishlistItem wishlistItem = wishlistItemRepo.findByFlower_Id(request.getFlowerId()).orElse(null);
            assert wishlistItem != null;
            wishlistItem.setQuantity(wishlistItem.getQuantity() + request.getQuantity());
            wishlistItemRepo.save(wishlistItem);
        } else {
            wishlist.getWishlistItemList().add(
                    wishlistItemRepo.save(
                            WishlistItem.builder()
                                    .wishlist(wishlist)
                                    .flower(flower)
                                    .quantity(request.getQuantity())
                                    .build()));
            accountRepo.save(account);
        }
        return AddToWishlistResponse.builder()
                .status("200")
                .message("Your flower has been added to wishlist successfully")
                .build();
    }

    private boolean checkExistedItem(AddToWishlistRequest request, Wishlist wishlist) {
        return wishlist.getWishlistItemList().stream()
                .anyMatch(item -> Objects.equals(item.getFlower().getId(), request.getFlowerId()));
    }

    //------------------------------UPDATE WISHLIST--------------------------------------//

    @Override
    public String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = updateWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateWishlistResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
            model.addAttribute("msg", (UpdateWishlistResponse) output);
            return "redirect:/buyer/wishlist";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "redirect:/buyer/wishlist";
    }

    @Override
    public UpdateWishlistResponse updateWishlistAPI(UpdateWishlistRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return UpdateWishlistResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        Object output = updateWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateWishlistResponse.class)) {
            return (UpdateWishlistResponse) output;
        }
        return UpdateWishlistResponse.builder()
                .status("400")
                .message("Wishlist updated fail")
                .build();
    }


    private Object updateWishlistLogic(UpdateWishlistRequest request) {
        Map<String, String> error = UpdateWishlistValidation.validate(request, wishlistItemRepo);
        if (!error.isEmpty()) {
            return error;
        }

        Wishlist wishlist = wishlistRepo.findById(request.getWishlistId()).orElse(null);
        assert wishlist != null;

        WishlistItem wishlistItem = wishlistItemRepo.findById(Integer.parseInt(request.getWishlistItemId())).orElse(null);
        assert wishlistItem != null;

        if ("asc".equals(request.getRequest())) {
            wishlistItem.setQuantity(wishlistItem.getQuantity() + 1);
        } else if ("desc".equals(request.getRequest())) {
            if (wishlistItem.getQuantity() > 1) {
                wishlistItem.setQuantity(wishlistItem.getQuantity() - 1);
            } else {
                wishlistItemRepo.delete(wishlistItem);
            }
        }

        wishlistItemRepo.save(wishlistItem);
        return UpdateWishlistResponse.builder()
                .status("200")
                .message("Wishlist updated successfully")
                .build();
    }

    //--------------------------------DELETE WISHLIST-----------------------------------//

    @Override
    public String deleteWishlist(DeleteWishlistRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }

        Object output = deleteWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
            model.addAttribute("msg", (DeleteWishlistResponse) output);
            return "redirect:/buyer/wishlist";
        }

        model.addAttribute("response", (Map<String, String>) output);
        session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
        return "viewWishlist";
    }

    @Override
    public DeleteWishlistResponse deleteWishlistAPI(DeleteWishlistRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return DeleteWishlistResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }

        Object output = deleteWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistResponse.class)) {
            return (DeleteWishlistResponse) output;
        }

        return DeleteWishlistResponse.builder()
                .message("400")
                .message("Wishlist deleted fail")
                .build();
    }

    private Object deleteWishlistLogic(DeleteWishlistRequest request) {
        Map<String, String> error = DeleteWishlistValidation.validate(request, accountRepo, wishlistRepo);
        if (!error.isEmpty()) {
            return error;
        }

        Wishlist wishlist = wishlistRepo.findById(request.getWishlistId()).orElse(null);
        assert wishlist != null;

        wishlistItemRepo.deleteAll(wishlist.getWishlistItemList());

        wishlist.getWishlistItemList().clear();
        wishlistRepo.save(wishlist);

        return DeleteWishlistResponse.builder()
                .status("200")
                .message("Wishlist deleted successfully")
                .build();
    }

    //----------------------------------------------DELETE WISHLIST ITEM----------------------------------------------//

    @Override
    public String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = deleteWishlistItemLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistItemResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
            model.addAttribute("msg", (DeleteWishlistItemResponse) output);
            return "redirect:/buyer/wishlist";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public DeleteWishlistItemResponse deleteWishlistItemAPI(DeleteWishlistItemRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return DeleteWishlistItemResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        Object output = deleteWishlistItemLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistItemResponse.class)) {
            return (DeleteWishlistItemResponse) output;
        }
        return DeleteWishlistItemResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object deleteWishlistItemLogic(DeleteWishlistItemRequest request) {
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        Map<String, String> error = DeleteWishlistItemValidation.validate(request, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Wishlist wishlist = account.getUser().getWishlist();
        Optional<WishlistItem> wishlistItemOptional = wishlist.getWishlistItemList().stream()
                .filter(item -> Objects.equals(item.getId(), request.getWishlistItemId()))
                .findFirst();

        WishlistItem wishlistItem = wishlistItemOptional.get();
        wishlist.getWishlistItemList().remove(wishlistItem);
        wishlistItemRepo.delete(wishlistItem);

        accountRepo.save(account);
        return DeleteWishlistItemResponse.builder()
                .status("200")
                .message("Flower is deleted")
                .build();
    }


    //-----------------------------------------------FORGOT PASSWORD------------------------------------------------------------//
    @Override
    public String sendEmail(ForgotPasswordRequest request, Model model, HttpSession session) {
        Object output = sendEmailLogic(request);
        if (!OutputCheckerUtil.checkIfThisIsAResponseObject(output, ForgotPasswordResponse.class)) {
            model.addAttribute("error", (Map<String, String>) output);
            return "redirect:/login";
        }
        ForgotPasswordResponse response = (ForgotPasswordResponse) output;
        session.setAttribute("mail", request.getToEmail());
        session.setAttribute("otp" + request.getToEmail(), response.getExtraInfo());
        model.addAttribute("email", request.getToEmail());
        return "forgotPassword";
    }

    @Override
    public ForgotPasswordResponse sendEmailAPI(ForgotPasswordRequest request) {
        Object output = sendEmailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ForgotPasswordResponse.class)) {
            ForgotPasswordResponse response = (ForgotPasswordResponse) output;
            response.setExtraInfo("");
            return response;
        }
        return ForgotPasswordResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object sendEmailLogic(ForgotPasswordRequest request) {
        Map<String, String> errors = ForgotPasswordValidation.validate(request, accountRepo);
        if (!errors.isEmpty()) {
            return errors;
        }
        // Generate the OTP
        String otp = Const.OTP_LINK + OTPGeneratorUtil.generateOTP(6);

        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        // Helper to set the attributes for the MimeMessage
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set the email attributes
            helper.setFrom("vannhuquynhp@gmail.com");
            helper.setTo(request.getToEmail());
            helper.setSubject(Const.EMAIL_SUBJECT);

            // Read HTML content from a file and replace placeholders (e.g., OTP)
            String emailContent = FileReaderUtil.readFile(otp); // Assuming readFile returns HTML content as a String

            // Set the email content as HTML
            helper.setText(emailContent, true);  // 'true' indicates that the text is HTML

            // Send the email
            mailSender.send(message);

            // Return response
            return ForgotPasswordResponse.builder()
                    .status("200")
                    .message("Send email successfully")
                    .extraInfo(otp)
                    .build();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //-----------------------------------------------Verify OTP------------------------------------------------------------//

    @Override
    public String handleOTP(String code, Model model, HttpSession session) {
        return handleOTPLogic(code, session);
    }

    private String handleOTPLogic(String code, HttpSession session) {
        System.out.println(code);
        System.out.println(session.getAttribute("mail"));
        System.out.println(session.getAttribute("otp" + session.getAttribute("mail")));
        return code.equals(session.getAttribute("otp" + session.getAttribute("mail"))) ? "renewPassword" : "login";
    }

    //-----------------------------------------------RENEW PASSWORD------------------------------------------------------------//

    @Override
    public String renewPass(RenewPasswordRequest request, Model model) {
        Object output = renewPassLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, RenewPasswordResponse.class)) {
            model.addAttribute("msg", (RenewPasswordResponse) output);
            return "login";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "renewPassword";
    }

    @Override
    public RenewPasswordResponse renewPassAPI(RenewPasswordRequest request) {
        Object output = renewPassLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, RenewPasswordResponse.class)) {
            return (RenewPasswordResponse) output;
        }
        return RenewPasswordResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object renewPassLogic(RenewPasswordRequest request) {

        Map<String, String> errors = RenewPasswordValidation.validate(request, accountRepo);
        if (!errors.isEmpty()) {
            return errors;
        }

        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);
        if (acc != null && request.getPassword().equals(request.getConfirmPassword())) {
            acc.setPassword(request.getPassword());
            accountRepo.save(acc);
        }
        return RenewPasswordResponse.builder()
                .status("200")
                .message("Renew password successfully")
                .build();
//        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);
//        if (acc != null && request.getPassword().equals(request.getConfirmPassword())) {
//            acc.setPassword(request.getPassword());
//            accountRepo.save(acc);
//            return RenewPasswordResponse.builder()
//                    .status("200")
//                    .message("Renew password successfully")
//                    .build();
//        }
//
//        return RenewPasswordResponse.builder()
//                .status("400")
//                .message("Invalid email or password")
//                .build();
    }

    //-------------------------------------------VIEW BUYER FLOWER LIST---------------------------------------//
    @Override
    public String viewFlowerList(HttpSession session, Model model) {
        ViewFlowerListResponse output = viewFlowerListLogic();
        model.addAttribute("msg", output);
        return "category";
    }

    @Override
    public ViewFlowerListResponse viewFlowerListAPI() {
        ViewFlowerListResponse output = viewFlowerListLogic();
        return output;
    }

    private ViewFlowerListResponse viewFlowerListLogic() {
        List<Flower> flowers = flowerRepo.findAll();
        // if find -> print size of flower
        return ViewFlowerListResponse.builder()
                .status("200")
                .message("Number of flowers: " + flowers.size())
                .keyword("")
                .flowerList(viewFlowerList(flowers))
                .build();
    }

    private List<ViewFlowerListResponse.Flower> viewFlowerList(List<Flower> flowers) {
        return flowers.stream()
                .map(item -> ViewFlowerListResponse.Flower.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .images(viewImageList(item.getFlowerImageList().stream().map(FlowerImage::getLink).toList()))
                        .build()
                ).toList();
    }

    private List<ViewFlowerListResponse.Image> viewImageList(List<String> imageList) {
        return imageList.stream()
                .map(img -> ViewFlowerListResponse.Image.builder()
                        .link(img)
                        .build())
                .toList();
    }

    //-------------------------------------------VIEW BUYER SLIDE BAR---------------------------------------//

    @Override
    public void viewSlideBar(Model model) {
        model.addAttribute("msg1", viewSlideBarLogic());
    }

    @Override
    public ViewSlideBarResponse viewSlideBarAPI() {
        return viewSlideBarLogic();
    }


    private ViewSlideBarResponse viewSlideBarLogic() {
        List<String> flowerImageLinkList = new ArrayList<>();

        flowerImageLinkList.add("https://static.vecteezy.com/system/resources/previews/003/110/648/original/spring-sale-banner-season-floral-discount-poster-with-flowers-vector.jpg");
        flowerImageLinkList.add("https://as2.ftcdn.net/v2/jpg/02/44/86/81/1000_F_244868120_ZDcYjdJ6NMJHumrT6FQQQDiiEkX9h427.jpg");
        flowerImageLinkList.add("https://static.vecteezy.com/system/resources/previews/003/110/679/large_2x/summer-sale-promo-web-banner-multicolour-editable-floral-flower-frame-vector.jpg");
        flowerImageLinkList.add("https://static.vecteezy.com/system/resources/previews/021/600/647/large_2x/3d-rendering-spring-sale-banner-with-beautiful-colorful-flower-can-be-used-for-template-banners-wallpaper-flyers-invitation-posters-brochure-voucher-discount-photo.jpg");
        flowerImageLinkList.add("https://as1.ftcdn.net/v2/jpg/02/40/86/86/1000_F_240868665_0HcnhSG2uUOvAvCdRrHnnTIDsCAGTUqK.jpg");

        return ViewSlideBarResponse.builder()
                .status("200")
                .message("")
                .imageList(flowerImageLinkList)
                .build();
    }

    //--------------------------------------VIEW ORDER HISTORY------------------------------------------//

    @Override
    public String viewOrderHistory(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute("error", ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderHistoryLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderHistoryResponse.class)) {
            model.addAttribute("msg", (ViewOrderHistoryResponse) output);
            return "viewOrderHistory";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderHistory";
    }

    @Override
    public ViewOrderHistoryResponse viewOrderHistoryAPI(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        Object output = viewOrderHistoryLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderHistoryResponse.class)) {
            return (ViewOrderHistoryResponse) output;
        }
        return ViewOrderHistoryResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object viewOrderHistoryLogic(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);

        List<Order> orderList = orderRepo.findAllByUser_Id(account.getUser().getId());

        Map<String, String> error = ViewOrderHistoryValidation.orderHistoryValidation(account, orderList);
        if (!error.isEmpty()) {
            return error;
        }

        if (!orderList.isEmpty()) {
            List<ViewOrderHistoryResponse.Order> orders = orderList.stream()
                    .map(this::viewOrderList)
                    .collect(Collectors.toList());

            // Trả về kết quả
            return ViewOrderHistoryResponse.builder()
                    .status("200")
                    .message("Orders found")
                    .orderList(orders)
                    .build();
        }

        // Trả về thông báo không có đơn hàng
        return ViewOrderHistoryResponse.builder()
                .status("404")
                .message("No orders found for User")
                .build();
    }

    private ViewOrderHistoryResponse.Order viewOrderList(Order order) {
        String sellerName = order.getOrderDetailList().stream()
                .findFirst()
                .map(detail -> detail.getFlower().getSeller().getUser().getName())
                .orElse("Unknown Seller");

        return ViewOrderHistoryResponse.Order.builder()
                .orderId(order.getId())
                .sellerName(sellerName)
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .detailList(viewOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<ViewOrderHistoryResponse.Detail> viewOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderHistoryResponse.Detail.builder()
                        .flowerName(detail.getFlowerName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //-------------------------------VIEW ORDER DETAIL--------------------------------------//

    @Override
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute("error", ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderDetailResponse.class)) {
            model.addAttribute("msg", (ViewOrderDetailResponse) output);
            return "viewOrderStatusDetail";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderStatusDetail";
    }

    @Override
    public ViewOrderDetailResponse viewOrderDetailAPI(ViewOrderDetailRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return ViewOrderDetailResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        Object output = viewOrderDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderDetailResponse.class)) {
            return (ViewOrderDetailResponse) output;
        }
        return ViewOrderDetailResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object viewOrderDetailLogic(ViewOrderDetailRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Map<String, String> error = ViewOrderDetailValidation.validate(request, account, order);
        if (!error.isEmpty()) {
            return error;
        }
        List<ViewOrderDetailResponse.Detail> detailList = viewOrderDetailLists(order.getOrderDetailList());

        String sellerName = order.getOrderDetailList().stream()
                .findFirst()
                .map(detail -> detail.getFlower().getSeller().getUser().getName())
                .orElse("Unknown Seller");

        return ViewOrderDetailResponse.builder()
                .status("200")
                .message("Order details retrieved successfully")
                .orderId(order.getId())
                .sellerName(sellerName)
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .paymentMethod(order.getPaymentMethod().getName())
                .detailList(detailList)
                .build();
    }

    private List<ViewOrderDetailResponse.Detail> viewOrderDetailLists(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderDetailResponse.Detail.builder()
                        .flowerName(detail.getFlowerName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //--------------------------------------VIEW FLOWER TOP LIST------------------------------------------//

    @Override
    public void viewFlowerTopList(int top, Model model) {
        model.addAttribute("msg2", viewFlowerTopListLogic(top));
    }

    @Override
    public ViewFlowerTopListResponse viewFlowerTopListAPI(int top) {
        return viewFlowerTopListLogic(top);
    }


    public ViewFlowerTopListResponse viewFlowerTopListLogic(int top) {

        return ViewFlowerTopListResponse.builder()
                .status("200")
                .message("")
                .flowerList(
                        flowerRepo.findAll()
                                .stream()
                                .limit(top)
                                .map(
                                        flower -> ViewFlowerTopListResponse.Flower.builder()
                                                .id(flower.getId())
                                                .name(flower.getName())
                                                .price(flower.getPrice())
                                                .images(
                                                        flower.getFlowerImageList().stream()
                                                                .map(img -> ViewFlowerTopListResponse.Image.builder()
                                                                        .link(img.getLink())
                                                                        .build())
                                                                .toList()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }

    //--------------------------------------SEARCH FLOWER------------------------------------------//

    @Override
    public String searchFlower(SearchFlowerRequest request, Model model) {
        model.addAttribute("msg", searchFlowerLogic(request));
        return "category";
    }

    @Override
    public SearchFlowerResponse searchFlowerAPI(SearchFlowerRequest request) {
        return searchFlowerLogic(request);
    }

    private SearchFlowerResponse searchFlowerLogic(SearchFlowerRequest request) {
        return SearchFlowerResponse.builder()
                .status("200")
                .message("")
                .keyword(request.getKeyword())
                .flowerList(
                        flowerRepo.findAll()
                                .stream()
                                .filter(flower -> flower.getName().contains(request.getKeyword()))
                                .map(
                                        flower -> SearchFlowerResponse.Flower.builder()
                                                .id(flower.getId())
                                                .name(flower.getName())
                                                .price(flower.getPrice())
                                                .images(
                                                        flower.getFlowerImageList().stream()
                                                                .map(img -> SearchFlowerResponse.Image.builder()
                                                                        .link(img.getLink())
                                                                        .build())
                                                                .toList()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }

    //--------------------------------------VIEW FLOWER DETAIL------------------------------------------//

    @Override
    public String viewFlowerDetail(ViewFlowerDetailRequest request, Model model) {
        Object output = viewFlowerDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerDetailResponse.class)) {
            model.addAttribute("msg", (ViewFlowerDetailResponse) output);
            return "flowerDetail";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public ViewFlowerDetailResponse viewFlowerDetailAPI(ViewFlowerDetailRequest request) {
        Object output = viewFlowerDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerDetailResponse.class)) {
            return (ViewFlowerDetailResponse) output;
        }
        return ViewFlowerDetailResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    public Object viewFlowerDetailLogic(ViewFlowerDetailRequest request) {
        Map<String, String> errors = ViewFlowerDetailValidation.validate(request, flowerRepo);
        if (errors.isEmpty()) {
            Flower flower = flowerRepo.findById(request.getId()).orElse(null);
            assert flower != null;
            return ViewFlowerDetailResponse.builder()
                    .status("200")
                    .message("")
                    .flower(

                            ViewFlowerDetailResponse.Flower.builder()
                                    .id(flower.getId())
                                    .name(flower.getName())
                                    .price(flower.getPrice())
                                    .quantity(flower.getQuantity())
                                    .flowerAmount(flower.getFlowerAmount())
                                    .soldQuantity(flower.getSoldQuantity())
                                    .imageList(flower.getFlowerImageList().stream()
                                            .map(
                                                    flowers -> ViewFlowerDetailResponse.Image.builder()
                                                            .link(flowers.getLink())
                                                            .build()
                                            )
                                            .toList())
                                    .build()
                    ).build();


//                    ViewFlowerDetailResponse.builder()
//                    .status("200")
//                    .message("")
//                    .id(flower.getId())
//                    .name(flower.getName())
//                    .price(flower.getPrice())
//                    .flowerAmount(flower.getFlowerAmount())
//                    .quantity(flower.getQuantity())
//                    .soldQuantity(flower.getSoldQuantity())
//                    .imageList(
//                            flower.getFlowerImageList().stream()
//                                    .map(
//                                            img -> ViewFlowerDetailResponse.Image.builder()
//                                                    .link(img.getLink())
//                                                    .build()
//                                    )
//                                    .toList()
//                    )
//                    .build();
        }

        return errors;
    }

    //-----------------------------------VIEW ORDER STATUS-------------------------------------------//

    @Override
    public String viewOrderStatus(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }

        int orderId = (int) session.getAttribute("orderId");
        ViewOrderStatusResponse statusResponse = viewOrderStatusLogic(orderId);

        if (statusResponse != null) {
            model.addAttribute("msg", statusResponse.getOrderStatus());
            return "orderPage";
        } else {
            model.addAttribute("error", "Order not found");
            return "errorPage";
        }
    }

    @Override
    public ViewOrderStatusResponse viewOrderStatusAPI(ViewOrderStatusRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return ViewOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        return viewOrderStatusLogic(request.getOrderId());
    }


    private ViewOrderStatusResponse viewOrderStatusLogic(int orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order != null) {
            return ViewOrderStatusResponse.builder()
                    .status("200")
                    .message("View Order Status successful")
                    .orderStatus(order.getStatus())
                    .build();
        }
        return ViewOrderStatusResponse.builder()
                .status("Order not found")
                .build();
    }


    //--------------------------------CANCEL ORDER------------------------------------------//

    @Override
    public String cancelOrder(CancelOrderRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute("error", ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build());
            return "login";
        }
        Object output = cancelOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangeOrderStatusResponse.class)) {
            model.addAttribute("msg", (ChangeOrderStatusResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "buyer";
    }

    @Override
    public CancelOrderResponse cancelOrderAPI(CancelOrderRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        Object output = cancelOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CancelOrderResponse.class)) {
            return (CancelOrderResponse) output;
        }
        return CancelOrderResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object cancelOrderLogic(CancelOrderRequest request) {
        Map<String, String> error = CancelOrderValidation.validate(request, orderRepo, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Status.changeOrderStatus(order, Status.ORDER_STATUS_CANCELLED, orderRepo);

        return CancelOrderResponse.builder()
                .status("200")
                .message("Cancel order successfully")
                .build();
    }

    //--------------------------------VIEW CATEGORY------------------------------------------//

    @Override
    public void viewCategory(Model model) {
        model.addAttribute("msg3", viewCategoryLogic());
    }

    @Override
    public ViewCategoryListResponse viewCategoryAPI() {
        return viewCategoryLogic();
    }


    private ViewCategoryListResponse viewCategoryLogic() {
        return ViewCategoryListResponse.builder()
                .status("200")
                .message("")
                .categoryList(getCategoryList(categoryRepo.findAll()))
                .build();
    }

    private List<ViewCategoryListResponse.Category> getCategoryList(List<Category> categories) {
        return categories.stream()
                .map(cate ->
                        ViewCategoryListResponse.Category.builder()
                                .id(cate.getId())
                                .name(cate.getName())
                                .build())
                .toList();

    }

    //--------------------------------VN Pay------------------------------------------//

    @Override
    public String createVNPayPaymentLink(VNPayRequest request, Model model, HttpServletRequest httpServletRequest) {
        VNPayResponse vnPayResponse = createVNPayPaymentLinkLogic(request, httpServletRequest);
        model.addAttribute("msg", vnPayResponse);
        return "redirect:" + vnPayResponse.getPaymentURL();
    }

    @Override
    public VNPayResponse createVNPayPaymentLinkAPI(VNPayRequest request, HttpServletRequest httpServletRequest) {
        return createVNPayPaymentLinkLogic(request, httpServletRequest);
    }

    private VNPayResponse createVNPayPaymentLinkLogic(VNPayRequest request, HttpServletRequest httpServletRequest) {
        Map<String, String> paramList = new HashMap<>();

        long amount = getAmount(request);
        String txnRef = VNPayConfig.getRandomNumber(8);
        String ipAddress = VNPayConfig.getIpAddress(httpServletRequest);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        paramList.put("vnp_Version", getVersion());
        paramList.put("vnp_Command", getCommand());
        paramList.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        paramList.put("vnp_Amount", String.valueOf(amount));
        paramList.put("vnp_CurrCode", "VND");
        paramList.put("vnp_TxnRef", txnRef);
        paramList.put("vnp_OrderInfo", "Order payment No " + txnRef + ", Amount: " + amount);
        paramList.put("vnp_OrderType", getOrderType());
        paramList.put("vnp_Locale", "vn");
        paramList.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        paramList.put("vnp_IpAddr", ipAddress);
        paramList.put("vnp_CreateDate", getCreateDate(calendar, formatter));
        paramList.put("vnp_ExpireDate", getExpiredDate(15, calendar, formatter));

        return VNPayResponse.builder()
                .status("200")
                .message("Create payment link successfully")
                .paymentURL(buildVNPayLink(paramList))
                .build();
    }

    private String getVersion() {
        return "2.1.0";
    }

    private String getCommand() {
        return "pay";
    }

    private String getOrderType() {
        return "other";
    }

    private Long getAmount(VNPayRequest request) {
        return (long) request.getAmount() * 100 * 25000;
    }

    private String getCreateDate(Calendar calendar, SimpleDateFormat dateFormat) {
        return dateFormat.format(calendar.getTime());
    }

    private String getExpiredDate(int minutes, Calendar calendar, SimpleDateFormat dateFormat) {
        calendar.add(Calendar.MINUTE, minutes);
        return dateFormat.format(calendar.getTime());
    }

    private String buildVNPayLink(Map<String, String> paramList) {
        try {
            List<String> fieldNames = new ArrayList<>(paramList.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) paramList.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //--------------------------------GET PAYMENT RESULT-------------------------------------//

    @Override
    public String getPaymentResult(Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        Object output = getPaymentResultLogic(params, account.getId(), httpServletRequest);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, VNPayResponse.class)) {
            model.addAttribute("msg", (VNPayResponse) output);
            session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
            return ((VNPayResponse) output).getPaymentURL();
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "paymentFailed";
    }

    @Override
    public VNPayResponse getPaymentResultAPI(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest) {

        Object output = getPaymentResultLogic(params, accountId, httpServletRequest);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, VNPayResponse.class)) {
            return (VNPayResponse) output;
        }
        return VNPayResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object getPaymentResultLogic(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest) {
        User user = Role.getCurrentLoggedAccount(accountId, accountRepo).getUser();
        Map<String, String> error = VNPayValidation.validate(params, httpServletRequest);
        if (!error.isEmpty()) {
            return error;
        }
        String transactionStatus = params.get("vnp_TransactionStatus");
        if ("00".equals(transactionStatus)) {
            List<WishlistItem> items = wishlistItemRepo.findAllByWishlist_User_Id(user.getId());
            saveOrder(params, user, items);
            return VNPayResponse.builder()
                    .status("200")
                    .message("Your payment is successfully")
                    .paymentURL("/viewOrderSummary")
                    .build();
        }
        return VNPayResponse.builder()
                .status("400")
                .message("Your payment is failed")
                .paymentURL("/viewOrderSummary")
                .build();

    }

    private void saveOrder(Map<String, String> params, User user, List<WishlistItem> items) {

        Map<Seller, List<WishlistItem>> itemsBySeller = items.stream()
                .collect(Collectors.groupingBy(item -> item.getFlower().getSeller()));

        for (Map.Entry<Seller, List<WishlistItem>> entry : itemsBySeller.entrySet()) {
            Seller seller = entry.getKey();
            List<WishlistItem> sellerItems = entry.getValue();

            float totalPrice = 0;

            for (WishlistItem item : sellerItems) {
                totalPrice += item.getFlower().getPrice() * item.getQuantity();
            }

            Order savedOrder = orderRepo.save(Order.builder()
                    .user(user)
                    .buyerName(user.getName())
                    .createdDate(LocalDateTime.now())
                    .totalPrice(totalPrice)
                    .status(Status.ORDER_STATUS_PROCESSING)
                    .build());

            for (WishlistItem item : items) {
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(savedOrder)
                        .flower(item.getFlower())
                        .flowerName(item.getFlower().getName())
                        .quantity(item.getQuantity())
                        .price(item.getFlower().getPrice())
                        .build();
                orderDetailRepo.save(orderDetail);
            }
        }
        wishlistItemRepo.deleteAll(items);
        user.getWishlist().setWishlistItemList(new ArrayList<>());
        userRepo.save(user);
    }

    //-----------------------------------GET COD PAYMENT RESULT--------------------------------------//

    @Override
    public String getCODPaymentResult(Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        User user = account.getUser();
        List<WishlistItem> items = wishlistItemRepo.findAllByWishlist_User_Id(user.getId());
        saveOrder(params, user, items);
        session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
        System.out.println("Updated wishlist size: " + ((Account) session.getAttribute("acc")).getUser().getWishlist().getWishlistItemList().size());
        CODPaymentResponse response = CODPaymentResponse.builder()
                .status("200")
                .message("Your order has been preparing...")
                .build();
        redirectAttributes.addFlashAttribute("msg", response);
        return "redirect:/viewOrderSummary";
    }

    //---------------------------------CHECK OUT---------------------------------//

    @Override
    public String confirmOrder(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        model.addAttribute("msg", viewWishlistLogic(account.getId()));
        return "checkout";
    }

    //---------------------------------FIELTER CATEGORY---------------------------------//

    @Override
    public String filterCategory(FilterCategoryRequest request, Model model) {

        // Kiểm tra điều kiện categoryId không hợp lệ hoặc null
        if (request.getCategoryId() < 0) {
            // Trả về trang thông báo lỗi hoặc thông báo
            model.addAttribute("nullCategory", "Invalid category ID");
        }
        model.addAttribute("msg3", filterCategoryLogic(request));
        return "category";
    }

    @Override
    public FilterCategoryResponse filterCategoryAPI(FilterCategoryRequest request) {
        return filterCategoryLogic(request);
    }

    public FilterCategoryResponse filterCategoryLogic(FilterCategoryRequest request) {

        Category category = categoryRepo.findById(request.getCategoryId()).orElse(null);
        assert category != null;

        return FilterCategoryResponse.builder()
                .status("200")
                .message("")
                .categoryId(request.getCategoryId())
                .keyword(category.getName())
                .flowerList(
                        category.getFlowerCategoryList().stream()
                                .map(flower -> FilterCategoryResponse.Flower.builder()
                                        .id(flower.getId())
                                        .name(flower.getFlower().getName())
                                        .price(flower.getFlower().getPrice())
                                        .images(
                                                flower.getFlower().getFlowerImageList().stream()
                                                        .map(img -> FilterCategoryResponse.Image.builder()
                                                                .link(img.getLink())
                                                                .build())
                                                        .toList()
                                        )
                                        .build())
                                .toList()
                )
                .build();
    }


}

