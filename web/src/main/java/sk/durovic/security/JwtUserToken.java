package sk.durovic.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtUserToken extends UsernamePasswordAuthenticationToken {

    public JwtUserToken(String token) {
        super(token, null);
    }

}
