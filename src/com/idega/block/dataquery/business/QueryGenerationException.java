/*
 * Created on Aug 1, 2003
 *
 */
package com.idega.block.dataquery.business;

/**
 * QueryGenerationException
 * @author aron 
 * @version 1.0
 */

public class QueryGenerationException extends Exception {
	
	/**
	 * 
	 */
	public QueryGenerationException() {
		super();
	
	}

	/**
	 * @param message
	 */
	public QueryGenerationException(String message) {
		super(message);
	
	}

/* Commented out because of JDK1.3 compliance...
	public QueryGenerationException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public QueryGenerationException(Throwable cause) {
		super(cause);
		
	}
*/
}
