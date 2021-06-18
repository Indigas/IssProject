package sk.durovic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.durovic.commands.CarCommand;
import sk.durovic.converters.CarCommandToCar;
import sk.durovic.converters.CarToCarCommand;
import sk.durovic.data.ImagesHandler;
import sk.durovic.model.*;
import sk.durovic.services.CarService;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.PricesService;
import sk.durovic.services.data.FileStorageServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/car")
public class CarController {

    private final CarService carService;
    private final PricesService pricesService;
    private Car car;

    public CarController(CarService carService, PricesService pricesService) {
        this.carService = carService;
        this.pricesService = pricesService;
    }

     @GetMapping("/detail/{id}")
    public String getCarById(@PathVariable Long id, Model model,
                             @AuthenticationPrincipal UserDetailImpl userDetail) {
        Car car1 = carService.findById(id);

        if(!isOwnerOfCar(userDetail, car1))
            return "redirect:/error?NotAuthorized";

        model.addAttribute("car", car1);

        try {
            model.addAttribute("images", ImagesHandler.getImages(car1).collect(Collectors.toList()));
        } catch (IOException e){
            log.error("Error in loading images, car ID: " + car1.getId());
            e.printStackTrace();
        }

        return "summaryCarForm";
    }

    @GetMapping("/new")
    public String saveCarForm(Model model) {
        model.addAttribute("carCommand", new CarCommand());
        return "saveCarForm";
    }

    @PostMapping({"/new/step-2", "/new/step-2/{id}"})
    public String saveImageForm(Model model, @ModelAttribute("carCommand") CarCommand carCommand,
                                @AuthenticationPrincipal UserDetailImpl userDetail,
                                @PathVariable("id")Long id) {

        car = new CarCommandToCar().convert(carCommand);

        Company company = userDetail.getCompany();

        if(id!=null && isOwnerOfCar(userDetail, carService.findById(id)))
            car.setId(id);

        car.setCompany(company);
        carService.save(car);
        return "saveCarForm2";
    }

    @PostMapping("/new/step-3")
    public String saveImagesToCar(@AuthenticationPrincipal UserDetailImpl userDetail, Model model,
                                  @RequestParam("imageFiles") MultipartFile... multipartFiles) {

        FileStorageService fileStorageService = saveImages(userDetail, multipartFiles);

        try {
            model.addAttribute("images", fileStorageService.loadAll(car).collect(Collectors.toList()));
        } catch (IOException e) {
            log.error("Error in loading images, car ID: " + car.getId());
            e.printStackTrace();
        }

        Optional<List<Prices>> listOfPrices = pricesService.findByCarId(car.getId());

        model.addAttribute("prices", listOfPrices.orElse(null));

        return "saveCarForm3";
    }

    @PostMapping("/new/summary")
    public String carSummary(HttpServletRequest request, Model model) {

        setPrices(request, car);

        carService.save(car);

        model.addAttribute("car", car);

        try {
            model.addAttribute("images", ImagesHandler.getImages(car).collect(Collectors.toList()));
        } catch (IOException e){
            log.error("Error in loading images, car ID: " + car.getId());
            e.printStackTrace();
        }


        return "summaryCarForm";
    }

    @GetMapping({"/publish/{id}", "/unpublish/{id}"})
    public String publishCar(@PathVariable("id") Long id, Model model,
                             @AuthenticationPrincipal UserDetailImpl userDetail){
        Car car1 = carService.findById(id);

        if(isOwnerOfCar(userDetail, car1)){
            car1.setEnabled(!car1.isEnabled());
            carService.save(car1);
        } else
            return "redirect:/error?NotAuthorized";


        model.addAttribute("car", car1);
        return "summaryCarForm";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") Long id,
                            @AuthenticationPrincipal UserDetailImpl userDetail){
        Car car1 = carService.findById(id);

        if(!isOwnerOfCar(userDetail, car1))
            return "redirect:/error?NotAuthorized";

        carService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateCar(@PathVariable("id") Long id, Model model,
                            @AuthenticationPrincipal UserDetailImpl userDetail){
        Car car1 = carService.findById(id);

        if(!isOwnerOfCar(userDetail, car1))
            return "redirect:/error?NotAuthorized";

        model.addAttribute("car", car1);
        model.addAttribute("carCommand", new CarToCarCommand().convert(car1));

        return "updateCar";
    }

    private void setPrices(HttpServletRequest request, Car carPrices) {
        Iterator<String> requestItems = request.getParameterNames().asIterator();

        Optional<List<Prices>> listOfPrices = pricesService.findByCarId(carPrices.getId());

        listOfPrices.ifPresent(prices -> prices.forEach(price -> {
            pricesService.deleteById(price.getId());
        }));

        while (requestItems.hasNext()) {
            String item = requestItems.next();
            log.debug("Request parameter name:: " + item);

            if (item.equals("mainImage")) {
                car.setMainImage(request.getParameter(item));
                continue;
            }

            if (item.contains("dayFrom")) {
                String number = item.substring(7);
                String day = request.getParameter("dayFrom" + number);
                String price = request.getParameter("dayPrice" + number);

                if (day.isEmpty() || price.isEmpty())
                    continue;

                log.debug("Prices to persist:: " + day + " day :: " + price + " price");

                car.getPrices().add(Prices.builder(car).addDay(Integer.parseInt(day))
                        .addPrice(Integer.parseInt(price)).build());
            }
        }
    }

    private FileStorageService saveImages(UserDetailImpl userDetail, MultipartFile[] multipartFiles) {
        Company company = userDetail.getCompany();

        FileStorageService fileStorageService = new FileStorageServiceImpl(company);
        car.setUriImages(fileStorageService.getImagesPath().toString());
        carService.save(car);
        for (MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.isEmpty())
                continue;

            try {
                Path path = fileStorageService.save(car, multipartFile.getOriginalFilename(),
                        multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Error occured while saving image");
            }
        }
        return fileStorageService;
    }

    private boolean isOwnerOfCar(UserDetailImpl userDetail, Car car1) {
        if(userDetail==null || !userDetail.getCompany().getId().equals(car1.getCompany().getId())) {
            log.debug("User not authorized to change car.");
            return false;
        }

        return true;
    }
}


