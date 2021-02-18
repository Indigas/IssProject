package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
    private int powerKw;
    private double volume;

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
