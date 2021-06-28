package sk.durovic.data;

import sk.durovic.model.Car;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ImagesHandler {

    public static Stream<Path> getImages(Car car) throws IOException {
        if(car.getUriImages()==null || car.getUriImages().equals(""))
            throw new IOException("Wrong field UriImages in carId::"+car.getId());

        return Files.walk(Paths.get(car.getUriImages() + File.separator + car.getId() + File.separator))
                .filter(path -> !Files.isDirectory(path));

    }
}
