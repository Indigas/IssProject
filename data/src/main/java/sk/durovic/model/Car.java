package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Car extends BaseEntity implements Comparable<Car>{

    private String brand;
    private String model;
    private Fuel fuel;
    private Gear gear;

    @ManyToOne
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carRented")
    private final Set<Availability> rentDates = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    private final Set<Prices> prices = new TreeSet<>();

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
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Car))
            throw new IllegalArgumentException();

        Car toCompareCar = (Car) obj;
        return getId().equals(toCompareCar.getId());
    }

    @Override
    public int compareTo(Car car) {
        if(this.brand.equals(car.brand))
            return this.model.compareTo(car.model);

        return this.brand.compareTo(car.brand);
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", fuel=" + fuel +
                ", gear=" + gear +
                ", company=" + company +
                ", powerKw=" + powerKw +
                ", volume=" + volume +
                '}';
    }
}
