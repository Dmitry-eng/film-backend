package org.film.db.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@AutoConfigurationPackage(basePackages = "org.db")
@EnableJpaRepositories(basePackages = "org.db")
public class ModuleAutoconfiguration {
}