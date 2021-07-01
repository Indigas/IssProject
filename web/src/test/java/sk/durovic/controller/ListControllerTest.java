package sk.durovic.controller;

import helper.CarBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import sk.durovic.commands.IndexSearch;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListController.class)
class ListControllerTest {

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
    void getListGridView() throws Exception {
        Mockito.when(carService.findByIsEnabled()).thenReturn(Optional.of(List.of(new Car())));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/list/car-grid"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attributeExists("priceComparator"))
                .andExpect(status().isOk()).andReturn();

        List<Car> list = (List<Car>) mvcResult.getModelAndView().getModel().get("cars");

        Mockito.verify(carService, Mockito.atMostOnce()).findByIsEnabled();
        assertThat(list, Matchers.notNullValue());
        assertThat(list, Matchers.hasSize(1));
    }

    @Test
    void getAllListings() throws Exception {
        Mockito.when(carService.findByIsEnabled()).thenReturn(Optional.of(List.of(new Car())));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/list"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attributeExists("priceComparator"))
                .andExpect(status().isOk()).andReturn();

        List<Car> list = (List<Car>) mvcResult.getModelAndView().getModel().get("cars");

        Mockito.verify(carService, Mockito.atMostOnce()).findByIsEnabled();
        assertThat(list, Matchers.notNullValue());
        assertThat(list, Matchers.hasSize(1));
    }

    @Test
    void getCarDetail() throws Exception{
        Car car = CarBuilder.getCarForTest();
        car.setEnabled(true);
        Mockito.when(carService.findById(Mockito.any())).thenReturn(car);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/list/detail/"+car.getId()))
                .andExpect(model().attributeExists("car"))
                .andExpect(view().name("car-listing-details"))
                .andExpect(status().isOk()).andReturn();

        Car inModel = (Car) mvcResult.getModelAndView().getModel().get("car");
        Mockito.verify(carService, Mockito.atMostOnce()).findByIsEnabled();
        assertThat(inModel, Matchers.is(car));
    }

    @Test
    void getListingByDate() throws Exception{
        Set<Car> cars = new TreeSet<>();
        cars.add(CarBuilder.getCarForTest());

        Mockito.when(availabilityService.listOfAvailableCars(Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(cars);

        IndexSearch is = new IndexSearch();
        is.setStartDate("12.5.21");
        is.setStartTime("");
        is.setEndDate("13.5.21");
        is.setEndTime("");

        mockMvc.perform(MockMvcRequestBuilders.post("/list")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("indexSearch", is))
                .andExpect(model().attribute("cars", Matchers.hasSize(1)))
                .andExpect(view().name("car-list-3col2"))
                .andExpect(status().isOk());

        Mockito.verify(availabilityService, Mockito.atMostOnce())
                .listOfAvailableCars(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void getCarsByCompany() throws Exception {
        Car car = CarBuilder.getCarForTest();
        car.setEnabled(true);
        Mockito.when(carService.findByCompany(Mockito.any())).thenReturn(Optional.of(List.of(car)));

        mockMvc.perform(MockMvcRequestBuilders.get("/list/company/"+car.getCompany().getId()))
                .andExpect(model().attribute("cars", Matchers.hasSize(1)))
                .andExpect(model().attributeExists("priceComparator"))
                .andExpect(status().isOk()).andExpect(view().name("car-list-3col2"));

        ArgumentCaptor<Company> argumentCaptor = ArgumentCaptor.forClass(Company.class);
        Mockito.verify(carService, Mockito.atMostOnce()).findByCompany(argumentCaptor.capture());
        Company company = argumentCaptor.getValue();

        assertThat(company.getId(), Matchers.is(car.getCompany().getId()));
    }
}