package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.durovic.set.CarTreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company extends BaseEntity {

    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private final Set<Car> listOfCars = new CarTreeSet(this);

    @Override
    public String toString() {
        return "Company{" +
                "name=" + name + "}";
    }
}
