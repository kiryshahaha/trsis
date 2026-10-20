package ru.spbguap.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Конфигурация Spring Security для Лабораторной работы №4.
 *
 * Реализует задания методички (стр. 47–50):
 *  2. Переведите веб-сервер на использование SSL  → application.properties
 *  3. Реализуйте form-based authentication        → formLogin()
 *  4. Защитите ресурсы, требующие запись + аудит  → .hasRole("ADMIN") + AuditService
 *  5. Форма должна содержать кнопку Logout        → logout() + logoutForm в шаблоне
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Правила доступа к ресурсам — form-based authentication (методичка стр. 48).
     *
     * Публично доступны: /login, /css/**, /js/** и GET-методы REST API (чтение).
     * Запись (POST/PUT/DELETE) → только роль ADMIN.
     * Остальное → любой аутентифицированный пользователь.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Открытые ресурсы: CSS, JS, страница логина
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                // GET к REST API — доступен аутентифицированным пользователям
                .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/publishers", "/api/publishers/**").authenticated()
                // Запись (POST/PUT/DELETE) — только ADMIN (защита ресурсов, п.4 задания)
                .requestMatchers(HttpMethod.POST, "/api/books/**", "/api/publishers/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/publishers/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/books/**", "/api/publishers/**").hasRole("ADMIN")
                // Журнал аудита — только ADMIN
                .requestMatchers("/api/audit/**").hasRole("ADMIN")
                // Swagger и H2 консоль
                .requestMatchers("/swagger-ui/**", "/api-docs/**").authenticated()
                .requestMatchers("/h2-console/**").hasRole("ADMIN")
                // Главная страница — любой аутентифицированный
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            // form-based authentication (методичка стр. 48)
            .formLogin(form -> form
                .loginPage("/login")           // наша кастомная форма логина
                .usernameParameter("login")    // имя поля в форме (методичка стр. 49)
                .passwordParameter("pass")     // имя поля в форме (методичка стр. 49)
                .permitAll()
                .defaultSuccessUrl("/books", true)
            )
            // Кнопка Logout (методичка стр. 50): POST /logout → redirect /login?logout
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // CSRF включён (Thymeleaf автоматически добавляет скрытый токен в формы)
            // Для AJAX/AngularJS передаём токен через заголовок (методичка стр. 50)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**")  // REST API и H2 без CSRF
            );

        return http.build();
    }

    /**
     * In-memory аутентификация с BCrypt (методичка стр. 47, 49).
     * user  — роль USER  (только чтение)
     * admin — роль ADMIN (чтение + запись)
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        var admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    /** BCryptPasswordEncoder — рекомендован методичкой (стр. 47). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
