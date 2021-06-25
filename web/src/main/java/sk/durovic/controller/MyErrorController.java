package sk.durovic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class MyErrorController {

   /* @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView getErrorPage(HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/error/error");
        Object statusCode = response.getStatus();

        log.error("Error status:: " + statusCode);
        modelAndView.addObject("statusCode", statusCode.toString());

        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return null;
    }*/
}
