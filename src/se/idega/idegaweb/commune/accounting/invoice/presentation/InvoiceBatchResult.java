package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.BatchRun;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunError;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunErrorHome;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessHome;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Joakim
 *
 */
public class InvoiceBatchResult extends AccountingBlock{
	
	public void init(IWContext iwc){
		Form form = new Form();
		Table table = new Table(2,6);
		
		try {
			handleAction(iwc);
		
			add(form);

			table.add(getLocalizedLabel("invbr.period","Period"),1,1);
			table.add(getLocalizedLabel("invbr.batchrun_starttime","Batchrun start-time"),1,2);
			table.add(getLocalizedLabel("invbr.batchrun_endtime","Batchrun end-time"),1,3);
			table.add(getLocalizedLabel("invbr.number_of_invoices","Number of invoices"),1,4);
			table.add(getLocalizedLabel("invbr.number_of_billed_children","Number of handled children"),1,5);
			table.add(getLocalizedLabel("invbr.totalAmount","Total amount"),1,6);

			BatchRunHome batchRunHome = (BatchRunHome)IDOLookup.getHome(BatchRun.class);
			Collection runColl = batchRunHome.findAllOrderByStart();
			Iterator runIter = runColl.iterator();
			
			if(runIter.hasNext()){
				BatchRun run = (BatchRun)runIter.next();
				IWTimestamp period = new IWTimestamp(run.getPeriod());
				IWTimestamp start = new IWTimestamp(run.getStart());
				IWTimestamp end = new IWTimestamp(run.getEnd());
				table.add(period.getDateString("MMM yyyy"),2,1);
				table.add(start.getDateString("yyyy-MM-dd kk:mm:ss"),2,2);
				table.add(end.getDateString("yyyy-MM-dd kk:mm:ss"),2,3);
			}
		
			form.add(table);
			
			Table errorTable = new Table();
			
			
			int row = 1;
			System.out.println("1: ");
			BatchRunErrorHome batchRunErrorHome = (BatchRunErrorHome)IDOLookup.getHome(BatchRunError.class);
			System.out.println("2: ");
			Collection errorColl = batchRunErrorHome.findAllOrdered();
			System.out.println("Size of table BatchRunError: "+errorColl.size());
			Iterator errorIter = errorColl.iterator();
			if(errorIter.hasNext()){
				System.out.println("Found error description");
				errorTable.add(getLocalizedLabel("invbr.number","Nr"),1,1);
				errorTable.add(getLocalizedLabel("invbr.related_object","Related object"),2,1);
				errorTable.add(getLocalizedLabel("invbr.suspected_error","Suspected error"),3,1);
				
				while(errorIter.hasNext()){
					BatchRunError batchRunError = (BatchRunError)errorIter.next();
					errorTable.add(new Text(new Integer(row).toString()),1,row+1);
					errorTable.add(new Text(batchRunError.getRelated()),2,row+1);
					errorTable.add(new Text(batchRunError.getDescription()),3,row+1);
					row++;
				}
				add(errorTable);
			}
			
			add(getLocalizedLabel("invbr.Total_number_of_suspected_errors","Total number of suspected errors"));
			add(new Text(new Integer(row-1).toString()));

			GenericButton cancelButton = this.getCancelButton();
			form.add(cancelButton);
			
		} catch (FinderException e) {
			add(getLocalizedSmallHeader("invbr.no_batchrun_available","No batchrun available"));
		} catch (Exception e) {
			add(getLocalizedSmallHeader("invbr.error_occured","Error occured"));
			e.printStackTrace();
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			handleSave(iwc);
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleSave(IWContext iwc) {
		User user = iwc.getCurrentUser();
		if(user!=null){
			add("Save pressed for user: "+user.getName());
		} else {
			add("Need to log in again.");
		}
	}

	public PostingBusinessHome getPostingBusinessHome() throws RemoteException {
		return (PostingBusinessHome) IDOLookup.getHome(PostingBusiness.class);
	}
}
