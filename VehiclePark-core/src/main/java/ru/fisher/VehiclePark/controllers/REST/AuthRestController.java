package ru.fisher.VehiclePark.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.AuthenticationDTO;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.services.RegistrationService;
import ru.fisher.VehiclePark.util.JWTUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final RegistrationService registrationService;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthRestController(RegistrationService registrationService, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> performRegistration(@RequestBody Manager manager,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect credential"));
        }
        registrationService.register(manager);
        String token = jwtUtil.generateToken(manager.getUsername());
        return ResponseEntity.ok(Map.of("jwt-token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect credential"));
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return ResponseEntity.ok(Map.of("jwt-token", token));
    }
}
