package se.idega.idegaweb.commune.accounting.userinfo.data;

/**
 * Is thrown when siblings are sorted by date of birth and that data is missing
 * 
 * @author Joakim
 * @see SortableSibling
 */
public class DateOfBirthMissingException extends RuntimeException{
	public DateOfBirthMissingException(String s){
		super(s);
	}
}
