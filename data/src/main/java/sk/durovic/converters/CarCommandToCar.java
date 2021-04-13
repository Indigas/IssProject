package sk.durovic.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sk.durovic.commands.CarCommand;
import sk.durovic.model.Car;

@Component
public class CarCommandToCar implements Converter<CarCommand, Car> {

    @Override
    public Car convert(CarCommand carCommand) {
        Car car = new Car();
        car.setBrand(carCommand.getBrand());
        car.setModel(carCommand.getModel());
        car.setFuel(carCommand.getFuel());
        car.setGear(carCommand.getGear());
        car.setAirCondition(carCommand.getAirCondition());
        car.setCategory(carCommand.getCategory());
        car.setNotes(carCommand.getNotes());
        car.setPack(carCommand.getPack());
        car.setPickUpCity(carCommand.getPickUpCity());
        car.setPowerKw(carCommand.getPowerKw());
        car.setSeats(carCommand.getSeats());
        car.setVolume(carCommand.getVolume());
        car.setCompany(null);
        car.setUriImages(null);
        return car;
    }
}
