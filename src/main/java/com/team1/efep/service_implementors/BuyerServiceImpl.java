package com.team1.efep.service_implementors;

import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.request_models.ForgotRequest;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.CartRepo;
import com.team1.efep.repositories.FlowerRepo;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.FileReaderUtil;
import com.team1.efep.utils.OTPGeneratorUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.ViewFlowerListValidation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final JavaMailSenderImpl mailSender;
    private final AccountRepo accountRepo;
    private final CartRepo cartRepo;
    private final FlowerRepo flowerRepo;

    @Override
    public String sendEmail(ForgotRequest request, Model model) {
        return "";
    }


    //---------------------------------------VIEW CART------------------------------------------//
    @Override
    public String viewCart(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        model.addAttribute("cart", viewCartItemList(account.getId()));
        return "viewCart";
    }

    @Override
    public ViewCartResponse viewCartAPI(int id) {
        Account account = Role.getCurrentLoggedAccount(id, accountRepo);
        if (account == null) {
            return ViewCartResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        return ViewCartResponse.builder()
                .status("200")
                .message("View cart successfully")
                .id(account.getUser().getCart().getId())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .cartItemList(viewCartItemList(account.getId()))
                .build();
    }

    private Cart viewCartLogic(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        assert account != null;
        return account.getUser().getCart();
    }

    private List<ViewCartResponse.CartItems> viewCartItemList(int accountId) {
        return viewCartLogic(accountId).getCartItemList().stream()
                .map(item -> new ViewCartResponse.CartItems(item.getId(), item.getFlower().getName(), item.getQuantity(), item.getFlower().getPrice()))
                .toList();
    }

    //---------------------------------------ADD TO CART--------------------------------------//

    @Override
    public String addToCart(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        model.addAttribute("cart", viewCartItemList(account.getId()));
        return "cart";
    }

    @Override
    public AddToCartResponse addToCartAPI(AddToCartRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return AddToCartResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }

        Cart cart = addToCartLogic(request);
        if (cart == null) {
            return AddToCartResponse.builder()
                    .status("400")
                    .message("Flower is out of stock")
                    .id(account.getUser().getCart().getId())
                    .userId(account.getUser().getId())
                    .userName(account.getUser().getName())
                    .cartItemList(viewCartItemList(account.getId()))
                    .build();
        }

        return AddToCartResponse.builder()
                .status("200")
                .message("Add to cart successfully")
                .id(account.getUser().getCart().getId())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .cartItemList(viewCartItemList(account.getId()))
                .build();
    }

    private Cart addToCartLogic(AddToCartRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        Cart cart = viewCartLogic(request.getAccountId());
        Flower flower = checkAvailableFlower(request.getFlowerId());
        CartItem cartItem = checkExistedItem(request, cart).get();
        if (checkExistedItem(request, cart).isPresent()) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            return cart;
        }
        if (flower != null) {
            cart.getCartItemList().add(
                    CartItem.builder()
                            .cart(account.getUser().getCart())
                            .flower(flower)
                            .quantity(cartItem.getQuantity())
                            .build());
        }
        return cartRepo.save(cart);
    }

    private Optional<CartItem> checkExistedItem(AddToCartRequest request, Cart cart) {
        Optional<CartItem> existingCartItem = cart.getCartItemList().stream()
                .filter(item -> item.getFlower().getId() == request.getFlowerId())
                .findFirst();
        return existingCartItem;
    }

    private Flower checkAvailableFlower(int flowerId) {
        Flower flower = flowerRepo.findById(flowerId).orElse(null);
        assert flower != null;
        if (flower.getFlowerStatus().getStatus().equals(Const.FLOWER_STATUS_AVAILABLE)) {
            return flower;
        }
        return null;
    }


    @Override
    public ForgotResponse sendEmailAPI(ForgotRequest request) {
        try {
            return sendEmailLogic(request);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private ForgotResponse sendEmailLogic(ForgotRequest request) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("quynhpvnse182895@fpt.edu.vn");
//        message.setTo(request.getToEmail());
//        String otp = OTPGeneratorUtil.generateOTP(6);
//        message.setText(FileReaderUtil.readFile(otp));
//        message.setSubject(request.getSubject());
//        message.;
//        mailSender.send(message);
//        return ForgotResponse.builder()
//                .status("200")
//                .message("Send email successfully")
//                .build();

        // Generate the OTP
        String otp = OTPGeneratorUtil.generateOTP(6);

        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        // Helper to set the attributes for the MimeMessage
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set the email attributes
        helper.setFrom("quynhpvnse182895@fpt.edu.vn");
        helper.setTo(request.getToEmail());
        helper.setSubject(request.getSubject());

        // Read HTML content from a file and replace placeholders (e.g., OTP)
        String emailContent = FileReaderUtil.readFile(otp); // Assuming readFile returns HTML content as a String

        // Set the email content as HTML
        helper.setText(emailContent, true);  // 'true' indicates that the text is HTML

        // Send the email
        mailSender.send(message);

        // Return response
        return ForgotResponse.builder()
                .status("200")
                .message("Send email successfully")
                .build();
    }

    @Override
    public String renewPass(RenewPasswordRequest request, Model model) {
        return "";
    }

    @Override
    public RenewPasswordResponse renewPassAPI(RenewPasswordRequest request) {
        return renewPassLogic(request);
    }

    private RenewPasswordResponse renewPassLogic(RenewPasswordRequest request) {
        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);
        if (acc != null && request.getPassword().equals(request.getConfirmPassword())) {
            acc.setPassword(request.getPassword());
            accountRepo.save(acc);
            return RenewPasswordResponse.builder()
                    .status("200")
                    .message("Renew password successfully")
                    .build();
        }

        return RenewPasswordResponse.builder()
                .status("400")
                .message("Invalid email or password")
                .build();
    }

    //-------------------------------------------VIEW BUYER FLOWER LIST---------------------------------------//
    @Override
    public String viewFlowerList(HttpSession session, Model model) {
        Object output = viewFlowerListLogic();
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerListResponse.class)){
            model.addAttribute("msg", (ViewFlowerListResponse)output);
            return "home";
        }
        model.addAttribute("error", ConvertMapIntoStringUtil.convert((Map<String, String>)output));
        return "home";
    }

    @Override
    public ViewFlowerListResponse viewFlowerListAPI() {
        Object output = viewFlowerListLogic();
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerListResponse.class)){
                return (ViewFlowerListResponse) output;
        }
        return ViewFlowerListResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>)output))
                .build();
    }


    private Object viewFlowerListLogic() {
        Map<String, String> errors = ViewFlowerListValidation.validate();
        List<Flower> flowers = flowerRepo.findAll();

        // if find -> print size of flower
        return ViewFlowerListResponse.builder()
                .status("200")
                .message("Number of flowers: " + flowers.size())
                .flowerList(viewFlowerList(flowers))
                .build();
    }

    private List<ViewFlowerListResponse.Flower> viewFlowerList(List<Flower> flowers) {
        return flowers.stream()
                .map(item -> ViewFlowerListResponse.Flower.builder()
                        .name(item.getName())
                        .price(item.getPrice())
                        .rating(item.getRating())
                        .images(viewImageList(item.getFlowerImageList()))
                        .build()
                ).toList();
    }

    private List<ViewFlowerListResponse.Image> viewImageList(List<FlowerImage> imageList) {
        return imageList.stream()
                .map(img -> ViewFlowerListResponse.Image.builder()
                        .link(img.getLink())
                        .build())
                .toList();
    }

    //-------------------------------------------VIEW BUYER SLIDE BAR---------------------------------------//

    @Override
    public void viewSliderBar(Model model) {
        model.addAttribute("msg", viewSliderBarLogic());
    }

    @Override
    public ViewSliderBarResponse viewSliderBarAPI() {
        return viewSliderBarLogic();
    }

    public ViewSliderBarResponse viewSliderBarLogic(){
        List<String> flowerImageLinkList = new ArrayList<>();

        flowerImageLinkList.add("https://static.vecteezy.com/system/resources/previews/003/110/648/original/spring-sale-banner-season-floral-discount-poster-with-flowers-vector.jpg");
        flowerImageLinkList.add("");
        flowerImageLinkList.add("");
        flowerImageLinkList.add("");
        flowerImageLinkList.add("");

        return ViewSliderBarResponse.builder()
                .status("200")
                .message("")
                .imageList(flowerImageLinkList)
                .build();
    }

}

