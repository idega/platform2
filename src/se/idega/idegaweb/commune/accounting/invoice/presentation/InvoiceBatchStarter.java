package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.business.ExportBusiness;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunQueue;
import se.idega.idegaweb.commune.accounting.invoice.business.SchoolCategoryNotFoundException;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.data.IDOLookupException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.util.IWTimestamp;

/**
 * Starts the batch run that will create billing and invoicing information
 * according to the parameters set in the UI.
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class InvoiceBatchStarter extends AccountingBlock{
	
	private static String PREFIX="cacc_invbs_";
	private static String PARAM_MONTH=PREFIX+"month";
	private static String PARAM_READ_DATE=PREFIX+"read_date";
	DateInput monthInput;
	DateInput dateInput;
	DateInput readDateInput;
	private String link=null;

	public void init(IWContext iwc){
	
		String schoolCategory=null;
		OperationalFieldsMenu opFields = new OperationalFieldsMenu();
		try {
			schoolCategory = getSession().getOperationalField();
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		}

		handleAction(iwc,schoolCategory);
		
		add(opFields);
		
		Form form = new Form();
		add(form);

		form.add(getShoolDropDown(iwc));

		try {
			ExportBusiness exportBusiness = getBusiness().getExportBusiness();
			ExportDataMapping exportDataMapping = exportBusiness.getExportDataMapping(schoolCategory);
			System.out.println(""+exportDataMapping.getAccountSettlementType());
			if(exportDataMapping.getAccountSettlementType() ==
				exportBusiness.getAccountSettlementTypeSpecificDate())
			{
				readDateInput = new DateInput(PARAM_READ_DATE,true);
				String date = iwc.getParameter(PARAM_READ_DATE);
				if(date!=null){
					readDateInput.setDate(new IWTimestamp(date).getDate());
				}else{
					readDateInput.setToCurrentDate();
				}
				readDateInput.setToDisplayDayLast(true);

				int currentYear = java.util.Calendar.getInstance ().get (java.util.Calendar.YEAR);
				readDateInput.setYearRange(currentYear - 1, currentYear + 1);
				InputContainer readDate = getInputContainer(PARAM_READ_DATE,"Read date", readDateInput);
				form.add(readDate);
			}else{
				monthInput = new DateInput(PARAM_MONTH,true);
				String date = iwc.getParameter(PARAM_MONTH);
				if(date!=null){
					monthInput.setDate(new IWTimestamp(date).getDate());
				}else{
					monthInput.setToCurrentDate();
				}
				monthInput.setToShowDay(false);

				int currentYear = java.util.Calendar.getInstance ().get (java.util.Calendar.YEAR);
				monthInput.setYearRange(currentYear - 1, currentYear + 1);
				
				InputContainer month = getInputContainer(PARAM_MONTH,"Month", monthInput);

				form.add(month);
			}
		} catch (IDOLookupException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		} catch (FinderException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		}
		
		GenericButton saveButton = this.getSaveButton();
		form.add(saveButton);
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(IWContext iwc, String schoolCategory) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			handleSave(iwc, schoolCategory);
		}
	}
	
	/**
	 * @param iwc
	 */
	protected void handleSave(IWContext iwc, String schoolCategory) {
		try {
//			InvoiceBusiness invoiceBusiness = (InvoiceBusiness)IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
			Date month = null;
			Date readDate = null;
			if(iwc.getParameter(PARAM_MONTH)!=null){
				month = new IWTimestamp(iwc.getParameter(PARAM_MONTH)).getDate();
			}
			if(iwc.getParameter(PARAM_READ_DATE)!=null){
				try{
					readDate = new IWTimestamp(iwc.getParameter(PARAM_READ_DATE)).getDate();
				}catch(IllegalArgumentException e){
					add(getErrorText(getLocalizedString("invbr.Please_provide_a_valid_date","Please provide a proper date.",iwc)));
					return;
				}
			}
			addBatchRunToQueue(month, readDate, schoolCategory, iwc);

//			invoiceBusiness.startPostingBatch(month, readDate, schoolCategory, iwc);
			add(getLocalizedText("invbr.batchrun_started","Batchrun started"));
			add(new Break());
			if(link!=null)
			{
				Link uiLink = new Link();
				uiLink.setText(getLocalizedLabel("invbr.progress","Progress"));
				uiLink.setTarget(link);
				add(uiLink);
			} else {
				System.out.println("WARNING need to set the Link property for invoice batch start block!");
			}
		} catch (SchoolCategoryNotFoundException e) {
			add(getErrorText(getLocalizedString("invbr.please_select_valid_school_category","Please select valid school category.",iwc)));
			e.printStackTrace();
//		} catch (BatchAlreadyRunningException e) {
//			add(getErrorText(getLocalizedString("invbr.batchrun_already_started","Batchrun already started",iwc)));
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}
	
	protected void addBatchRunToQueue(Date month, Date readDate, String schoolCategory, IWContext iwc) throws SchoolCategoryNotFoundException{
		BatchRunQueue.addBatchRunToQueue(month, readDate, schoolCategory, iwc);		
	}
	
	/**
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param string
	 */
	public void setLink(String page) {
		link = page;
	}

	/*
	 * The IWContext parameter is used in the TestPosts subclass
	 */
	protected PresentationObject getShoolDropDown(IWContext iwc){
		return new Text("");
	}

}
