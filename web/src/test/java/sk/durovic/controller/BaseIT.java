package sk.durovic.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sk.durovic.repositories.AvailabilityRepository;
import sk.durovic.repositories.CarRepository;
import sk.durovic.repositories.CompanyRepository;
import sk.durovic.repositories.PricesRepository;
import sk.durovic.services.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


public abstract class BaseIT {
    @Autowired
    WebApplicationContext wap;

    MockMvc mockMvc;

    @MockBean
    CarRepository carRepository;

    @MockBean
    CarService carService;

    @MockBean
    CompanyRepository companyRepository;

    @MockBean
    CompanyService companyService;

    @MockBean
    AvailabilityRepository availabilityRepository;

    @MockBean
    AvailabilityService availabilityService;

    @MockBean
    PricesRepository pricesRepository;

    @MockBean
    PricesService pricesService;

    @MockBean
    FileStorageService fileStorageService;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wap)
                .apply(springSecurity()).build();
    }
}
