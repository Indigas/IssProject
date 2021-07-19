package sk.durovic.api.dto;

import sk.durovic.model.Car;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class AvailabilityDto {
    private LocalDateTime start;
    private LocalDateTime end;

    private CarDto carRented;
}
