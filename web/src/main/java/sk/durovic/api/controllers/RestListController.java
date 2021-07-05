package sk.durovic.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.durovic.httpError.NotFound;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/list")
public class RestListController {

    private CarService carService;
    private AvailabilityService availabilityService;

    private ObjectMapper jsonData = new ObjectMapper();

    @GetMapping
    public List<Car> getAllListings(){
        List<Car> listOfCars = carService.findByIsEnabled().orElse(null);

        if(listOfCars==null || listOfCars.isEmpty())
            throw new NotFound();

        return listOfCars;

    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<String> getError(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No cars available");
    }
}
