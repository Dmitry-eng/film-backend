package org.film.auth.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.film.auth.dto.ExpirationTime;
import org.film.auth.tool.JwtHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class SpringConfig {

    @Value("${jwt.secret.access}")
    private String jwtAccessSecret;
    @Value("${jwt.secret.refresh}")
    private String jwtRefreshSecret;

    @ConfigurationProperties(prefix = "security.expiration-time.access")
    @Bean("accessExpirationTime")
    public ExpirationTime accessExpirationTime() {
        return new ExpirationTime();
    }

    @ConfigurationProperties(prefix = "security.expiration-time.refresh")
    @Bean("refreshExpirationTime")
    public ExpirationTime refreshExpirationTime() {
        return new ExpirationTime();
    }

    @Bean
    public JwtHelper jwtTool(ExpirationTime accessExpirationTime, ExpirationTime refreshExpirationTime) {
        SecretKey accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtAccessSecret));
        SecretKey refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtRefreshSecret));
        return new JwtHelper(accessSecretKey, refreshSecretKey, accessExpirationTime, refreshExpirationTime);
    }

}
