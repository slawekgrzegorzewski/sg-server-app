package pl.sg.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
@Profile("dev")
public class SwaggerConfig {

    @Bean
    public Docket api() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("global", "accessEverything")
        };
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(List.of(
                                SecurityContext
                                        .builder()
                                        .securityReferences(List.of(new SecurityReference("Authorization", scopes)))
                                        .build()
                        )
                )
                .securitySchemes(List.of(
                        new ApiKey("Authorization", "Authorization", "header")
                ))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(
                        PathSelectors.regex("/ipr.*")
                                .or(PathSelectors.regex("/login.*"))
                )
                .build();
    }
}