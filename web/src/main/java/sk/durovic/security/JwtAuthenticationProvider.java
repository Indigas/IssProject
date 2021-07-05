package sk.durovic.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private DecodedJWT decodedJWT;
    private final String SECRET = "secret";   // must be at least 32 bytes - 32 chars
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public JwtAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        // check if token is not in repo as invalidated token

        /////////////////
        String username = decodedJWT.getClaim("user").asString();

        if(!username.equals(userDetails.getUsername()))
            throw new BadCredentialsException("Not valid JWT token");

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        // retrieve user with userdetialsservice
        if(!(usernamePasswordAuthenticationToken instanceof JwtUserToken))
            throw new BadCredentialsException("Not valid JWT token");

        JwtUserToken jwtUserToken = (JwtUserToken) usernamePasswordAuthenticationToken;

        if(!this.validateToken((String) jwtUserToken.getPrincipal()))
            throw new BadCredentialsException("Not valid JWT token");

        DecodedJWT decodedJWT = JWT.decode((String) jwtUserToken.getPrincipal());

        String username = decodedJWT.getClaim("user").asString();

        return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        logger.debug("JwtAuthenticationProvider::Method::Support::"+
                authentication.equals(JwtUserToken.class));
        return authentication.equals(JwtUserToken.class);
    }

    private boolean validateToken(String token){
        try {
            this.decodedJWT = JWT.require(algorithm)
                    .withClaimPresence("user")
                    .withClaimPresence("iat").withClaimPresence("exp").build()
                    .verify(token);
        } catch (JWTVerificationException e){
            return false;
        }

        return true;
    }
}
