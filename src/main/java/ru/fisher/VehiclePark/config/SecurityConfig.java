package ru.fisher.VehiclePark.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.fisher.VehiclePark.services.ManagerDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final ManagerDetailsService managerDetailsService;
    //private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(ManagerDetailsService managerDetailsService) {
        this.managerDetailsService = managerDetailsService;
    }

    // Конфигурация цепочки фильтров безопасности
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("admin/**").hasRole("ADMIN")
                        .requestMatchers("api/managers", "api/**", "managers/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()  // Разрешаем доступ к страницам логина и регистрации всем
                        .anyRequest().authenticated()) // Остальные запросы требуют аутентификации
                .formLogin(form -> form
                        .loginPage("/auth/login")  // Кастомная страница логина
                        .loginProcessingUrl("/process_login")  // URL для обработки формы логина
                        //.defaultSuccessUrl("/vehicles", true)  // Редирект после успешного логина
                        .defaultSuccessUrl("/managers/enterprises", true)
//                        .defaultSuccessUrl("/api/managers", true)
                        .failureUrl("/auth/login?error")  // Страница при ошибке логина
                        .permitAll())  // Разрешаем доступ ко всем ресурсам, связанным с логином
                .logout(l -> l  // Настройки для выхода из системы по умолчанию
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));
                http.httpBasic(Customizer.withDefaults());
                //.sessionManagement(Customizer.withDefaults());

        //http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Конфигурация менеджера аутентификации с использованием кастомного `ManagerDetailsService`
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(managerDetailsService)
                .passwordEncoder(getPasswordEncoder());  // Шифрование паролей
        return authenticationManagerBuilder.build();
    }

    // Настройка PasswordEncoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
