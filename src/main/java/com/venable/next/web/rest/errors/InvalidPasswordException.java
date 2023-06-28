package com.venable.next.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class InvalidPasswordException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(
                HttpStatus.BAD_REQUEST,
                ProblemDetailWithCause.ProblemDetailWithCauseBuilder
                        .instance()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withType(ErrorConstants.INVALID_PASSWORD_TYPE)
                        .withTitle("Incorrect password")
                        .build(),
                null
        );
    }
}
