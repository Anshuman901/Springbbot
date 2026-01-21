package ecommerce.repository;

import ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 🛑 DUPLICATE CHECK
    boolean existsByName(String name);

    // 🔍 SEARCH
    Optional<Product> findByName(String name);
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 📦 STOCK MANAGEMENT
    List<Product> findByStockGreaterThan(int quantity);
    List<Product> findByStockLessThan(int quantity);

    // 💰 PRICE FILTER
    List<Product> findByPriceBetween(double min, double max);
}
