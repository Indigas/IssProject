package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Car extends BaseEntity{

    private String brand;
    private String model;
    private Fuel fuel;
    private Gear gear;

    @ManyToOne
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carRented")
    private final Set<Availability> rentDates = new HashSet<>();

    private int powerKw;
    private double volume;

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
