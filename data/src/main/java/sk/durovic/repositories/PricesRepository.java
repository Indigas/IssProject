package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.Prices;

public interface PricesRepository extends CrudRepository<Prices, Long> {
}
