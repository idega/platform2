package se.idega.idegaweb.commune.accounting.invoice.business;

import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;

/**
 * @author Joakim
 * Interface to be implemented by classes of all the different 
 * Special Calculation Types. Each type will then be called 
 * during the accounting process and can then do the specific work for each case
 */
public interface SpecialCalculationType {
	public void handle(Regulation reg, ChildCareContract cont);
}
