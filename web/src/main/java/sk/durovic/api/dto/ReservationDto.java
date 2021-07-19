package sk.durovic.api.dto;

import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long carId;
    private Long companyId;
    private String email;
    private String phone;
}
