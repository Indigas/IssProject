package sk.durovic.services;

import sk.durovic.model.JwtToken;

import java.util.Optional;

public interface JwtTokenService {

    Optional<JwtToken> findByToken(String token);

    boolean isValid(String token);

    JwtToken save(JwtToken token);
}
