package helper;

import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.CarService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CarServiceHelper implements CarService {
    private Long id;
    private Company company;

    public CarServiceHelper(Long id, Company company) {
        this.id = id;
        this.company = company;
    }

    @Override
    public Optional<List<Car>> findByIsEnabled() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Car>> findByCompany(Company company) {
        return Optional.empty();
    }

    @Override
    public Set<Car> findAll() {
        return null;
    }

    @Override
    public Car findById(Long aLong) {
        return new CarBuilder().setId(aLong).setCompany(company).build();
    }

    @Override
    public Car save(Car object) {
        return object;
    }

    @Override
    public void delete(Car object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
