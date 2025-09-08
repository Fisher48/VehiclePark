package ru.fisher.VehiclePark.controllers;//package ru.fisher.VehiclePark.controllers;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//import ru.fisher.VehiclePark.dto.AuthenticationDTO;
//import ru.fisher.VehiclePark.models.Manager;
//import ru.fisher.VehiclePark.services.reactive.RegistrationReactiveService;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class ReactiveAuthController {
//
//    private final RegistrationReactiveService registrationService;
//    private final JWTUtil jwtUtil;
//    private final ReactiveAuthenticationManager authenticationManager;
//
//    @PostMapping("/registration")
//    public Mono<ResponseEntity<Map<String, String>>> register(@RequestBody Manager manager) {
//        return registrationService.register(manager)
//                .thenReturn(ResponseEntity.ok(Map.of("message", "Registered successfully")));
//    }
//
//    @PostMapping("/login")
//    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody AuthenticationDTO dto) {
//        UsernamePasswordAuthenticationToken authToken =
//                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
//
//        return authenticationManager.authenticate(authToken)
//                .map(auth -> jwtUtil.generateToken(dto.getUsername()))
//                .map(token -> ResponseEntity.ok(Map.of("jwt-token", token)))
//                .onErrorResume(e -> Mono.just(ResponseEntity
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .body(Map.of("message", "Invalid credentials"))));
//    }
//}
