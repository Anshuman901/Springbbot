package ecommerce.repository;

import ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔐 AUTH
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // 👮 ADMIN
    List<User> findByRole(String role);

    // 📊 ANALYTICS
    long countByRole(String role);
}
