package sk.durovic.services;


import sk.durovic.model.Car;

import java.time.LocalDateTime;
import java.util.Set;

public interface AvailabilityService {

    Set<Car> listOfAvailableCars(LocalDateTime start, LocalDateTime end);
}
