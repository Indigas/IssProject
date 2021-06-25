package sk.durovic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public ModelAndView getErrorPage(HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/error/error");
        Object statusCode = response.getStatus();
        String errorMessage;

        log.error("Error status:: " + statusCode);
        errorMessage = getErrorMessage(Integer.parseInt(statusCode.toString()));

        modelAndView.addObject("statusCode", statusCode.toString());
        modelAndView.addObject("errorMessage", errorMessage);

        return modelAndView;
    }

    private String getErrorMessage(int errorMsg) {
        String msg;
        switch (errorMsg){
            case 403: msg= "Not Authorized";
            break;
            case 404: msg= "Not Found";
            break;
            case 500: msg= "Internal server error";
            break;
            default:
                msg= "Some error occured";
        }

        return msg;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
