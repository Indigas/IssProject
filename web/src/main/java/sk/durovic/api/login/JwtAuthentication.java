package sk.durovic.api.login;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sk.durovic.model.AuthRequest;
import sk.durovic.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/data/login")
public class JwtAuthentication {


    @PostMapping
    public ResponseEntity<?> login(@AuthenticationPrincipal UserDetails userDetails){
        Map<String, String> model = new HashMap<>();
        model.put("authentication", "successfull");
        model.put("username", userDetails.getUsername());
        model.put("token", JwtUtil.createJWTtoken(userDetails.getUsername()));
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails){
        Map<String, String> model = new HashMap<>();
        model.put("user", userDetails.getUsername());

        return ResponseEntity.ok(model);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> getError(Exception e){
        return ResponseEntity.status(403).body("Bad credentials");
    }

}
