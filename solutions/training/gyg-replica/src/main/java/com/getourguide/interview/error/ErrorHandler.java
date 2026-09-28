package com.getourguide.interview.error;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Was one catch-all that turned every problem, including a 404, into a 500. */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ActivityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(ActivityNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badParameter(MethodArgumentTypeMismatchException e) {
        return Map.of("error", "invalid value for '" + e.getName() + "'");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> unexpected(Exception e) {
        log.error("Unexpected error", e);
        return Map.of("error", "Something went wrong");
    }
}
