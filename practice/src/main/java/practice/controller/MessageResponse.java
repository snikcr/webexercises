package practice.controller;

/**
 * Message to be sent as a response when an exception occurs.
 */
public class MessageResponse {

	/**
	 * Message to be shown.
	 */
	private String message;
	
	/**
	 * Constructor
	 * @param message Message to be shown
	 */
	public MessageResponse(String message) {
		super();
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
