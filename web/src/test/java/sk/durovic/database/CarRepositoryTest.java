package sk.durovic.database;

import helper.CarBuilder;
import helper.CompanyBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.repositories.CarRepository;
import sk.durovic.services.CarService;
import sk.durovic.services.data.CarServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CarRepository carRepository;

    private Company company;
    private Car car;

    @BeforeEach
    void setUp() {
        car = new CarBuilder().setBrand("TestCar").setModel("Test").build();
        company = new CompanyBuilder().setEmail("abc@abc.com").setName("CompanyTest").build();
        car.setCompany(company);

        testEntityManager.persist(company);
    }

    @Test
    public void saveCar(){
        Car savedCar = carRepository.save(car);
        Car serviceCar = testEntityManager.find(Car.class, savedCar.getId());

        assertThat(serviceCar, Matchers.samePropertyValuesAs(car, "id"));
    }

    @Test
    public void deleteCar(){
        long savedCarId = (Long) testEntityManager.persistAndGetId(car);
        car.setId(savedCarId);

        carRepository.delete(car);

        Car found = testEntityManager.find(Car.class, car.getId());
        assertThat(found, Matchers.nullValue());
    }

    @Test
    public void findAllCar(){
        Car anotherCar = new CarBuilder().setBrand("Testing").build();
        anotherCar.setCompany(company);

        testEntityManager.persist(car);
        testEntityManager.persist(anotherCar);

        Set<Car> cars = new TreeSet<>();
        carRepository.findAll().forEach(cars::add);

        assertThat(cars, Matchers.hasSize(2));
        assertThat(cars, Matchers.containsInAnyOrder(car, anotherCar));

    }

    @Test
    public void findByIdCar(){
        long id = testEntityManager.persistAndGetId(car, Long.class);

        Car found = carRepository.findById(id).orElse(null);

        assertThat(found, Matchers.samePropertyValuesAs(car, "id"));

    }

    @Test
    public void deleteByIdCar(){
        long id = testEntityManager.persistAndGetId(car, Long.class);

        carRepository.deleteById(id);

        Car found = testEntityManager.find(Car.class, id);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    public void notDeleteByIdCar(){
        long id = testEntityManager.persistAndGetId(car, Long.class);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
                carRepository.deleteById(2000L);
        });
    }

    @Test
    public void findByIsEnabledCar(){
        car.setEnabled(true);
        long id = testEntityManager.persistAndGetId(car, Long.class);

        List<Car> found = carRepository.findByIsEnabledTrue().orElse(new ArrayList<>());

        assertThat(found, Matchers.hasSize(1));
        assertThat(found, Matchers.contains(car));
    }

    @Test
    public void notFoundByIsEnabledCar(){
        car.setEnabled(false);
        long id = testEntityManager.persistAndGetId(car, Long.class);

        List<Car> found = carRepository.findByIsEnabledTrue().orElse(new ArrayList<>());

        assertThat(found, Matchers.empty());
    }

    @Test
    public void findByCompanyCar(){
        testEntityManager.persist(car);

        List<Car> cars = carRepository.findByCompanyId(car.getCompany().getId()).orElse(new ArrayList<>());

        assertThat(cars, Matchers.hasSize(1));
    }

    @Test
    public void notFoundByCompanyCar(){
        Company another = new CompanyBuilder().setName("Another").setEmail("another").build();
        car.setCompany(another);
        testEntityManager.persist(another);
        testEntityManager.persist(car);

        List<Car> cars = carRepository.findByCompanyId(company.getId()).orElse(new ArrayList<>());

        assertThat(cars, Matchers.empty());
    }
}
