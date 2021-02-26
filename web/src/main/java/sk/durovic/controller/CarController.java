package sk.durovic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.durovic.commands.CarCommand;
import sk.durovic.model.Category;
import sk.durovic.model.Fuel;
import sk.durovic.model.Gear;
import sk.durovic.services.CarService;


@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @RequestMapping("/car/detail/{id}")
    public String getCarById(@PathVariable Long id, Model model){
        model.addAttribute("car", carService.findById(id));

        return "car-listing-details";
    }

    @RequestMapping("/car/new")
    public String saveCarForm(Model model){
        model.addAttribute("car", new CarCommand());
        model.addAttribute("fuelEnum", Fuel.values());
        model.addAttribute("categoryEnum", Category.values());
        model.addAttribute("gearEnum", Gear.values());
        return "saveCarForm";
    }


}
