package se.idega.idegaweb.commune.accounting.invoice.business;

/**
 * Exception thrown by InvoiceBusinessBean when a Schoolcategory doesn't 
 * have any logic for billiing
 *  
 * @author Joakim
 */
public class SchoolCategoryNotFoundException extends Exception{
	public SchoolCategoryNotFoundException(String s){
		super(s);
	}
}
