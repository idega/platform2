package se.idega.idegaweb.commune.accounting.invoice.presentation;


import com.idega.presentation.IWContext;


/**
 * Displays the results of the batchrun, and all possible errors that could have occured
 * see fonster 33 in C&P req spec.
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 * 
 * @author Joakim
 */
public class TestResult extends InvoiceBatchResult{
	
	public void init(IWContext iwc){
		super.init(iwc);
	}
	
}
