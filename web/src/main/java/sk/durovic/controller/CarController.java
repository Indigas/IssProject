package sk.durovic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.durovic.commands.CarCommand;
import sk.durovic.converters.CarCommandToCar;
import sk.durovic.model.*;
import sk.durovic.services.CarService;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.data.FileStorageServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


@Controller
public class CarController {

    private final CarService carService;
    private Company company;
    private Car car;

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
        model.addAttribute("carCommand", new CarCommand());
        return "saveCarForm";
    }

    @RequestMapping("/car/new/step-2")
    public String saveImageForm(Model model, @ModelAttribute("carCommand") CarCommand carCommand){
        company = new Company();
        company.setId(1L);
        company.setName("AAA auto");

        // treba carcommand to car
        car = new CarCommandToCar().convert(carCommand);
        car.setCompany(company);
        carService.save(car);
        return "saveCarForm2";
    }

    @PostMapping("/car/new/step-3")
    public String saveImagesToCar(Model model, @RequestParam("imageFiles") MultipartFile... multipartFiles){

        FileStorageService fileStorageService = new FileStorageServiceImpl(company);
        car.setUriImages(fileStorageService.getImagesPath().toString());
        carService.save(car);
        for(MultipartFile multipartFile : multipartFiles){
            try {
                Path path = fileStorageService.save(car, multipartFile.getOriginalFilename(),
                        multipartFile.getBytes());
                 } catch (IOException e){
                e.printStackTrace();
                System.out.println("Error occured while saving image");
            }
        }

        try {
            model.addAttribute("images", fileStorageService.loadAll(car).collect(Collectors.toList()));
        } catch (IOException e){}

        return "saveCarForm3";
    }

    @PostMapping("/car/new/summary")
    public String carSummary(HttpServletRequest request, Model model){
        Iterator<String> requestItems = request.getParameterNames().asIterator();
        Map<Integer, Double> prices = new TreeMap<>();


        while(requestItems.hasNext()){
            String item = requestItems.next();

            if(item.equals("mainImage")){
                car.setMainImage(request.getParameter(item));
                continue;
            }

            if(item.contains("dayFrom")){
                String number = item.substring(7);
                String day = request.getParameter("dayFrom"+number);
                String price = request.getParameter("dayPrice"+number);

                if(day.isEmpty() || price.isEmpty())
                    continue;


                car.getPrices().add(new Prices.Builder(car).addDay(Integer.parseInt(number))
                .addPrice(Integer.parseInt(price)).build());
            }
        }

        carService.save(car);
        model.addAttribute("car", car);

        return "summaryCarForm";
    }

}
