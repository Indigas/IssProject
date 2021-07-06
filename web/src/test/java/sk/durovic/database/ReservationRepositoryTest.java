package sk.durovic.database;


import config.JwtTokenTestService;
import helper.CompanyBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import sk.durovic.model.Company;
import sk.durovic.model.Reservation;
import sk.durovic.repositories.ReservationRepository;
import sk.durovic.services.JwtTokenService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JwtTokenTestService.class)
public class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation;
    private Company company;

    @BeforeEach
    void setUp() {
        company = CompanyBuilder.getCompanyForTest();
        company.setId(null);
        testEntityManager.persist(company);

        reservation = new Reservation();
        reservation.setEmail("test@test.com");


        testEntityManager.persist(new Reservation());
        testEntityManager.persist(new Reservation());
    }

    @Test
    void findAll(){
        testEntityManager.persist(reservation);

        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.findAll().forEach(reservations::add);

        assertThat(reservations, Matchers.hasSize(3));
        assertThat(reservations, Matchers.hasItem(reservation));
    }

    @Test
    void findById(){
        long id = testEntityManager.persistAndGetId(reservation, Long.class);

        Reservation found = reservationRepository.findById(id).orElse(null);

        assertThat(found, Matchers.samePropertyValuesAs(reservation));
    }

    @Test
    void notFoundById(){
        long id = testEntityManager.persistAndGetId(reservation, Long.class);

        Reservation found = reservationRepository.findById(2000L).orElse(null);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void save(){
        long id = reservationRepository.save(reservation).getId();

        Reservation found = testEntityManager.find(Reservation.class, id);

        assertThat(found, Matchers.samePropertyValuesAs(reservation));
    }

    @Test
    void delete(){
        Reservation saved = testEntityManager.persist(reservation);

        reservationRepository.delete(saved);

        Reservation found = testEntityManager.find(Reservation.class, saved.getId());

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDelete(){
        Reservation saved = testEntityManager.persist(reservation);

        Reservation ab = new Reservation();
        ab.setEmail("ab");
        reservationRepository.delete(ab);

        Reservation found = testEntityManager.find(Reservation.class, saved.getId());

        assertThat(found, Matchers.samePropertyValuesAs(reservation));
    }

    @Test
    void deleteById(){
        long id = reservationRepository.save(reservation).getId();

        reservationRepository.deleteById(id);

        Reservation found = testEntityManager.find(Reservation.class, id);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDeleteById(){
        long id = reservationRepository.save(reservation).getId();

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            reservationRepository.deleteById(2000L);
                });
    }

    @Test
    void findByCompanyId(){
        reservation.setCompanyId(20L);
        testEntityManager.persist(reservation);

        List<Reservation> found = reservationRepository.findByCompanyId(reservation.getCompanyId())
                .orElse(new ArrayList<>());

        assertThat(found, Matchers.hasSize(1));
        assertThat(found, Matchers.hasItem(reservation));
    }

    @Test
    void notFoundByCompanyId(){
        reservation.setCompanyId(20L);
        testEntityManager.persist(reservation);

        List<Reservation> found = reservationRepository.findByCompanyId(3000L)
                .orElse(new ArrayList<>());

        assertThat(found, Matchers.empty());
    }
}
