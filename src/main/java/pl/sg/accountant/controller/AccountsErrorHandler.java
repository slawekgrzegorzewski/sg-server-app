package pl.sg.accountant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.sg.accountant.model.AccountsException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class AccountsErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AccountsException.class, ConstraintViolationException.class})
    protected ResponseEntity<String> badRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}