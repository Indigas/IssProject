package sk.durovic.httpError;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason = "Bad arguments")
public class BadRequestArguments extends RuntimeException{
}
