package pl.sg.loans.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.sg.loans.utils.LoansException;

@ControllerAdvice
public class LoansErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {LoansException.class})
    protected ResponseEntity<String> badRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}