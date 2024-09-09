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
        String msg = registerLogic(request);
        if (!msg.isEmpty()) {
            model.addAttribute("error", msg);
            return "register";
        }
        return "login";
    }

    @Override
    public RegisterResponse registerAPI(RegisterRequest request) {
        String msg = registerLogic(request);
        if (!msg.isEmpty()) {
            return RegisterResponse.builder()
                    .status("400")
                    .message(msg)
                    .build();
        }
        return RegisterResponse.builder()
                .status("200")
                .message("Register successfully")
                .build();

    }

    private String registerLogic(RegisterRequest request) {
        String msg = validateInput(request);

        if (msg.isEmpty()) {
            createNewBuyer(request);
        }

        return msg;
    }

    private String validateInput(RegisterRequest request) {
        String msg = "";
        if (accountRepo.findByEmail(request.getEmail()).isPresent()) {
            msg += "email, ";
        }

        if (userRepo.findByName(request.getName()).isPresent()) {
            msg += "name, ";
        }

        if (userRepo.findByPhone(request.getPhone()).isPresent()) {
            msg += "phone, ";
        }

        if (!msg.isEmpty()) {
            msg = formatMessage(msg);
        }

        if(!request.getPassword().equals(request.getConfirmPassword())){
            if(!msg.isEmpty()) {
                msg += ", confirm password is not matched";
            } else {
                msg += "Confirm password is not matched";
            }

        }

        return msg;
    }

    private String formatMessage(String msg) {
        msg = msg.substring(0, 1).toUpperCase() + msg.trim().substring(1, msg.length() - 2);

        String verbBe = "";
        String[] fields = msg.trim().substring(0, msg.length() - 1).split("\\,");
        if (fields.length == 1) {
            verbBe = " is existed";
        } else {
            verbBe = " are existed";
        }

        return msg + verbBe;
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
            model.addAttribute("error", "Invalid username or password");
            return "register";
        }
        session.setAttribute("acc", loggedAccount);
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
