package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.entity_models.Wishlist;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.UserRepo;
import com.team1.efep.repositories.WishlistRepo;
import com.team1.efep.services.AccountService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.GoogleLoginGeneratorUtil;
import com.team1.efep.utils.GoogleLoginUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.ChangePasswordValidation;
import com.team1.efep.validations.RegisterValidation;
import com.team1.efep.validations.UpdateProfileValidation;
import com.team1.efep.validations.ViewProfileValidation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final WishlistRepo wishlistRepo;
    private final GoogleLoginGeneratorUtil googleLoginGeneratorUtil;

    //-------------------------------REGISTER----------------------------------//
    @Override
    public String register(RegisterRequest request, Model model) {
        String error = registerLogic(request);
        if (!error.isEmpty()) {
            model.addAttribute("error", RegisterResponse.builder()
                    .status("400")
                    .message(error)
                    .build());
            return "register";
        }
        return "login";
    }

    @Override
    public RegisterResponse registerAPI(RegisterRequest request) {
        String error = registerLogic(request);
        if (!error.isEmpty()) {
            return RegisterResponse.builder()
                    .status("400")
                    .message(error)
                    .build();
        }
        return RegisterResponse.builder()
                .status("200")
                .message("Register successfully")
                .build();

    }

    private String registerLogic(RegisterRequest request) {
        String error = validateInput(request);

        if (error.isEmpty()) {
            createNewBuyer(request);
        }

        return error;
    }

    private String validateInput(RegisterRequest request) {
        String error = "";

        // Check email, phone, password format using RegisterValidation
        String validError = RegisterValidation.validateRegisterInput(request.getEmail(), request.getPhone(), request.getPassword());
        if (!validError.isEmpty()) {
            return validError;
        }

        if (accountRepo.findByEmail(request.getEmail()).isPresent()) {
            error += "email, ";
        }

        if (userRepo.findByName(request.getName()).isPresent()) {
            error += "name, ";
        }

        if (userRepo.findByPhone(request.getPhone()).isPresent()) {
            error += "phone, ";
        }

        if (!error.isEmpty()) {
            error = formatErrorMsg(error);
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            if (!error.isEmpty()) {
                error += ", confirm password is not matched";
            } else {
                error += "Confirm password is not matched";
            }

        }

        return error;
    }

    private String formatErrorMsg(String error) {
        error = error.substring(0, 1).toUpperCase() + error.trim().substring(1, error.length() - 2);

        String verbBe = "";
        String[] fields = error.trim().substring(0, error.length() - 1).split(",");
        if (fields.length == 1) {
            verbBe = " is existed";
        } else {
            verbBe = " are existed";
        }

        return error + verbBe;
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
                        .status(null)
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .role(Role.BUYER)
                        .build()
        );
    }


    //-------------------------------LOGIN-------------------------------------//
    @Override
    public String login(LoginRequest request, Model model, HttpSession session) {
        Account loggedAccount = loginLogic(request);
        if (loggedAccount == null) {
            model.addAttribute("error", LoginResponse.builder()
                    .status("400")
                    .message("Invalid username or password")
                    .build());
            return "login";
        }
        session.setAttribute("acc", loggedAccount);
        model.addAttribute("msg", LoginResponse.builder()
                .status("200")
                .message("Login successfully")
                .build());
        return "login";
    }

    @Override
    public LoginResponse loginAPI(LoginRequest request) {
        Account loggedAccount = loginLogic(request);
        if (loggedAccount == null) {
            return LoginResponse.builder()
                    .status("400")
                    .message("Invalid username or password")
                    .build();
        }
        return LoginResponse.builder()
                .status("200")
                .message("Login successfully")
                .build();

    }

    private Account loginLogic(LoginRequest request) {
        return accountRepo.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElse(null);
    }

    //__________________________________________________________________________________//

    @Override
    public LoginGoogleResponse getGoogleLoginUrl() {
        return LoginGoogleResponse.builder()
                .status("200")
                .message("")
                .loginUrl(GoogleLoginUtil.generateGoogleLoginUrl())
                .build();
    }

    @Override
    public void exchangeGoogleCode(String code) {
        GoogleLoginUtil.accessGoogleInfo(
                googleLoginGeneratorUtil.exchangeAuthorizationCode(code).getAccess_token()
        );
    }

    //-------------------------------VIEW PROFILE(LAM LAI --> HOI VO LY + KO HIEU VAN DE)-------------------------------------//

    @Override
    public String viewProfile(ViewProfileRequest request, Model model) {
        Object output = viewProfileLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewProfileRequest.class)) {
            model.addAttribute("msg", (ViewProfileResponse) output);
            return "myAccount";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "login";
    }

    @Override
    public ViewProfileResponse viewProfileAPI(ViewProfileRequest request) {
        Object output = viewProfileLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewProfileRequest.class)) {
            return (ViewProfileResponse) output;
        }
        return ViewProfileResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    // việc trả về hồ sơ người dùng nên trả về ViewProfileResponse
    // thay vì Object (nếu phải check kiểu dữ liệu của Object ở nhiều nơi khác nhau ==> lỗi runtime)
    private Object viewProfileLogic(ViewProfileRequest request) {
        Map<String, String> errors = ViewProfileValidation.validate();
        if (errors.isEmpty()) {
            Account account = accountRepo.findById(request.getId()).orElse(null);
            assert account != null;
            return ViewProfileResponse.builder()
                    .status("200")
                    .message("View profile successfully")
                    .email(account.getEmail())
                    .role(account.getRole())
                    .accountStatus(account.getStatus())
                    .build();
        }
        return errors;
    }

