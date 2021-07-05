package sk.durovic.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyUserPasswordRestAuthFilter extends UsernamePasswordAuthenticationFilter {

    public MyUserPasswordRestAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/data/login", "POST"));
        this.setAuthenticationSuccessHandler(successHandler());
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return (String) request.getAttribute("password");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request,response);
    }

    private AuthenticationSuccessHandler successHandler(){
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) throws IOException {
            }
        });
        return handler;
    }

    private String createResponseWithToken(String username) throws JsonProcessingException {
        Map<String, String> model = new HashMap<>();
        model.put("authentication", "successfull");
        model.put("username", username);
        model.put("token", JwtUtil.createJWTtoken(username));

        return new ObjectMapper()
                .writeValueAsString(model);
    }
}
