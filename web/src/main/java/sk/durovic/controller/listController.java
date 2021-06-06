package sk.durovic.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.durovic.commands.IndexSearch;
import sk.durovic.model.Car;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Slf4j
@RequestMapping("/list")
@Controller
public class listController {

    private final CompanyService companyService;
    private final CarService carService;
    private final AvailabilityService availabilityService;

    public listController(CompanyService companyService, CarService carService,
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
        model.addAttribute("cars", carService.findAll());
        return "car-list-3col2";
    }

    @PostMapping
    private String getListingByDate(@ModelAttribute IndexSearch indexSearch, Model model){
        log.debug("Start date"+ indexSearch.getStartDate());
        Set<Car> listCars = availabilityService.listOfAvailableCars(carService.findAll(),
                getLocalDateTime(indexSearch.getStartDate(), indexSearch.getStartTime()),
                getLocalDateTime(indexSearch.getEndDate(), indexSearch.getEndTime()));


        model.addAttribute("cars", listCars);
        return "car-list-3col2";
    }

    private LocalDateTime getLocalDateTime(String date, String time){
        String[] dayAndMonth = date.split("\\.");
        String[] hourAndMinute;

        if(!time.equals(""))
            hourAndMinute = time.split(":");
        else
            hourAndMinute = new String[]{"0","0"};

        LocalDate stDate = LocalDate.of(Integer.parseInt("20"+dayAndMonth[2].trim()),
                Integer.parseInt(dayAndMonth[1].trim()), Integer.parseInt(dayAndMonth[0].trim()));
        LocalTime stTime = LocalTime.of(Integer.parseInt(hourAndMinute[0]),
                Integer.parseInt(hourAndMinute[1]));

        log.debug("Finding available cars at: " + stDate.toString() + " / " + stTime.toString());
        return LocalDateTime.of(stDate, stTime);
    }
}
