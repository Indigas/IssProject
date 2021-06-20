package sk.durovic.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reservation extends BaseEntity{

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long carId;
    private Long companyId;

}
