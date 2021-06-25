package sk.durovic.httpError;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestArguments extends RuntimeException{
    public BadRequestArguments() {
        super();
    }

    public BadRequestArguments(String message) {
        super(message);
    }

    public BadRequestArguments(String message, Throwable cause) {
        super(message, cause);
    }
}
