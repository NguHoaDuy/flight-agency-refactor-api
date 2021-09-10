package vn.nhd.flightagency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.nhd.flightagency.exception.extension.BadCredentialException;
import vn.nhd.flightagency.exception.extension.SocialBadCredentialException;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SocialBadCredentialException.class)
    public ResponseEntity<?> handleSocialCredentialException(SocialBadCredentialException ex, WebRequest request) {
        String SIGNUP_URI = "http://localhost:4200/auth/sign-in";
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(SIGNUP_URI + "?" + "social=signed"))
                .build();
    }

    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<?> handleCredentialException(BadCredentialException ex, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .timestamp(LocalDate.now())
                .details(request.getDescription(false))
                .build();
        if (ex.getResult() != null)
            errorDetail.setErrors(ex.getResult().getFieldErrors());
        else
        {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Error", ex.getMessage());
            errorDetail.setErrorMap(errorMap);
        }
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
