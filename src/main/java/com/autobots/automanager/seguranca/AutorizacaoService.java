package com.autobots.automanager.seguranca;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AutorizacaoService {

    private static final Set<String> ROLES_INTERNAS = Set.of(
            "ROLE_ADMINISTRADOR",
            "ROLE_GERENTE",
            "ROLE_VENDEDOR"
    );

    public boolean isInterno(Authentication authentication) {
        return hasAnyRole(authentication, ROLES_INTERNAS);
    }

    public boolean isDonoOuInterno(Long usuarioId, Authentication authentication) {
        if (isInterno(authentication)) {
            return true;
        }

        if (authentication == null || !authentication.isAuthenticated() || usuarioId == null) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Long principalId) {
            return usuarioId.equals(principalId);
        }

        if (principal instanceof Number numeroPrincipal) {
            return usuarioId.longValue() == numeroPrincipal.longValue();
        }

        if (principal instanceof String principalString) {
            try {
                return usuarioId.equals(Long.parseLong(principalString));
            } catch (NumberFormatException ignored) {
                return false;
            }
        }

        return false;
    }

    private boolean hasAnyRole(Authentication authentication, Set<String> roles) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (roles.contains(authority.getAuthority())) {
                return true;
            }
        }

        return false;
    }
}