package sk.durovic.services.data;

import org.springframework.stereotype.Service;
import sk.durovic.model.Availability;
import sk.durovic.model.Car;
import sk.durovic.repositories.AvailabilityRepository;
import sk.durovic.services.AvailabilityService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public Set<Car> listOfAvailableCars(Set<Car> listOfCars, LocalDateTime start, LocalDateTime end) {
        Set<Car> listOfNotAvailableCars = new HashSet<>();

        availabilityRepository.findAll().forEach(t -> {
            if(!isAvailable(t, start, end))
                listOfNotAvailableCars.add(t.getCarRented());
        });

        listOfCars.removeAll(listOfNotAvailableCars);
        return listOfCars;
    }

    public boolean isAvailable(Availability availability, LocalDateTime start, LocalDateTime end){

        if(availability.getStart().isBefore(start) && availability.getEnd().isBefore(start))
            return true;

        if(availability.getStart().isAfter(start) && availability.getStart().isAfter(end))
            return true;

        return false;
    }

    @Override
    public Set<Availability> findAll() {
        Set<Availability> list = new HashSet<>();
        availabilityRepository.findAll().forEach(list::add);

        return list;
    }

    @Override
    public Availability findById(Long aLong) {
        return availabilityRepository.findById(aLong).orElse(null);
    }

    @Override
    public Availability save(Availability object) {
        return availabilityRepository.save(object);
    }

    @Override
    public void delete(Availability object) {
        availabilityRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        availabilityRepository.deleteById(aLong);
    }
}
