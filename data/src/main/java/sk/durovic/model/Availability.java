package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Availability extends BaseEntity{

    private LocalDateTime start;
    private LocalDateTime end;

    @ManyToOne
    private Car carRented;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Availability))
            throw new IllegalArgumentException();

        Availability av = (Availability) obj;
        return getId() == av.getId();
    }

    @Override
    public String toString() {
        return "Availability{" +
                "start=" + start +
                ", end=" + end +
                ", carRented=" + carRented.getBrand() +
                '}';
    }
}
