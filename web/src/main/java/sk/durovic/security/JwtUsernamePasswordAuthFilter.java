package sk.durovic.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import sk.durovic.model.AuthRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class JwtUsernamePasswordAuthFilter extends GenericFilterBean {

    private final ObjectMapper jsonData = new ObjectMapper();


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if(!shouldDoFilter(req)) {
            chain.doFilter(request, response);
            return;
        }

        String body = getBodyRequest(req);

        if(!body.equals("")) {

            AuthRequest authRequest = jsonData.readValue(body, AuthRequest.class);

            request.setAttribute("username", authRequest.getUsername());
            request.setAttribute("password", authRequest.getPassword());
        }

        chain.doFilter(request,response);

    }

    private String getBodyRequest(HttpServletRequest req) throws IOException {
        BufferedReader bfr = req.getReader();
        char[] body = new char[128];
        int read=0;
        StringBuilder bodyString = new StringBuilder();
        while((read = bfr.read(body, 0, body.length)) != -1){
            bodyString.append(body);
        }

        return bodyString.toString();
    }

    private boolean shouldDoFilter(HttpServletRequest request){
        return request.getRequestURI().equals("/api/data/login");
    }
}
