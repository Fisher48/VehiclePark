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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.fisher.VehiclePark.services.ManagerDetailsService;
import ru.fisher.VehiclePark.services.PersonDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;
    //private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

//    @Bean
//    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(managerDetailsService);
//        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
//        //http.csrf(AbstractHttpConfigurer::disable);
//        http.authenticationManager(authenticationManager)
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/auth/login", "/auth/registration").permitAll()
//                        .anyRequest().authenticated()
//                );
//        // http.httpBasic(Customizer.withDefaults());
//        http.formLogin((formLogin) -> formLogin
//                        .loginPage("/auth/login")
//                        .loginProcessingUrl("/process_login")
//                        .defaultSuccessUrl("/api/managers", true)
//                        .failureUrl("/auth/login?error")
//                        .permitAll()
//        );
//        http.logout((logout) -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/auth/login")
//        );
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        // Настройка для Stateless сессий
//        http.sessionManagement(Customizer.withDefaults());
//        return http.build();
//    }

    // Конфигурация цепочки фильтров безопасности
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("api/managers/1/**").hasRole("MANAGER1")
                        .requestMatchers("api/managers/2/**").hasRole("MANAGER2")
                        .requestMatchers("api/managers", "api/**", "managers/**").hasAnyRole("MANAGER1", "MANAGER2")
                        .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()  // Разрешаем доступ к страницам логина и регистрации всем
                        .anyRequest().authenticated()) // Остальные запросы требуют аутентификации
                .formLogin(form -> form
                        .loginPage("/auth/login")  // Кастомная страница логина
                        .loginProcessingUrl("/process_login")  // URL для обработки формы логина
                        //.defaultSuccessUrl("/vehicles", true)  // Редирект после успешного логина
                        .defaultSuccessUrl("/managers/enterprises", true)
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
                .userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());  // Шифрование паролей
        return authenticationManagerBuilder.build();
    }

    // Настройка PasswordEncoder (временно NoOp для простоты, рекомендуется заменить на BCrypt в продакшн)
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();  // Временно отключаем шифрование (только для разработки)
    }

}
