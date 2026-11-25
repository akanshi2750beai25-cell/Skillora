package com.skillora.exception;

/**
 * Thrown when a student attempts to apply for an internship they have already submitted an application for.
 * Demonstrates: Custom Exceptions, Business Rule Enforcement.
 * 
 * Contributed by: Navdeep Randhawa
 */
public class DuplicateApplicationException extends SkilloraException {

    public DuplicateApplicationException(String message) {
        super(message);
    }
}
