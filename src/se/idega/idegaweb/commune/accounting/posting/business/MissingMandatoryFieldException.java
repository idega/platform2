package se.idega.idegaweb.commune.accounting.posting.business;

/**
 * @author Joakim
 *
 */
public class MissingMandatoryFieldException extends Exception {
	/**
	 * Exception thrown when a posting string has incorrect format
	 * 
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public MissingMandatoryFieldException(String textKey) {
		super(textKey);
	}
}
