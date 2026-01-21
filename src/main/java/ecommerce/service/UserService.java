package ecommerce.service;

import ecommerce.entity.Cart;
import ecommerce.entity.User;
import ecommerce.exception.UserNotFoundException;
import ecommerce.repository.CartRepository;
import ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    /* ================= AUTH / REGISTER ================= */

    public User registerUser(User user) {
        // NOTE: password encoding + role assignment
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        // 🛒 Create empty cart for user
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        return savedUser;

    }

    /* ================= USER ================= */

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + userId));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + email));
    }

    /* ================= ADMIN ================= */

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public long countUsersByRole(String role) {
        return userRepository.countByRole(role);
    }

    /* ================= DELETE ================= */

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);

        // Delete cart first (FK safety)
        cartRepository.findByUserId(userId)
                .ifPresent(cartRepository::delete);

        userRepository.delete(user);
    }
}
