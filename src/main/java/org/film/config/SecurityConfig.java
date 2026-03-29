package org.film.config;

import org.film.filter.CookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    /// /    TODO временное решение
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers("/**").permitAll());
//        return http.build();
//    }
//
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CookieFilter cookieFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(cookieFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/api-docs/**",
                                        "/swagger-resources/**",
                                        "/api/film",
                                        "/api/film/all",
                                        "/webjars/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/api/film/{id}",
                                        "/api/film",
                                        "/api/account/validate/email/{email}")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,
                                        "/api/film/all",
                                        "/api/account/registration")
                                .permitAll()
                                .requestMatchers("/api/auth/login").permitAll()
                                .anyRequest().authenticated()
                );
        return http.build();
    }

}