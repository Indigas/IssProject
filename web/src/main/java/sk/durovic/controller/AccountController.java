package sk.durovic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.durovic.model.Company;
import sk.durovic.model.Reservation;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.AvailabilityService;
import sk.durovic.services.ReservationService;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/account")
public class AccountController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    AvailabilityService availabilityService;

    @GetMapping
    public String showAccount(Model model, @AuthenticationPrincipal UserDetailImpl userDetail){
        Company company = userDetail.getCompany();;
        Optional<List<Reservation>> listOfReservations = reservationService.findByCompanyId(company.getId());


        model.addAttribute("company", company);
        model.addAttribute("reservations", listOfReservations.orElse(null));

        return "myaccount";
    }


}
