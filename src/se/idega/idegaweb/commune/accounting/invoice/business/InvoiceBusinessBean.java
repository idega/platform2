package se.idega.idegaweb.commune.accounting.invoice.business;

import java.sql.Date;
import com.idega.business.IBOServiceBean;

/**
 * Holds most of the logic for the batchjob that creates the information that is base for invoicing 
 * and payment data, that is sent to external finance system. Now moved to InvoiceThread
 * 
 * @author Joakim
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceThread
 */

public class InvoiceBusinessBean extends IBOServiceBean implements InvoiceBusiness{

	/**
	 * Spawns a new thread and starts the execution of the posting calculation and then returns
	 * @param month
	 */
	public void startPostingBatch(Date month){
		new InvoiceThread(month).start();
	}
}
