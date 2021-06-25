package sk.durovic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import sk.durovic.httpError.BadRequestArguments;
import sk.durovic.httpError.NotAuthorized;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NotAuthorized.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleNotAuthorized(){
        log.debug("Handling NotAuthorized::ExceptionHandler");
        log.debug("Not authorized. Redirecting to login page...");

    }

    @ExceptionHandler(BadRequestArguments.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleGlobalExceptions(Exception exception){
        log.error("Handling BadRequestArguments::ExceptionHandler::Exception::"+exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error/errorGlobalPage");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }


}
