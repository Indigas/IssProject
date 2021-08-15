package sk.durovic.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sk.durovic.api.dto.PricesDto;
import sk.durovic.httpError.BadRequestArguments;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.mappers.PricesMapper;
import sk.durovic.model.BaseEntity;
import sk.durovic.model.Car;
import sk.durovic.model.Prices;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.CarService;
import sk.durovic.services.PricesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/data/car")
@ExposesResourceFor(Prices.class)
public class PricesControllerRest {

    @Autowired
    private PricesService pricesService;

    @Autowired
    private CarService carService;

    @Autowired
    private EntityLinks entityLinks;

    @GetMapping(value = "/{id}/prices", produces = "application/hal+json")
    public CollectionModel<?> getPrices(@PathVariable("id") Long id){
        Optional<List<Prices>> prices = pricesService.findByCarId(id);

        List<PricesDto> pricesDtos = prices.orElseGet(ArrayList::new).stream()
                .map(PricesMapper.INSTANCE::toDto).collect(Collectors.toList());

        List<EntityModel<PricesDto>> list = prices.orElse(new ArrayList<>())
                .stream().map(prices1 -> {
                    EntityModel<PricesDto> entityModel = EntityModel.of(PricesMapper.INSTANCE.toDto(prices1));
                    entityModel.add(linkTo(methodOn(PricesControllerRest.class).getPriceWithId(prices1.getId())).withSelfRel());
                    return entityModel;
                }).collect(Collectors.toList());

        return CollectionModel.of(list);

    }

    @GetMapping(value = "/prices/{priceId}", produces = "application/hal+json")
    public EntityModel<PricesDto> getPriceWithId(
                                                  @PathVariable("priceId") Long priceId){
        Prices price = pricesService.findById(priceId);
        PricesDto pricesDto = PricesMapper.INSTANCE.toDto(price);

        EntityModel<PricesDto> model = EntityModel.of(pricesDto);

        model.add(linkTo(methodOn(PricesControllerRest.class).getPriceWithId(priceId)).withSelfRel());
        model.add(entityLinks.linkToItemResource(Car.class, price.getCar().getId()).withRel("car"));


        return model;
    }

    @DeleteMapping("/prices/{priceId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePriceById(@PathVariable Long priceId, @AuthenticationPrincipal UserDetails userDetails){
        Prices price = pricesService.findById(priceId);

        if (price == null)
            throw new BadRequestArguments();

        Car car = carService.findById(price.getCar().getId());

        if (car.getCompany().getId().equals(((UserDetailImpl)userDetails).getCompany().getId()))
            pricesService.deleteById(priceId);
        else
            throw new NotAuthorized();

    }


}
