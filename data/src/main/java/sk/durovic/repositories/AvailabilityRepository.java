package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.Availability;

public interface AvailabilityRepository extends CrudRepository<Availability, Long> {
}
