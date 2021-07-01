package sk.durovic.helper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import sk.durovic.httpError.BadRequestArguments;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

class DateTimeHelperTest {

    @Test
    void getLocalDateTimeSuccess(){
        LocalDateTime ldl = DateTimeHelper.getLocalDateTime("5.12.21","10:00");

        assertThat(ldl.getDayOfMonth(), Matchers.is(5));
        assertThat(ldl.getMonthValue(), Matchers.is(12));
        assertThat(ldl.getYear(), Matchers.is(2021));
        assertThat(ldl.getHour(), Matchers.is(10));
        assertThat(ldl.getMinute(), Matchers.is(0));
    }

    @Test
    void getLocalDateTimeWithErrorDate(){
        assertThrows(BadRequestArguments.class, () ->
                DateTimeHelper.getLocalDateTime("5.12", "10:00"));
    }

    @Test
    void getLocalDateTimeWithErrorTime() {
        assertThrows(BadRequestArguments.class, () -> {
                DateTimeHelper.getLocalDateTime("5.12.21", "10");
        });
    }

    @Test
    void getLocalDateTimeWithEmptyTime(){
        LocalDateTime ldl = DateTimeHelper.getLocalDateTime("5.12.21", "");

        assertThat(ldl.getHour(), Matchers.is(0));
        assertThat(ldl.getMinute(), Matchers.is(0));
    }

}