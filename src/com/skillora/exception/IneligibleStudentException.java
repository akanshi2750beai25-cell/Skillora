package com.skillora.exception;

/**
 * Thrown when a student attempts to apply for an internship but does not satisfy
 * the required criteria (e.g. CGPA cutoff, eligible academic branch, or minimum semester).
 * Demonstrates: Custom Exceptions, Business Validation.
 * 
 * Contributed by: Navdeep Randhawa
 */
public class IneligibleStudentException extends SkilloraException {

    public IneligibleStudentException(String message) {
        super(message);
    }
}
