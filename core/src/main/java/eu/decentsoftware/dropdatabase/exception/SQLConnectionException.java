package eu.decentsoftware.dropdatabase.exception;

public class SQLConnectionException extends RuntimeException {

    public SQLConnectionException(String message) {
        super(message);
    }

    public SQLConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLConnectionException(Throwable cause) {
        super(cause);
    }

}
