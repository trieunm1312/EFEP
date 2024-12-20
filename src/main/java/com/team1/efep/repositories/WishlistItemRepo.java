package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepo extends JpaRepository<WishlistItem, Integer> {

    Optional<WishlistItem> findByFlower_IdAndWishlist_Id(int flowerId, int wishlistId);

//    Optional<WishlistItem> findByFlower_IdAndWishlist_Id(int flowerId, int wishlistId);

    List<WishlistItem> findAllByFlower_Id(int flowerId);

    List<WishlistItem> findAllByWishlist_User_Id(int userId);

    Optional<WishlistItem> findByFlower_IdAndWishlist_User_Id(int flowerId, int userId);
}
