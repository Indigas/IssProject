package sk.durovic.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.durovic.api.dto.CarDto;
import sk.durovic.commands.IndexSearch;
import sk.durovic.helper.DateTimeHelper;
import sk.durovic.mappers.CarMapper;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.model.Prices;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/list")
public class ListControllerRest {

    @Autowired
    private CarService carService;

    @Autowired
    private PricesService pricesService;

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping()
    public ResponseEntity<?> getAllListings(){
        Optional<List<Car>> optionalCars = carService.findByIsEnabled();

        return ResponseEntity.ok(optionalCars.orElse(new ArrayList<>()));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getCarDetail(@PathVariable(value = "id",
            required = true) Long id){

        Car car = carService.findById(id);

        if (car==null || !car.isEnabled())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        CarDto carDto = CarMapper.INSTANCE.toDto(car);
        Optional<List<Prices>> pricesList = pricesService.findByCarId(id);
        Set<Prices> pricesSet = new TreeSet<>(pricesList.orElse(new ArrayList<>()));

        car.setPrices(pricesSet);

        return ResponseEntity.ok(carDto);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<?> getCarsByCompanyId(@PathVariable(value = "id",
            required = true) Long id){

        Company company = new Company();
        company.setId(id);

        Optional<List<Car>> carList = carService.findByCompany(company);

        if(carList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<Car> enabledCars = carList.orElse(new ArrayList<>()).stream()
                .filter(Car::isEnabled).collect(Collectors.toList());

        enabledCars.forEach(car -> car.setPrices(null));

        return ResponseEntity.ok(enabledCars);
    }

    @GetMapping("/detail/{id}/prices")
    public ResponseEntity<?> getPriceOfCar(@PathVariable(value = "id",
            required = true) Long id){

        Car car = carService.findById(id);

        if (car == null || !car.isEnabled())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Optional<List<Prices>> prices = pricesService.findByCarId(id);

        return ResponseEntity.ok(prices.orElse(new ArrayList<>()));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCars(@RequestBody IndexSearch indexSearch) throws JsonProcessingException {

        try {
            Set<Car> carSet = availabilityService.listOfAvailableCars(new TreeSet<>(carService.findByIsEnabled()
                            .orElse(new ArrayList<>())),
                    DateTimeHelper.getLocalDateTime(indexSearch.getStartDate(), indexSearch.getStartTime()),
                    DateTimeHelper.getLocalDateTime(indexSearch.getEndDate(), indexSearch.getEndTime()));

            return ResponseEntity.ok(carSet);

        } catch (NullPointerException e){
            IndexSearch indexSearch1 = new IndexSearch();
            indexSearch1.setStartDate("5.12.21");
            indexSearch1.setEndDate("12.12.21");
            indexSearch1.setStartTime("10:00");
            indexSearch1.setEndTime("");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad parameter date/time for search cars at your requested date. " +
                            "Must be string and in followed formats:" +
                            "\nDate is required in format d.m.yy (like 5.12.21)" +
                            "\nTime is not required. Format: HH:mm (like 10:00) or can be empty string (\"\")" +
                            "\n\n\nExample:\n" +
                            new ObjectMapper().writeValueAsString(indexSearch1));
        }

    }
}
