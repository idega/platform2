package se.idega.idegaweb.commune.accounting.invoice.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;
/**
 * @author Kelly
 *
 */
public class ControlListException extends AccountingException {
	public ControlListException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
