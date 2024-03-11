package org.acme.Utils;

import jakarta.validation.ConstraintViolation;
import org.acme.Entities.Note;
import org.acme.Entities.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ErrorResponse {
    public List<GenericValidationError> errors;

    public ErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(Set<ConstraintViolation<?>> errors) {
        this.errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : errors) {
            this.errors.add(new GenericValidationError(violation.getPropertyPath().toString(), violation.getMessage()));
        }
    }
}