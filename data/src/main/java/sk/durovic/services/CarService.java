package sk.durovic.services;


import sk.durovic.model.Car;
import sk.durovic.model.Company;

import java.util.List;
import java.util.Optional;

public interface CarService extends CrudService<Car, Long> {

    Optional<List<Car>> findByIsEnabled();

    Optional<List<Car>> findByCompany(Company company);
}
