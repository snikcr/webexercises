package practice.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown in any stage of the process.
 */
@SuppressWarnings("serial")
public class ProcessException extends RuntimeException  {

	/**
	 * {@link HttpStatus} to be used in response entity.
	 */
	private HttpStatus httpStatus;
	
	private String [] args;
	
	public ProcessException(HttpStatus httpStatus, Throwable cause, String message) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}
	
	public ProcessException(HttpStatus httpStatus, Throwable cause, String message, String... args) {
		this(httpStatus, cause, message);
		this.args = args;
	}
	
	public ProcessException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public ProcessException(HttpStatus httpStatus, String message, String... args) {
		this(httpStatus, message);
		this.args = args;
	}
	
	/**
	 * @return the httpStatus
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @return the args
	 */
	public String[] getArgs() {
		return args;
	}
}
