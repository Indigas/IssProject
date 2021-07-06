package config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import sk.durovic.model.JwtToken;
import sk.durovic.services.JwtTokenService;

import java.util.Optional;

@TestConfiguration
public class JwtTokenTestService {

    @Bean
    @Primary
    public JwtTokenService getJwtTokenService(){
        return new MyJwtTokenService();
    }

    private class MyJwtTokenService implements JwtTokenService{
        @Override
        public Optional<JwtToken> findByToken(String token) {
            switch (token){
                case "empty":
                    return Optional.empty();
                case "token":
                    return Optional.of(new JwtToken("token"));
                default:
                    return Optional.empty();
            }
        }

        @Override
        public boolean isValid(String token) {
            return this.findByToken(token).isEmpty();
        }

        @Override
        public JwtToken save(JwtToken token) {
            return token;
        }
    }

}
