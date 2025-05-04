package com.rootbr.network.adapter.in.rest.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        UUID.randomUUID().toString(),
        404
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<com.rootbr.network.adapter.in.rest.exception.ErrorResponse> handleGenericException(
      Exception ex) {
    String requestId = UUID.randomUUID().toString();

    // Log the exception with the request ID for traceability
    System.err.println("Error ID: " + requestId);
    ex.printStackTrace();

    com.rootbr.network.adapter.in.rest.exception.ErrorResponse errorResponse = new com.rootbr.network.adapter.in.rest.exception.ErrorResponse(
        "An unexpected error occurred",
        requestId,
        500
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
