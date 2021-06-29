package sk.durovic.database;


import helper.CarBuilder;
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
import sk.durovic.model.Prices;
import sk.durovic.repositories.PricesRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PricesRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PricesRepository pricesRepository;

    private Prices prices;
    private Car car;

    @BeforeEach
    void setUp() {
        car = CarBuilder.getCarForTest();
        car.setId(null);
        car.getCompany().setId(null);

        testEntityManager.persist(car.getCompany());
        testEntityManager.persist(car);

        prices = new Prices(car, 1, 10);
    }

    @Test
    void findAll(){
        Prices anotherPrice = new Prices(null, 5,5);

        testEntityManager.persist(prices);
        testEntityManager.persist(anotherPrice);

        List<Prices> pricesList = new ArrayList<>();
        pricesRepository.findAll().forEach(pricesList::add);

        assertThat(pricesList, Matchers.hasSize(2));
        assertThat(pricesList, Matchers.containsInAnyOrder(prices, anotherPrice));
    }

    @Test
    void findById(){
        long id = testEntityManager.persistAndGetId(prices, Long.class);

        Prices found = pricesRepository.findById(id).orElse(null);

        assertThat(found, Matchers.samePropertyValuesAs(prices));
    }

    @Test
    void notFoundById(){
        testEntityManager.persist(prices);

        Prices found = pricesRepository.findById(2000L).orElse(null);

        assertThat(found, Matchers.nullValue());

    }

    @Test
    void save(){
        long id = pricesRepository.save(prices).getId();

        Prices found = testEntityManager.find(Prices.class, id);

        assertThat(found, Matchers.samePropertyValuesAs(prices));
    }

    @Test
    void delete(){
        Prices saved = testEntityManager.persist(prices);

        pricesRepository.delete(saved);

        Prices found = testEntityManager.find(Prices.class, saved.getId());

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDelete(){
        long id = testEntityManager.persistAndGetId(prices, Long.class);
        Prices saved = new Prices();

        pricesRepository.delete(saved);

        Prices found = testEntityManager.find(Prices.class, id);

        assertThat(found, Matchers.samePropertyValuesAs(prices));
    }

    @Test
    void deleteById(){
        long id = testEntityManager.persistAndGetId(prices, Long.class);

        pricesRepository.deleteById(id);

        Prices found = testEntityManager.find(Prices.class, id);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDeleteById(){
        long id = testEntityManager.persistAndGetId(prices, Long.class);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            pricesRepository.deleteById(2000L);
        });
    }

    @Test
    void findByCarId(){
        long id = testEntityManager.persistAndGetId(prices, Long.class);

        List<Prices> found = pricesRepository.findByCarIdOrderByDaysAsc(car.getId()).orElse(new ArrayList<>());

        assertThat(found, Matchers.hasSize(1));
        assertThat(found, Matchers.contains(prices));
    }

    @Test
    void notFoundByCarId(){
        long id = testEntityManager.persistAndGetId(prices, Long.class);

        List<Prices> found = pricesRepository.findByCarIdOrderByDaysAsc(2000L).orElse(new ArrayList<>());

        assertThat(found, Matchers.empty());
    }
}
