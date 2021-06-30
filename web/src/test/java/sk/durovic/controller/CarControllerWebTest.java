package sk.durovic.controller;

import config.WithMockCustomUser;
import helper.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.model.Prices;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.MatcherAssert.*;


@WebMvcTest(CarController.class)
@AutoConfigureMockMvc
class CarControllerWebTest {

    @MockBean
    private CarService carService;

    @MockBean
    private PricesService pricesService;

    @Autowired
    private MockMvc mockMvc;

    private Car car;
    private UserDetails user;

    @BeforeEach
    void setUp() {
        car = CarBuilder.getCarForTest();

        Mockito.when(carService.findById(Mockito.any())).thenReturn(car);
        Mockito.when(pricesService.findByCarId(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(carService.save(Mockito.any())).thenReturn(car);

        try {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException ignored){
        }
    }

    @Test
    @WithMockCustomUser
    void showCarDetailPageWithId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/car/detail/1")
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("summaryCarForm"))
                .andExpect(MockMvcResultMatchers.model().attribute("car", Matchers.equalTo(car)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("prices"));

        Mockito.verify(carService,Mockito.atLeast(1)).findById(Mockito.any());
    }

    @Test
    @WithMockCustomUser
    void accessCarDetailAsNotOwnerOfCar() throws Exception {
        car = new CarBuilder(car).setIdCompany(2L).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/car/detail/1")
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @WithAnonymousUser
    public void accessCreatingNewCarAsAnonymous() throws Exception {
        mockMvc.perform(get("/car/new"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void accessCreatingNewCarAsLoggedUser() throws Exception {
        mockMvc.perform(get("/car/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carCommand"))
                .andExpect(view().name("saveCarForm"));
    }

    @Test
    @WithMockCustomUser
    public void continuingCreatingCarInStepTwo() throws Exception {
        mockMvc.perform(post("/car/new/step-2/").with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk()).andExpect(view().name("saveCarForm2"));

        Mockito.verify(carService).save(ArgumentMatchers.any());
    }

    @Test
    @WithMockCustomUser
    public void updatingCarAtStepTwo() throws Exception {
        mockMvc.perform(post("/car/new/step-2/1").with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("saveCarForm2"));

        Mockito.verify(carService).save(car);
    }

    @Test
    @WithMockCustomUser
    public void savingImageFiles() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("imageFiles",
                "img.txt","text/plain", "hello".getBytes());

        mockMvc.perform(multipart("/car/new/step-3/"+car.getId()).file(multipartFile)
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk()).andExpect(view().name("saveCarForm3"));

        Mockito.verify(carService).save(car);

        Path pathToDelete = Paths.get(File.separator + "tmp" + File.separator + "companies" +
                File.separator + "CompanyTest");

        Files.walk(pathToDelete).sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @WithMockCustomUser
    public void savingImagesEmptyParam() throws Exception {

        mockMvc.perform(multipart("/car/new/step-3/"+car.getId())
                .file("imageFiles",new byte[0])
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk()).andExpect(view().name("saveCarForm3"));

        Mockito.verify(carService).save(car);
    }

    @Test
    @WithMockCustomUser
    public void showSummaryPageAsCarOwner() throws Exception{
        mockMvc.perform(post("/car/new/summary/"+car.getId())
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk()).andExpect(view().name("summaryCarForm"));

        Mockito.verify(carService).save(car);
    }

    @Test
    @WithMockCustomUser
    public void showSummaryPageWithNonExistIdParam() throws Exception{
        Mockito.when(carService.findById(30L)).thenReturn(null);

        mockMvc.perform(post("/car/new/summary/30")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser
    public void showSummaryPageWithLetterIdParam() throws Exception {
        mockMvc.perform(post("/car/new/summary/test")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser
    public void addingPricesToCarAsHttpRequest() throws Exception{
        MultiValueMap<String, String> map = PricesHelper.getPricesParams();


        mockMvc.perform(post("/car/new/summary/"+car.getId())
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .params(map))
                .andExpect(status().isOk()).andExpect(view().name("summaryCarForm"));
    }

    @Test
    @WithMockCustomUser
    public void addingPricesToCarWithPricesExist() throws Exception {
        MultiValueMap<String, String> map = PricesHelper.getPricesParams();
        car.getPrices().add(Prices.builder(car).addDay(10).addPrice(2).build());

        MvcResult mvcResult = mockMvc.perform(post("/car/new/summary/"+car.getId())
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .params(map))
                .andExpect(model().attributeExists("prices"))
                .andExpect(status().isOk()).andExpect(view().name("summaryCarForm"))
                .andReturn();

        Set<Prices> prices = (Set<Prices>) mvcResult.getModelAndView().getModel().get("prices");

        assertThat(prices, Matchers.hasSize(2));
        Mockito.verify(carService).save(car);
    }

    @Test
    @WithMockCustomUser
    public void setPublishToggleButton() throws Exception{
        car.setEnabled(true);

        mockMvc.perform(get("/car/publish/"+car.getId())
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk()).andExpect(view().name("summaryCarForm"));

        assertFalse(car.isEnabled());
        Mockito.verify(carService).save(car);
    }

    @Test
    @WithMockCustomUser
    public void getListOfMyCars() throws Exception{
        Car car2 = new CarBuilder().setCompany(car.getCompany()).setId(2L).build();
        Mockito.when(carService.findByCompany(Mockito.any())).thenReturn(Optional.of(List.of(car, car2)));

        MvcResult mvcResult = mockMvc.perform(get(("/car/list"))
                        .with(csrf()).with(SecurityMockMvcRequestPostProcessors.user(user)))
                        .andExpect(status().isOk()).andExpect(view().name("mycars"))
                        .andExpect(model().attributeExists("cars"))
                        .andExpect(model().attribute("priceComparator", Matchers.notNullValue()))
                        .andReturn();

        List<Car> cars = (List<Car>) mvcResult.getModelAndView().getModel().get("cars");

        assertThat(cars, Matchers.hasSize(2));
        assertThat(cars, Matchers.containsInAnyOrder(car, car2));
    }

    @Test
    @WithMockCustomUser
    public void deleteCarPage() throws Exception{
        mockMvc.perform(get("/car/delete/"+car.getId())
                .with(csrf()).with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/list"));

        Mockito.verify(carService).deleteById(car.getId());

    }

    @Test
    @WithMockCustomUser
    public void updateCarPage() throws Exception{
        mockMvc.perform(get("/car/update/"+car.getId())
                .with(csrf()).with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk()).andExpect(view().name("updateCar"))
                .andExpect(model().attribute("car", Matchers.equalTo(car)))
                .andExpect(model().attributeExists("carCommand"));

    }


}