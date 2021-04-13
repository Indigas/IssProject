package sk.durovic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.durovic.model.Car;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;
import sk.durovic.services.data.AvailabilityServiceImpl;
import sk.durovic.services.data.CarServiceImpl;
import sk.durovic.services.data.CompanyServiceImpl;
import sk.durovic.services.data.PricesServiceImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssProjectApplicationTest {

    @Autowired
    AvailabilityServiceImpl availabilityService;
    @Autowired
    CarServiceImpl carService;
    @Autowired
    CompanyServiceImpl companyService;
    @Autowired
    PricesServiceImpl pricesService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void first(){
        Set<Car> listCars = availabilityService.listOfAvailableCars(carService.findAll(),
                LocalDateTime.of(2020, Month.FEBRUARY, 15,10,0),
                LocalDateTime.of(2020,Month.FEBRUARY, 17,10,0)
        );

        //listCars.forEach(System.out::println);
       // assertEquals(1, listCars.size());
    }

    @Test
    void savingCar(){
       /* Car toSave = new Car();
        toSave.setBrand("Dacia");
        toSave.setModel("sandero");

        carService.save(toSave);

        Optional<Car> optionalCar = carService.findAll().stream().filter(car -> {
            return car.getBrand().equals(toSave.getBrand());
        }).findAny();

        assertEquals(toSave.getBrand(), optionalCar.get().getBrand());*/
    }
}