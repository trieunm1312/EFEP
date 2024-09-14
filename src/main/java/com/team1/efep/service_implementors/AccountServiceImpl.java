package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.models.response_models.LoginResponse;
import com.team1.efep.models.response_models.RegisterResponse;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.UserRepo;
import com.team1.efep.services.AccountService;
import com.team1.efep.validations.RegisterValidation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {


    private final AccountRepo accountRepo;

    private final UserRepo userRepo;

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

        if(!request.getPassword().equals(request.getConfirmPassword())){
            if(!error.isEmpty()) {
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
        String[] fields = error.trim().substring(0, error.length() - 1).split("\\,");
        if (fields.length == 1) {
            verbBe = " is existed";
        } else {
            verbBe = " are existed";
        }

        return error + verbBe;
    }

    private void createNewBuyer(RegisterRequest request) {
        userRepo.save(
                User.builder()
                        .account(createNewAccount(request))
                        .name(request.getName())
                        .phone(request.getPhone())
                        .avatar(request.getAvatar())
                        .background(request.getBackground())
                        .build()
        );
    }

    private Account createNewAccount(RegisterRequest request) {
        return accountRepo.save(
                Account.builder()
                        .accountStatus(null)
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
            return "register";
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
}
