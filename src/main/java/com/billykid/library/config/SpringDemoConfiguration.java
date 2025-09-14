package com.billykid.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("demo")
public class SpringDemoConfiguration implements WebMvcConfigurer {

    public SpringDemoConfiguration() {
        System.out.println("-------------------------");
        System.out.println("|                        |");
        System.out.println("|  YOU ARE IN DEMO MODE  |");
        System.out.println("|                        |");
        System.out.println("-------------------------");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*");
    }
}
