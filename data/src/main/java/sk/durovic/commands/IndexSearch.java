package sk.durovic.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IndexSearch {
    private String startDate;
    private String endDate;
    private String pickOff;
    private String dropOff;
    private String startTime;
    private String endTime;

}
