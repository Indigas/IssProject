package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.CompanyCredentials;

import java.util.Optional;

public interface CompanyCredentialsRepository extends CrudRepository<CompanyCredentials, Long> {

    Optional<CompanyCredentials> findByUsername(String username);
}
