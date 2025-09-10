package com.billykid.template.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.billykid.template.config.SpringSecurityConfiguration;
import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.utils.DTO.DBUserDTO;
import com.billykid.template.utils.properties.CorsProperties;

@Profile("test")
@WebMvcTest(controllers = AuthentificationController.class)
@EnableConfigurationProperties(CorsProperties.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SpringSecurityConfiguration.class)

public class AuthentificationControllerTest {
    @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CustomUserDetailsService service;

    @Test
    void shouldReturnNewUserAfterSignup() throws Exception {
        // Préparer la réponse du service mocké
        DBUserDTO mockUser = new DBUserDTO(1,"Billy","billy@gmail.com",null,"ROLE_CUSTOMER");
        when(service.registerUser(any(DBUserDTO.class))).thenReturn(mockUser);

        // Effectuer la requête GET et vérifier la réponse
        mockMvc.perform(post("/auth/signup")
                .contentType("application/json")
                .content("""
                    {
                        "username": "Billy",
                        "email": "billy@gmail.com",
                        "password": "billy"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Billy"))
                .andExpect(jsonPath("$.email").value("billy@gmail.com"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));
    }

    @Test
    void shouldReturnUserAfterLogin() throws Exception {
        // Préparer la réponse du service mocké
        DBUserDTO mockUser = new DBUserDTO(1,"Billy","billy@gmail.com",null,"ROLE_ADMIN");
        when(service.registerUser(any(DBUserDTO.class))).thenReturn(mockUser);

        // Effectuer la requête GET et vérifier la réponse
        mockMvc.perform(post("/auth/signup")
                .contentType("application/json")
                .content("""
                        {
                            "username": "Billy",
                            "email": "billy@gmail.com",
                            "password": "billy",
                            "role": "ROLE_ADMIN"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Billy"))
                .andExpect(jsonPath("$.email").value("billy@gmail.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }
}
