package com.klpdapp.klpd.config;

import com.klpdapp.klpd.Security.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
        .userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login", "/submit", "/register", "/products", "/", "/search",
                                "/category/{categoryId}", "/css/**", "/js/**", "/images/**",
                                "/admin","/product/{pid}","/wholesaler","/CategoryImages/**","/SegmentImages/**",
                                "/ProductImages/**","/registerwholesale")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // only admin can access admin pages
                        .requestMatchers("/profile").authenticated() // only authenticated users can access profile
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/submit")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler(customAuthenticationFailureHandler())
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .failureHandler(customAuthenticationFailureHandler()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true"));
        return http.build();
    }

    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().setAttribute("errorMessage", "Invalid email or password!");
            response.sendRedirect("/login?error=true");
        };
    }

    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String Role = authentication.getAuthorities().iterator().next().getAuthority();

            if(Role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/dashboard");
            } else if (Role.equals("ROLE_CUSTOMER")) {
                response.sendRedirect("/profile");
            } else if (Role.equals("ROLE_WHOLESALER")) {
                response.sendRedirect("/profile");
            } 
            else {
                response.sendRedirect("/login?error=true");
            }
        };
    }
}
