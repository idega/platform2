/*
 * Created on Jan 8, 2004
 *
 */
package se.idega.idegaweb.commune.printing.business;

/**
 * ContentCreationException
 * @author aron 
 * @version 1.0
 */
public class ContentCreationException extends RuntimeException {
	/**
	 * 
	 */
	public ContentCreationException() {
		super();
		
	}

	/**
	 * @param message
	 */
	public ContentCreationException(String message) {
		super(message);
	
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ContentCreationException(String message, Throwable cause) {
		super(message, cause);
		
	}

	/**
	 * @param cause
	 */
	public ContentCreationException(Throwable cause) {
		super(cause);
		
	}

}
