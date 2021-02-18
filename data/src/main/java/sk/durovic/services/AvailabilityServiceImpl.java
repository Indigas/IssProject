package sk.durovic.services;

import org.springframework.stereotype.Service;
import sk.durovic.model.Availability;
import sk.durovic.model.Car;
import sk.durovic.repositories.AvailabilityRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public Set<Car> listOfAvailableCars(LocalDateTime start, LocalDateTime end) {
        Set<Car> listOfAvailableCars = new HashSet<>();
        availabilityRepository.findAll().forEach(t -> {
            if(isAvailable(t, start, end))
                listOfAvailableCars.add(t.getCarRented());
        });

        return listOfAvailableCars;
    }

    public static boolean isAvailable(Availability availability, LocalDateTime start, LocalDateTime end){

        if(availability.getStart().isBefore(start) && availability.getEnd().isBefore(start))
            return true;

        if(availability.getStart().isAfter(start) && availability.getStart().isAfter(end))
            return true;

        return false;
    }
}
