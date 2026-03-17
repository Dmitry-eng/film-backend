package org.film.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = "org.film")
public class ModuleAutoconfiguration {
}