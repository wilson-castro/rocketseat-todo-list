package br.com.wilsoncastro.todolist.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
  
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handlerHttpMessageNotReadbleException(HttpMessageNotReadableException e) {
    return ResponseEntity.badRequest().body(e.getMostSpecificCause().getMessage());
  }
}
