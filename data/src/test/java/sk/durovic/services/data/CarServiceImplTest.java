package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.repositories.CarRepository;
import sk.durovic.services.CarService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;


@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    private Car car;
    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarServiceImpl(carRepository);

        car = new Car();
        car.setId(1L);
        car.setBrand("audi");
    }

    @Test
    void findAll() {
        Set<Car> cars = Set.of(car, new Car());
        Mockito.when(carRepository.findAll()).thenReturn(cars);

        Set<Car> carList = carService.findAll();

        Mockito.verify(carRepository, Mockito.atMostOnce()).findAll();
        assertThat(carList, Matchers.hasSize(2));
        assertThat(carList, Matchers.hasItem(car));
    }

    @Test
    void findById() {
        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.ofNullable(car));

        Car found = carService.findById(1L);

        Mockito.verify(carRepository, Mockito.atMostOnce()).findById(1L);
        assertThat(found, Matchers.notNullValue());
        assertThat(found, Matchers.is(car));
    }

    @Test
    void save() {
        Mockito.when(carRepository.save(car)).thenReturn(car);

        Car saved = carService.save(car);

        Mockito.verify(carRepository, Mockito.atMostOnce()).save(car);
        assertThat(saved, Matchers.notNullValue());
        assertThat(saved, Matchers.is(car));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(carRepository).delete(car);

        carService.delete(car);

        Mockito.verify(carRepository, Mockito.atMostOnce()).delete(car);
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(carRepository).deleteById(1L);

        carService.deleteById(1L);

        Mockito.verify(carRepository, Mockito.atMostOnce()).deleteById(1L);
    }

    @Test
    void findByIsEnabled() {
        Mockito.when(carRepository.findByEnabledTrue()).thenReturn(Optional.of(List.of(car, new Car())));

        List<Car> list = carService.findByIsEnabled().orElse(null);

        Mockito.verify(carRepository, Mockito.atMostOnce()).findByEnabledTrue();
        assertThat(list, Matchers.notNullValue());
        assertThat(list, Matchers.hasSize(2));
        assertThat(list, Matchers.hasItem(car));

    }

    @Test
    void findByCompany() {
        Mockito.when(carRepository.findByCompanyId(1L)).thenReturn(Optional.of(List.of(car, new Car())));
        Company company = new Company();
        company.setId(1L);

        List<Car> list = carService.findByCompany(company).orElse(null);

        Mockito.verify(carRepository, Mockito.atMostOnce()).findByCompanyId(1L);
        assertThat(list, Matchers.notNullValue());
        assertThat(list, Matchers.hasSize(2));
        assertThat(list, Matchers.hasItem(car));
    }
}