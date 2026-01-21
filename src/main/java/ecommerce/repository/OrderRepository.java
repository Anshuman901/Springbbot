package ecommerce.repository;

import ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 📜 USER HISTORY
    List<Order> findByUserId(Long userId);

    // 📦 STATUS BASED
    List<Order> findByStatus(String status);

    // ⏱ TIME BASED (REPORTING)
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}
