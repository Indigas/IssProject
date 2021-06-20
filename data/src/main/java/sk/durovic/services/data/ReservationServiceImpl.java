package sk.durovic.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.durovic.model.Reservation;
import sk.durovic.repositories.ReservationRepository;
import sk.durovic.services.ReservationService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Set<Reservation> findAll() {
        Set<Reservation> listOf = new HashSet<>();

        reservationRepository.findAll().forEach(listOf::add);
        return listOf;
    }

    @Override
    public Reservation findById(Long aLong) {
        return reservationRepository.findById(aLong).orElse(null);
    }

    @Override
    public Reservation save(Reservation object) {
        return reservationRepository.save(object);
    }

    @Override
    public void delete(Reservation object) {
        reservationRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        reservationRepository.deleteById(aLong);
    }

    @Override
    public Optional<List<Reservation>> findByCompanyId(Long id) {
        return reservationRepository.findByCompanyId(id);
    }
}
