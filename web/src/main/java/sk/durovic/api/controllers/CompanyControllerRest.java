package sk.durovic.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.durovic.api.dto.CarDto;
import sk.durovic.api.dto.CompanyDto;
import sk.durovic.mappers.CarMapper;
import sk.durovic.mappers.CompanyMapper;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/data/company")
public class CompanyControllerRest {

    @Autowired
    CarService carService;

    @GetMapping("/info")
    public ResponseEntity<?> getCompanyInfo(@AuthenticationPrincipal UserDetails userDetails){
        Company company = ((UserDetailImpl)userDetails).getCompany();
        CompanyDto companyDto = CompanyMapper.INSTANCE.toDto(company);

        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getCarsByCompany(@AuthenticationPrincipal UserDetails userDetails){
        Optional<List<Car>> list = carService.findByCompany(((UserDetailImpl)userDetails)
                .getCompany());

        List<JsonNode> carDtos = list.orElse(new ArrayList<>()).stream().map(CarMapper.INSTANCE::toDto)
                .map(new ObjectMapper()::<JsonNode>valueToTree)
                .collect(Collectors.toList());

        carDtos.forEach(json -> ((ObjectNode)json).remove("company"));

        return ResponseEntity.ok(carDtos);
    }
}
