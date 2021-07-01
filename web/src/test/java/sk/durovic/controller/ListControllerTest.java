package sk.durovic.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ListController.class)
class ListControllerTest {

    @MockBean
    private CompanyService companyService;

    @MockBean
    private CarService carService;

    @MockBean
    private AvailabilityService availabilityService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getListGridView() {
    }

    @Test
    void getCarDetail(){

    }

    @Test
    void getListingByDate(){

    }

    @Test
    void getCarsByCompany() {
    }
}