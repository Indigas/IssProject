package sk.durovic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.durovic.commands.CompanyCommand;
import sk.durovic.commands.IndexSearch;
import sk.durovic.model.Company;
import sk.durovic.services.CompanyService;

import java.util.Set;


@Controller
public class IndexController {


    @GetMapping({"/", "/index"})
    public String getIndex(Model model){
        model.addAttribute("indexSearch", new IndexSearch());
        return "index";
    }

    @RequestMapping("/about")
    public String getAbout(){
        return "about";
    }

    @RequestMapping("/register")
    public String getRegister(Model model){
        model.addAttribute("companyCommand", new CompanyCommand());
        return "register";
    }

    @RequestMapping("/reservation")
    public String getReservation(){
        return "reservation1";
    }

    @RequestMapping("/contact")
    public String getContact(){
        return "contact";
    }

    @RequestMapping("/checkout")
    public String getCheckout(){
        return "checkout";
    }

    @RequestMapping("/shopping")
    public String getShopping(){
        return "shopping";
    }

    @RequestMapping("/news")
    public String getNews(){
        return "news";
    }

    @RequestMapping("/news-details")
    public String getNewsDetails(){
        return "news-details";
    }

}
