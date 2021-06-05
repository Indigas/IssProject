package sk.durovic.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sk.durovic.commands.CompanyCommand;
import sk.durovic.converters.CompanyCommandToCompany;
import sk.durovic.model.Company;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyCredentialsService companyCredentialsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register/new")
    public String registerCompany(@ModelAttribute("companyCommand") CompanyCommand companyCommand){

        Company company = new CompanyCommandToCompany().convert(companyCommand);
        CompanyCredentials cc = new CompanyCommandToCompany().convertToCredentials(companyCommand);

        companyService.save(company);
        cc.setPassword(passwordEncoder.encode(cc.getPassword()));
        companyCredentialsService.save(cc);

        log.debug("Company registered: " + cc.getIdCompany());

        return "redirect:/register?successfull";
    }
}
