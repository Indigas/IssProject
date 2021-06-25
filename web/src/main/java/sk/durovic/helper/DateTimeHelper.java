package sk.durovic.helper;

import sk.durovic.httpError.BadRequestArguments;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DateTimeHelper {

    static LocalDateTime getLocalDateTime(String date, String time) {
        String[] dayAndMonth = date.split("\\.");
        String[] hourAndMinute;

        time = time.trim();

        if (!time.equals(""))
            hourAndMinute = time.split(":");
        else
            hourAndMinute = new String[]{"0", "0"};

        try {
            LocalDate stDate = LocalDate.of(Integer.parseInt("20" + dayAndMonth[2].trim()),
                    Integer.parseInt(dayAndMonth[1].trim()), Integer.parseInt(dayAndMonth[0].trim()));
            LocalTime stTime = LocalTime.of(Integer.parseInt(hourAndMinute[0]),
                    Integer.parseInt(hourAndMinute[1]));


            return LocalDateTime.of(stDate, stTime);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BadRequestArguments();
        }

    }
}