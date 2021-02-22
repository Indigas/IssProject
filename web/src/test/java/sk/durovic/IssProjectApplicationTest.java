package sk.durovic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.durovic.model.Car;
import sk.durovic.services.data.AvailabilityServiceImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssProjectApplicationTest {

    @Autowired
    AvailabilityServiceImpl availabilityService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void first(){
        Set<Car> listCars = availabilityService.listOfAvailableCars(
                LocalDateTime.of(2020, Month.FEBRUARY, 15,10,0),
                LocalDateTime.of(2020,Month.FEBRUARY, 17,10,0)
        );

        listCars.forEach(System.out::println);
        assertEquals(2, listCars.size());
    }
}