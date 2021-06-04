package sk.durovic.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.durovic.model.*;
import sk.durovic.repositories.AvailabilityRepository;
import sk.durovic.repositories.CarRepository;
import sk.durovic.repositories.CompanyRepository;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyCredentialsService;
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
    private final CompanyCredentialsService companyCredentialsService;

    public bootstrap(AvailabilityService availabilityService, CarService carService,
                     CompanyService companyService, CompanyCredentialsService companyCredentialsService) {
        this.availabilityService = availabilityService;
        this.carService = carService;
        this.companyService = companyService;
        this.companyCredentialsService = companyCredentialsService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(companyService.findById(1L)==null) {
            Company company;
            company = new Company();
            company.setName("AAA auto");
            company.setAddress("Kohárova 5");
            company.setCity("Levice");
            company.setEmail("marek@marek.sk");
            company.setPhone("0915225363");
            companyService.save(company);

            companyCredentialsService.save(new CompanyCredentials(company.getEmail(), "$2a$10$GsJxCZwFwzcFinwveGMTuen4.RA3CjIAwpqzKAfUtK.rtmezlNce6"));
        }


    }
}
