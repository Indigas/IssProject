package sk.durovic.httpError;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAuthorized extends RuntimeException{

    public NotAuthorized() {
        super();
    }

    public NotAuthorized(String message) {
        super(message);
    }

    public NotAuthorized(String message, Throwable cause) {
        super(message, cause);
    }
}
