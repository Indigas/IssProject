package sk.durovic.converters;

import org.springframework.core.convert.converter.Converter;
import sk.durovic.commands.CarCommand;
import sk.durovic.model.Car;

public class CarToCarCommand implements Converter<Car, CarCommand> {

    @Override
    public CarCommand convert(Car car) {
        CarCommand carCommand = new CarCommand();
        carCommand.setModel(car.getModel());
        carCommand.setBrand(car.getBrand());
        carCommand.setCategory(car.getCategory());
        carCommand.setUriImages(car.getUriImages());
        carCommand.setAirCondition(car.getAirCondition());
        carCommand.setFuel(car.getFuel());
        carCommand.setGear(car.getGear());
        carCommand.setNotes(car.getNotes());
        carCommand.setPack(car.getPack());
        carCommand.setPickUpCity(car.getPickUpCity());
        carCommand.setPowerKw(car.getPowerKw());
        carCommand.setSeats(car.getSeats());
        carCommand.setVolume(car.getVolume());

        return carCommand;
    }
}
