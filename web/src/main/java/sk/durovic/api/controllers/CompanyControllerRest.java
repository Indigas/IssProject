package sk.durovic.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/data/company")
public class CompanyControllerRest {

    @Autowired
    CarService carService;

    @GetMapping("/info")
    public ResponseEntity<?> getCompanyInfo(@AuthenticationPrincipal UserDetails userDetails){
        Company company = ((UserDetailImpl)userDetails).getCompany();
        company.setListOfCars(null);

        return ResponseEntity.ok(company);
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getCarsByCompany(@AuthenticationPrincipal UserDetails userDetails){
        Optional<List<Car>> list = carService.findByCompany(((UserDetailImpl)userDetails)
                .getCompany());

        return ResponseEntity.ok(list.orElse(new ArrayList<>()));
    }
}
