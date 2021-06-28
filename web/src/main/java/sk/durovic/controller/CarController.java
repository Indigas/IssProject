package sk.durovic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.durovic.commands.CarCommand;
import sk.durovic.comparators.PricesComparatorByPrice;
import sk.durovic.converters.CarCommandToCar;
import sk.durovic.converters.CarToCarCommand;
import sk.durovic.data.ImagesHandler;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.model.*;
import sk.durovic.services.CarService;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.PricesService;
import sk.durovic.services.data.FileStorageServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/car")
public class CarController {

    private final CarService carService;
    private final PricesService pricesService;

    public CarController(CarService carService, PricesService pricesService) {
        this.carService = carService;
        this.pricesService = pricesService;
    }

     @GetMapping("/detail/{id}")
    public String getCarById(@PathVariable Long id, Model model,
                             @AuthenticationPrincipal UserDetailImpl userDetail) {
        Car car1 = carService.findById(id);

        if(car1==null || !isOwnerOfCar(userDetail, car1)) {
            log.error("Not authorized in Carcontroller for user::"+userDetail);
            throw new NotAuthorized();
        }

        model.addAttribute("car", car1);

        model.addAttribute("prices", car1.getPrices());

        try {
            model.addAttribute("images", ImagesHandler.getImages(car1).collect(Collectors.toList()));
        } catch (IOException e){
            log.error("Error in loading images, car ID: "+e.getMessage());
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
                                @AuthenticationPrincipal UserDetails userDetail,
                                @Nullable @PathVariable("id")Long id) {

        Car car = new CarCommandToCar().convert(carCommand);


        Company company = ((UserDetailImpl)userDetail).getCompany();

        if(id!=null) {
            if (isOwnerOfCar(userDetail, carService.findById(id)))
                car.setId(id);
            else
                throw new NotAuthorized();
        }



        car.setCompany(company);
        car = carService.save(car);

        model.addAttribute("carId", car.getId());

        return "saveCarForm2";
    }

    @PostMapping("/new/step-3/{id}")
    public String saveImagesToCar(@AuthenticationPrincipal UserDetails userDetail, Model model,
                                  @PathVariable("id") Long id,
                                  @RequestParam("imageFiles") MultipartFile... multipartFiles) {

        Car car = carService.findById(id);

        if (!isOwnerOfCar(userDetail, car))
            throw new NotAuthorized();

        FileStorageService fileStorageService = saveImages(userDetail, car, multipartFiles);

        try {
            model.addAttribute("images", fileStorageService.loadAll(car).collect(Collectors.toList()));
        } catch (IOException e) {
            log.error("Error in loading images, car ID: " + car.getId());
            e.printStackTrace();
        }

        Optional<List<Prices>> listOfPrices = pricesService.findByCarId(car.getId());

        model.addAttribute("prices", listOfPrices.orElse(null));
        model.addAttribute("carId", car.getId());

        return "saveCarForm3";
    }

    @PostMapping("/new/summary/{id}")
    public String carSummary(HttpServletRequest request, Model model,
                             @PathVariable("id") Long id,
                             @AuthenticationPrincipal UserDetails userDetail) {

        Car car = carService.findById(id);

        if (!isOwnerOfCar(userDetail, car))
            throw new NotAuthorized();

        setPrices(request, car);

        car = carService.save(car);

        model.addAttribute("car", car);
        model.addAttribute("prices", car.getPrices());

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
                             @AuthenticationPrincipal UserDetails userDetail){
        Car car1 = carService.findById(id);

        if(isOwnerOfCar(userDetail, car1)){
            car1.setEnabled(!car1.isEnabled());
            carService.save(car1);
        } else
            throw new NotAuthorized();


        model.addAttribute("car", car1);
        return "summaryCarForm";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") Long id,
                            @AuthenticationPrincipal UserDetails userDetail){
        Car car1 = carService.findById(id);

        if(!isOwnerOfCar(userDetail, car1))
            throw new NotAuthorized();

        carService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateCar(@PathVariable("id") Long id, Model model,
                            @AuthenticationPrincipal UserDetails userDetail){
        Car car1 = carService.findById(id);

        if(!isOwnerOfCar(userDetail, car1))
            throw new NotAuthorized();

        model.addAttribute("car", car1);
        model.addAttribute("carCommand", new CarToCarCommand().convert(car1));

        return "updateCar";
    }

    @GetMapping("/list")
    public String listOfMyCars(Model model, @AuthenticationPrincipal UserDetails userDetail){
        Optional<List<Car>> listOfCars = carService.findByCompany(((UserDetailImpl)userDetail).getCompany());

        model.addAttribute("cars", listOfCars.orElse(new ArrayList<>()));
        model.addAttribute("priceComparator", new PricesComparatorByPrice());

        return "mycars";
    }


    private void setPrices(HttpServletRequest request, Car car) {
        Iterator<String> requestItems = request.getParameterNames().asIterator();

        Optional<List<Prices>> listOfPrices = pricesService.findByCarId(car.getId());

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

    private FileStorageService saveImages(UserDetails userDetail, Car car, MultipartFile[] multipartFiles) {
        Company company = ((UserDetailImpl)userDetail).getCompany();

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

    public static boolean isOwnerOfCar(UserDetails userDetail, Car car1) {
        if(userDetail==null || !((UserDetailImpl)userDetail).getCompany().getId().equals(car1.getCompany().getId())) {
            log.debug("User not authorized to change car.");
            log.debug("user::"+userDetail);
            return false;
        }

        return true;
    }
}


