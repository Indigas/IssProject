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
import sk.durovic.model.Availability;
import sk.durovic.model.Car;
import sk.durovic.repositories.AvailabilityRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AvailabilityRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    private Availability availability;
    private Car car;

    @BeforeEach
    void setUp() {
        car = CarBuilder.getCarForTest();
        car.setId(null);
        car.getCompany().setId(null);

        availability = new Availability();
        availability.setStart(LocalDateTime.of(2021, Month.APRIL, 1, 10, 0));
        availability.setEnd(LocalDateTime.of(2021, Month.APRIL, 10, 10,0));
        availability.setCarRented(car);

        testEntityManager.persist(car.getCompany());
        testEntityManager.persist(car);
    }

    @Test
    void notFindAll(){
        List<Availability> list = new ArrayList<>();
        availabilityRepository.findAll().forEach(list::add);

        assertThat(list, Matchers.empty());
    }

    @Test
    void findAll(){
        testEntityManager.persist(availability);

        List<Availability> list = new ArrayList<>();
        availabilityRepository.findAll().forEach(list::add);

        assertThat(list, Matchers.hasSize(1));
        assertThat(list, Matchers.contains(availability));
    }

    @Test
    void findById(){
        long id = testEntityManager.persistAndGetId(availability, Long.class);

        Availability found = availabilityRepository.findById(id).orElse(null);

        assertThat(found, Matchers.samePropertyValuesAs(availability));
    }

    @Test
    void notFoundById(){
        testEntityManager.persist(availability);

        Availability found = availabilityRepository.findById(2000L).orElse(null);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void save(){
        Availability saved = availabilityRepository.save(availability);

        Availability found = testEntityManager.find(Availability.class, saved.getId());

        assertThat(found, Matchers.samePropertyValuesAs(availability, "id"));
    }

    @Test
    void delete(){
        Availability saved = testEntityManager.persist(availability);

        availabilityRepository.delete(saved);

        Availability found = testEntityManager.find(Availability.class, saved.getId());

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDelete(){
        Availability saved = testEntityManager.persist(availability);
        long id = saved.getId();
        saved.setId(200L);

        availabilityRepository.delete(saved);

        Availability found = testEntityManager.find(Availability.class, id);

        assertThat(found, Matchers.samePropertyValuesAs(availability, "id"));
    }

    @Test
    void deleteById(){
        long id = testEntityManager.persistAndGetId(availability, Long.class);

        availabilityRepository.deleteById(id);

        Availability found = testEntityManager.find(Availability.class, id);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDeleteById(){
        long id = testEntityManager.persistAndGetId(availability, Long.class);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            availabilityRepository.deleteById(2000L);
                });
    }
}
