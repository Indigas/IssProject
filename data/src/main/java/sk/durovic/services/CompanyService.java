package sk.durovic.services;


import sk.durovic.model.Company;

import java.util.Optional;

public interface CompanyService extends CrudService<Company, Long> {

    Optional<Company> findByEmail(String email);
}
