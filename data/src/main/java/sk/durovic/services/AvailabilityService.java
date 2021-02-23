package sk.durovic.services;


import sk.durovic.model.Availability;
import sk.durovic.model.Car;

import java.time.LocalDateTime;
import java.util.Set;

public interface AvailabilityService extends CrudService<Availability, Long> {

    Set<Car> listOfAvailableCars(Set<Car> listOfCars, LocalDateTime start, LocalDateTime end);
}
