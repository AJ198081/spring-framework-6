package dev.aj.exception_handling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<HttpStatus> handleNotFoundException() {
        log.info("Preparing Exception Handler for 'NotFoundException' from Controller Advice.");
        return ResponseEntity.notFound().build();
    }
}
