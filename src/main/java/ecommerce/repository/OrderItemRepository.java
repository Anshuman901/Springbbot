package ecommerce.repository;

import ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // 📊 PRODUCT SALES
    List<OrderItem> findByProductId(Long productId);

    // 📦 ORDER DETAILS
    List<OrderItem> findByOrderId(Long orderId);
}
