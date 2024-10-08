package com.example.producttestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http ) throws  Exception{
        http.csrf().disable();
        http.httpBasic();
        http.authorizeHttpRequests(
          auth -> {
              auth.requestMatchers(
                      "/e-commerce/auth/**",
                      "/v3/api-docs",
                      "/v3/api-docs/**",
                      "/swagger-ui.html"
              ).permitAll();
              auth.requestMatchers(HttpMethod.GET, "/**").hasAnyAuthority("ADMIN","USER","MANAGER")
                      .requestMatchers(HttpMethod.POST, "/cart/add-item").hasAnyAuthority("ADMIN","USER","MANAGER")
                      .requestMatchers(HttpMethod.PUT, "/cart/update-item").hasAnyAuthority("ADMIN","USER","MANAGER")
                      .requestMatchers(HttpMethod.POST, "/e-commerce/auth/**").permitAll()
                      .requestMatchers(HttpMethod.DELETE,"/**").hasAnyAuthority("MANAGER");
              auth.anyRequest().authenticated();
          }
        );
        return http.build();

    }
}
