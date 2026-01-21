package ecommerce.repository;

import ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 🛑 DUPLICATE ITEM AVOID
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    // 🧾 VIEW CART
    List<CartItem> findByCartId(Long cartId);

    // ❌ REMOVE ITEM
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
