package sk.durovic.api.controllers;

import helper.CarBuilder;
import helper.CompanyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.JwtTokenService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@WebMvcTest(RestListController.class)
class RestListControllerTest {
    @MockBean
    private CarService carService;

    @MockBean
    private AvailabilityService availabilityService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllListings() throws Exception {
        Mockito.when(carService.findByIsEnabled()).thenReturn(Optional.of(getListOfCars()));

        mockMvc.perform(get("/api/list"))
                .andExpect(status().isOk());
    }

    private List<Car> getListOfCars(){
        Company company = CompanyBuilder.getCompanyForTest();
        List<Car> list = new ArrayList<>();
        list.add(new CarBuilder().setId(1L).setBrand("Audi").setModel("A4")
                .setCompany(company).build());
        list.add(new CarBuilder().setId(2L).setBrand("BMW").setModel("320D")
                .setCompany(company).build());
        list.add(new CarBuilder().setId(3L).setBrand("Volvo").setModel("XC")
                .setCompany(new CompanyBuilder().setName("RestTest").setEmail("testing")
                .setId(2L).build())
                .build());

        return list;
    }
}