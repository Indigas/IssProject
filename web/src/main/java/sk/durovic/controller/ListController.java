package sk.durovic.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.durovic.commands.IndexSearch;
import sk.durovic.comparators.PricesComparatorByPrice;
import sk.durovic.data.ImagesHandler;
import sk.durovic.helper.DateTimeHelper;
import sk.durovic.httpError.NotFound;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/list")
@Controller
public class ListController {

    private final CompanyService companyService;
    private final CarService carService;
    private final AvailabilityService availabilityService;

    public ListController(CompanyService companyService, CarService carService,
                          AvailabilityService availabilityService) {
        this.companyService = companyService;
        this.carService = carService;
        this.availabilityService = availabilityService;
    }

    @RequestMapping("/car-grid")
    public String getListGridView(Model model){
        getAllListings(model);
        return "car-list-grid";
    }

    @GetMapping
    private String getAllListings(Model model){

        model.addAttribute("cars", carService.findByIsEnabled().orElse(new ArrayList<>()));
        model.addAttribute("priceComparator", new PricesComparatorByPrice());

        return "car-list-3col2";
    }

    @GetMapping("/detail/{id}")
    private String getCarDetail(Model model, @PathVariable("id") Long id){
        Car car1 = carService.findById(id);

        if(car1==null || !car1.isEnabled())
            throw new NotFound();

        model.addAttribute("car", car1);
        model.addAttribute("prices", car1.getPrices());
        model.addAttribute("company", car1.getCompany());

        try {
            model.addAttribute("images", ImagesHandler.getImages(car1).collect(Collectors.toList()));
        } catch (IOException e){
            log.error("Error in loading images, car ID: " + car1.getId());
            e.printStackTrace();
        }

        return "car-listing-details";
    };

    @PostMapping
    private String getListingByDate(@ModelAttribute IndexSearch indexSearch, Model model){
        log.debug("ListController::getListingDate::"+indexSearch.toString());

        Set<Car> listCars = availabilityService.listOfAvailableCars(carService.findByIsEnabled()
                        .map(TreeSet::new).orElseGet(TreeSet::new),
                DateTimeHelper.getLocalDateTime(indexSearch.getStartDate(), indexSearch.getStartTime()),
                DateTimeHelper.getLocalDateTime(indexSearch.getEndDate(), indexSearch.getEndTime()));


        model.addAttribute("cars", listCars);
        return "car-list-3col2";
    }

    @GetMapping("/company/{id}")
    public String getCarsByCompany(Model model, @PathVariable("id") Long id){
        Company company = new Company();
        company.setId(id);

        Optional<List<Car>> listOfCars = carService.findByCompany(company);

        if(listOfCars.isEmpty())
            throw new NotFound();

        List<Car> listOfEnabledCars = listOfCars.get().stream()
                .filter(Car::isEnabled).collect(Collectors.toList());


        model.addAttribute("cars", listOfEnabledCars);
        model.addAttribute("priceComparator", new PricesComparatorByPrice());
        return "car-list-3col2";
    }



}
