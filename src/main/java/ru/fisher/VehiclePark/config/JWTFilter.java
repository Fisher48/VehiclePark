//package ru.fisher.VehiclePark.config;
//
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import ru.fisher.VehiclePark.services.ManagerDetailsService;
//import ru.fisher.VehiclePark.util.JWTUtil;
//
//import java.io.IOException;
//
//@Component
//public class JWTFilter extends OncePerRequestFilter {
//
//    private final JWTUtil jwtUtil;
//    private final ManagerDetailsService managerDetailsService;
//
//    @Autowired
//    public JWTFilter(JWTUtil jwtUtil, ManagerDetailsService managerDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.managerDetailsService = managerDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
//            String jwt = authHeader.substring(7);
//
//            if (jwt.isBlank()) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
//                        "Invalid JWT Token in Bearer Header");
//            } else {
//                try {
//                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
//                    UserDetails userDetails = managerDetailsService.loadUserByUsername(username);
//
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    userDetails.getPassword(),
//                                    userDetails.getAuthorities());
//
//                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                } catch (JWTVerificationException exc) {
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
//                            "Invalid JWT Token");
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
