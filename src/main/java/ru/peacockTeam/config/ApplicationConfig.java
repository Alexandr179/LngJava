package ru.peacockTeam.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "ru.peacockTeam")
@PropertySource(value = "classpath:application.properties")
public class ApplicationConfig {
}
