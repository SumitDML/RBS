package com.dml.project.rbs.exception;

import com.dml.project.rbs.model.response.ResponseModel;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
    private static final String EXCEPTION = "exception :";


    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseModel<?>> handleValidationException(final ValidationException exception) {
        final ResponseModel<?> reponseModel = new ResponseModel<>(HttpStatus.BAD_REQUEST, exception.getCause().getMessage(), null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<Object> ItemNotFoundException(ItemNotFoundException exception) {
        final ResponseModel reponseModel = new ResponseModel(HttpStatus.NOT_FOUND, exception.getMessage(),null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TokenValidationException.class)
    public ResponseEntity<Object> handleTokenValidationException(TokenValidationException exception) {
        final ResponseModel reponseModel = new ResponseModel(HttpStatus.UNAUTHORIZED, exception.getMessage(),null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(value = InvalidArgumentException.class)
    public ResponseEntity<Object> InvalidArgumentException(InvalidArgumentException exception) {
        final ResponseModel reponseModel = new ResponseModel(HttpStatus.BAD_REQUEST ,exception.getMessage(),null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnsupportedFileException.class)
    public ResponseEntity<Object> UnsupportedFileException(UnsupportedFileException exception) {
        final ResponseModel reponseModel = new ResponseModel(HttpStatus.BAD_REQUEST, exception.getMessage(),null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value = UnsupportedFileException.class)
//    public ResponseEntity<Object> customException(UnsupportedFileException exception) {
//        final ResponseModel reponseModel = new ResponseModel(HttpStatus.NOT_FOUND, exception.getMessage(),null,null);
//        return new ResponseEntity<>(reponseModel, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseModel<?>> handleConstraintViolationException(final ConstraintViolationException exception) {
        final ResponseModel<?> reponseModel = new ResponseModel<>(HttpStatus.BAD_REQUEST, exception.getMessage(), null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ResponseModel<?>> SQLIntegrityConstraintViolationException(final SQLIntegrityConstraintViolationException exception) {
        final ResponseModel<?> reponseModel = new ResponseModel<>(HttpStatus.BAD_REQUEST,"Duplicate Entries are being Addedd!", null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<Object> FileNotFoundException(InvalidArgumentException exception) {
        final ResponseModel reponseModel = new ResponseModel(HttpStatus.NOT_FOUND ,exception.getMessage(),null,null);
        return new ResponseEntity<>(reponseModel, HttpStatus.NOT_FOUND);
    }

}