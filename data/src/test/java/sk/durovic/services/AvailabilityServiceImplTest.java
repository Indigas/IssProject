package sk.durovic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Availability;
import sk.durovic.model.Car;
import sk.durovic.repositories.AvailabilityRepository;
import sk.durovic.services.data.AvailabilityServiceImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplTest {

    @Mock
    AvailabilityRepository availabilityRepository;

    @InjectMocks
    AvailabilityServiceImpl availabilityService;

    @BeforeEach
    void setUp() { }

    @Test
    void listOfAvailableCars() {
        Car audi = new Car();
        audi.setBrand("Audi");
        Availability availability = new Availability();
        availability.setStart(LocalDateTime.of(2021, Month.FEBRUARY, 18,10,0));
        availability.setEnd(LocalDateTime.of(2021, Month.MARCH, 5,10,0));
        availability.setCarRented(audi);
        Set<Availability> forTest = new HashSet<>();

        forTest.add(availability);

        Mockito.when(availabilityRepository.findAll()).thenReturn(forTest);

        Set<Car> listOfCars = availabilityService.listOfAvailableCars(new HashSet<>(),
                LocalDateTime.of(2021, Month.FEBRUARY, 14,10,0),
                LocalDateTime.of(2021,Month.FEBRUARY, 17,10,0));

        //assertEquals(1, listOfCars.size());
        verify(availabilityRepository, times(1)).findAll();

    }

    @Test
    void isAvailable() {
    }
}