//-------------------------------UPDATE PROFILE-------------------------------------//

    @Override
    public String updateProfile(UpdateProfileRequest request, Model model) {
        Object output = updateProfileLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateProfileResponse.class)) {
            model.addAttribute("msg", (UpdateProfileResponse) output);
            return "myAccount";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "myAccount";
    }

    @Override
    public UpdateProfileResponse updateProfileAPI(UpdateProfileRequest request) {
        Object output = updateProfileLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateProfileResponse.class)) {
            return (UpdateProfileResponse) output;
        }
        return UpdateProfileResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object updateProfileLogic(UpdateProfileRequest request) {
        Map<String, String> errors = UpdateProfileValidation.validate(request);
        if (errors.isEmpty()) {
            Account account = accountRepo.findById(request.getId()).orElse(null);
            assert account != null;
            User user = account.getUser();
            user.setName(request.getName());
            user.setPhone(request.getPhone());
            user.setAvatar(request.getAvatar());
            user.setBackground(request.getBackground());
            userRepo.save(user);
            return UpdateProfileResponse.builder()
                    .status("200")
                    .message("Update profile successfully")
                    .build();
        }
        return errors;
    }

    //-------------------------------CHANGE PASSWORD-------------------------------------//

    @Override
    public String changePassword(ChangePasswordRequest request, Model model) {
        Object output = changePasswordLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangePasswordResponse.class)) {
            model.addAttribute("msg", (ChangePasswordResponse) output);
            return "login";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "changePassword";
    }

    @Override
    public ChangePasswordResponse changePasswordAPI(ChangePasswordRequest request) {
        Object output = changePasswordLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangePasswordResponse.class)) {
            return (ChangePasswordResponse) output;
        }
        return ChangePasswordResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object changePasswordLogic(ChangePasswordRequest request) {
        Map<String, String> errors = ChangePasswordValidation.validate(request);
        if (errors.isEmpty()) {
            Account account = accountRepo.findById(request.getId()).orElse(null);
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

    //-------------------------------LOG OUT-------------------------------------//

    @Override
    public String logout(HttpSession session) {
        if (session.getAttribute("acc") != null) {
            session.invalidate();
            return "home`````````````````````````````````````````                                                                                                                                                                                                                                                   ";
        }
        return "home";
    }

}


