package se.idega.idegaweb.commune.accounting.invoice.business;

/**
 * @author Joakim
 *
 */
public class BatchAlreadyRunningException extends Exception{
	public BatchAlreadyRunningException(String s){
		super(s);
	}
}
