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

                                                //CART
                                                .requestMatchers("/cart/**").hasRole("BUYER")

                                                //CATEGORIES
                                                .requestMatchers(HttpMethod.POST, "/categories/**").hasAnyRole("ADMIN") 
                                                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()

                                                //DIGITAL KEYS
                                                .requestMatchers(HttpMethod.GET, "/digital_keys/product/**/count").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/digital_keys/product/**").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/digital_keys").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.DELETE, "/digital_keys/**").hasRole("SELLER")

                                                //DISCOUNTS
                                                .requestMatchers(HttpMethod.GET, "/discounts/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/discounts").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/discounts/**").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/discounts/buyer/active-coupons").hasRole("BUYER")

                                                //ORDERS
                                                .requestMatchers(HttpMethod.POST, "/orders").hasRole("BUYER")
                                                .requestMatchers(HttpMethod.GET, "/orders/my").hasRole("BUYER")
                                                .requestMatchers(HttpMethod.GET, "/orders/seller/**").hasAnyRole("SELLER", "ADMIN")

                                                //PRODUCT IMAGES
                                                .requestMatchers(HttpMethod.GET,"/product_images/**").permitAll()
                                                .requestMatchers(HttpMethod.POST,"/product_images").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.DELETE,"/product_images/**").hasAnyRole("SELLER", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT,"/product_images/**").hasRole("SELLER")

                                                //PRODUCTS
                                                .requestMatchers(HttpMethod.POST, "/products/**").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.PATCH, "/products/**").hasRole("SELLER")

                                                //REVIEWS
                                                .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/reviews").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.PUT, "/reviews/**").hasRole("SELLER")
                                                .requestMatchers(HttpMethod.DELETE, "/reviews/**").hasRole("SELLER")

                                                //USERS
                                                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll() 
                                                .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")






                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
