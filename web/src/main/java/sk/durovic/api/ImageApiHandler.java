package sk.durovic.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sk.durovic.controller.CarController;
import sk.durovic.httpError.BadRequestArguments;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.model.Car;
import sk.durovic.model.CarRestApi;
import sk.durovic.model.Company;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.CarService;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.data.FileStorageServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageApiHandler {

    @Autowired
    CarService carService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteImg(@AuthenticationPrincipal UserDetailImpl userDetail,
                         @RequestBody CarRestApi carRestApi){


        if(!CarController.isOwnerOfCar(userDetail, carService.findById(carRestApi.getCarId()) )){
            log.debug("Not authorized to delete images of car:: " + carRestApi);
            throw new NotAuthorized();
        }

        Company company = userDetail.getCompany();
        FileStorageService fileStorageService = new FileStorageServiceImpl(company);
        String imgPath = Paths.get(fileStorageService.getImagesPath() +
                File.separator + carRestApi.getCarId() + File.separator + carRestApi.getImgName()).toString();

        try {
            fileStorageService.delete(imgPath);
            log.debug("Image deleted:: Car:: " + carRestApi.getCarId() + ":: Image:: " + carRestApi.getImgName());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> getError(Exception exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
