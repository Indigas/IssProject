package sk.durovic.api;

import javassist.tools.web.BadHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sk.durovic.httpError.BadRequestArguments;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.model.*;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@Slf4j
@RequestMapping("/api/reservation")
public class ReservationApiHandler {

    @Autowired
    ReservationService reservationService;

    @Autowired
    AvailabilityService availabilityService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createReservation(@RequestBody ReservationRestApi reservationRestApi){
        Reservation reservation = new Reservation();

        reservation.setStartDate(getLocalDateTime(reservationRestApi.getStartDate(), reservationRestApi.getStartTime()));
        reservation.setEndDate(getLocalDateTime(reservationRestApi.getEndDate(), reservationRestApi.getEndTime()));

        reservation.setCarId(reservationRestApi.getCarId());
        reservation.setCompanyId(reservationRestApi.getCompanyId());
        reservation.setEmail(reservationRestApi.getEmail());
        reservation.setPhone(reservationRestApi.getPhone());

        reservationService.save(reservation);

    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservation(@RequestBody Reservation reservation,
                                  @AuthenticationPrincipal UserDetailImpl userDetail){
        if(!reservation.getCompanyId().equals(userDetail.getCompany().getId()))
            throw new NotAuthorized();

        reservationService.deleteById(reservation.getId());
    }

    @PatchMapping("/accept")
    @ResponseStatus(HttpStatus.OK)
    public void acceptReservation(@AuthenticationPrincipal UserDetailImpl userDetail,
                                  @RequestBody Reservation reservation){
        if(!reservation.getCompanyId().equals(userDetail.getCompany().getId()))
            throw new NotAuthorized();

        Reservation temp = new Reservation();
        temp = reservationService.findById(reservation.getId());

        reservationService.deleteById(reservation.getId());
        Availability availability = new Availability();
        availability.setStart(temp.getStartDate());
        availability.setEnd(temp.getEndDate());
        Car car = new Car();
        car.setId(temp.getCarId());
        availability.setCarRented(car);
        availabilityService.save(availability);
    }


    private LocalDateTime getLocalDateTime(String date, String time){
        String[] dayAndMonth = date.split("\\.");
        String[] hourAndMinute;

        time = time.trim();

        if(!time.equals(""))
            hourAndMinute = time.split(":");
        else
            hourAndMinute = new String[]{"0","0"};

        try {
            LocalDate stDate = LocalDate.of(Integer.parseInt("20" + dayAndMonth[2].trim()),
                    Integer.parseInt(dayAndMonth[1].trim()), Integer.parseInt(dayAndMonth[0].trim()));
            LocalTime stTime = LocalTime.of(Integer.parseInt(hourAndMinute[0]),
                    Integer.parseInt(hourAndMinute[1]));


            return LocalDateTime.of(stDate, stTime);
        } catch(ArrayIndexOutOfBoundsException e){
            throw new BadRequestArguments();
        }

    }
}
