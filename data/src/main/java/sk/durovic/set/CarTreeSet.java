package sk.durovic.set;

import sk.durovic.model.Car;
import sk.durovic.model.Company;

import java.util.TreeSet;

public class CarTreeSet extends TreeSet<Car> {

    private final Company company;

    public CarTreeSet(Company company) {
        this.company = company;
    }

    @Override
    public boolean add(Car car) {
        car.setCompany(company);
        return super.add(car);
    }
}
