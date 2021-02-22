package sk.durovic.services.data;

import org.springframework.stereotype.Service;
import sk.durovic.model.Car;
import sk.durovic.repositories.CarRepository;
import sk.durovic.services.CarService;

import java.util.HashSet;
import java.util.Set;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Set<Car> findAll() {
        Set<Car> list = new HashSet<>();
        carRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Car findById(Long aLong) {
        return carRepository.findById(aLong).orElse(null);
    }

    @Override
    public Car save(Car object) {
        return carRepository.save(object);
    }

    @Override
    public void delete(Car object) {
        carRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        carRepository.deleteById(aLong);
    }


}
