package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Prices extends BaseEntity implements Comparable<Prices> {

    @ManyToOne
    private Car car;

    private Integer days;
    private Integer price;

    @Override
    public int compareTo(Prices prices) {
        return this.days.compareTo(prices.days);
    }
}
