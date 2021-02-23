package sk.durovic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.durovic.services.CarService;

@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @RequestMapping("/car/show/{id}")
    public String getCarById(@PathVariable Long id, Model model){
        model.addAttribute("car", carService.findById(id));
        return "car-listing-details";
    }
}
