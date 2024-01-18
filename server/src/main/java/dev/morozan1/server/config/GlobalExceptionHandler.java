package dev.morozan1.server.config;

import dev.morozan1.server.dto.ErrorResponseDto;
import dev.morozan1.server.exception.NoIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponseDto.setReasons(errors);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchElementException(NoSuchElementException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponseDto.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoIdException.class)
    public ResponseEntity<ErrorResponseDto> handleNoIdException(NoIdException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponseDto.setReasons(List.of("Id is missing or can not be parsed to Long"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // DEBUG ONLY
        errorResponseDto.setError(e.getMessage());
        //______________

        //errorResponseDto.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}