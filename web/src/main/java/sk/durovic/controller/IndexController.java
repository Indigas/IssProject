package sk.durovic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.durovic.model.Company;
import sk.durovic.services.CompanyService;

import java.util.Set;


@Controller
public class IndexController {

    private final CompanyService companyService;

    public IndexController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndex(Model model){
        Set<Company> listOfCompanies = companyService.findAll();
        model.addAttribute("companies", listOfCompanies);
        return "index";
    }
}
