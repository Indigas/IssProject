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

        Availability av = new Availability(LocalDateTime.now(),
                LocalDateTime.of(2021, Month.MARCH, 5, 10,5),
                mod);

        companyRepository.save(cm);
        carRepository.save(mod);
        carRepository.save(mod2);
        availabilityRepository.save(av);

        av.setStart(LocalDateTime.of(2021,2,1,10,0));
        av.setEnd(LocalDateTime.now());
        av.setId(5);
        availabilityRepository.save(av);


        System.out.println("Loaded in bootstrap: " + carRepository.count());
        System.out.println();

    }
}
