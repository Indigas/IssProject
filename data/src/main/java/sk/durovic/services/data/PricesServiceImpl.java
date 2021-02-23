package sk.durovic.services.data;

import org.springframework.stereotype.Service;
import sk.durovic.model.Prices;
import sk.durovic.repositories.PricesRepository;
import sk.durovic.services.PricesService;

import java.util.Set;
import java.util.TreeSet;

@Service
public class PricesServiceImpl implements PricesService {

    private final PricesRepository pricesRepository;

    public PricesServiceImpl(PricesRepository pricesRepository) {
        this.pricesRepository = pricesRepository;
    }

    @Override
    public Set<Prices> findAll() {
        Set<Prices> list = new TreeSet<>();
        pricesRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Prices findById(Long aLong) {
        return pricesRepository.findById(aLong).orElse(null);
    }

    @Override
    public Prices save(Prices object) {
        return pricesRepository.save(object);
    }

    @Override
    public void delete(Prices object) {
        pricesRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        pricesRepository.deleteById(aLong);
    }
}
