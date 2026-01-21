package ecommerce.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Getter @Setter
public class LoginRequest {
    private String email;       // or username
    private String password;
}
