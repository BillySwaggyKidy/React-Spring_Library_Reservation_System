package com.billykid.library.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import com.billykid.library.entity.DBUser;
import com.billykid.library.repository.UserRepository;
import com.billykid.library.utils.enums.UserRole;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@Profile("prod")
public class SpringProdConfiguration implements WebMvcConfigurer {

    public SpringProdConfiguration() {
        System.out.println("-------------------------");
        System.out.println("|                        |");
        System.out.println("|  YOU ARE IN PROD MODE  |");
        System.out.println("|                        |");
        System.out.println("-------------------------");
    }
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        this.serveDirectory(registry, "/", "classpath:/static/");
    }
 
    private void serveDirectory(ResourceHandlerRegistry registry, String endpoint, String location) {
        String[] endpointPatterns = endpoint.endsWith("/")
            ? new String[]{endpoint.substring(0, endpoint.length() - 1), endpoint, endpoint + "**"}
            : new String[]{endpoint, endpoint + "/", endpoint + "/**"};
        registry.addResourceHandler(endpointPatterns)
            .addResourceLocations(location.endsWith("/") ? location : location + "/")
            .resourceChain(false)
            .addResolver(new PathResourceResolver() {
                @Override
                public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
                    Resource resource = super.resolveResource(request, requestPath, locations, chain);
                    if (resource != null) {
                        return resource;
                    }
                    return super.resolveResource(request, "/index.html", locations, chain);
                }
            });
    }
    
    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByRole(UserRole.ROLE_ADMIN).isEmpty()) {
                DBUser admin = new DBUser();
                admin.setUsername("Admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123!"));
                admin.setRole(UserRole.ROLE_ADMIN);
                admin.setActive(true);
                userRepository.save(admin);
                System.out.println("| Admin user create |");
            } else {
                System.out.println("Admin user already exists");
            }
        };
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*");
        registry.addMapping("/demo/**").allowedOriginPatterns("*");
    }
 
}