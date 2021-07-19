package sk.durovic.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.httpError.NotFound;
import sk.durovic.model.Car;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import static sk.durovic.helper.CarOwnerHelper.isOwnerOfCar;

@RestController
@Slf4j
@RequestMapping("/api/data/car")
public class CarControllerRest {

    @Autowired
    CarService carService;

    @Autowired
    PricesService pricesService;

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailOfCar(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserDetails userDetails){
        Car car = carService.findById(id);

        if(car == null || !car.isEnabled())
            throw new NotFound();
        else if(!isOwnerOfCar(userDetails, car))
            throw new NotAuthorized();

        car.setCompany(null);

        return ResponseEntity.ok(car);
    }
}
