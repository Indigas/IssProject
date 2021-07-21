package sk.durovic.api.controllers;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.durovic.api.dto.CarDto;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.httpError.NotFound;
import sk.durovic.mappers.CarMapper;
import sk.durovic.model.*;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sk.durovic.helper.CarOwnerHelper.isOwnerOfCar;

@RestController
@Slf4j
@RequestMapping("/api/data/car")
public class CarControllerRest {

    @Autowired
    CarService carService;

    @Autowired
    PricesService pricesService;

    private ObjectMapper jsonData = new ObjectMapper();

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailOfCar(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        Car car = carService.findById(id);

        if(car == null || !car.isEnabled())
            throw new NotFound();
        else if(!isOwnerOfCar(userDetails, car))
            throw new NotAuthorized();

        CarDto carDto = CarMapper.INSTANCE.toDto(car);

        return ResponseEntity.ok(carDto);
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public ResponseEntity<?> createNewCar(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody Car car){
        car.setId(null);
        car.setCompany(((UserDetailImpl)userDetails).getCompany());
        car.setUriImages(null);

        // najskor ulozit car
        Set<Prices> prices = car.getPrices();
        prices.forEach(price -> price.setCar(car));

        Set<Availability> availabilities = car.getRentDates();
        availabilities.forEach(availability -> availability.setCarRented(car));

        Car saved = carService.save(car);

        JsonNode jsonNode = jsonData.valueToTree(CarMapper.INSTANCE.toDto(saved));
        ((ObjectNode)jsonNode).remove("company");

        return ResponseEntity.ok(jsonNode);
    }
}
