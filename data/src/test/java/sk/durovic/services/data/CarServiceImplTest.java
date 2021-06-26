package sk.durovic.services.data;

import static org.hamcrest.MatcherAssert.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.repositories.CarRepository;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car testCar;

    @BeforeEach
    void setUp() {
        //carService = new CarServiceImpl(carRepository);
        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Audi");
    }

    @Test
    void findAll() {
        Car b = new Car();
        b.setId(2L);
        Mockito.when(carRepository.findAll()).thenReturn(Arrays.asList(testCar, b));

        Set<Car> listOfCars = carService.findAll();

        assertThat(listOfCars, Matchers.hasSize(2));

        Mockito.verify(carRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findById() {

        Mockito.when(carRepository.findById(Mockito.any())).thenReturn(Optional.of(testCar));
        Car b = carService.findById(1L);

        assertThat(testCar, Matchers.equalTo(b));

        Mockito.verify(carRepository, Mockito.atMostOnce()).findById(Mockito.any());
    }

    @Test
    void save() {
        Mockito.when(carRepository.save(Mockito.any())).thenReturn(testCar);

        Car b = carService.save(testCar);

        assertThat(testCar, Matchers.equalTo(b));
        Mockito.verify(carRepository, Mockito.atMostOnce()).save(Mockito.any());
    }

    @Test
    void findByIsEnabled() {
        Mockito.when(carRepository.findByIsEnabledTrue()).thenReturn(Optional.of(Collections.singletonList(testCar)));

        Optional<List<Car>> b = carService.findByIsEnabled();

        assertThat(b.orElse(null), Matchers.hasSize(1));
        Mockito.verify(carRepository, Mockito.atMostOnce()).findByIsEnabledTrue();
    }

    @Test
    void findByCompany() {
        Mockito.when(carRepository.findByCompanyId(Mockito.any())).thenReturn(Optional.of(Collections.singletonList(testCar)));

        Optional<List<Car>> b = carService.findByCompany(new Company());

        assertThat(b.orElse(null), Matchers.hasSize(1));
        Mockito.verify(carRepository, Mockito.atMostOnce()).findByCompanyId(Mockito.any());
    }
}