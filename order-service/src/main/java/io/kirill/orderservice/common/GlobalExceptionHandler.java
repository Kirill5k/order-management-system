package io.kirill.orderservice.common;

import io.kirill.orderservice.common.models.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.joining;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ApiErrorResponse> handleBadRequest(WebExchangeBindException exception) {
    log.error("error validating a request {}", exception.getMessage(), exception);
    String message = exception.getBindingResult().getAllErrors()
      .stream()
      .map(error -> String.format("%s: %s", getFieldName(error), error.getDefaultMessage()))
      .collect(joining(", "));
    return Mono.just(new ApiErrorResponse(message));
  }

  private String getFieldName(ObjectError error) {
    return error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Mono<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
    log.error("error http message not readable: {}, {}", exception.getMessage());
    return Mono.just(new ApiErrorResponse(exception.getMessage()));
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public Mono<ApiErrorResponse> handleGenericException(Exception exception) {
    log.error("unexpected error: {}", exception.getMessage(), exception);
    return Mono.just(new ApiErrorResponse(exception.getMessage()));
  }
}
