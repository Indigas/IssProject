package sk.durovic.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.durovic.model.*;
import sk.durovic.repositories.AvailabilityRepository;
import sk.durovic.repositories.CarRepository;
import sk.durovic.repositories.CompanyRepository;

import java.time.LocalDateTime;
import java.time.Month;

@Component
public class bootstrap implements CommandLineRunner {

    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
    private final AvailabilityRepository availabilityRepository;

    public bootstrap(CarRepository carRepository, CompanyRepository companyRepository, AvailabilityRepository availability) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
        this.availabilityRepository = availability;
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

        Availability av = new Availability();
        av.setStart(LocalDateTime.of(2021,Month.FEBRUARY, 18,10,0));
        av.setEnd(LocalDateTime.of(2021, Month.MARCH, 5, 10,5));
        av.setCarRented(mod);

        Availability av2 = new Availability();
        av2.setStart(LocalDateTime.of(2021,Month.FEBRUARY,1,10,0));
        av2.setEnd(LocalDateTime.of(2021, Month.FEBRUARY, 18,10,0));
        av2.setCarRented(mod2);

        companyRepository.save(cm);
        carRepository.save(mod);
        carRepository.save(mod2);
        availabilityRepository.save(av);
        availabilityRepository.save(av2);


        System.out.println("Loaded in bootstrap: " + carRepository.count());
        System.out.println();

    }
}
