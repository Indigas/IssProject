package sk.durovic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class IndexControllerIT extends BaseIT{

    @Test
    void getIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk());
    }

    @Test
    void getAbout() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk());
    }

    @Test
    void getRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void getReservation() throws Exception {
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk());
    }

    @Test
    void getContact() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }

    @Test
    void getCheckout() throws Exception {
        mockMvc.perform(get("/checkout"))
                .andExpect(status().isOk());
    }

    @Test
    void getShopping() throws Exception {
        mockMvc.perform(get("/shopping"))
                .andExpect(status().isOk());
    }

    @Test
    void getNews() throws Exception {
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk());
    }

    @Test
    void getNewsDetails() throws Exception {
        mockMvc.perform(get("/news-details"))
                .andExpect(status().isOk());
    }
}