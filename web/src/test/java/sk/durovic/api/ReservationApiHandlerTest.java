package sk.durovic.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Reservation;
import sk.durovic.model.ReservationRestApi;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.ReservationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationApiHandler.class)
class ReservationApiHandlerTest {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private AvailabilityService availabilityService;

    @Autowired
    private MockMvc mockMvc;

    private ReservationRestApi reservationRestApi;
    private Reservation reservation;
    private UserDetails user;
    private ObjectMapper jsonData;

    @BeforeEach
    void setUp() {
        reservationRestApi = new ReservationRestApi();
        reservationRestApi.setCarId(1L);
        reservationRestApi.setEmail("test@test.com");
        reservationRestApi.setPhone("123456");
        reservationRestApi.setStartDate("5.12.21");
        reservationRestApi.setEndDate("10.12.21");
        reservationRestApi.setStartTime("");
        reservationRestApi.setEndTime("");

        reservation = new Reservation();
        reservation.setId(5L);
        reservation.setCompanyId(1L);

        jsonData=jsonData = new ObjectMapper();
        }

    @Test
    void createReservation() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation/create")
                //.header("Content-type","application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonData.writeValueAsString(reservationRestApi)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    void deleteReservation() throws Exception {
        Mockito.doNothing().when(reservationService).deleteById(Mockito.any());
        user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservation/delete")
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .header("Content-type", "application/json")
                .accept("application/json").content(jsonData.writeValueAsString(reservation)))
                .andExpect(status().isOk());

        Mockito.verify(reservationService, Mockito.atMostOnce())
                .deleteById(reservation.getId());
    }

    @Test
    @WithMockCustomUser
    void deleteReservationAsNotOwner() throws Exception{
        Mockito.doNothing().when(reservationService).deleteById(Mockito.any());
        user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        reservation.setCompanyId(3L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservation/delete")
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData.writeValueAsString(reservation)))
                .andExpect(status().is(403));

        Mockito.verify(reservationService, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    @WithMockCustomUser
    void acceptReservation() throws Exception{
        Mockito.when(reservationService.findById(Mockito.any()))
                .thenReturn(reservation);
        Mockito.doNothing().when(reservationService).deleteById(Mockito.any());
        user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation/accept")
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData.writeValueAsString(reservation)))
                .andExpect(status().isOk());

        Mockito.verify(reservationService, Mockito.atMostOnce())
                .findById(reservation.getId());
        Mockito.verify(reservationService, Mockito.atMostOnce())
                .deleteById(reservation.getId());
    }

    @Test
    @WithMockCustomUser
    void acceptReservationAsNotOwner() throws Exception{
        Mockito.when(reservationService.findById(Mockito.any()))
                .thenReturn(reservation);
        Mockito.doNothing().when(reservationService).deleteById(Mockito.any());
        user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        reservation.setCompanyId(3L);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation/accept")
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData.writeValueAsString(reservation)))
                .andExpect(status().is(403));

        Mockito.verify(reservationService,Mockito.never()).findById(Mockito.any());
        Mockito.verify(reservationService, Mockito.never()).deleteById(Mockito.any());
    }

}