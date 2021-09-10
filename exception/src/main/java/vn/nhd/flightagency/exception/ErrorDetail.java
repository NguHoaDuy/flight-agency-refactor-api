package vn.nhd.flightagency.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ErrorDetail {

    private LocalDate timestamp;
    private String message;
    private String details;
    private Map<String, String> errors;

    public void setErrorMap(Map<String, String> errorMap) {
        this.errors = errorMap;
    }
    public void setErrors(List<FieldError> errorList) {
        LinkedHashMap<String, String> errorMap = new LinkedHashMap<>();
        for (FieldError err: errorList) {
            errorMap.put(err.getField(), err.getDefaultMessage());
        }
        this.errors = errorMap;
    }
}
