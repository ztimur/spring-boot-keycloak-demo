package io.ztimur.demo.util;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {

    public static String getCurrentUsername() {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return "anonymous";
        }

        if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp =
                    (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            return kp.getKeycloakSecurityContext().getIdToken() != null
                    ? kp.getKeycloakSecurityContext().getIdToken().getPreferredUsername()
                    : kp.getKeycloakSecurityContext().getToken().getPreferredUsername();
        } else {
            return authentication.getName();
        }
    }
}
