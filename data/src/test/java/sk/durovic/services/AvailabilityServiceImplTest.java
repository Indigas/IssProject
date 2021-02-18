package sk.durovic.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sk.durovic.model.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityServiceImplTest {


    static Set<Car> listOfCars = new HashSet<>();
    static Set<Availability> listOfAvailableCars = new HashSet<>();

    @BeforeAll
    static void setUp(){
        Car mod = new Car("Audi", "A4", Fuel.Diesel, Gear.Manual, null, 76, 2.2d);
        Company cm = new Company();
        cm.setName("AAA auto");
        mod.setCompany(cm);

        Car mod2 = new Car();
        mod2.setBrand("BMW");
        mod2.setCompany(cm);

        Availability av = new Availability();
        av.setStart(LocalDateTime.of(2021,Month.FEBRUARY, 18,10,0));
        av.setEnd(LocalDateTime.of(2021, Month.MARCH, 5, 10,5));
        av.setCarRented(mod);

        Availability av2 = new Availability();
        av2.setStart(LocalDateTime.of(2021,Month.FEBRUARY,1,10,0));
        av2.setEnd(LocalDateTime.of(2021, Month.FEBRUARY, 18,10,0));
        av2.setCarRented(mod2);

        listOfAvailableCars.add(av);
        listOfAvailableCars.add(av2);
        listOfCars.add(mod);
        listOfCars.add(mod2);
    }

    @Test
    void isAvailableTrueTest(){
        LocalDateTime start = LocalDateTime.of(2021,1,10,10,0);
        LocalDateTime end = LocalDateTime.of(2021,1,16,10,2);

        listOfAvailableCars.forEach(t -> {
            assertTrue(AvailabilityServiceImpl.isAvailable(t, start,end));
        });
    }

    @Test
    void isAvailableNotTest(){
        LocalDateTime start = LocalDateTime.of(2021,2,15,10,0);
        LocalDateTime end = LocalDateTime.of(2021,2,18,10,2);

        listOfAvailableCars.forEach(t -> {
            assertFalse(AvailabilityServiceImpl.isAvailable(t, start,end));
        });
    }

    @Test
    void isAvailableOneCar(){
        LocalDateTime start = LocalDateTime.of(2021,2,19,10,0);
        LocalDateTime end = LocalDateTime.of(2021,2,21,10,2);
        List<Availability> onlyOne = new ArrayList<>();

        listOfAvailableCars.forEach(t -> {
            if(AvailabilityServiceImpl.isAvailable(t, start,end)) {
                onlyOne.add(t);
                System.out.println("Only one car must be available:");
                System.out.println(t.toString());
            }
        });

        assertEquals(1, onlyOne.size());
    }

}