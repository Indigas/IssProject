package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.durovic.model.Car;
import sk.durovic.model.Company;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceImplTest {

    private FileStorageServiceImpl fileStorageService;
    private Company company;
    private Car car;
    private Path tempFiles;
    private static Path pathToDelete;

    @BeforeEach
    void setUp() throws Exception {

        company = new Company();
        company.setName("CompanyTest");
        car = new Car();
        car.setId(1L);

        fileStorageService = new FileStorageServiceImpl(company);
        pathToDelete = Paths.get(File.separator + "tmp" + File.separator + "companies" +
                File.separator + company.getId());

        setPrefix();

        String[] filenames = {"firstTest.txt", "secondTest.txt"};
        Arrays.stream(filenames).forEach(name ->
        {
            try {
                tempFiles = fileStorageService.save(car, name, "test".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @AfterAll
    static void afterAll() throws IOException {
        Files.walk(pathToDelete).sorted(Comparator.reverseOrder())
                .forEach(file -> {
                    try {
                        Files.deleteIfExists(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    void save() throws Exception {
        String filename = "test.txt";

        Path path = fileStorageService.save(car, filename, "test".getBytes());

        assertTrue(Files.exists(path));
        Files.deleteIfExists(path);
    }

    @Test
    void delete() throws IOException {
        boolean deleted = fileStorageService.delete(tempFiles.toString());

        assertTrue(deleted);
    }

    @Test
    void loadAll() throws IOException {
        Stream<Path> files = fileStorageService.loadAll(car);

        assertThat(files.count(), Matchers.is(2L));
    }

    @Test
    void load() {
        Path path = fileStorageService.load(tempFiles.toString());

        assertTrue(Files.exists(path));
    }

    @Test
    void errorLoadAsNotExistFile() {
        Path path = fileStorageService.load(tempFiles.toString()+"ab");

        assertFalse(Files.exists(path));
    }

    @Test
    void deleteAll() throws IOException {
        fileStorageService.deleteAll(car);
        Stream<Path> files = fileStorageService.loadAll(car);

        assertThat(files.count(), Matchers.is(0L));
    }

    @Test
    void getImagesPath() {
        Path path=fileStorageService.getImagesPath();

        assertThat(path.toString(), Matchers.is(pathToDelete.toString()));
    }

    void setPrefix() throws Exception{
        Field field = fileStorageService.getClass().getDeclaredField("path");
        field.setAccessible(true);
        Path path = (Path) field.get(fileStorageService);
        String tempPath = File.separator + "tmp" +path.toString();
        field.set(fileStorageService, Paths.get(tempPath));
    }
}