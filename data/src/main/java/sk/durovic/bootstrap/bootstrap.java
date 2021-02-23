package sk.durovic.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.durovic.model.*;
import sk.durovic.repositories.AvailabilityRepository;
import sk.durovic.repositories.CarRepository;
import sk.durovic.repositories.CompanyRepository;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Component
public class bootstrap implements CommandLineRunner {

    private final AvailabilityService availabilityService;
    private final CarService carService;
    private final  CompanyService companyService;

    public bootstrap(AvailabilityService availabilityService, CarService carService, CompanyService companyService) {
        this.availabilityService = availabilityService;
        this.carService = carService;
        this.companyService = companyService;
    }

    @Override
    public void run(String... args) throws Exception {
        Car mod = new Car("Audi", "A4", Fuel.Diesel, Gear.Manual,
                null,76, 2.2d, "Banská Bystrica",
                "AAA auto/skoda_fabia.png", 5, AirCondition.AUTOMATIC,
                "Style", Category.SEDAN, "");



        mod.getPrices().add(new Prices(mod, 1,10));
        mod.getPrices().add(new Prices(mod, 2, 8));
        Company cm = new Company();
        cm.setName("AAA auto");
        mod.setCompany(cm);

        Car mod2 = new Car("BMW", "320d", Fuel.Diesel, Gear.Manual,
                cm, 126, 2.5d, "Prešov",
                "AAA auto/skoda_fabia.png", 5,
                AirCondition.AUTOMATIC, "Extra Plus", Category.COMBI, "");
        mod2.getPrices().add(new Prices(mod2, 1, 15));

        Car mod3 = new Car("Audi", "A5", Fuel.Diesel, Gear.Manual,
                cm, 140, 2.0d, "Banská Bystrica",
                "AAA auto/skoda_fabia.png", 5, AirCondition.AUTOMATIC,
                "Packet Plus", Category.COMBI, "");
        mod3.getPrices().add(new Prices(mod3, 1, 17));

        Availability av = new Availability();
        av.setStart(LocalDateTime.of(2021,Month.FEBRUARY, 18,10,0));
        av.setEnd(LocalDateTime.of(2021, Month.MARCH, 5, 10,5));
        av.setCarRented(mod);

        Availability av2 = new Availability();
        av2.setStart(LocalDateTime.of(2021,Month.FEBRUARY,1,10,0));
        av2.setEnd(LocalDateTime.of(2021, Month.FEBRUARY, 18,10,0));
        av2.setCarRented(mod2);

        mod.getRentDates().add(av);
        mod2.getRentDates().add(av2);
        cm.getListOfCars().add(mod);
        cm.getListOfCars().add(mod2);
        cm.getListOfCars().add(mod3);
        companyService.save(cm);


        System.out.println("Loaded in bootstrap: " + carService.findAll().size());
        System.out.println();

    }
}
