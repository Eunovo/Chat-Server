package com.eunovo.userservice.exceptions.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import java.util.*;
import java.util.stream.Collectors;

import com.eunovo.userservice.exceptions.IllegalParameterException;
import com.eunovo.userservice.models.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<ApiResponse> handleTransactionSystemException(TransactionSystemException ex)
            throws Throwable {
        return this.handleConstraintViolation(((ConstraintViolationException) ex.getRootCause()));
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiError> errors = ex.getConstraintViolations().stream().map((e) -> {
            String className = e.getLeafBean().getClass().getSimpleName();
            String field = this.getField(e);
            ApiError error = new ApiValidationError(className, field, e.getInvalidValue(), e.getMessage());
            return error;
        }).collect(Collectors.toList());
        ApiResponse response = ApiResponse.error("Validation error", errors);
        return buildResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    private String getField(ConstraintViolation ex) {
        Iterator<Path.Node> propertyPath = ex.getPropertyPath().iterator();
        Path.Node lastNode = null;
        while (propertyPath.hasNext()) {
            lastNode = propertyPath.next();
        }
        String field = "";
        if (lastNode != null) {
            field = lastNode.toString();
        }
        return field;
    }

    @ExceptionHandler(IllegalParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse handleIllegalParameter(IllegalParameterException ex) {
        List<ApiError> errors = new ArrayList();
        errors.add(
                new ApiValidationError(ex.getResource(), ex.getField(), ex.getRejectedValue(), ex.getMessage()));
        return ApiResponse.error("Illegal parameter", errors);
    }

    private ResponseEntity<ApiResponse> buildResponseEntity(ApiResponse apiRepsonse, HttpStatus status) {
        return new ResponseEntity<>(apiRepsonse, status);
    }
}