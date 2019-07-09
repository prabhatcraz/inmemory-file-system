package com.prabhat.filesystem.exceptions;

/**
 * Indicates bad path provided by user.
 */
public class BadPathException extends RuntimeException {
    public BadPathException(final String message) {
        super(message);
    }
}
