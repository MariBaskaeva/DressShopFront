package ru.baskaeva.dressshopfront;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtTokenAuthentication extends AbstractAuthenticationToken {
    private final String accessToken;
    public JwtTokenAuthentication(String token) {
        super(List.of(new SimpleGrantedAuthority("USER_ROLE")));
        accessToken = token;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }
}
