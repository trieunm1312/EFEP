package com.team1.efep.service_implementors;

import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Cart;
import com.team1.efep.models.entity_models.CartItem;
import com.team1.efep.models.entity_models.Flower;
import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.request_models.ViewCartRequest;
import com.team1.efep.models.response_models.AddToCartResponse;
import com.team1.efep.models.response_models.ViewCartResponse;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.CartRepo;
import com.team1.efep.repositories.FlowerRepo;
import com.team1.efep.services.BuyerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final CartRepo cartRepo;

    private final FlowerRepo flowerRepo;

    private final AccountRepo accountRepo;


    //---------------------------------------VIEW CART------------------------------------------//
    @Override
    public String viewCart(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        model.addAttribute("cart", viewCartItemList(account.getId()));
        return "cart";
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
        }else{
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

}
