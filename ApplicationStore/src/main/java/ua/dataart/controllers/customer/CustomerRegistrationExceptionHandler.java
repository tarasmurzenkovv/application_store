package ua.dataart.controllers.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
public class CustomerRegistrationExceptionHandler {

    private final static String ERROR_SUFFIX = "Error";
    private final static String GENERAL_ERROR_LABEL_NAME = "globalServerError";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        String errorFieldName;

        Map<String, String> errorMessagesForFields = new HashMap<>();

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        for(ObjectError objectError:errors){
            errorFieldName =  ((FieldError) objectError).getField() + ERROR_SUFFIX;
            errorMessagesForFields.put(errorFieldName, objectError.getDefaultMessage());
        }
        return new ResponseEntity<>(errorMessagesForFields, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CredentialsAreInDataBaseException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> credentialsAreInDataBasesException(CredentialsAreInDataBaseException e) {
        Map<String, String> errorFieldAndErrorMessage = new HashMap<>();
        errorFieldAndErrorMessage.put(GENERAL_ERROR_LABEL_NAME, e.getMessage());
        return new ResponseEntity<>(errorFieldAndErrorMessage, HttpStatus.NOT_FOUND);
    }
}
