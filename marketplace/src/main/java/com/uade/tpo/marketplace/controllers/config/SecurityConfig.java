package com.uade.tpo.marketplace.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers("/api/v1/auth/**")
                                                .permitAll()

                                                //CATEGORIES
                                                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/categories/**").hasRole("ADMIN")

                                                //DIGITAL KEYS
                                                .requestMatchers(HttpMethod.POST, "/digital_keys").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.GET, "/digital_keys/product/*/count").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/digital_keys/product/**").hasAnyRole("SELLER", "ADMIN")

                                                //DISCOUNTS
                                                .requestMatchers(HttpMethod.POST, "/discounts/validate", "/discounts/validate/bulk")
                                                        .hasAnyRole("BUYER", "SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/discounts/buyer/active-coupons").hasAnyRole("BUYER", "SELLER")
                                                .requestMatchers(HttpMethod.GET, "/discounts/product/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/discounts").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/discounts/**").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/discounts/**").hasRole("ADMIN")
                                                
                                                //ORDERS
                                                .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("BUYER", "SELLER")
                                                .requestMatchers(HttpMethod.GET, "/orders/my").hasAnyRole("BUYER", "SELLER")
                                                .requestMatchers(HttpMethod.GET, "/orders/seller/**").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.PATCH, "/orders/*/complete").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PATCH, "/orders/*/status").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/orders/**").authenticated()

                                                //PRODUCT IMAGES
                                                .requestMatchers(HttpMethod.GET, "/product_images/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/product_images").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/product_images/**").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.PATCH, "/product_images/*/primary").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/product_images/**").hasAnyRole("SELLER", "ADMIN")

                                                //PRODUCTS
                                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/products/**").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.PATCH, "/products/**").hasRole("SELLER")

                                                //REVIEWS
                                                .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/reviews").hasAnyRole("BUYER", "SELLER")
                                                .requestMatchers(HttpMethod.PUT, "/reviews/**").hasAnyRole("BUYER", "SELLER")
                                                .requestMatchers(HttpMethod.DELETE, "/reviews/**").hasAnyRole("BUYER", "SELLER")

                                                //USERS
                                                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/users/**").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/users/me/profile").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/users/seller/*/profile").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/users/**").authenticated()
                                                .requestMatchers(HttpMethod.PATCH, "/users/me/balance").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/users/*/avatar").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/users/*/avatar").authenticated()
                                                .requestMatchers(HttpMethod.DELETE, "/users/*/avatar").authenticated()





                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
