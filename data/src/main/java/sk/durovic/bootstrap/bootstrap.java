package sk.durovic.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.model.Fuel;
import sk.durovic.model.Gear;
import sk.durovic.repositories.CarRepository;
import sk.durovic.repositories.CompanyRepository;

@Component
public class bootstrap implements CommandLineRunner {

    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;

    public bootstrap(CarRepository carRepository, CompanyRepository companyRepository) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Car mod = new Car("Audi", "A4", Fuel.Diesel, Gear.Manual, null, 76, 2.2d);
        Company cm = new Company();
        cm.setName("AAA auto");
        mod.setCompany(cm);

        Car mod2 = new Car();
        mod2.setBrand("BMW");
        mod2.setCompany(cm);

        companyRepository.save(cm);
        carRepository.save(mod);
        carRepository.save(mod2);

        System.out.println("Loaded in bootstrap: " + carRepository.count());
    }
}
