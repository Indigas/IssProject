package sk.durovic.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long carId;
    private Long companyId;
    private String email;
    private String phone;
}
