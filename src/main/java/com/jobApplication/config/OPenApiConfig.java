package com.jobApplication.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Anish",
                        email = "anishawn05@gmail.com",
                        url = "https://MyUrl.com"

                ),
                description = "OpenApi Documentation for JobApplication",
                title = "OpenApi specification - Anish ",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://url.com"
                ),
                termsOfService = "Terms Of Service"

        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:19198"
                ),
                @Server(
                        description = "Prod Env",
                        url = "https://MyUrl.com"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OPenApiConfig {
}
