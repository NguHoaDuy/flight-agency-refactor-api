package vn.nhd.flightagency.exception.extension;

import org.springframework.validation.BindingResult;

public class BadCredentialException extends Exception {

    private BindingResult result;
    private String message;

    public BadCredentialException(BindingResult result) {
        this.result = result;
    }
    public BadCredentialException(String message) {
        super(message);
    }

    public BindingResult getResult() {
        return result;
    }

    public void setResult(BindingResult result) {
        this.result = result;
    }
}
