package sk.durovic.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.durovic.services.CarService;
import sk.durovic.services.CompanyService;

@RequestMapping("/list")
@Controller
public class listController {

    private final CompanyService companyService;
    private final CarService carService;

    public listController(CompanyService companyService, CarService carService) {
        this.companyService = companyService;
        this.carService = carService;
    }

    @GetMapping
    private String getAllListings(Model model){
        model.addAttribute("cars", carService.findAll());
        return "car-list-3col2";
    }

    @PostMapping
    private String getListingByDate(Model model){
        return "car-list-3col2";
    }
}
