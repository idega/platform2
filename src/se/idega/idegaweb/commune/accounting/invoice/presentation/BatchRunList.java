package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;

import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunQueue;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunQueue.BatchRunObject;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author Joakim
 *
 */
public class BatchRunList extends AccountingBlock{
	private static String KEY = "removeKey"; 
	public void init(IWContext iwc){
		handleAction(iwc);
		Form form = new Form();
		
		Iterator queueIterator = BatchRunQueue.iterator();
		
		int row = 1;

		//Middle section with the error list
		Table errorTable = new Table();

		if(queueIterator.hasNext()){
			errorTable.add(getLocalizedLabel("batchlist.batch","Batch"),1,1);
			
			while(queueIterator.hasNext()){
				BatchRunObject batchRunObject = (BatchRunObject)queueIterator.next();
				errorTable.setRowColor (row + 1, (row % 2 == 0) ? getZebraColor1 ()
						: getZebraColor2 ());
//					errorTable.add(new Text(new Integer(row).toString()),1,row+1);
				errorTable.add(new Text(batchRunObject.toString()),1,row+1);
				SubmitButton submitButton = new SubmitButton(getLocalizedString("batchlist.remove","remove",iwc),KEY,batchRunObject.toString());
				submitButton.setAsImageButton(true);
				errorTable.add(submitButton,2,row+1);
				row++;
			}
			System.out.println("Found errors: "+row);
			form.add(errorTable);
			add(form);
		} else {
			add(getLocalizedLabel("batchlist.No_batchruns_in_queue","No batchruns in queue"));
		}
	}
	
	/**
	 * Probably remove to handle the navigation outside this presentation block
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
		if(iwc.isParameterSet(KEY)){
			BatchRunQueue.removeBatchRunFromQueue(iwc.getParameter(KEY));
			System.out.println(iwc.getParameter(KEY));
		}
	}
	
	protected PostingBusiness getPostingBusiness(IWApplicationContext iwc) throws RemoteException {
		return (PostingBusiness) IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}	
	
	protected InvoiceBusiness getInvoiceBusiness(IWApplicationContext iwc) throws RemoteException {
		return (InvoiceBusiness) IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
	}

}
