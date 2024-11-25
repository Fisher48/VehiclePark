package ru.fisher.VehiclePark.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.AuthenticationDTO;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.services.RegistrationService;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;

//    private final JWTUtil jwtUtil;
//
//    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("manager") Manager manager) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("manager") Manager manager,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        registrationService.register(manager);
        return "redirect:/auth/login";
    }

//    @ResponseBody
//    @PostMapping("/registration")
//    public Map<String, String> performRegistration(@RequestBody Manager manager,
//                                                   BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return Map.of("message", "Ошибка");
//        }
//
//        registrationService.register(manager);
//
//        String token = jwtUtil.generateToken(manager.getUsername());
//
//        return Map.of("jwt-token", token);
//    }
//
//    @PostMapping("/login")
//    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
//        UsernamePasswordAuthenticationToken authInputToken =
//                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
//                        authenticationDTO.getPassword());
//
//        try {
//            authenticationManager.authenticate(authInputToken);
//        } catch (BadCredentialsException e) {
//            return Map.of("message", "Incorrect credential");
//        }
//
//        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
//        return Map.of("jwt-token", token);
//    }

}
