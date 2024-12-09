package model;

public class ExporterException extends RuntimeException {
    public ExporterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExporterException(String message) {
        super(message);
    }
}
