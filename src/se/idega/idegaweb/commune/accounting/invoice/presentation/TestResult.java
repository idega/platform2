package se.idega.idegaweb.commune.accounting.invoice.presentation;




/**
 * Displays the results of the batchrun, and all possible errors that could have occured
 * see fonster 33 in C&P req spec.
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 * 
 * @author Roar
 */
public class TestResult extends InvoiceBatchResult{
	
	protected boolean isTestRun(){
		return true;
	}
	
}
