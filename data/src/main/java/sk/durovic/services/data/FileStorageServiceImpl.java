package sk.durovic.services.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.FileStorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


/* Image handler */
public class FileStorageServiceImpl implements FileStorageService {

    private Company company;
    private final Path path;

    public FileStorageServiceImpl(Company company) {
        this.company = company;
        this.path = Paths.get(File.separator + "companies" + File.separator + company.getName());
    }

    @Override
    public Path save(Car car, String fileName, byte[] imageBytes) throws IOException {
        String folder = path.toString() + File.separator + car.getId() + File.separator;
        if (!Files.exists(Paths.get(folder)))
            Files.createDirectories(Paths.get(folder));


        Path path = Paths.get(folder + fileName);
        Files.write(path, imageBytes);

        return path;
    }

    @Override
    public boolean delete(String imagePath) throws IOException {
        Files.delete(Paths.get(imagePath));
        return !Files.exists(Paths.get(imagePath));
    }

    @Override
    public Stream<Path> loadAll(Car car) throws IOException {
        return Files.walk(Paths.get(path.toString() + File.separator + car.getId() + File.separator))
                .filter(path -> !Files.isDirectory(path));
    }

    @Override
    public Path load(String imagePath) {
        return Paths.get(imagePath);
    }

    @Override
    public void deleteAll(Car car) throws IOException {
        loadAll(car).forEach(path -> {
            try{
                Files.deleteIfExists(path);
            } catch (IOException e){
                System.out.println("Error in deleteAll");
            }
        });
    }

    @Override
    public Path getImagesPath(){
        return path;
    }



}
