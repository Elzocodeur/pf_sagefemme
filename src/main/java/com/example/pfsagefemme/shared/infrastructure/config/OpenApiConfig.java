package com.example.pfsagefemme.shared.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI / Swagger UI.
 * UI accessible a /swagger-ui.html, spec a /v3/api-docs.
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI planiFemOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PlaniFem - API de Planification Familiale")
                        .version("v1")
                        .description("""
                                API REST pour la gestion de la planification familiale dans les postes de sante.

                                **Hierarchie des roles** :
                                - `SUPER_ADMIN` : cree les postes de sante et les administrateurs de poste
                                - `ADMIN_POSTE` : gere son poste, cree les sages-femmes
                                - `SAGE_FEMME` : gere les patientes, consultations et visites

                                **Authentification** : POST /api/v1/auth/login pour obtenir un JWT,
                                puis envoyer `Authorization: Bearer <token>` sur tous les autres endpoints.
                                """)
                        .contact(new Contact().name("Equipe PlaniFem").email("contact@planifem.local"))
                        .license(new License().name("Proprietary")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local"),
                        new Server().url("/").description("Current host")
                ))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME_NAME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT obtenu via /api/v1/auth/login")));
    }
}
