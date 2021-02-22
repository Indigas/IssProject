package sk.durovic.services.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Car;
import sk.durovic.repositories.CarRepository;
import sk.durovic.services.CarService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarServiceImpl carService;

    Set<Car> listTest;

    @BeforeEach
    void setUp() {
        listTest = new HashSet<>();
        Car a = new Car();
        a.setBrand("Audi");
        a.setId(1L);
        Car b = new Car();
        b.setBrand("BMW");
        b.setId(2L);

        listTest.add(a);
        listTest.add(b);
    }

    @Test
    void findAll() {
        Mockito.when(carRepository.findAll()).thenReturn(listTest);

        Set<Car> fromService = carService.findAll();

        assertEquals(2, fromService.size());
    }

    @Test
    void findById() {
        Car a = new Car();
        a.setId(1L);

        Mockito.when(carRepository.findById(any())).thenReturn(Optional.of(a));

        Car found = carService.findById(1L);

        assertEquals(a, found);
    }

    @Test
    void save() {
        Car a = new Car();
        a.setBrand("Audi");
        a.setId(1L);

        Mockito.when(carRepository.save(any())).thenReturn(a);

        Car fromService = carService.save(a);

        assertEquals(a, fromService);
    }

}