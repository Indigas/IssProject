package sk.durovic.api.dto;

import lombok.Getter;
import lombok.Setter;
import sk.durovic.model.*;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
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

    private CompanyDto company;
    private Set<AvailabilityDto> rentDates;
    private Set<PricesDto> prices;
}
