package sk.durovic.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.UserDetailsTestService;
import helper.CarBuilder;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Car;
import sk.durovic.model.CarRestApi;
import sk.durovic.services.CarService;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.JwtTokenService;
import sk.durovic.services.data.FileStorageServiceImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ImageApiHandler.class)
@Import(UserDetailsTestService.class)
class ImageApiHandlerTest {

    @MockBean
    private CarService carService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper jsonData = new ObjectMapper();
    static private FileStorageService fileStorageService;
    static private CarRestApi carRestApi;
    static private Car car;

    @BeforeAll
    static void init () throws IOException {
        car = CarBuilder.getCarForTest();
        carRestApi = new CarRestApi("xy.txt", 1L);
        fileStorageService = new FileStorageServiceImpl(car.getCompany());

    }

    @WithUserDetails
    @Test
    void deleteImgOk() throws Exception {
        Mockito.when(carService.findById(Mockito.anyLong())).thenReturn(car);

        fileStorageService.save(car, "xy.txt", "test".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/image")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData.writeValueAsString(carRestApi)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithUserDetails
    @Test
    void deleteImgWithNotOwner() throws Exception {
        Car notOwner = new CarBuilder().setIdCompany(2L).build();
        Mockito.when(carService.findById(Mockito.anyLong())).thenReturn(notOwner);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/image")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData.writeValueAsString(carRestApi)))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }
}