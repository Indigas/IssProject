package sk.durovic.controller;

import config.WithMockCustomUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Reservation;
import sk.durovic.services.ReservationService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    private UserDetails user;

    @BeforeEach
    void setUp() {
        try {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException ignored){
        }
    }

    @Test
    @WithMockCustomUser
    void showAccount() throws Exception {
        Mockito.when(reservationService.findByCompanyId(Mockito.any()))
                .thenReturn(Optional.of(List.of(new Reservation(), new Reservation())));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/account")
                .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("company"))
                .andExpect(model().attributeExists("reservations"))
                .andExpect(view().name("myaccount"))
                .andReturn();

        List<Reservation> list = (List<Reservation>) mvcResult.getModelAndView().getModel().get("reservations");

        assertThat(list, Matchers.notNullValue());
        assertThat(list, Matchers.hasSize(2));
    }
}