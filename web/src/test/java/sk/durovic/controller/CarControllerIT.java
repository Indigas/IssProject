package sk.durovic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sk.durovic.repositories.CarRepository;
import sk.durovic.repositories.PricesRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarControllerIT {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PricesRepository pricesRepository;

    @Test
    void getCarById() {
    }

    @Test
    void saveCarForm() {
    }

    @Test
    void saveImageForm() {
    }

    @Test
    void saveImagesToCar() {
    }

    @Test
    void carSummary() {
    }

    @Test
    void publishCar() {
    }

    @Test
    void deleteCar() {
    }

    @Test
    void updateCar() {
    }

    @Test
    void listOfMyCars() {
    }

    @Test
    void isOwnerOfCar() {
    }
}