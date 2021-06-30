package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Availability;
import sk.durovic.model.Car;
import sk.durovic.repositories.AvailabilityRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplTest {
    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private Availability availability;
    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setId(150L);
        car.setBrand("audi");
        availability = new Availability();
        availability.setId(1L);
        availability.setStart(LocalDateTime.of(2021, Month.APRIL, 1, 10,0));
        availability.setEnd(LocalDateTime.of(2021, Month.APRIL, 10, 10,0));
        availability.setCarRented(car);

    }

    @Test
    void listOfAvailableCars() {
        Set<Car> carList = Set.of(car);
        Set<Availability> list = Set.of(availability);
        Mockito.when(availabilityRepository.findAll()).thenReturn(list);

        Set<Car> found = availabilityService.listOfAvailableCars(carList,
                LocalDateTime.of(2021, Month.APRIL, 11,10,0),
                LocalDateTime.of(2021, Month.APRIL,20,10,0));

        Mockito.verify(availabilityRepository, Mockito.atMostOnce()).findAll();
        assertThat(found, Matchers.hasSize(1));
    }

    @Test
    void listOfAvailableCarsAndReduceList() {
        Car car1 = new Car();
        car1.setId(50L);
        Set<Car> carList = new TreeSet<>(List.of(car, car1));
        Set<Availability> list = Set.of(availability);
        Mockito.when(availabilityRepository.findAll()).thenReturn(list);

        Set<Car> found = availabilityService.listOfAvailableCars(carList,
                LocalDateTime.of(2021, Month.APRIL, 8,10,0),
                LocalDateTime.of(2021, Month.APRIL,15,10,0));

        Mockito.verify(availabilityRepository, Mockito.atMostOnce()).findAll();
        assertThat(found, Matchers.hasSize(1));
    }


    @Test
    void findAll() {
        Mockito.when(availabilityRepository.findAll()).thenReturn(Set.of(availability));

        Set<Availability> list = availabilityService.findAll();

        Mockito.verify(availabilityRepository, Mockito.atMostOnce()).findAll();
        assertThat(list, Matchers.contains(availability));
    }

    @Test
    void findById() {
        Mockito.when(availabilityRepository.findById(1L)).thenReturn(Optional.ofNullable(availability));

        Availability found = availabilityService.findById(1L);

        Mockito.verify(availabilityRepository, Mockito.atMostOnce()).findById(1L);
        assertThat(found, Matchers.notNullValue());
        assertThat(found, Matchers.is(availability));
    }

    @Test
    void save() {
        Mockito.when(availabilityRepository.save(availability)).thenReturn(availability);

        Availability saved = availabilityService.save(availability);

        Mockito.verify(availabilityRepository, Mockito.atMostOnce()).save(availability);
        assertThat(saved, Matchers.notNullValue());
        assertThat(saved, Matchers.is(availability));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(availabilityRepository).delete(availability);

        availabilityService.delete(availability);

        Mockito.verify(availabilityRepository, Mockito.atMostOnce()).delete(availability);
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(availabilityRepository).deleteById(1L);

        availabilityService.deleteById(1L);

        Mockito.verify(availabilityRepository, Mockito.atMostOnce()).deleteById(1L);
    }

    @Nested
    class isAvailableTests {
        @Test
        void isAvailable() {
            boolean result = availabilityService.isAvailable(availability,
                    LocalDateTime.of(2021, Month.APRIL,11,10,0),
                    LocalDateTime.of(2021,Month.APRIL,20,10,0));

            assertTrue(result);
        }

        @Test
        void notAvailable(){
            boolean result = availabilityService.isAvailable(availability,
                    LocalDateTime.of(2021, Month.APRIL,1,10,0),
                    LocalDateTime.of(2021,Month.APRIL,20,10,0));

            assertFalse(result);
        }

        @Test
        void lastDayReservation(){
            boolean result = availabilityService.isAvailable(availability,
                    LocalDateTime.of(2021, Month.APRIL,10,10,1),
                    LocalDateTime.of(2021,Month.APRIL,20,10,0));

            assertTrue(result);
        }

        @Test
        void makingReservationToLastDay(){
            boolean result = availabilityService.isAvailable(availability,
                    LocalDateTime.of(2021, Month.MARCH,10,10,1),
                    LocalDateTime.of(2021,Month.APRIL,1,9,59));

            assertTrue(result);
        }

        @Test
        void makingReservationBeforeStartAndEndAfterExistStart(){
            boolean result = availabilityService.isAvailable(availability,
                    LocalDateTime.of(2021, Month.MARCH,10,10,1),
                    LocalDateTime.of(2021,Month.APRIL,3,9,59));

            assertFalse(result);
        }

    }
}