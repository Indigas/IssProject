package sk.durovic.api.controllers;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring5.expression.Fields;
import sk.durovic.api.dto.CarDto;
import sk.durovic.data.ImagesHandler;
import sk.durovic.helper.DateTimeHelper;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.httpError.NotFound;
import sk.durovic.mappers.CarMapper;
import sk.durovic.model.*;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.PricesService;
import sk.durovic.services.data.FileStorageServiceImpl;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static sk.durovic.helper.CarOwnerHelper.isOwnerOfCar;
import static sk.durovic.helper.DateTimeHelper.getLocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api/data/car")
public class CarControllerRest {

    @Autowired
    CarService carService;

    @Autowired
    PricesService pricesService;

    @Autowired
    AvailabilityService availabilityService;

    private final ObjectMapper jsonData = new ObjectMapper();

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailOfCar(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        Car car = carService.findById(id);

        if(car == null)
            throw new NotFound();
        else if(!isOwnerOfCar(userDetails, car))
            throw new NotAuthorized();

        return ResponseEntity.ok(getJsonNodeFromCar(car));
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public ResponseEntity<?> createNewCar(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody Car car){
        car.setId(null);
        car.setUriImages(null);
        car.setRentDates(new HashSet<>());

        car.setCompany(((UserDetailImpl)userDetails).getCompany());

        if(car.getPrices()!=null) {
            for (Prices price : car.getPrices()) {
                price.setCar(car);
                price.setId(null);
            }
        }

        return ResponseEntity.ok(getJsonNodeFromCar(carService.save(car)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCar(@PathVariable("id") Long id,
                          @RequestBody String jsonBody,
                          @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Car car = carService.findById(id);
        Set<Long> listPrices = pricesService.findAll().stream().map(Prices::getId)
                .collect(Collectors.toSet());
        Set<Long> listRentDates = availabilityService.findAll().stream().map(Availability::getId)
                .collect(Collectors.toSet());

        if(!isOwnerOfCar(userDetails, car))
            throw new NotAuthorized();

        JsonNode jn = jsonData.readTree(jsonBody);
        boolean isEmptyBody=true;


        if(!jn.path("prices").isMissingNode()){
            long[] prices = jsonData.treeToValue(jn.path("prices"), long[].class);
            Arrays.stream(prices).filter(listPrices::contains).forEach(pricesService::deleteById);
            isEmptyBody=false;
        }

        if(!jn.path("rentDates").isMissingNode()){
            long[] rentDates = jsonData.treeToValue(jn.path("rentDates"), long[].class);
            Arrays.stream(rentDates).filter(listRentDates::contains).forEach(availabilityService::deleteById);
            isEmptyBody=false;
        }

        if(!jn.path("images").isMissingNode()){
            String[] images= jsonData.treeToValue(jn.path("images"), String[].class);
            FileStorageService fss = new FileStorageServiceImpl(car.getCompany());
            Set<String> paths = fss.loadAll(car).map(Path::getFileName).map(Path::toString).collect(Collectors.toSet());
            Arrays.stream(images)
                    .filter(paths::contains)
                    .forEach(path -> {
                        try {
                            fss.delete(transformPath(fss, car.getId(), path));
                        } catch (IOException e){
                            log.error("Error in deleting images of car");
                        }
                    });

            isEmptyBody=false;
        }

        if(isEmptyBody)
            carService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable("id") Long id,
                                       @RequestBody String car,
                                       @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException, JsonProcessingException, NoSuchFieldException {
        Car updateCar = carService.findById(id);

        if(!isOwnerOfCar(userDetails, updateCar))
            throw new NotAuthorized();

        JsonNode updateFields = jsonData.readTree(car);

        /// prerobit na samostatne funkcie
        boolean changed = false;
        Iterator<Map.Entry<String, JsonNode>> it = updateFields.fields();

        while(it.hasNext()){
            Map.Entry<String, JsonNode> map = it.next();

            if(map.getKey().equals("id") || map.getKey().equals("company"))
                continue;

            Field field = Car.class.getDeclaredField(map.getKey());
            field.setAccessible(true);
            Object valueOfField = getValue(field, map, updateCar);

            if(valueOfField!=null) {
                field.set(updateCar, valueOfField);
                changed=true;
            }
        }

        if (changed)
            updateCar = carService.save(updateCar);

        return ResponseEntity.status(200).body(getJsonNodeFromCar(updateCar));
    }

    @PostMapping(value = "/{id}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public void uploadImages(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Long id,
                             @RequestParam("images") MultipartFile... files) throws JsonProcessingException {
        Car car = carService.findById(id);

        if(!isOwnerOfCar(userDetails, car))
            throw new NotAuthorized();

        ImagesHandler.saveImages(userDetails, car, files, carService);
    }

    private String transformPath(FileStorageService fss, Long id, String path) {
        return fss.getImagesPath() + File.separator + id.toString() + File.separator + path;
    }

    private Object getValue(Field field, Map.Entry<String, JsonNode> map, Car car) throws JsonProcessingException {
        Class<?> clazz = field.getType();

        switch (map.getKey()){
            case "prices":
                return getPrices(map, car);
            case "rentDates":
                return getRentDates(map, car);
            case "mainImage":
                return new String(File.separator + "companies" + File.separator + car.getCompany().getId()
                        + File.separator + car.getId() + File.separator+map.getValue().asText());

        }
        return jsonData.treeToValue(map.getValue(), clazz);

    }

    private Set<Availability> getRentDates(Map.Entry<String, JsonNode> map, Car car){
        Set<Availability> availabilitySet = car.getRentDates();
        ArrayNode arrayNode = (ArrayNode) map.getValue();

        Iterator<JsonNode> it = arrayNode.iterator();

        while(it.hasNext()){
            JsonNode jn = it.next();
            Availability availability = new Availability();
            availability.setCarRented(car);

            if(jn.path("start").isMissingNode() || jn.path("end").isMissingNode())
                continue;

            if(!jn.path("id").isMissingNode()) {
                availability = car.getRentDates().stream().filter(av -> av.getId().equals(jn.get("id").asLong()))
                        .findFirst().orElse(new Availability());
                availability.setCarRented(car);
            }

            String[] start = jn.get("start").asText().split(" ");
            String[] end = jn.get("end").asText().split(" ");

            if(start.length==1)
                start = new String[]{start[0], ""};

            if(end.length==1)
                end = new String[]{end[0], ""};

            availability.setStart(getLocalDateTime(start[0], start[1]));
            availability.setEnd(getLocalDateTime(end[0], end[1]));

            availabilitySet.add(availability);
        }

        return availabilitySet;
    }

    private Set<Prices> getPrices(Map.Entry<String, JsonNode> map, Car car) {
        Set<Prices> pricesSet = car.getPrices();
        ArrayNode arrayNode = (ArrayNode) map.getValue();

        Iterator<JsonNode> it = arrayNode.iterator();

        while(it.hasNext()){
            JsonNode jn = it.next();
            Prices prices = new Prices();
            prices.setCar(car);

            if(jn.path("days").isMissingNode() ||
                jn.path("price").isMissingNode())
                continue;

            if(!jn.path("id").isMissingNode())
                prices = pricesSet.stream().filter(price -> price.getId().equals(jn.get("id").asLong()))
                    .findFirst().orElse(Prices.builder(car).build());
            else
                prices = pricesSet.stream().filter(price -> price.getDays().equals(jn.get("days").asInt()))
                    .findFirst().orElse(Prices.builder(car).build());

            prices.setDays(jn.get("days").asInt());
            prices.setPrice(jn.get("price").asInt());

            if(prices.getDays()!=null && prices.getPrice()!=null)
                pricesSet.add(prices);
        }

        return pricesSet;
    }

    private JsonNode getJsonNodeFromCar(Car car) {
        car.getCompany().setListOfCars(new HashSet<>());
        Set<Prices> pricesSet = new TreeSet<>(car.getPrices());
        car.setPrices(new HashSet<>());
        Set<Availability> availabilitySet = new HashSet<>(car.getRentDates());
        car.setRentDates(new HashSet<>());
        JsonNode jsonNode = jsonData.valueToTree(car);
        ((ObjectNode) jsonNode).remove("company");
        ArrayNode prices = (ArrayNode) jsonNode.path("prices");
        ArrayNode availability = (ArrayNode) jsonNode.path("rentDates");

        for (Prices price : pricesSet) {
            ObjectNode priceNode = prices.addObject();
            priceNode.put("id", price.getId());
            priceNode.put("days", price.getDays());
            priceNode.put("price", price.getPrice());
        }

        for (Availability available : availabilitySet) {
            ObjectNode availableNode = availability.addObject();
            availableNode.put("id", available.getId());
            availableNode.put("start", available.getStart().toString());
            availableNode.put("end", available.getEnd().toString());
        }

        ArrayNode arrayNode = ((ObjectNode) jsonNode).putArray("images");
        FileStorageService fss = new FileStorageServiceImpl(car.getCompany());
        try {
            fss.loadAll(car).map(Path::toString).forEach(arrayNode::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonNode;
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> deleteByIdError(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("not exist");
    }


}
