package org.film.auth.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = "org.auth")
public class ModuleAutoconfiguration {
}