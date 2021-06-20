package sk.durovic.services;

import sk.durovic.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService extends CrudService<Reservation, Long> {

    Optional<List<Reservation>> findByCompanyId(Long id);
}
