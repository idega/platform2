package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.SortedSet;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

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
			HEADER_YOUR_CHOICE =
				page.getLocalHeader("ccatp2_your_choice", "Your Choice");
			HEADER_OFFER = page.getLocalHeader("ccatp2_offer", "Offer");
			HEADER_PROGNOSE =
				page.getLocalHeader("ccatp2_prognose", "Prognoses");
			HEADER_YES =
				page.getLocalHeader("ccatp2_yes", "Yes, keep application");
			HEADER_NO =
				page.getLocalHeader("ccatp2_no", "No, remove application");

			GRANTED =
				page
					.localize(
						"ccatp2_granted",
						"You have received an offer from ")
					.toString();

			_initializeStatics = true;
		}
	}

	public ChildCarePlaceOfferTable2(
		ChildCareCustomerApplicationTable page,
		SortedSet applications)
		throws RemoteException {
		super(5, applications.size() + 1); //Heading
		initConstants(page);

		initTable();

		System.out.println("Applications: " + applications);
		Iterator i = applications.iterator();
		int row = 2;

		while (i.hasNext()) {
			ChildCareApplication app =
				((ComparableApp) i.next()).getApplication();

			boolean offer =
				app.getStatus().equalsIgnoreCase(
					ChildCareCustomerApplicationTable.STATUS_PREL)
					&& app.getApplicationStatus()
						== _page.childCarebusiness.getStatusParentsAccept();

//			System.out.println("ChoiceNr: " + app.getChoiceNumber());
//			System.out.println("offerChoiceNr: " + offerChoiceNr);

			//No row is disabled if the first choice on the list is offered
			//			boolean disable = app.getApplicationStatus() == _page.childCarebusiness.getStatusCancelled() ||  
			//				app.getApplicationStatus() == _page.childCarebusiness.getStatusRejected() || 
			//				(app.getChoiceNumber() > offerChoiceNr && offerChoiceNr != 1);
			//			boolean disable = app.getChoiceNumber() > offerChoiceNr && offerChoiceNr != 1;


			addToTable(
				row,
				((Integer) app.getPrimaryKey()).toString(),
				app.getChoiceNumber() + ": " + app.getProvider().getName() + _page.getDebugInfo(app),
				offer ? GRANTED + app.getFromDate() : "",
				app.getPrognosis() != null ? app.getPrognosis() : "",
				offer,
				app.isCancelledOrRejectedByParent());

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
	 * @return Javascript for resetting this rows radiobutton
	 */
	private void addToTable(
		int row,
		String id,
		String name,
		String status,
		String prognosis,
		boolean offer,
		boolean isCancelled) {
			
		int index = row - 1;
		//row=2 for first row because of heading is in row 1
		add(new HiddenInput(CCConstants.APPID + index, id));
		String textColor = isCancelled ? "red" : "black";

		if (name != null) {
			Text t = _page.getSmallText(name);
			if (offer) {
				t.setBold(true);
			}
			t.setStyleAttribute("color:" + textColor);
			add(t, 1, row);
		}

		if (status != null) {
			Text t = _page.getSmallText(status);
			t.setStyleAttribute("color:" + textColor);
			add(t, 2, row);
		}

		if (prognosis != null) {
			Text t = _page.getSmallText(prognosis);
			t.setStyleAttribute("color:" + textColor);
			add(t, 3, row);
		}

		RadioButton rb1 =
			new RadioButton(CCConstants.KEEP_IN_QUEUE + index, CCConstants.YES);
		RadioButton rb2 =
			new RadioButton(CCConstants.KEEP_IN_QUEUE + index, CCConstants.NO);

		if (offer) {
			rb1.setAttribute("disabled");
			rb2.setAttribute("disabled");
		} else {
			rb1.setAttribute("checked");
		}

		if (!isCancelled && !offer) {
			add(rb1, 4, row);
			add(rb2, 5, row);
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

		//		setBorder(1);
		//		setBorderColor("RED");

		setRowColor(1, _page.getHeaderColor());

		//		setCellspacing(_page.getCellspacing());
		//		setCellpadding(_page.getCellpadding());

		setCellspacing(2);
		setCellpadding(4);

		//Heading
		add(HEADER_YOUR_CHOICE, 1, 1);
		add(HEADER_OFFER, 2, 1);
		add(HEADER_PROGNOSE, 3, 1);
		add(HEADER_YES, 4, 1);
		add(HEADER_NO, 5, 1);

	}

}
