package com.team1.efep.service_implementors;

import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Flower;
import com.team1.efep.models.entity_models.FlowerImage;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.FlowerImageRepo;
import com.team1.efep.repositories.FlowerRepo;
import com.team1.efep.repositories.FlowerStatusRepo;
import com.team1.efep.services.SellerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.CreateFlowerValidation;
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

    private final FlowerStatusRepo flowerStatusRepo;

    private final FlowerImageRepo flowerImageRepo;
    private final AccountRepo accountRepo;


    //--------------------------------CREATE FLOWER------------------------------------//
//    @Override
//    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model) {
//        Account account = (Account) session.getAttribute("acc");
//        if (!Role.checkIfThisAccountIsSeller(account)) {
//            model.addAttribute("error", "You are not seller");
//            return "seller";
//        }
//
//        String error = createFlowerLogic(request);
//        if (!error.isEmpty()) {
//            model.addAttribute("error", CreateFlowerResponse.builder()
//                    .status("400")
//                    .message(error)
//                    .build());
//            return "flower";
//        }
//
//        model.addAttribute("flower", createFlowerLogic(request));
//        return "flower";
//    }
//
//    @Override
//    public CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request) {
//        if(!createFlowerLogic(request).isEmpty()){
//            return CreateFlowerResponse.builder()
//                    .status("400")
//                    .message(createFlowerLogic(request))
//                    .build();
//        }
//        return CreateFlowerResponse.builder()
//                .status("200")
//                .message("create flower success")
//                .build();
//    }
//
//    private String createFlowerLogic(CreateFlowerRequest request) {
//        String error = validateInput(request);
//
//        if (error.isEmpty()) {
//            createNewFlower(request);
//        }
//
//        return error;
//    }
//
//    private String validateInput(CreateFlowerRequest request) {
//        String error = "";
//        if(flowerRepo.findByName(request.getName()).isPresent()){
//            error = "Flower name is existed, ";
//        }
//        if (request.getName() == null || request.getName().trim().isEmpty()) {
//            error = "Flower name is empty, ";
//        }
//        if (request.getName().length() < 3 || request.getName().length() > 30) {
//            error = "Flower name must be between 3 and 30 characters, ";
//        }
//
//        if (request.getPrice() <= 0) {
//            error = "Price must be numeric greater than 0, ";
//        }
//
//        if (request.getFlowerAmount() <= 0) {
//            error = "Flower amount must be numeric greater than 0, ";
//        }
//
//        if (request.getQuantity() < 0) {
//            error = "Quantity must be numeric greater than 0, ";
//        }
//
//        if (!error.isEmpty()) {
//            error = formatErrorMsg(error);
//        }
//
//        return error;
//    }
//
//    private String formatErrorMsg(String error) {
//        error = error.substring(0, 1).toUpperCase() + error.trim().substring(1, error.length() - 2);
//        return error;
//    }
//
//    private void createNewFlower(CreateFlowerRequest request) {
//        Flower flower = Flower.builder()
//                .name(request.getName())
//                .price(request.getPrice())
//                .rating(0)
//                .description(request.getDescription())
//                .flowerAmount(request.getFlowerAmount())
//                .quantity(request.getQuantity())
//                .soldQuantity(0)
//                .flowerStatus(flowerStatusRepo.findByStatus(Const.FLOWER_STATUS_AVAILABLE))
//                .build();
//
//        List<FlowerImage> flowerImages = addFlowerImages(request, flower);
//        flowerImageRepo.saveAll(flowerImages);
//        flowerRepo.save(flower);
//    }
//
//    private List<FlowerImage> addFlowerImages(CreateFlowerRequest request, Flower flower) {
//        return request.getImgList().stream()
//                .map(link -> FlowerImage.builder()
//                        .flower(flower) // Gán đối tượng Flower vào FlowerImage
//                        .link(link) // Gán link của ảnh từ request
//                        .build())
//                .collect(Collectors.toList());
//    }

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
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFlowerResponse.class)){
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
                .flowerStatus(flowerStatusRepo.findByStatus(Const.FLOWER_STATUS_AVAILABLE))
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
}
