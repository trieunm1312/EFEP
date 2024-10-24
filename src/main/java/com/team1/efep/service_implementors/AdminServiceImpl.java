package com.team1.efep.service_implementors;

import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.response_models.ViewBusinessServiceResponse;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.AdminService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.FileReaderUtil;
import com.team1.efep.utils.OTPGeneratorUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final BusinessServiceRepo businessServiceRepo;

    private final BusinessPlanRepo businessPlanRepo;

    private final PlanServiceRepo planServiceRepo;

    private final AccountRepo accountRepo;

    private final UserRepo userRepo;

    private final SellerRepo sellerRepo;

    private final JavaMailSenderImpl mailSender;

    //-------------------------------------VIEW BUSINESS PLAN----------------------------//
    @Override
    public String viewBusinessPlan(HttpSession session, Model model) {
        model.addAttribute("msg", viewBusinessPlanLogic());
        return "manageBusinessPlan";
    }

    @Override
    public ViewBusinessPlanResponse viewBusinessPlanAPI() {
        return viewBusinessPlanLogic();
    }

    private ViewBusinessPlanResponse viewBusinessPlanLogic() {
        return ViewBusinessPlanResponse.builder()
                .status("200")
                .message("")
                .serviceList(
                        businessServiceRepo.findAll()
                                .stream()
                                .map(
                                        service -> ViewBusinessPlanResponse.BusinessService.builder()
                                                .id(service.getId())
                                                .name(service.getName())
                                                .description(service.getDescription())
                                                .price(service.getPrice())
                                                .build()
                                )
                                .toList()
                )
                .businessPlanList(
                        businessPlanRepo.findAll()
                                .stream()
                                .map(
                                        plan -> ViewBusinessPlanResponse.BusinessPlan.builder()
                                                .id(plan.getId())
                                                .name(plan.getName())
                                                .description(plan.getDescription())
                                                .price(plan.getPrice())
                                                .duration(plan.getDuration())
                                                .status(plan.getStatus())
                                                .businessServiceList(plan.getPlanServiceList().stream()
                                                        .map(service -> ViewBusinessPlanResponse.BusinessService.builder()
                                                                .id(service.getBusinessService().getId())
                                                                .name(service.getBusinessService().getName())
                                                                .description(service.getBusinessService().getDescription())
                                                                .price(service.getBusinessService().getPrice())
                                                                .build()
                                                        )
                                                        .toList())
                                                .build()
                                )
                                .toList())
                .build();

    }

    //-------------------------------------CREATE BUSINESS PLAN------------------------------------------//
    @Override
    public String createBusinessPlan(CreateBusinessPlanRequest request, Model model) {
        Object output = createBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateBusinessPlanResponse.class)) {
            model.addAttribute("msg", (CreateBusinessPlanResponse) output);
            return "redirect:/admin/view/plan";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public CreateBusinessPlanResponse createBusinessPlanAPI(CreateBusinessPlanRequest request) {
        Object output = createBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateBusinessPlanResponse.class)) {
            return (CreateBusinessPlanResponse) output;
        }

        return CreateBusinessPlanResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object createBusinessPlanLogic(CreateBusinessPlanRequest request) {
        Map<String, String> errors = CreateBusinessPlanValidation.validate(request);
        if (errors.isEmpty()) {
            //success
            BusinessPlan businessPlan = businessPlanRepo.save(BusinessPlan.builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .description(request.getDescription())
                    .duration(request.getDuration())
                    .planServiceList(new ArrayList<>())
                    .build()
            );
            return CreateBusinessPlanResponse.builder()
                    .status("200")
                    .message("Created business plan successfully")
                    .name(request.getName())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .duration(request.getDuration())
                    .plansStatus(Status.BUSINESS_PLAN_STATUS_ACTIVE)
                    .businessServiceList(
                            importPlanService(request, businessPlan).stream()
                                    .map(
                                            ps -> CreateBusinessPlanResponse.BusinessService.builder()
                                                    .id(ps.getBusinessService().getId())
                                                    .name(ps.getBusinessService().getName())
                                                    .build()
                                    )
                                    .toList()
                    )
                    .build();
        }
        return errors;
    }

    private List<PlanService> importPlanService(CreateBusinessPlanRequest request, BusinessPlan businessPlan) {
        return planServiceRepo.saveAll(
                businessServiceRepo.findAll().stream()
                        .filter(  //chuyen no sang Integer
                                service -> request.getBusinessServiceList().stream()
                                        .map(s -> Integer.valueOf(s.getId()))
                                        .toList()
                                        .contains(service.getId())
                        )
                        .map(
                                service -> PlanService.builder()
                                        .businessPlan(businessPlan)
                                        .businessService(service)
                                        .build()
                        ).toList()
        );
    }

    //-------------------------------------UPDATE BUSINESS PLAN------------------------------------------//

    @Override
    public String updateBusinessPlan(UpdateBusinessPlanRequest request, Model model) {
        Object output = updateBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateBusinessPlanResponse.class)) {
            model.addAttribute("msg", (UpdateBusinessPlanResponse) output);
            return "redirect:/admin/view/plan";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";

    }

    @Override
    public UpdateBusinessPlanResponse updateBusinessPlanAPI(UpdateBusinessPlanRequest request) {
        Object output = updateBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateBusinessPlanResponse.class)) {
            return (UpdateBusinessPlanResponse) output;
        }
        return UpdateBusinessPlanResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object updateBusinessPlanLogic(UpdateBusinessPlanRequest request) {
        Map<String, String> errors = UpdateBusinessPlanValidation.validate(request, businessPlanRepo, businessServiceRepo);
        if (errors.isEmpty()) {
            BusinessPlan businessPlan = businessPlanRepo.findById(request.getId()).orElse(null);
            assert businessPlan != null;
            businessPlan.setName(request.getName());
            businessPlan.setPrice(request.getPrice());
            businessPlan.setDescription(request.getDescription());
            businessPlan.setDuration(request.getDuration());
            return UpdateBusinessPlanResponse.builder()
                    .status("200")
                    .message("Updated business plan successfully")
                    .name(request.getName())
                    .price(request.getPrice())
                    .description(request.getDescription())
                    .duration(request.getDuration())
                    .businessServiceList(updatePlanService(request, businessPlan).stream()
                            .map(ps -> UpdateBusinessPlanResponse.BusinessService.builder()
                                    .id(ps.getBusinessService().getId())
                                    .name(ps.getBusinessService().getName())
                                    .build())
                            .toList())
                    .build();
        }
        return errors;
    }

    private List<PlanService> updatePlanService(UpdateBusinessPlanRequest request, BusinessPlan businessPlan) {
        //ko co status moi dc xoa trắng để tạo lại thằng mới
        planServiceRepo.deleteAll(
                planServiceRepo.findAll().stream().filter(ps -> ps.getBusinessPlan().equals(businessPlan)).toList()
        );
        return planServiceRepo.saveAll(
                businessServiceRepo.findAll().stream()
                        .filter(  //chuyen no sang Integer
                                service -> request.getBusinessServiceList().stream()

                                        .toList()
                                        .contains(service.getId())
                        )
                        .map(
                                service -> PlanService.builder()
                                        .businessPlan(businessPlan)
                                        .businessService(service)
                                        .build()
                        ).toList()
        );
    }

    private Object sendEmailLogic(BusinessPlan businessPlan, User user) {
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
//            helper.setTo(request.getToEmail());
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

    //-------------------------------------DISABLE BUSINESS PLAN------------------------------------------//

    @Override
    public String disableBusinessPlan(DisableBusinessPlanRequest request, Model model) {
        Object output = disableBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DisableBusinessPlanResponse.class)) {
            model.addAttribute("msg", (DisableBusinessPlanResponse) output);
            return "redirect:/admin/view/plan";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public DisableBusinessPlanResponse disableBusinessPlanAPI(DisableBusinessPlanRequest request) {
        Object output = disableBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DisableBusinessPlanResponse.class)) {
            return (DisableBusinessPlanResponse) output;
        }
        return DisableBusinessPlanResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object disableBusinessPlanLogic(DisableBusinessPlanRequest request) {
        Map<String, String> errors = DisableBusinessPlanValidation.validate(request, businessPlanRepo);
        if (errors.isEmpty()) {
            BusinessPlan businessPlan = businessPlanRepo.findById(request.getId()).orElse(null);
            assert businessPlan != null;
            businessPlan.setStatus(Status.BUSINESS_PLAN_STATUS_DISABLED);
            businessPlanRepo.save(businessPlan);
            return DisableBusinessPlanResponse.builder()
                    .status("200")
                    .message("Disabled successfully")
                    .build();
        }
        return errors;
    }

    //-------------------------------------VIEW BUSINESS SERVICE----------------------------//

    @Override
    public String viewBusinessService(HttpSession session, Model model) {
        model.addAttribute("msg", viewBusinessServiceLogic());
        return "manageBusinessService";
    }

    @Override
    public ViewBusinessServiceResponse viewBusinessServiceAPI() {
        return viewBusinessServiceLogic();
    }

    private ViewBusinessServiceResponse viewBusinessServiceLogic() {
        return ViewBusinessServiceResponse.builder()
                .status("200")
                .message("")
                .servicesList(
                        businessServiceRepo.findAll()
                                .stream()
                                .map(
                                        service -> ViewBusinessServiceResponse.Services.builder()
                                                .id(service.getId())
                                                .name(service.getName())
                                                .description(service.getDescription())
                                                .price(service.getPrice())
                                                .build()
                                )
                                .toList()
                )
                .build();

    }


    //-------------------------------------CREATE BUSINESS SERVICE----------------------------//
    @Override
    public String createBusinessService(CreateBusinessServiceRequest request, Model model) {
        Object output = createBusinessServiceLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateBusinessServiceResponse.class)) {
            model.addAttribute("msg", (CreateBusinessServiceResponse) output);
            return "redirect:/admin/view/service";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public CreateBusinessServiceResponse createBusinessServiceAPI(CreateBusinessServiceRequest request) {
        Object output = createBusinessServiceLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateBusinessServiceResponse.class)) {
            return (CreateBusinessServiceResponse) output;
        }
        return CreateBusinessServiceResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object createBusinessServiceLogic(CreateBusinessServiceRequest request) {
        Map<String, String> errors = CreateBusinessServiceValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }
        businessServiceRepo.save(BusinessService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build());
        return CreateBusinessServiceResponse.builder()
                .status("200")
                .message("Created business service successfully")
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }

    //-------------------------------------UPDATE BUSINESS SERVICE----------------------------//

    @Override
    public String updateBusinessService(UpdateBusinessServiceRequest request, Model model) {
        Object output = updateBusinessServiceLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateBusinessServiceResponse.class)) {
            model.addAttribute("msg", (UpdateBusinessServiceResponse) output);
            return "redirect:/admin/view/service";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public UpdateBusinessServiceResponse updateBusinessServiceAPI(UpdateBusinessServiceRequest request) {
        Object output = updateBusinessServiceLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateBusinessServiceResponse.class)) {
            return (UpdateBusinessServiceResponse) output;
        }
        return UpdateBusinessServiceResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object updateBusinessServiceLogic(UpdateBusinessServiceRequest request) {
        Map<String, String> errors = UpdateBusinessServiceValidation.validate(request, businessServiceRepo);
        if (!errors.isEmpty()) {
            return errors;
        }
        BusinessService businessService = businessServiceRepo.findById(request.getId()).orElse(null);
        assert businessService != null;
        businessService.setName(request.getName());
        businessService.setDescription(request.getDescription());
        businessService.setPrice(request.getPrice());
        businessServiceRepo.save(businessService);
        return UpdateBusinessServiceResponse.builder()
                .status("200")
                .message("Updated business service successfully")
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }
    //-------------------------------------DELETE BUSINESS SERVICE----------------------------//

    @Override
    public String deleteBusinessService(DeleteBusinessServiceRequest request, Model model) {
        Object output = deleteBusinessServiceLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteBusinessServiceResponse.class)) {
            model.addAttribute("msg", (DeleteBusinessServiceResponse) output);
            return "redirect:/admin/view/service";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public DeleteBusinessServiceResponse deleteBusinessServiceAPI(DeleteBusinessServiceRequest request) {
        Object output = deleteBusinessServiceLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteBusinessServiceResponse.class)) {
            return (DeleteBusinessServiceResponse) output;
        }
        return DeleteBusinessServiceResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object deleteBusinessServiceLogic(DeleteBusinessServiceRequest request) {
        Map<String, String> errors = DeleteBusinessServiceValidation.validate(request, businessServiceRepo);
        if (!errors.isEmpty()) {
            return errors;
        }
        BusinessService businessService = businessServiceRepo.findById(request.getId()).orElse(null);
        assert businessService != null;
        businessServiceRepo.delete(businessService);
        return DeleteBusinessServiceResponse.builder()
                .status("200")
                .message("Deleted business service successfully")
                .build();
    }


    //-------------------------------------VIEW USER LIST----------------------------//

    @Override
    public String viewUserList(HttpSession session, Model model) {
        model.addAttribute("msg", viewUserListLogic());
        return "manageUser";
    }

    @Override
    public ViewUserListResponse viewUserListAPI() {
        return viewUserListLogic();
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
                                                .background(user.getBackground())
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
        model.addAttribute("msg", searchUserListLogic(request));
        return "manageUser";
    }

    @Override
    public SearchUserListResponse searchUserListAPI(SearchUserListRequest request) {
        return searchUserListLogic(request);
    }


    private SearchUserListResponse searchUserListLogic(SearchUserListRequest request) {
        return SearchUserListResponse.builder()
                .status("200")
                .message("")
                .userList(
                        userRepo.findAll().stream()
                                .filter(user -> user.getName().toLowerCase().contains(request.getKeyword()))
                                .map(
                                        user -> SearchUserListResponse.User.builder()
                                                .id(user.getId())
                                                .name(user.getName())
                                                .phone(user.getPhone())
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
    public String banUser(BanUserRequest request, Model model) {
        Object output = banUserLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, BanUserResponse.class)) {
            model.addAttribute("msg", (BanUserResponse) output);
            return "redirect:/admin/user/list";
        }
        model.addAttribute("error", ((Map<String, String>) output));
        return "home";
    }

    @Override
    public BanUserResponse banUserAPI(BanUserRequest request) {
        Object output = banUserLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, BanUserResponse.class)) {
            return (BanUserResponse) output;
        }
        return BanUserResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
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
    public String unBanUser(UnBanUserRequest request, Model model) {
        Object output = unBanUserLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UnBanUserResponse.class)) {
            model.addAttribute("msg", (UnBanUserResponse) output);
            return "redirect:/admin/user/list";
        }
        model.addAttribute("error", ((Map<String, String>) output));
        return "home";
    }

    @Override
    public UnBanUserResponse unBanUserAPI(UnBanUserRequest request) {
        Object output = unBanUserLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UnBanUserResponse.class)) {
            return (UnBanUserResponse) output;
        }
        return UnBanUserResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
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
    public String createAccountForSeller(CreateAccountForSellerRequest request, Model model) {
        Object output = createAccountForSellerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateAccountForSellerResponse.class)) {
            model.addAttribute("msg", (CreateAccountForSellerResponse) output);
            return "redirect:/admin/user/list";
        }
        model.addAttribute("error", ((Map<String, String>) output));
        return "home";
    }

    @Override
    public CreateAccountForSellerResponse createAccountForSellerAPI(CreateAccountForSellerRequest request) {
        Object output = createAccountForSellerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateAccountForSellerResponse.class)) {
            return (CreateAccountForSellerResponse) output;
        }
        return CreateAccountForSellerResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object createAccountForSellerLogic(CreateAccountForSellerRequest request) {
        Map<String, String> errors = CreateAccountForSellerValidation.validate(request, accountRepo);

        if (!errors.isEmpty()) {
            return errors;
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
                        .build()
        );
    }

}

