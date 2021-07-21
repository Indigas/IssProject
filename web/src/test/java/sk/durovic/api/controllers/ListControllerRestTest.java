package sk.durovic.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helper.CarBuilder;
import helper.CompanyBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.api.dto.CarDto;
import sk.durovic.api.dto.PricesDto;
import sk.durovic.commands.IndexSearch;
import sk.durovic.mappers.CarMapper;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.model.Prices;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.JwtTokenService;
import sk.durovic.services.PricesService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@WebMvcTest(ListControllerRest.class)
class ListControllerRestTest {
    @MockBean
    private CarService carService;

    @MockBean
    private AvailabilityService availabilityService;

    @MockBean
    private PricesService pricesService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    private List<Car> list;
    private ObjectMapper jsonData = new ObjectMapper();

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();

        setListOfCars();
    }

    @Test
    void getAllListings() throws Exception {
        Mockito.when(carService.findByIsEnabled()).thenReturn(Optional.of(list));

        MvcResult mvcResult = mockMvc.perform(get("/api/list"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        CarDto[] carList = jsonData.readValue(content, CarDto[].class);

        Mockito.verify(carService,Mockito.atMostOnce()).findByIsEnabled();

        assertEquals(3, carList.length);
    }

    @Test
    void getDetailOfCar() throws Exception {
        Mockito.when(carService.findById(Mockito.any())).thenReturn(list.get(0));
        Mockito.when(pricesService.findByCarId(Mockito.any())).thenReturn(Optional.empty());

        MvcResult mvcResult = mockMvc.perform(get("/api/list/detail/"+list.get(0).getId()))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        CarDto car = jsonData.readValue(content, CarDto.class);

        assertThat(car, Matchers.notNullValue());
        assertEquals(car.getId(), list.get(0).getId());
    }

    @Test
    void getCarsByCompanyId() throws Exception {
        Mockito.when(carService.findByCompany(Mockito.any())).thenReturn(Optional.of(list));

        MvcResult mvcResult = mockMvc.perform(get("/api/list/company/1"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<CarDto> car = (List<CarDto>) jsonData.readValue(content, List.class);

        Mockito.verify(carService, Mockito.atMostOnce()).findByCompany(Mockito.any());
        assertThat(car, Matchers.notNullValue());
        assertThat(car, Matchers.hasSize(2));
    }

    @Test
    void getPricesOfCar() throws Exception {
        Mockito.when(carService.findById(Mockito.any())).thenReturn(list.get(0));
        Mockito.when(pricesService.findByCarId(Mockito.any()))
                .thenReturn(Optional.of(List.of(Prices.builder(null).addPrice(5).addDay(5).build())));

        MvcResult mvcResult =
                mockMvc.perform(get("/api/list/detail/"+ list.get(0).getId()
                        +"/prices")).andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<PricesDto> prices = (List<PricesDto>) jsonData.readValue(content, List.class);

        Mockito.verify(carService, Mockito.atMostOnce()).findById(Mockito.any());
        Mockito.verify(pricesService, Mockito.atMostOnce()).findByCarId(Mockito.any());

        assertThat(prices, Matchers.hasSize(1));
    }

    @Test
    void getAvailableCars() throws Exception {
        Mockito.when(availabilityService.listOfAvailableCars(Mockito.any(),
                Mockito.any(), Mockito.any())).thenReturn(new HashSet<>(list));
        Mockito.when(carService.findByIsEnabled()).thenReturn(Optional.of(list));

        IndexSearch indexSearch = new IndexSearch();
        indexSearch.setStartTime("");
        indexSearch.setStartDate("5.12.21");
        indexSearch.setEndTime("");
        indexSearch.setEndDate("6.12.21");

        MvcResult mvcResult = mockMvc.perform(get("/api/list/available")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData.writeValueAsString(indexSearch)))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<CarDto> carDtos = (List<CarDto>) jsonData.readValue(content, List.class);

        Mockito.verify(availabilityService, Mockito.atMostOnce()).listOfAvailableCars(
                Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(carService, Mockito.atMostOnce()).findByIsEnabled();

        assertThat(carDtos, Matchers.notNullValue());
        assertThat(carDtos, Matchers.hasSize(3));
    }

    @Test
    void getAvailableCarsError() throws Exception {
        Mockito.when(availabilityService.listOfAvailableCars(Mockito.any(),
                Mockito.any(), Mockito.any())).thenReturn(new HashSet<>(list));
        Mockito.when(carService.findByIsEnabled()).thenReturn(Optional.of(list));
        IndexSearch indexSearch = new IndexSearch();

        mockMvc.perform(get("/api/list/available")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData.writeValueAsString(indexSearch)))
                .andExpect(status().isBadRequest());
    }

    private void setListOfCars(){
        Company company = CompanyBuilder.getCompanyForTest();

        list.add(new CarBuilder().setId(1L).setBrand("Audi").setModel("A4")
                .setEnabled(true).setCompany(company).build());
        list.add(new CarBuilder().setId(2L).setBrand("BMW").setModel("320D")
                .setCompany(company).build());
        list.add(new CarBuilder().setId(3L).setBrand("Volvo").setModel("XC")
                .setEnabled(true).setCompany(new CompanyBuilder().setName("RestTest")
                        .setEmail("testing").setId(2L).build())
                .build());

    }
}