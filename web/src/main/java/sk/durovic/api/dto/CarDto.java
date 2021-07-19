package sk.durovic.api.dto;

import sk.durovic.model.AirCondition;
import sk.durovic.model.Category;
import sk.durovic.model.Fuel;
import sk.durovic.model.Gear;

public class CarDto {
    private Long id;
    private String brand;
    private String model;
    private String fuel;
    private String gear;
    private int powerKw;
    private double volume;
    private String pickUpCity;
    private String uriImages;
    private String mainImage;
    private int seats;
    private AirCondition airCondition;
    private String pack;
    private Category category;
    private String notes;
    private boolean isEnabled;
}
