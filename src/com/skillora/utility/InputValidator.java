package com.skillora.utility;

import java.util.regex.Pattern;

/**
 * Validates console inputs such as email format, passwords, CGPA ranges, and non-empty inputs.
 * Demonstrates: Regular expressions, input sanitization, static helper methods.
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= 4;
    }

    public static boolean isValidCgpa(double cgpa) {
        return cgpa >= 0.0 && cgpa <= 10.0;
    }

    public static boolean isValidSemester(int semester) {
        return semester >= 1 && semester <= 12;
    }

    public static boolean isNonEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
