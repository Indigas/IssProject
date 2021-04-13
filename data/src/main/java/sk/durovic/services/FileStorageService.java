package sk.durovic.services;

import sk.durovic.model.Car;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {

    Path save(Car car, String fileName, byte[] imageBytes) throws IOException;

    boolean delete(String imagePath) throws IOException;

    Stream<Path> loadAll(Car car) throws IOException;

    Path load(String imagePath);

    void deleteAll(Car car) throws IOException;

    public Path getImagesPath();
}
