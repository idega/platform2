package se.idega.idegaweb.commune.accounting.invoice.business;

/**
 * @author Joakim
 * 
 * Thrown when both a student and a school is outside the default commune.
 */
public class NotDefaultCommuneException extends Exception{
	public NotDefaultCommuneException(String s){
		super(s);
	}
}