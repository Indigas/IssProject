package sk.durovic.services;


import sk.durovic.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService extends CrudService<Car, Long> {

    Optional<List<Car>> findByIsEnabled();
}
