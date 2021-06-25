package sk.durovic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Controller
public class MyErrorController implements ErrorViewResolver {

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
                msg= "Some error occurred";
        }

        return msg;
    }

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ModelAndView modelAndView = new ModelAndView("/error/error");

        modelAndView.addObject("statusCode", status.value());
        modelAndView.addObject("errorMessage", getErrorMessage(status.value()));

        return modelAndView;
    }
}
