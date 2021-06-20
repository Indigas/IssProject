package sk.durovic.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sk.durovic.model.Company;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.CompanyService;

@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountApiHandler {

    @Autowired
    CompanyService companyService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateDetails(@AuthenticationPrincipal UserDetailImpl userDetail,
                                 @RequestBody Company company){
        company.setId(userDetail.getCompany().getId());

        companyService.save(company);
    }
}
