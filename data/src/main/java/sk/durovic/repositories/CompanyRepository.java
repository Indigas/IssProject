package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.Company;

public interface CompanyRepository extends CrudRepository<Company, Long> {
}
