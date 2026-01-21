package ecommerce.controller;

import ecommerce.entity.User;
import ecommerce.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🆕 Public API (Register)
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // 👤 USER can access
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 👑 ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
