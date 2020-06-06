package ru.otus.edu.levina.serialization.json;

public class JsonSerializationException extends Exception {

    public JsonSerializationException() {
        super();
    }

    public JsonSerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonSerializationException(String message) {
        super(message);
    }

    public JsonSerializationException(Throwable cause) {
        super(cause);
    }

}
