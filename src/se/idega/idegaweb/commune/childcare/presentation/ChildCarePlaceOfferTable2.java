package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.SortedSet;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;

class ChildCarePlaceOfferTable2 extends Table {

	private static Text HEADER_YOUR_CHOICE;
	private static Text HEADER_OFFER;
	private static Text HEADER_PROGNOSE;
	private static Text HEADER_YES;
	private static Text HEADER_NO;

	private static String GRANTED;

	private static boolean _initializeStatics = false;

	private static ChildCareCustomerApplicationTable _page;

	private void initConstants(ChildCareCustomerApplicationTable page) {
		if (!_initializeStatics) {
			_page = page;
			HEADER_YOUR_CHOICE = page.getLocalHeader("ccatp2_your_choice", "Your Choice");
			HEADER_OFFER = page.getLocalHeader("ccatp2_offer", "Offer");
			HEADER_PROGNOSE = page.getLocalHeader("ccatp2_queue_position", "Queue position");
			HEADER_YES = page.getLocalHeader("ccatp2_yes", "Yes, keep application");
			HEADER_NO = page.getLocalHeader("ccatp2_no", "No, remove application");

			GRANTED = page.localize("ccatp2_granted", "You have accepted this offer, starting ").toString();

			_initializeStatics = true;
		}
	}

	public ChildCarePlaceOfferTable2(IWContext iwc, ChildCareCustomerApplicationTable page, SortedSet applications) throws RemoteException {
		super(5, applications.size() + 1); //Heading
		initConstants(page);

		Iterator i = applications.iterator();
		int row = 2;
		while (i.hasNext()) {
			ChildCareApplication app = ((ComparableApp) i.next()).getApplication();

			boolean offer = app.getStatus().equalsIgnoreCase(ChildCareCustomerApplicationTable.STATUS_PREL) && app.getApplicationStatus() == _page.childCarebusiness.getStatusParentsAccept();

			String prognosis = String.valueOf(getChildCareBusiness(iwc).getNumberInQueue(app));
			//String status = app.getChoiceNumber() + ": " + app.getProvider().getName() + _page.getDebugInfo(app) + " (" + app.getMessage() + ")";
			
			addToTable(row, app.getPrimaryKey().toString(), app.getProvider().getSchoolName(), prognosis, offer, app.isCancelledOrRejectedByParent());

			row++;
		}
		
		initTable();
	}

	/**
	 * Method addToTable.
	 * @param table
	 * @param row
	 * @param name
	 * @param status
	 * @param prognosis
	 * @return Javascript for resetting this rows radiobutton
	 */
	private void addToTable(int row, String id, String name, String prognosis, boolean offer, boolean isCancelled) {

		int index = row - 1;
		int column = 1;
		//row=2 for first row because of heading is in row 1
		add(new HiddenInput(CCConstants.APPID + index, id));
		String textColor = isCancelled ? "red" : "black";

		if (name != null) {
			Text t = _page.getSmallText(name);
			if (offer) {
				t.setBold(true);
			}
			t.setStyleAttribute("color:" + textColor);
			add(t, column++, row);
		}

		/*if (status != null) {
			Text t = _page.getSmallText(status);
			t.setStyleAttribute("color:" + textColor);
			add(t, column++, row);
		}*/

		if (prognosis != null) {
			Text t = _page.getSmallText(prognosis);
			t.setStyleAttribute("color:" + textColor);
			add(t, column++, row);
		}

		RadioButton rb1 = new RadioButton(CCConstants.KEEP_IN_QUEUE + index, CCConstants.YES);
		RadioButton rb2 = new RadioButton(CCConstants.KEEP_IN_QUEUE + index, CCConstants.NO);

		if (offer) {
			rb1.setAttribute("disabled");
			rb2.setAttribute("disabled");
		}
		else {
			rb1.setAttribute("checked");
		}

		if (!isCancelled && !offer) {
			add(rb1, column++, row);
			add(rb2, column++, row);
		}

		if (row % 2 == 0)
			setRowColor(row++, _page.getZebraColor1());
		else
			setRowColor(row++, _page.getZebraColor2());
	}

	/**
	 * Method createTable.
	 * @return Table
	 */
	private void initTable() {
		setRowColor(1, _page.getHeaderColor());

		setCellspacing(_page.getCellspacing());
		setCellpadding(_page.getCellpadding());

		int col = 1;
		//Heading
		add(HEADER_YOUR_CHOICE, col++, 1);
		//setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
		//add(HEADER_OFFER, col++, 1);
		setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
		add(HEADER_PROGNOSE, col++, 1);
		setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
		add(HEADER_YES, col++, 1);
		setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
		add(HEADER_NO, col++, 1);
	}

	ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}
}