package org.acme.Utils;

public class GenericValidationError {
    public String path;
    public String message;

    public GenericValidationError(String path, String message) {
        this.path = path;
        this.message = message;
    }
}