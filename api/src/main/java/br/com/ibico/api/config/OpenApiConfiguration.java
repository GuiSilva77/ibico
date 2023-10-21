package br.com.ibico.api.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecuritySchemes( value = {@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic"
), @SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "bearerAuth",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization", description = "Bearer <token>")})
public class OpenApiConfiguration {
    @Bean
    public OpenAPI ibicoOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Ibico API")
                .version("v0.0.1")
                .description("API para o projeto Ibico")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .description("API para o projeto Ibico"));
    }
}
