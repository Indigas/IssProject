package sk.durovic.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AvailabilityDto {
    private LocalDateTime start;
    private LocalDateTime end;

    private CarDto carRented;
}
