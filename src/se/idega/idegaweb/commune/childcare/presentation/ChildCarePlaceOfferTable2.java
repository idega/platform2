package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.SortedSet;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
	
class ChildCarePlaceOfferTable2 extends Table{	
	
	private static Text HEADER_YOUR_CHOICE;
	private static Text HEADER_OFFER;
	private static Text HEADER_PROGNOSE;
	private static Text HEADER_QUEUE_INFO;
	private static Text HEADER_YES;
	private static Text HEADER_YES_BUT;
	private static Text HEADER_NO;

	private static String GRANTED;
	private static String REQUEST_INFO;

	private static boolean _initializeStatics = false;
	
	private static ChildCareCustomerApplicationTable _localizer;
	

	private void initConstants(ChildCareCustomerApplicationTable localizer){
		if (!_initializeStatics) {
			_localizer = localizer;
			HEADER_YOUR_CHOICE = localizer.getLocalHeader("ccatp2_your_choice", "Your Choice");
			HEADER_OFFER = localizer.getLocalHeader("ccatp2_offer", "Offer");
			HEADER_PROGNOSE = localizer.getLocalHeader("ccatp2_prognose", "Prognoses");
			HEADER_YES = localizer.getLocalHeader("ccatp2_yes", "Yes, keep application");
			HEADER_NO = localizer.getLocalHeader("ccatp2_no", "No, remove application");
	
			GRANTED = localizer.localize("ccatp2_granted", "You have received an offer from ").toString();

			_initializeStatics = true;
		}
	}
	
	public ChildCarePlaceOfferTable2(ChildCareCustomerApplicationTable localizer, SortedSet applications) throws RemoteException {
		super(5, applications.size() + 1); //Heading
		initConstants(localizer);
	
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
			
		RadioButton rb1 = new RadioButton(CCConstants.KEEP_IN_QUEUE + index, CCConstants.YES);
		RadioButton rb2 = new RadioButton(CCConstants.KEEP_IN_QUEUE + index, CCConstants.NO);
			
		if (offer){
			rb1.setAttribute("disabled");
			rb2.setAttribute("disabled");
		} else {
			rb1.setAttribute("checked");
		}
			
		add(rb1, 4, row);
		add(rb2, 5, row);
	}
	
	/**
	 * Method createTable.
	 * @return Table
	 */
	private void initTable() {

//		setBorder(1);
//		setBorderColor("RED");
		setCellspacing(2);
		setCellpadding(4);
		setHorizontalZebraColored("WHITE", _localizer.getBackgroundColor());
	
		//Heading
		add(HEADER_YOUR_CHOICE, 1, 1);
		add(HEADER_OFFER, 2, 1);
		add(HEADER_PROGNOSE, 3, 1);
		add(HEADER_YES, 4, 1);
		add(HEADER_NO, 5, 1);

	}
	
}
