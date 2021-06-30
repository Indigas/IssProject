package sk.durovic.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.CarService;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.data.FileStorageServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class ImagesHandler {

    public static Stream<Path> getImages(Car car) throws IOException {
        if(car.getUriImages()==null || car.getUriImages().equals(""))
            throw new IOException("Wrong field UriImages in carId::"+car.getId());

        return Files.walk(Paths.get(car.getUriImages() + File.separator + car.getId() + File.separator))
                .filter(path -> !Files.isDirectory(path));

    }

    public static FileStorageService saveImages(UserDetails userDetail, Car car, MultipartFile[] multipartFiles,
                                                CarService carService) {
        Company company = ((UserDetailImpl)userDetail).getCompany();

        FileStorageService fileStorageService = new FileStorageServiceImpl(company);
        car.setUriImages(fileStorageService.getImagesPath().toString());
        carService.save(car);
        for (MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.isEmpty())
                continue;

            try {
                fileStorageService.save(car, multipartFile.getOriginalFilename(),
                        multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Error occured while saving image");
            }
        }
        return fileStorageService;
    }
}
