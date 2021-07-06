package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.JwtToken;

import java.util.Optional;

public interface JwtTokenRepository extends CrudRepository<JwtToken, Long> {

    Optional<JwtToken> findByToken(String token);
}
