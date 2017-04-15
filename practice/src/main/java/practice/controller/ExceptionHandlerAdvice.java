package practice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import practice.exception.ProcessException;

/**
 * Class to intercept exceptions in the processes.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
	
	@Autowired
	private MessageSource messageSource;

	/**
	 * Class logger.
	 */
	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * Method to handle the business {@link ProcessException}
	 * @param pex
	 * @return
	 */
	@ExceptionHandler(ProcessException.class)
	public ResponseEntity<MessageResponse> handleProcessException(ProcessException pex) {
		
		if (pex.getCause() != null) {
			logger.error(pex.getMessage(), pex.getCause());
		}
		
		MessageResponse messageResponse = createResponseMessage(pex.getMessage(), pex.getArgs());
		return ResponseEntity.status(pex.getHttpStatus()).body(messageResponse);
	}
	
	/**
	 * Method to handle general {@link RuntimeException}
	 * @param pex
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<MessageResponse> handleUnknownException(RuntimeException rex) {
		
		logger.error("Internal error: ", rex);
		
		MessageResponse messageResponse = createResponseMessage(rex.getMessage());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
	}
	
	/**
	 * Method to handle the exceptions while reading requests.
	 * @param pex
	 * @return
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<MessageResponse> handleHttpNotReadableException(HttpMessageNotReadableException nrex) {
		
		logger.error(nrex);
		
		MessageResponse messageResponse = createResponseMessage("error.input.unreadable");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}
	
	
	/**
	 * Method to handle validation exceptions {@link MethodArgumentNotValidException}
	 * @param pex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<List<MessageResponse>> handleValidationError(MethodArgumentNotValidException vex) {
		
		List<MessageResponse> validationErrors = vex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> createResponseMessage(fieldError.getDefaultMessage()))
				.collect(Collectors.toList());
		
		return ResponseEntity.badRequest().body(validationErrors);
	}
	
	/**
	 * Creates a {@link MessageResponse}.
	 * @param msg
	 * @return
	 */
	private MessageResponse createResponseMessage(String msg) {
		
		return createResponseMessage(msg, null);
	}
	
	/**
	 * Creates a {@link MessageResponse}.
	 * @param msg The 
	 * @param args
	 * @return
	 */
	private MessageResponse createResponseMessage(String msg, Object [] args) {
		
		MessageResponse messageResponse = null;
		
		if (msg == null) {
			msg = "error.internal";
		}
		
		String message = msg;
		try {
			message = messageSource.getMessage(msg, args, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			Logger.getLogger(getClass()).warn(e.getMessage());
		} finally {
			messageResponse = new MessageResponse(message);
		}
		
		
		return messageResponse;
	}
	
}
