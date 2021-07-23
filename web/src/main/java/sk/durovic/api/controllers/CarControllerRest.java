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
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.httpError.NotFound;
import sk.durovic.mappers.CarMapper;
import sk.durovic.model.*;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static sk.durovic.helper.CarOwnerHelper.isOwnerOfCar;

@RestController
@Slf4j
@RequestMapping("/api/data/car")
public class CarControllerRest {

    @Autowired
    CarService carService;

    @Autowired
    PricesService pricesService;

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
        car.setCompany(null);
        Set<Prices> pricesSet = new TreeSet<>(car.getPrices());
        car.setPrices(new HashSet<>());
        Set<Availability> availabilitySet = new HashSet<>(car.getRentDates());
        car.setRentDates(new HashSet<>());
        JsonNode jsonNode = jsonData.valueToTree(car);
        ((ObjectNode)jsonNode).remove("company");
        ArrayNode prices = (ArrayNode) jsonNode.path("prices");
        ArrayNode availability = (ArrayNode) jsonNode.path("rentDates");

        for(Prices price : pricesSet) {
            ObjectNode priceNode = prices.addObject();
            priceNode.put("id", price.getId());
            priceNode.put("days", price.getDays());
            priceNode.put("price", price.getPrice());
        }

        for(Availability available : availabilitySet){
            ObjectNode availableNode = availability.addObject();
            availableNode.put("id", available.getId());
            availableNode.put("start", available.getStart().toString());
            availableNode.put("end", available.getEnd().toString());
        }

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

        Car saved = carService.save(car);

        /// same as code above
        car.setCompany(null);
        Set<Prices> pricesSet = new TreeSet<>(car.getPrices());
        car.setPrices(new HashSet<>());
        Set<Availability> availabilitySet = new HashSet<>(car.getRentDates());
        car.setRentDates(new HashSet<>());
        JsonNode jsonNode = jsonData.valueToTree(car);
        ((ObjectNode)jsonNode).remove("company");
        ArrayNode prices = (ArrayNode) jsonNode.path("prices");
        ArrayNode availability = (ArrayNode) jsonNode.path("rentDates");

        for(Prices price : pricesSet) {
            ObjectNode priceNode = prices.addObject();
            priceNode.put("id", price.getId());
            priceNode.put("days", price.getDays());
            priceNode.put("price", price.getPrice());
        }

        for(Availability available : availabilitySet){
            ObjectNode availableNode = availability.addObject();
            availableNode.put("id", available.getId());
            availableNode.put("start", available.getStart().toString());
            availableNode.put("end", available.getEnd().toString());
        }


        return ResponseEntity.ok(jsonNode);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCar(@PathVariable("id") Long id){
        carService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable("id") Long id,
                                       @RequestBody String car) throws IllegalAccessException, JsonProcessingException, NoSuchFieldException {
        Car updateCar = carService.findById(id);
        JsonNode updateFields = jsonData.readTree(car);

        /// prerobit na samostatne funkcie
        Iterator<Map.Entry<String, JsonNode>> it = updateFields.fields();
        while(it.hasNext()){
            Map.Entry<String, JsonNode> map = it.next();

            if(map.getKey().equals("id"))
                continue;

            Field field = Car.class.getDeclaredField(map.getKey());
            field.setAccessible(true);
            Object valueOfField = getValue(field, map);
            field.set(updateCar, valueOfField);
        }



        return ResponseEntity.status(200).body(CarMapper.INSTANCE.toDto(updateCar));
    }

    private Object getValue(Field field, Map.Entry<String, JsonNode> map) throws JsonProcessingException {
        Class<?> clazz = field.getType();

        if(clazz.equals(int.class))
            return map.getValue().asInt();
        else if(clazz.equals(String.class))
            return map.getValue().asText();
        else if(clazz.equals(boolean.class))
            return map.getValue().asBoolean();
        else if(clazz.equals(double.class))
            return map.getValue().asDouble();
        else if(clazz.equals(Fuel.class) || clazz.equals(Category.class)
                || clazz.equals(Gear.class))
            return jsonData.treeToValue(map.getValue(), clazz);
        else
            return null;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> deleteByIdError(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("not exist");
    }


}
