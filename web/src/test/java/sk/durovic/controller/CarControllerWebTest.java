package sk.durovic.controller;

import config.WithMockCustomUser;
import helper.CarBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Car;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
        car = new CarBuilder().setDefault();

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
    //@WithUserDetails(value = "abc@abc.com", userDetailsServiceBeanName = "getService")
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
    }

    @Test
    @WithMockCustomUser
    public void updatingCarAtStepTwo() throws Exception {
        mockMvc.perform(post("/car/new/step-2/1").with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("saveCarForm2"));
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

    }



}