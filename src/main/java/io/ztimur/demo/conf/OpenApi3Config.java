package io.ztimur.demo.conf;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApi3Config {
    private BuildProperties buildProperties;
    private String authServer;
    private String realm;

    @Autowired
    public OpenApi3Config(
            BuildProperties buildProperties,
            @Value("${keycloak.auth-server-url}") String authServer,
            @Value("${keycloak.realm}") String realm) {
        this.buildProperties = buildProperties;
        this.authServer = authServer;
        this.realm = realm;
    }

    @Bean
    public OpenAPI openAPI() {
        String authUrl =
                String.format("%s/realms/%s/protocol/openid-connect", this.authServer, this.realm);

        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "spring_oauth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OAUTH2)
                                                .description("Oauth2 flow")
                                                .flows(
                                                        new OAuthFlows()
                                                                .authorizationCode(
                                                                        new OAuthFlow()
                                                                                .authorizationUrl(authUrl + "/auth")
                                                                                .refreshUrl(authUrl + "/token")
                                                                                .tokenUrl(authUrl + "/token")
                                                                                .scopes(new Scopes()))))
                                .addParameters(
                                        "Version",
                                        new Parameter()
                                                .in("header")
                                                .name("Version")
                                                .schema(new StringSchema())
                                                .required(false)))
                .security(Collections.singletonList(new SecurityRequirement().addList("spring_oauth")))
                .info(
                        new Info()
                                .title("Demo Service API")
                                .description("Keycloak & Spring Boot integration demo")
                                .version(this.buildProperties.getVersion())
                                .contact(
                                        new Contact()
                                                .name("OOO Ltd")
                                                .url("https://github.com/ztimur/spring-boot-keycloak-demo")
                                                .email("ztimur@gmail.com")));
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi ->
                openApi.getPaths().values().stream()
                        .flatMap(pathItem -> pathItem.readOperations().stream())
                        .forEach(
                                operation ->
                                        operation.addParametersItem(
                                                new HeaderParameter().$ref("#/components/parameters/Version")));
    }
}
