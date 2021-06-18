package sk.durovic.services;

import sk.durovic.model.Prices;

import java.util.List;
import java.util.Optional;

public interface PricesService extends CrudService<Prices, Long>{

    Optional<List<Prices>> findByCarId(Long id);
}
