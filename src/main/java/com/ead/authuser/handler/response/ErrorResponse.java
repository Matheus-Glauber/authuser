package com.ead.authuser.handler.response;

import com.ead.authuser.dtos.response.FieldErrorResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(String path, HttpStatus httpStatus, Integer statusCode, String message, List<FieldErrorResponse> errors, LocalDateTime timestamp) {
}
