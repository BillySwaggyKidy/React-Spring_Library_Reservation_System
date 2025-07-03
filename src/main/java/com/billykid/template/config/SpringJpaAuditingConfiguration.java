package com.billykid.template.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class SpringJpaAuditingConfiguration {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Valeur d'exemple pour l'utilisateur courant, à adapter
        return () -> Optional.of("system");
    }
}
