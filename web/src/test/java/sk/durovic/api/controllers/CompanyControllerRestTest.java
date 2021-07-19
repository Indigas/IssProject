package sk.durovic.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.UserDetailsTestService;
import helper.CarBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.security.JwtUtil;
import sk.durovic.services.CarService;
import sk.durovic.services.JwtTokenService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@WebMvcTest(CompanyControllerRest.class)
@Import(UserDetailsTestService.class)
class CompanyControllerRestTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private CarService carService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    private StringBuilder token;
    private ObjectMapper jsonData = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.token = new StringBuilder("Bearer ");
        this.token.append(JwtUtil.createJWTtoken("testUser"));
    }

    @Test
    void getCompanyInfo() throws Exception {
        Mockito.when(jwtTokenService.isValid(Mockito.any())).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/data/company/info")
                .header(HttpHeaders.AUTHORIZATION, token.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Company company = jsonData.readValue(content, Company.class);

        Mockito.verify(jwtTokenService, Mockito.atMostOnce()).isValid(Mockito.any());
        assertThat(company, Matchers.hasProperty("email",
                Matchers.is("testUser")));
    }

    @Test
    void getCarsByCompany() throws Exception {
        Mockito.when(carService.findByCompany(Mockito.any()))
                .thenReturn(Optional.of(List.of(CarBuilder.getCarForTest())));
        Mockito.when(jwtTokenService.isValid(Mockito.any())).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/data/company/cars")
                .header(HttpHeaders.AUTHORIZATION, token.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<Car> cars = (List<Car>) jsonData.readValue(content, List.class);

        Mockito.verify(jwtTokenService, Mockito.atMostOnce()).isValid(Mockito.any());
        Mockito.verify(carService, Mockito.atMostOnce()).findByCompany(Mockito.any());
        assertThat(cars, Matchers.hasSize(1));
    }
}