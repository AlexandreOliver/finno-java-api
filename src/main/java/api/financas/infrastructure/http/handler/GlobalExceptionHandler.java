package api.financas.infrastructure.http.handler;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleInvalidCredentials(@NonNull BadCredentialsException exception) {
    return new ErrorResponse("Senha ou Email errados",  HttpStatus.UNAUTHORIZED.value());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse handleInvalidCredentials(@NonNull MethodArgumentNotValidException exception) {

    Map<String, String> fieldsErrors = new HashMap<>();

    exception.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();

      fieldsErrors.put(fieldName, errorMessage);
    });

    return new ValidationErrorResponse("Formato errado", HttpStatus.BAD_REQUEST.value(), fieldsErrors);
  }

  public record ErrorResponse(String message, int status) {
  }

  public record ValidationErrorResponse(String message, int status, Map<String, String> errors) {
  }
}
