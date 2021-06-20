package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.Car;
import sk.durovic.model.Company;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, Long> {

    Optional<List<Car>> findByIsEnabledTrue();

    Optional<List<Car>> findByCompanyId(Long companyId);
}
