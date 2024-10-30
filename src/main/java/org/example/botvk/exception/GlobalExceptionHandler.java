package org.example.botvk.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<String> handleException(DataValidationException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
