package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.SortedSet;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
	
class ChildCarePlaceOfferTable1 extends Table{	
	
	private static Text HEADER_YOUR_CHOICE;
	private static Text HEADER_OFFER;
	private static Text HEADER_PROGNOSE;
	private static Text HEADER_QUEUE_INFO;
	private static Text HEADER_YES;
	private static Text HEADER_YES_BUT;
	private static Text HEADER_NO;

	private static String GRANTED;

	private static boolean _initializeStatics = false;
	

	private static ChildCareCustomerApplicationTable _page;
	


	final static String[] REQUEST_INFO = new String[]{"ccatp1_request_info", "Request info"};
	

	private void initConstants(ChildCareCustomerApplicationTable page){
		if (!_initializeStatics) {
			_page = page;
			HEADER_YOUR_CHOICE = page.getLocalHeader("ccatp1_your_choice", "Your Choice");
			HEADER_OFFER = page.getLocalHeader("ccatp1_offer", "Offer");
			HEADER_PROGNOSE = page.getLocalHeader("ccatp1_prognose", "Prognoses");
			HEADER_QUEUE_INFO = page.getLocalHeader("ccatp1_queue_info", "Request queue information");
			HEADER_YES = page.getLocalHeader("ccatp1_yes", "Yes");
			HEADER_YES_BUT = page.getLocalHeader("ccatp1_yes_but", "No, but don't delete from queue");
			HEADER_NO = page.getLocalHeader("ccatp1_no", "No");
	
			GRANTED = page.localize("ccatp1_granted", "You have received an offer from ").toString();
		
			_initializeStatics = true;
		}
	}
	
	public ChildCarePlaceOfferTable1(ChildCareCustomerApplicationTable page, SortedSet applications) throws RemoteException {
		super(7, applications.size() + 1);
		initConstants(page);
	
		initTable();
					
		System.out.println("Applications: " + applications);
		Iterator i = applications.iterator();
		int row = 2;
		while (i.hasNext()) {
			ChildCareApplication app = ((ComparableApp) i.next()).getApplication();
			app.getChoiceNumber();
			
			String id = ((Integer) app.getPrimaryKey()).toString();
				
			String name = app.getProvider().getName();
				
			String offerText = "";
			boolean offer = app.getStatus().equalsIgnoreCase(ChildCareCustomerApplicationTable.STATUS_UBEH); /**@TODO: is this correct status?*/
			if (offer) {
					offerText = GRANTED + app.getFromDate(); 
			}
					
			String prognosis = app.getPrognosis() != null ? app.getPrognosis() : "";
	
			addToTable(row, id, app.getChoiceNumber() + ": " + name 
				//+ " (nodeId:" + app.getNodeID() + ")"
				, offerText, prognosis, offer);
	
			row++;
		}
	}
	
	/**
	 * Method addToTable.
	 * @param table
	 * @param row 
	 * @param name
	 * @param status
	 * @param prognosis
	 */
	private void addToTable(int row, String id, String name, String status, String prognosis, boolean offer) {
		int index = row - 1; //row=2 for first row because of heading is in row 1
		add(new HiddenInput(CCConstants.APPID + index, id)); 

		add(name, 1, row);
		add(status, 2, row);
		add(prognosis, 3, row);
		SubmitButton reqBtn = new SubmitButton(_page.localize(REQUEST_INFO), CCConstants.APPID, id);
		reqBtn.setName(REQUEST_INFO[0]);
		reqBtn.setAsImageButton(true);
		add(reqBtn, 4, row);
			
		RadioButton rb1 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.YES);
		RadioButton rb2 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.NO_NEW_DATE);
		RadioButton rb3 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.NO);
		rb1.setOnChange(CCConstants.NEW_DATE + index + ".disabled=true;"); //NewDate" + index + ".value=''
		rb2.setOnChange(CCConstants.NEW_DATE + index + ".disabled=false;");
		rb3.setOnChange(CCConstants.NEW_DATE + index + ".disabled=true;"); //NewDate" + index + ".value=''
		
		TextInput ti = new TextInput(CCConstants.NEW_DATE + index);
		ti.setLength(8);
		
//The trigger is called when the radio button is turned on

		
//		ti.setWidth("10");

			
		if (!offer){
			rb1.setDisabled(true);
			rb2.setDisabled(true);
			rb3.setDisabled(true);
			ti.setDisabled(true);
		} else {
			rb1.setAttribute("checked");
			ti.setDisabled(true);			
		}
			
		add(rb1, 5, row);
		add(rb2, 6, row);
		add(ti, 6, row);
		add(rb3, 7, row);
	}
	
	/**
	 * Method createTable.
	 * @return Table
	 */
	private void initTable() {
//		Table table = new Table(7, rows + 1); //Heading
//		setBorder(1);
//		setBorderColor("GREEN");
		
		setCellspacing(2);
		setCellpadding(4);
		setHorizontalZebraColored("WHITE", _page.getBackgroundColor());
	
		//Heading
		add(HEADER_YOUR_CHOICE, 1, 1);
		add(HEADER_OFFER, 2, 1);
		add(HEADER_PROGNOSE, 3, 1);
		add(HEADER_QUEUE_INFO, 4, 1);
		add(HEADER_YES, 5, 1);
		add(HEADER_YES_BUT, 6, 1);
		add(HEADER_NO, 7, 1);
	}
	

	
}
