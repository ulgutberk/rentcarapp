package com.rentcarapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomExceptions {
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class MissingParameterException extends RuntimeException {
        public MissingParameterException(String message) {super(message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class WrongUUIDFormatException extends RuntimeException {
        public WrongUUIDFormatException(String message) {super(message);
        }
    }

}
