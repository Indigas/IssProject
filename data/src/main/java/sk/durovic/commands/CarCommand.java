package sk.durovic.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.durovic.model.AirCondition;
import sk.durovic.model.Category;
import sk.durovic.model.Fuel;
import sk.durovic.model.Gear;

@NoArgsConstructor
@Getter
@Setter
public class CarCommand {
    private String brand;
    private String model;
    private Fuel fuel;
    private Gear gear;
    private int powerKw;
    private double volume;
    private String pickUpCity;
    private String uriImages;
    private int seats;
    private AirCondition airCondition;
    //vybava auta
    private String pack;
    private Category category;
    private String notes;

    @Override
    public String toString() {
        return brand + " " + model + " " + fuel.getValue() + " " + gear.getValue() +
                " " + powerKw + " " + volume + " " + pickUpCity + " " + seats + " " +
                airCondition.getValue() + " " + pack + " " + category.getValue() + " " +
                notes;
    }
}
