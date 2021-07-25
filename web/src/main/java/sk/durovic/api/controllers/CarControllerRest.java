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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring5.expression.Fields;
import sk.durovic.api.dto.CarDto;
import sk.durovic.helper.DateTimeHelper;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.httpError.NotFound;
import sk.durovic.mappers.CarMapper;
import sk.durovic.model.*;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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

        //// refactor code
        JsonNode jsonNode = getJsonNodeFromCar(car);

        return ResponseEntity.ok(jsonNode);
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
                          @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
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

    private Object getValue(Field field, Map.Entry<String, JsonNode> map, Car car) throws JsonProcessingException {
        Class<?> clazz = field.getType();

        if(map.getKey().equals("prices")){
            return getPrices(map, car);
        }

        if(map.getKey().equals("rentDates"))
            return getRentDates(map, car);

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
        car.setCompany(null);
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
        return jsonNode;
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> deleteByIdError(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("not exist");
    }


}
