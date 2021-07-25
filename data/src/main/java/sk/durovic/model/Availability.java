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

    public boolean equals(Availability obj) {
        return this.getId().equals(obj.getId());
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
