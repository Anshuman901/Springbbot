package ecommerce;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevController {

    @GetMapping("/dev")
    public String devTest() {
        return "Hello from DEV Branch 🚀";
    }
}
