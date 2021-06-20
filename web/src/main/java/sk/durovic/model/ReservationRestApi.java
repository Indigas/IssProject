package sk.durovic.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRestApi {
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private Long carId;
    private Long companyId;
}
