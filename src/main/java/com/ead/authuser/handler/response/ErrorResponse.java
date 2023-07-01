package com.ead.authuser.handler.response;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(String path, HttpStatus httpStatus, Integer statusCode, String message, List<FieldError> errors, LocalDateTime timestamp) {
}
