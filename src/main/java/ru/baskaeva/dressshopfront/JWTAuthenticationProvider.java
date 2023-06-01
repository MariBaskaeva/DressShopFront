package ru.baskaeva.dressshopfront;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.baskaeva.dressshopfront.dto.LoginDTO;
import ru.baskaeva.dressshopfront.dto.TokenDTO;

public class JWTAuthenticationProvider implements AuthenticationProvider {
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ResponseEntity<TokenDTO> response = restTemplate.postForEntity("http://localhost:9090/login", new LoginDTO((String) authentication.getPrincipal(), (String) authentication.getCredentials()), TokenDTO.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            JwtTokenAuthentication jwtAuthentication = new JwtTokenAuthentication(response.getBody().getAuthToken());
            jwtAuthentication.setDetails(authentication.getDetails());
            jwtAuthentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            return jwtAuthentication;
        }
        throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
