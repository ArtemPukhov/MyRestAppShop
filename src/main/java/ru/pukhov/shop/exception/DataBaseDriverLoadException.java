package ru.pukhov.shop.exception;

public class DataBaseDriverLoadException extends RuntimeException {
    public DataBaseDriverLoadException(String message) {
        super(message);
    }
}
