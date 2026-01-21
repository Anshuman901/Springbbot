package ecommerce.repository;

import ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  CartRepository extends JpaRepository<Cart,Long> {
    // 🛒 USER CART
    Optional<Cart> findByUserId(Long userId);

    // 🧹 CLEANUP
    void deleteByUserId(Long userId);
}
