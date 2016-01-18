package ru.various.sdp2partpost.exceptions;

/**
 * Throws if input data from database is incomplete.
 * For example, zipcode not match six number pattern,
 * or name has no surname or firstname etc.
 */
public class IncompleteDataException extends Exception {
    public IncompleteDataException(String message) {
        super(message);
    }
}
