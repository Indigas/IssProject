package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company extends BaseEntity implements Comparable<Company>{

    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private final Set<Car> listOfCars = new TreeSet<>();

    @Override
    public String toString() {
        return "Company{" +
                "name=" + name + "}";
    }

    @Override
    public int compareTo(Company company) {

        return this.name.compareTo(company.getName());
    }


}
