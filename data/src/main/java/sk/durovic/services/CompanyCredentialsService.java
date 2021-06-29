package sk.durovic.services;

import sk.durovic.model.CompanyCredentials;

import java.util.Optional;

public interface CompanyCredentialsService {

    Optional<CompanyCredentials> findByUsername(String username);

    CompanyCredentials save(CompanyCredentials object);

    void delete(CompanyCredentials object);

    void deleteById(Long id);
}
