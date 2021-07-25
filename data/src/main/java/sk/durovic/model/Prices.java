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

    public static class Builder {
        private int day;
        private int price;
        private Car car;

        private Builder(Car car) {
            day=0;
            price = 0;
            this.car = car;
        }

        public Builder addDay(int fromDay){
            day=fromDay;
            return this;
        }

        public Builder addPrice(int price){
            this.price = price;
            return this;
        }

        public Prices build(){
            Prices price = new Prices();
            price.setDays(this.day);
            price.setPrice(this.price);
            price.setCar(car);
            return price;
        }
    }

    @ManyToOne
    private Car car;

    private Integer days;
    private Integer price;

    @Override
    public int compareTo(Prices prices) {
        return this.days.compareTo(prices.getDays());
    }

    public static Builder builder(Car car){
        return new Builder(car);
    }

    public boolean equals(Prices obj) {
        return this.getId().equals(obj.getId()) ||
                this.getDays().equals(obj.getDays());
    }
}
