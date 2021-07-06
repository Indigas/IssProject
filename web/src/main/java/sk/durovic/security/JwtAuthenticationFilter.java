package sk.durovic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.durovic.httpError.NotAuthorized;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/data/**"), authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if(header==null || header.isEmpty())
            throw new BadCredentialsException("Not valid token");

        String jwtToken = header.substring(7).trim();

        logger.debug("JwtAuthenticationFilter::JwtToken::"+jwtToken);

        Authentication auth = new JwtUserToken(jwtToken);

        return this.getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        ((SimpleUrlAuthenticationSuccessHandler)this.getSuccessHandler())
                .setRedirectStrategy(new RedirectStrategy() {
                    @Override
                    public void sendRedirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) throws IOException {

                    }
                });

        logger.debug("JwtAuthenticationFilter::Successfull");

        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request,response);

    }

    private boolean shouldDoFilter(HttpServletRequest request){
        return !request.getRequestURI().equals("/api/data/login");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return shouldDoFilter(request);
    }
}
