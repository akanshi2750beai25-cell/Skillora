package com.skillora.exception;

/**
 * Base checked exception for the Skillora platform.
 * All domain-specific exceptions inherit from this class.
 * Demonstrates: Exception Handling, Custom Exceptions, Inheritance.
 */
public class SkilloraException extends Exception {

    public SkilloraException(String message) {
        super(message);
    }

    public SkilloraException(String message, Throwable cause) {
        super(message, cause);
    }
}
