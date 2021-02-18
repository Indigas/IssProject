package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.Car;

public interface CarRepository extends CrudRepository<Car, Long> {

}
