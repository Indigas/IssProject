package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Reservation;
import sk.durovic.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setId(1L);
    }

    @Test
    void findAll() {
        Mockito.when(reservationRepository.findAll())
                .thenReturn(List.of(reservation, new Reservation()));

        Set<Reservation> list = reservationService.findAll();

        Mockito.verify(reservationRepository, Mockito.atMostOnce()).findAll();
        assertThat(list, Matchers.hasSize(2));
        assertThat(list, Matchers.hasItem(reservation));
    }

    @Test
    void findById() {
        Mockito.when(reservationRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(reservation));

        Reservation found = reservationService.findById(1L);

        Mockito.verify(reservationRepository, Mockito.atMostOnce()).findById(1L);
        assertThat(found, Matchers.notNullValue());
        assertThat(found, Matchers.is(reservation));
    }

    @Test
    void save() {
        Mockito.when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation saved = reservationService.save(reservation);

        Mockito.verify(reservationRepository, Mockito.atMostOnce()).save(reservation);
        assertThat(saved, Matchers.is(reservation));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(reservationRepository).delete(Mockito.any());

        reservationService.delete(reservation);

        Mockito.verify(reservationRepository, Mockito.atMostOnce()).delete(reservation);
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(reservationRepository).deleteById(Mockito.any());

        reservationService.deleteById(1L);

        Mockito.verify(reservationRepository, Mockito.atMostOnce()).deleteById(1L);

    }

    @Test
    void findByCompanyId() {
        Mockito.when(reservationRepository.findByCompanyId(Mockito.any()))
                .thenReturn(Optional.of(List.of(reservation, new Reservation())));

        List<Reservation> list = reservationService.findByCompanyId(1L).orElse(null);

        Mockito.verify(reservationRepository, Mockito.atMostOnce()).findByCompanyId(1L);
        assertThat(list, Matchers.notNullValue());
        assertThat(list, Matchers.hasSize(2));
        assertThat(list, Matchers.hasItem(reservation));
    }
}