package se.idega.idegaweb.commune.accounting.posting.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * Exception thrown when a posting string has incorrect format
 * 
 * @author Joakim
 */
public class PostingException extends AccountingException {
	/**
	 * Exception thrown when a posting string has incorrect format
	 * 
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public PostingException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
