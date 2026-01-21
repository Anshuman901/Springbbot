package ecommerce.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter @Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
}