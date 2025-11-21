package com.hendisantika.config;

import com.hendisantika.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration using MongoDB User table
 * Uses plaintext password (no hashing)
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PlaintextPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/login", "/register", "/public/register", "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/favicon.ico")
                        .permitAll()
                        // Doctor management - ADMIN only
                        .requestMatchers("/doctors/**").hasRole("ADMIN")
                        // Patient management - ADMIN or PATIENT (can view own)
                        .requestMatchers("/patients/**").hasAnyRole("ADMIN", "PATIENT")
                        // Appointments and Calendar - all authenticated roles
                        .requestMatchers("/appointments/**", "/calendar/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        // Any other request requires authentication
                        .anyRequest().authenticated())
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout");
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**", "/favicon.ico", "/assets/**", "/css/**", "/img" +
                "/**", "/js**", "/admin/**", "/webjars/**", "/templates/**");
    }
}
