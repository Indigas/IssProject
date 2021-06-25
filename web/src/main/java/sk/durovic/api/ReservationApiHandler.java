package sk.durovic.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sk.durovic.helper.DateTimeHelper;
import sk.durovic.httpError.BadRequestArguments;
import sk.durovic.httpError.NotAuthorized;
import sk.durovic.model.*;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.ReservationService;

import java.time.LocalDateTime;

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

        reservation.setStartDate(DateTimeHelper.getLocalDateTime(reservationRestApi.getStartDate(), reservationRestApi.getStartTime()));
        reservation.setEndDate(DateTimeHelper.getLocalDateTime(reservationRestApi.getEndDate(), reservationRestApi.getEndTime()));

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

    @ExceptionHandler({BadRequestArguments.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> getError(BadRequestArguments exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
