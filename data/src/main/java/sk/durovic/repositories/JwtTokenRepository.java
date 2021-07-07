package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.JwtToken;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends CrudRepository<JwtToken, Long> {

    Optional<JwtToken> findByToken(String token);

    void deleteAllByUserId(Long id);

}
