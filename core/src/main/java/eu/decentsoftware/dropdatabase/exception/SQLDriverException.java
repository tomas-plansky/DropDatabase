package eu.decentsoftware.dropdatabase.exception;

public class SQLDriverException extends RuntimeException {

	public SQLDriverException(String message) {
		super(message);
	}

	public SQLDriverException(String message, Throwable cause) {
		super(message, cause);
	}

	public SQLDriverException(Throwable cause) {
		super(cause);
	}

}
