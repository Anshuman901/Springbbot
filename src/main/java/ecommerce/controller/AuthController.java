package ecommerce.controller;

import ecommerce.dto.LoginRequest;
import ecommerce.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * LOGIN
     */


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        // 🔐 Authenticate + generate JWT
        String token = authService.login(request);

        // 🍪 Create HttpOnly cookie
        Cookie cookie = new Cookie("ACCESS_TOKEN", token);
        cookie.setHttpOnly(true);     // JS access disabled (XSS safe)
        cookie.setSecure(false);      // true in PROD (HTTPS)
        cookie.setPath("/");          // available for all APIs
        cookie.setMaxAge(60 * 60);    // 1 hour

        response.addCookie(cookie);

        return ResponseEntity.ok(
                Map.of("message", "Login successful")
        );
    }

    /**
     * LOGOUT
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        // ❌ Delete cookie
        Cookie cookie = new Cookie("ACCESS_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // expire immediately

        response.addCookie(cookie);

        return ResponseEntity.ok(
                Map.of("message", "Logged out successfully")
        );
    }
}
