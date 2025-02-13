package com.billykid.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("dev")
public class SpringDevConfiguration implements WebMvcConfigurer {
 
    public SpringDevConfiguration() {
        System.out.println("------------------------");
        System.out.println("|                       |");
        System.out.println("|  YOU ARE IN DEV MODE  |");
        System.out.println("|                       |");
        System.out.println("------------------------");
    }
}