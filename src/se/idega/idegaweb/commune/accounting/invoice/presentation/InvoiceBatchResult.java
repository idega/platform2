package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRun;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunError;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunErrorHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * Displays the results of the batchrun, and all possible errors that could have occured
 * see fonster 33 in C&P req spec.
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 * 
 * @author Joakim
 */
public class InvoiceBatchResult extends AccountingBlock{
	private static final String EXCEL_BUTTON = "Excel file";
	public void init(IWContext iwc){
		Form form = new Form();
		Table table = new Table(2,9);
		// set alignment to right for the second column
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
		
		OperationalFieldsMenu opFields = new OperationalFieldsMenu();
		
		try {
		
			//"Top section"
			add(opFields);

			InvoiceBusiness invoiceBusiness = getInvoiceBusiness(iwc);
			String schoolCategory = getSession().getOperationalField();
			
			BatchRun batchRun = invoiceBusiness.getBatchRunByCategory(schoolCategory, isTestRun());

			boolean categoryIsChildCare = invoiceBusiness.isChildCare(schoolCategory);
			
			int i = 1;
			table.add(getLocalizedLabel("invbr.period","Period"),1,i++);
			table.add(getLocalizedLabel("invbr.batchrun_starttime","Batchrun start-time"),1,i++);
			table.add(getLocalizedLabel("invbr.batchrun_endtime","Batchrun end-time"),1,i++);
			if (categoryIsChildCare) {
				table.add(getLocalizedLabel("invbr.number_of_invoices","Number of invoices"),1, i++);
				table.add(getLocalizedLabel("invbr.number_of_billed_children","Number of handled children"),1,i++);
				table.add(getLocalizedLabel("invbr.total_number_of_handled_providers","Number of handled providers"),1,i++);
				table.add(getLocalizedLabel("invbr.total_amount_of_invoices","Total amount of invoices"),1,i++);
				table.add(getLocalizedLabel("invbr.total_amount_of_payments","Total amount of payments"),1,i++);
			}
			else {
				table.add(getLocalizedLabel("invbr.total_number_of_handled_providers","Number of handled providers"),1,i++);
				table.add(getLocalizedLabel("invbr.number_of_billed_individuals","Number of handled individuals"), 1, i++);
				table.add(getLocalizedLabel("invbr.total_number_of_handled_placements","Number of handled placements"),1,i++);
				table.add(getLocalizedLabel("invbr.total_Amount_excluding_VAT","Total amount excluding VAT"),1,i++);
			}
	
			IWTimestamp period = new IWTimestamp(batchRun.getPeriod());
			IWTimestamp start = new IWTimestamp(batchRun.getStart());
			
			IWTimestamp end = null;
			if(batchRun.getEnd()!=null){
				end = new IWTimestamp(batchRun.getEnd());
			}
			// reset row counter
			i = 1;
			table.add(period.getDateString("MMM yyyy"),2, i++);
			table.add(start.getDateString("yyyy-MM-dd kk:mm:ss"),2,i++);
			if(end!=null){
				table.add(end.getDateString("yyyy-MM-dd kk:mm:ss"),2,i++);
			} else {
				table.add(getLocalizedSmallText("invbr.not_finished","Not finished"),2,i++);
			}
	
			// get a nice formatter
			NumberFormat numberFormat = NumberFormat.getNumberInstance(iwc.getCurrentLocale());
			numberFormat.setMaximumFractionDigits(0);
			
			if (categoryIsChildCare) {
				table.add(Integer.toString(invoiceBusiness.getNumberOfInvoices(batchRun)),2,i++);
				table.add(Integer.toString(invoiceBusiness.getNumberOfHandledChildren(batchRun)),2,i++);
				table.add(Integer.toString(invoiceBusiness.getNoProviders(batchRun)), 2, i++);
				table.add(numberFormat.format(invoiceBusiness.getTotalAmountOfInvoices(batchRun)),2, i++);
			}
			else {
				table.add(Integer.toString(invoiceBusiness.getNoProviders(batchRun)),2,i++);
				table.add(Integer.toString(invoiceBusiness.getNumberOfHandledChildren(batchRun)), 2, i++);
				table.add(Integer.toString(invoiceBusiness.getNoPlacements(batchRun)),2,i++);
			}
			table.add(numberFormat.format(invoiceBusiness.getTotAmountWithoutVAT(batchRun)),2,i++);
		
			add(table);
			
			GenericButton excelButton = this.getButton(new SubmitButton(EXCEL_BUTTON,localize("invoice.Generate_excel_file","Generate excel file")));
			addBreak();
			form.add(excelButton);
			add(form);
			handleAction(iwc, batchRun);
			addBreak();
			
			int row = 1;
			BatchRunErrorHome batchRunErrorHome = (BatchRunErrorHome)IDOLookup.getHome(BatchRunError.class);
			Collection errorColl = batchRunErrorHome.findByBatchRun(batchRun, isTestRun());

			//Bottom section
			add(getLocalizedLabel("invbr.Total_number_of_suspected_errors","Total number of suspected errors"));
			add(Text.getNonBrakingSpace());
			add(new Text(new Integer(errorColl.size()).toString()));

			//Middle section with the error list
			Table errorTable = new Table();

			System.out.println("Size of table BatchRunError: "+errorColl.size());
			Iterator errorIter = errorColl.iterator();
			if(errorIter.hasNext()){
				System.out.println("Found error description");
				errorTable.add(getLocalizedLabel("invbr.number","Nr"),1,1);
				errorTable.add(getLocalizedLabel("invbr.related_object","Related object"),2,1);
				errorTable.add(getLocalizedLabel("invbr.suspected_error","Suspected error"),3,1);
				
				while(errorIter.hasNext()){
					BatchRunError batchRunError = (BatchRunError)errorIter.next();
					errorTable.setRowColor (row + 1, (row % 2 == 0) ? getZebraColor1 ()
									   : getZebraColor2 ());
					errorTable.add(new Text(new Integer(row).toString()),1,row+1);
					errorTable.add(new Text(batchRunError.getRelated()),2,row+1);
					errorTable.add(new Text(batchRunError.getDescription()),3,row+1);
					row++;
				}
				System.out.println("Found errors: "+row);
				add(errorTable);
			}
			
		} catch (FinderException e) {
			add(getLocalizedSmallHeader("invbr.no_batchrun_available","No batchrun available"));
		} catch (Exception e) {
			add(getLocalizedSmallHeader("invbr.error_occured","Error occured"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Probably remove to handle the navigation outside this presentation block
	 * @param iwc
	 */
	private void handleAction(IWContext iwc, BatchRun batchRun) {
		if(iwc.isParameterSet(EXCEL_BUTTON)){
			ICFile file;
			try {
				file = getInvoiceBusiness(iwc).
				exportToExcel(getBundle(iwc).getResourceBundle(iwc), localize("invoice.Batchresult","Batchresult")+".xls", batchRun, isTestRun());
				if (null!=file) {
					add(new Link(((Integer)file.getPrimaryKey()).intValue(),localize("invoice.Excel_file","Excel file")));
					addBreak();
				}
				else {
					add(getLocalizedString("invoice.Unable_to_export_file", "Unable to export file",iwc));
				}
			} catch (Exception e) {
				add(getLocalizedString("invoice.Unable_to_export_file", "Unable to export file",iwc));
				e.printStackTrace();
			}
		}
	}
	
	protected PostingBusiness getPostingBusiness(IWApplicationContext iwc) throws RemoteException {
		return (PostingBusiness) IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}	
	
	protected InvoiceBusiness getInvoiceBusiness(IWApplicationContext iwc) throws RemoteException {
		return (InvoiceBusiness) IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
	}
	
	protected boolean isTestRun(){
		return false;
	}

}
