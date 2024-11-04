package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.entity_models.Wishlist;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.UserRepo;
import com.team1.efep.repositories.WishlistRepo;
import com.team1.efep.services.AccountService;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.GoogleLoginGeneratorUtil;
import com.team1.efep.utils.GoogleLoginUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.utils.PasswordEncryptUtil;
import com.team1.efep.validations.ChangePasswordValidation;
import com.team1.efep.validations.LoginValidation;
import com.team1.efep.validations.RegisterValidation;
import com.team1.efep.validations.UpdateProfileValidation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;

    private final UserRepo userRepo;

    private final WishlistRepo wishlistRepo;

    private final GoogleLoginGeneratorUtil googleLoginGeneratorUtil;

    private final BuyerService buyerService;

//--------------------------Hash password------------------------//


//    private boolean checkPassword(String inputPassword, String storedPassword) {
//        String hashedInputPassword = PasswordEncryptUtil.encrypt(inputPassword);
//        return hashedInputPassword.equals(storedPassword);
//    }

    //----------------------------------------------REGISTER-------------------------------------------------//
    @Override
    public String register(RegisterRequest request, Model model, RedirectAttributes redirectAttributes) {
        request.setPassword(PasswordEncryptUtil.encrypt(request.getPassword()));
        Object output = registerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, RegisterResponse.class)) {
            redirectAttributes.addFlashAttribute("msg", (RegisterResponse) output);
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("error", output);
        redirectAttributes.addFlashAttribute("userInput", request);
        return "redirect:/register";
    }

    private Object registerLogic(RegisterRequest request) {
        Map<String, String> errors = RegisterValidation.validate(request, accountRepo);
        if (errors.isEmpty()) {
            createNewBuyer(request);
            return RegisterResponse.builder()
                    .status("200")
                    .message("Register successfully")
                    .build();
        }

        return errors;
    }

    private void createNewBuyer(RegisterRequest request) {

        wishlistRepo.save(Wishlist.builder()
                .user(userRepo.save(User.builder()
                        .account(createNewAccount(request))
                        .name(request.getName())
                        .phone(request.getPhone())
                        .avatar(request.getAvatar())
                        .background(request.getBackground())
                        .build()))
                .build()
        );
    }

    private Account createNewAccount(RegisterRequest request) {
        return accountRepo.save(
                Account.builder()
                        .status(Status.ACCOUNT_STATUS_ACTIVE)
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .role(Role.BUYER)
                        .build()
        );
    }

    //-------------------------------------------LOGIN------------------------------------------------------//
    @Override
    public String login(LoginRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Object output = loginLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, LoginResponse.class)) {
            Account acc = accountRepo.findByEmailAndPassword(request.getEmail(), PasswordEncryptUtil.encrypt(request.getPassword())).get();
            session.setAttribute("acc", acc);
            redirectAttributes.addFlashAttribute("msg", (LoginResponse) output);
            switch (acc.getRole().toUpperCase()) {
                case "SELLER":
                    return "redirect:/seller/dashboard";
                case "ADMIN":
                    return "redirect:/admin/dashboard";
                default:
//                    HomepageConfig.config(model, buyerService);
                    return "redirect:/";
            }
        }
        redirectAttributes.addFlashAttribute("error", output);
        redirectAttributes.addFlashAttribute("userInput", request);
        return "redirect:/login";
    }


    private Object loginLogic(LoginRequest request) {
        Map<String, String> errors = LoginValidation.validate(request, accountRepo);
        if (errors.isEmpty()) {
            Account account = accountRepo.findByEmail(request.getEmail()).orElse(null);
            assert account != null;
            return LoginResponse.builder()
                    .status("200")
                    .message("Login successfully")
                    .build();
        }
        return errors;
    }

    //----------------------------------------LOGIN WITH GMAIL------------------------------------------//

    @Override
    public void getGoogleLoginUrl(HttpServletResponse response) {
        try {
            response.sendRedirect(GoogleLoginUtil.generateGoogleLoginUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String exchangeGoogleCode(String code, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        return GoogleLoginUtil.accessGoogleInfo(
                googleLoginGeneratorUtil.exchangeAuthorizationCode(code).getAccess_token(),
                this,
                model,
                accountRepo,
                session,
                userRepo,
                wishlistRepo,
                redirectAttributes
        );
    }

    //------------------------------------------VIEW PROFILE-----------------------------------------------//

    @Override
    public String viewProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        ViewProfileRequest profileRequest = ViewProfileRequest.builder().id(account.getUser().getId()).build();
        ViewProfileResponse response = viewProfileLogic(profileRequest);
        session.setAttribute("acc", account);
        redirectAttributes.addFlashAttribute("msg", response);
        switch (account.getRole().toUpperCase()) {
            case "SELLER":
                return "redirect:/seller/profile";
            case "BUYER":
                return "redirect:/myAccount";
        }
        return "redirect:/";
    }

    // việc trả về hồ sơ người dùng nên trả về ViewProfileResponse
    // thay vì Object (nếu phải check kiểu dữ liệu của Object ở nhiều nơi khác nhau ==> lỗi runtime)
    private ViewProfileResponse viewProfileLogic(ViewProfileRequest request) {
        User user = userRepo.findById(request.getId()).orElse(null);
        assert user != null;
        if (user.getAccount().getRole().equals(Role.SELLER)) {
            return ViewProfileResponse.builder()
                    .status("200")
                    .message("View profile successfully")
                    .id(user.getId())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .email(user.getAccount().getEmail())
                    .avatar(user.getAvatar())
                    .background(user.getBackground())
                    .totalFlower(user.getSeller().getFlowerList().size())
                    .sellerRating(user.getSeller().getRating())
                    .feedbackList(
                            user.getSeller().getFeedbackList().stream()
                                    .map(feedback -> ViewProfileResponse.FeedbackDetail.builder()
                                            .id(feedback.getId())
                                            .name(feedback.getUser().getName())
                                            .avatar(feedback.getUser().getAvatar())
                                            .content(feedback.getContent())
                                            .rating(feedback.getRating())
                                            .createDate(feedback.getCreateDate().toLocalDate())
                                            .build()
                                    )
                                    .toList()
                    )
                    .build();
        }
        return ViewProfileResponse.builder()
                .status("200")
                .message("View profile successfully")
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getAccount().getEmail())
                .avatar(user.getAvatar())
                .background(user.getBackground())
                .build();
    }

//------------------------------------------UPDATE PROFILE--------------------------------------------------//

    @Override
    public String updateProfile(UpdateProfileRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        Object output = updateProfileLogic(request);
        Account account = accountRepo.findById(request.getId()).orElse(null);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateProfileResponse.class)) {
            session.setAttribute("acc", account);
            redirectAttributes.addFlashAttribute("msg", (UpdateProfileResponse) output);
            switch (account.getRole().toUpperCase()) {
                case "SELLER":
                    return "redirect:/seller/profile";
                case "BUYER":
                    System.out.println(account.getUser().getWishlist().getWishlistItemList().size());
                    return "redirect:/myAccount";
            }
        }
        redirectAttributes.addFlashAttribute("error", output);
        if (account.getRole().toUpperCase().matches("SELLER"))
            return "redirect:/seller/profile";
        else {
            System.out.println(account.getUser().getWishlist().getWishlistItemList().size());
            return "redirect:/myAccount";
        }
    }

    private Object updateProfileLogic(UpdateProfileRequest request) {
        Map<String, String> errors = UpdateProfileValidation.validate(request, accountRepo);
        if (errors.isEmpty()) {
            Account account = accountRepo.findById(request.getId()).orElse(null);
            assert account != null;
            User user = account.getUser();
            user.setName(request.getName());
            user.setPhone(request.getPhone());
            user.setAvatar(request.getAvatar());
            user = userRepo.save(user);

            return UpdateProfileResponse.builder()
                    .status("200")
                    .message("Update profile successfully")
                    .id(user.getId())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .email(user.getAccount().getEmail())
                    .avatar(user.getAvatar())
                    .build();
        }
        return errors;
    }

    //---------------------------------------CHANGE PASSWORD-------------------------------------//

    @Override
    public String changePassword(ChangePasswordRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Object output = changePasswordLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangePasswordResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getId()).orElse(null));
            redirectAttributes.addFlashAttribute("msg", (ChangePasswordResponse) output);
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("error", output);
        return "redirect:/change/password";
    }

    private Object changePasswordLogic(ChangePasswordRequest request) {
        Map<String, String> errors = ChangePasswordValidation.validate(request);
        if (errors.isEmpty()) {
            Account account = accountRepo.findByIdAndPassword(request.getId(), request.getCurrentPassword()).orElse(null);
            assert account != null;
            account.setPassword(request.getNewPassword());
            accountRepo.save(account);
            return ChangePasswordResponse.builder()
                    .status("200")
                    .message("Change password successfully")
                    .build();
        }
        return errors;
    }

    //-------------------------------LOG OUT----------------------------------------------//

    @Override
    public String logout(HttpSession session) {
        if (session.getAttribute("acc") != null) {
            session.invalidate();
            return "redirect:/login";
        }
        return "redirect:/login";
    }


}


