package se.idega.idegaweb.commune.accounting.posting.business;

/**
 * Exception thrown when a posting string is missing a field which is marked as mandatory
 * @author Joakim
 */
public class MissingMandatoryFieldException extends Exception {
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public MissingMandatoryFieldException(String textKey) {
		super(textKey);
	}
}
