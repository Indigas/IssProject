package sk.durovic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class CarControllerTest extends BaseIT {

    @Test
    void getCarById() {
    }

    @Test
    void saveCarForm() throws Exception {
        mockMvc.perform(get("/car/new").with(httpBasic("marek", "spring")))
                .andExpect(status().isOk())
                .andExpect(view().name("saveCarForm"))
                .andExpect(model().attributeExists("carCommand"));
    }

    @Test
    void saveImageForm() {

    }

    @Test
    void saveImagesToCar() {
    }

    @Test
    void carSummary() {
    }
}