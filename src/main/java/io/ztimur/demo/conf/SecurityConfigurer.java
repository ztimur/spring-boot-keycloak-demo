package io.ztimur.demo.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "keycloak", value = "enabled", havingValue = "false")
public class SecurityConfigurer implements WebSecurityCustomizer {

    public void customize(WebSecurity webSecurity) {
        webSecurity.ignoring().anyRequest();
    }

}
