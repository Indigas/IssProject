package sk.durovic.services.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceImplTest {

    FileStorageService fileStorageService;
    Path image;
    Car car;

    @BeforeEach
    void setUp() {
        Company company = new Company();
        company.setName("auto u hofera");
        car = new Car();
        car.setId(1L);
        car.setBrand("BMW");
        car.setModel("320d");
        company.getListOfCars().add(car);
        car.setCompany(company);

        fileStorageService = new FileStorageServiceImpl(company);
        image = Paths.get("C:\\Users\\durov\\Documents\\blog-item-01.png");
    }

    @Test
    void save() {
        try {
            Path newImage = fileStorageService.save(car, image.getFileName().toString(), Files.readAllBytes(image));
            assertTrue(Files.exists(newImage));
        } catch (IOException e){
            System.out.println("Chyba");
        }

    }

    @Test
    void delete() {
        try {
            Path newImage = fileStorageService.save(car, image.getFileName().toString(), Files.readAllBytes(image));
            fileStorageService.delete(newImage.toString());
            assertFalse(Files.exists(newImage));
        } catch (IOException e){
            System.out.println("Chyba");
        }
    }

    @Test
    void loadAll() {
        try {
            Path newImage = fileStorageService.save(car, image.getFileName().toString(), Files.readAllBytes(image));
            Stream<Path> stream = fileStorageService.loadAll(car);
            assertTrue(stream.findAny().isPresent());
        } catch (IOException e){
            System.out.println("Chyba");
        }
    }

    @Test
    void load() {
        try {
            Path newImage = fileStorageService.save(car, image.getFileName().toString(), Files.readAllBytes(image));
            newImage = fileStorageService.load(newImage.toString());

            assertTrue(Files.exists(newImage));
        } catch (IOException e){
            System.out.println("Chyba");
        }
    }

    @Test
    void deleteAll() {
        try {
            Path newImage = fileStorageService.save(car, image.getFileName().toString(), Files.readAllBytes(image));
            fileStorageService.deleteAll(car);
            assertFalse(Files.exists(newImage));
        } catch (IOException e){
            System.out.println("Chyba");
        }
    }
}