package se.idega.idegaweb.commune.accounting.invoice.business;

/**
 * Thrown when null is found as foreign key in sch_school table
 * 
 * @author Joakim
 */
public class SchoolMissingVitalDataException extends Exception{
	public SchoolMissingVitalDataException(String s){
		super(s);
	}
}
