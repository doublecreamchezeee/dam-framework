package com.example.damfw.exception;

public class ORMException extends RuntimeException {

    /**
     * Constructor with a message.
     *
     * @param message The exception message.
     */
    public ORMException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and a cause.
     *
     * @param message The exception message.
     * @param cause   The underlying cause of the exception.
     */
    public ORMException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     *
     * @param cause The underlying cause of the exception.
     */
    public ORMException(Throwable cause) {
        super(cause);
    }
}