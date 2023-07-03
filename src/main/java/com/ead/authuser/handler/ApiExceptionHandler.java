package com.ead.authuser.handler;

import com.ead.authuser.dtos.response.FieldErrorResponse;
import com.ead.authuser.exceptions.InvalidPasswordException;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.exceptions.ParamAlreadyExistsException;
import com.ead.authuser.handler.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        final ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                extractErrors(ex.getBindingResult().getFieldErrors()),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    private List<FieldErrorResponse> extractErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream().map(fieldError -> new FieldErrorResponse(
                fieldError.getField(),
                fieldError.getDefaultMessage()))
                .toList();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNotFoundError(NotFoundException ex, HttpServletRequest request) {
        final ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handlerNotFoundError(InvalidPasswordException ex, HttpServletRequest request) {
        final ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ParamAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlerNotFoundError(ParamAlreadyExistsException ex, HttpServletRequest request) {
        final ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
