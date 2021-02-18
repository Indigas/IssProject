package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

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
    private Set<Car> listOfCars;

    @Override
    public String toString() {
        return "Company{" +
                "name=" + name + "}";
    }
}
