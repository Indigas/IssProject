package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.Prices;

import java.util.List;
import java.util.Optional;

public interface PricesRepository extends CrudRepository<Prices, Long> {

    Optional<List<Prices>> findByCarIdOrderByDaysAsc(Long id);
}
