package com.skillora.exception;

/**
 * Thrown when user authentication fails due to invalid credentials,
 * non-existent account, or unauthorized role access.
 * Demonstrates: Custom Exceptions, Inheritance.
 * 
 * Contributed by: Akanshi
 */
public class AuthenticationException extends SkilloraException {

    public AuthenticationException(String message) {
        super(message);
    }
}
