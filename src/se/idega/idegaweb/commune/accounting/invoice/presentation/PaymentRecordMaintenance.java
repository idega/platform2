package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author Joakim
 *
 */
public class PaymentRecordMaintenance extends AccountingBlock{
	private static String PREFIX="cacc_payment_record_";
	private static String PARAM_PERIOD=PREFIX+"period";
	private static String PARAM_NAME=PREFIX+"name";
	private static String PARAM_PROVIDER=PREFIX+"provider";
	private static String PARAM_SEARCH=PREFIX+"search";
	
	public void init(IWContext iwc){
		Form form = new Form();
		Table table = new Table(2,4);
		OperationalFieldsMenu opFields = new OperationalFieldsMenu();  
		
		try {
			handleAction(iwc);
		
			DropdownMenu providerDropdown = new DropdownMenu("provider_dropdown");
			providerDropdown.addMenuElementFirst("",localize(PARAM_NAME,"Name"));
			String schoolCategory = getSession().getOperationalField();
			Iterator schoolIter = getSchoolBusiness(iwc).findAllSchoolsByCategory(schoolCategory).iterator();
			while (schoolIter.hasNext()) {
				School school = (School) schoolIter.next();
				providerDropdown.addMenuElement(((Integer)school.getPrimaryKey()).intValue(),school.getName());
			}
			InputContainer provider = getInputContainer(PARAM_PROVIDER,"provider",providerDropdown);
			
			DateInput monthInput = new DateInput(PARAM_PERIOD,true);
			monthInput.setToCurrentDate();
			monthInput.setToShowDay(false);
			InputContainer month = getInputContainer(PARAM_PERIOD,"Period", monthInput);
			
			table.add(opFields,1,1);
			table.add(provider,1,2);
			table.add(month,1,3);

			GenericButton search = new SubmitButton(PARAM_SEARCH,localize(PARAM_SEARCH,"Search"));
			table.add(search,2,2);
			add(form);
			

			form.add(table);
			
			//Middle section with the payment list
			try{
				
				Table paymentTable = new Table();

				int row = 2;
				
				Collection paymentColl = getInvoiceBusiness(iwc).getPaymentRecordsByCategoryProviderAndPeriod(schoolCategory,iwc.getParameter("provider_dropdown"),new Date(System.currentTimeMillis()));
				System.out.println("Size of payment list "+paymentColl.size());
				//TODO (JJ) 
				Iterator paymentIter = paymentColl.iterator();
				if(paymentIter.hasNext()){
					paymentTable.add(getLocalizedLabel(PREFIX+"status","Status"),1,1);
					paymentTable.add(getLocalizedLabel(PREFIX+"period","Period"),2,1);
					paymentTable.add(getLocalizedLabel(PREFIX+"placedd","Placed"),3,1);
					paymentTable.add(getLocalizedLabel(PREFIX+"nr_of_placements","Nr of placements"),4,1);
					paymentTable.add(getLocalizedLabel(PREFIX+"tot_sum","Total sum"),5,1);
					paymentTable.add(getLocalizedLabel(PREFIX+"notes","notes"),6,1);
				
					while(paymentIter.hasNext()){
						PaymentRecord paymentRecord = (PaymentRecord)paymentIter.next();
						paymentTable.add(new Text(""+paymentRecord.getStatus()),1,row);
						paymentTable.add(new Text(""+paymentRecord.getDateCreated()),2,row);
						paymentTable.add(new Text(paymentRecord.getPaymentText()),3,row);
						paymentTable.add(new Text(""+paymentRecord.getPlacements()),4,row);
						paymentTable.add(new Text(""+paymentRecord.getTotalAmount()),5,row);
						if(paymentRecord.getNotes()!=null){
							paymentTable.add(new Text(paymentRecord.getNotes()),6,row);
						}
						row++;
					}
					add(paymentTable);
				} else {
					add(getLocalizedSmallHeader(PREFIX+"no_payment_records_found","No payment records found."));
				}
			}catch(FinderException e){
				add(getLocalizedSmallHeader(PREFIX+"no_payment_records_found","No payment records found."));
				e.printStackTrace();
				System.out.println("Problems finding payment records!!!");
			}
			
		} catch (Exception e) {
			add(getLocalizedSmallHeader("invbr.error_occured","Error occured"));
			e.printStackTrace();
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SEARCH)){
			handleSearch(iwc);
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleSearch(IWContext iwc) {
		//TODO (JJ) create
		System.out.println("Handle search "+iwc.getCurrentUserId());
	}

	protected PostingBusiness getPostingBusiness(IWApplicationContext iwc) throws RemoteException {
		return (PostingBusiness) IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}	
	
	protected InvoiceBusiness getInvoiceBusiness(IWApplicationContext iwc) throws RemoteException {
		return (InvoiceBusiness) IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
	}	
	
	protected SchoolBusiness getSchoolBusiness(IWApplicationContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}	
	
}
