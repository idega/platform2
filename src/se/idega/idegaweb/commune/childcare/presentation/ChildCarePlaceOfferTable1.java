package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.SortedSet;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.presentation.IWContext;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;

class ChildCarePlaceOfferTable1 extends Table {

	private static Text HEADER_YOUR_CHOICE;
	private static Text HEADER_OFFER;
	private static Text HEADER_PROGNOSE;
	private static Text HEADER_QUEUE_INFO;
	private static Text HEADER_YES;
	private static Text HEADER_YES_BUT;
	private static Text HEADER_NO;
	private static String CONFIRM_DELETE;

	private static String GRANTED, VALID_UNTIL;

	private final static 
		String[] SUBMIT_ALERT_1 =
			new String[] {
				"ccot_alert_1",
				"Do you want to commit your choice? This can not be undone afterwards." 
			},
		SUBMIT_UNVALID_DATE =
			new String[] {
				"ccot_valid_date",
				"Please select a valid date." 
			},
		EDIT_TOOLTIP = 
			new String[] {
				"ccot_edit_tooltip",
				"View prognosis and provider queue"		
			},
		DELETE_TOOLTIP = 
			new String[] {
				"ccot_delete_tooltip",
				"Delete"		
			},
		ALERT_TERMINATE_CONTRACT = 
			new String[] {
				"ccot_terminate_contract",
				"After accepting this offer, remember to cancel your active contract./n"		
			};			
		

	private static boolean _initializeStatics = false;

	private String _onSubmitHandler = "";

	private static ChildCareCustomerApplicationTable _page;

	final static String[] REQUEST_INFO =
		new String[] { "ccatp1_request_info", "Request info" };

	private void initConstants(ChildCareCustomerApplicationTable page) {
		if (!_initializeStatics) {
			_page = page;
			HEADER_YOUR_CHOICE =
				page.getLocalHeader("ccatp1_your_choice", "Your Choice");
			HEADER_OFFER = page.getLocalHeader("ccatp1_offer", "Offer");
			HEADER_PROGNOSE =
				page.getLocalHeader("ccatp1_prognose", "Prognoses");
			HEADER_QUEUE_INFO =
				page.getLocalHeader(
					"ccatp1_queue_info",
					"Request queue information");
			HEADER_YES = page.getLocalHeader("ccatp1_yes", "Yes");
			HEADER_YES_BUT =
				page.getLocalHeader(
					"ccatp1_yes_but",
					"No, but don't delete from queue");
			HEADER_NO = page.getLocalHeader("ccatp1_no", "No");

			CONFIRM_DELETE =
				page
					.localize("ccatp1_confirm_delete", "Really delete?")
					.toString();

			GRANTED =
				page
					.localize(
						"ccatp1_granted",
						"You have received an offer from")
					.toString();
			VALID_UNTIL =
				page
					.localize("ccatp1_valid_until", "This offer is valid until")
					.toString();

			_initializeStatics = true;
		}
	}

	public ChildCarePlaceOfferTable1(
		IWContext iwc,
		ChildCareCustomerApplicationTable page,
		SortedSet applications,
		boolean hasOffer,
		boolean hasActivePlacement)
		throws RemoteException {

		//		super(9, applications.size() + 1);
		initConstants(page);

		initTable(hasOffer);

		//		System.out.println("Applications: " + applications);
		Iterator i = applications.iterator();
		int row = 2;
		boolean offerPresented = false;
		//To avoid more than the first offer to be presented with accept/reject possibilities
		StringBuffer validateDateScript = new StringBuffer("false ");
		StringBuffer alertTerminateContractScript = new StringBuffer("false ");
		
				

		boolean choiceOneAccepted = false;
		while (i.hasNext()) {
			ChildCareApplication app =
				((ComparableApp) i.next()).getApplication();

			if (app.isActive()) {
				continue;
			}

			//Only first offer should be presented with possibility to accept / reject
			boolean isOffer =
				app.getStatus().equalsIgnoreCase(
					ChildCareCustomerApplicationTable.STATUS_BVJD);

			//When simultaneous offers for choice 1 and 2 and choice 1 is accepted, the user shall, for offer 2,
			//be presented with possibilities only to reject with new date or reject, not accept.
			boolean disableAccept = false;
			if (app.isAcceptedByParent() && app.getChoiceNumber() == 1) {
				choiceOneAccepted = true;
			}
			if (app.getChoiceNumber() == 2 && isOffer && choiceOneAccepted) {
				disableAccept = true;
			}

			//Adding row to the table
			validateDateScript.append(" || ");
			alertTerminateContractScript.append(" || ");
			
			String[] scripts = 
				addToTable(
					row,
					app,
					hasOffer,
					isOffer,
					offerPresented,
					disableAccept,
					iwc.getSessionAttribute(_page.REQ_BUTTON + app.getNodeID())
						!= null
				);
				
			validateDateScript.append(scripts[0]);				
			alertTerminateContractScript.append(scripts[1]);				

			if (isOffer) {
				offerPresented = true;
			}

			row++;
		}

		//Cannot use DateInput.setAsNotEmpty because we doesn't want this requirement 
		//unless the user has selected the actual radio button.		

		Script script = new Script("javascript");

		script.setFunction(
			"validateDates",
			"function validateDates() { if("
				+ validateDateScript
				+ ") { alert('" + _page.localize(SUBMIT_UNVALID_DATE) + "'); return false; } else {return true;}}");

		script.setFunction(
			"alertTerminateContract",
			"function alertTerminateContract() { " +
			(! hasActivePlacement ? "return true; }" :
			"if("
				+ alertTerminateContractScript
				+ ") { alert('" + _page.localize(ALERT_TERMINATE_CONTRACT) + "'); return false; } else {return true;}}"));


		_onSubmitHandler =
			"if (!validateDates()) " +				"return false; " +			"if (!alertTerminateContract()) " +				"return false; " +			"return confirm('"
				+ _page.localize(SUBMIT_ALERT_1)
				+ "')";

		page.add(script);
	}

	public String getOnSubmitHandler() {
		return _onSubmitHandler;
	}

	/**
	 * Method addToTable.
	 * @param table
	 * @param row 
	 * @param name
	 * @param status
	 * @param prognosis
	 */
	private String[] addToTable(
		int row,
		ChildCareApplication app,
		boolean hasOffer,
		boolean isOffer,
		boolean offerPresented,
		boolean disableAccept,
		boolean disableReqBtn)
		throws RemoteException {

		int providerId = app.getProviderId();
		int ownerId = app.getOwner().getID();
		
		String name =
			app.getChoiceNumber()
				+ ": "
				+ app.getProvider().getName()
				+ _page.getDebugInfo(app)
				+ " ("+app.getMessage()+")";

		String validUntil =
			app.getOfferValidUntil() != null
				? VALID_UNTIL + " " + app.getOfferValidUntil() + "."
				: "";

		String offerText =
			isOffer
				? app.getQueueDate()
					+ ": "
					+ GRANTED
					+ " "
					+ app.getFromDate()
					+ ". "
					+ validUntil
				: "";

		boolean presentOffer = isOffer && !offerPresented;
		boolean disable =
			offerPresented
				|| app.getApplicationStatus()
					== _page.childCarebusiness.getStatusRejected();

		boolean isAccepted = app.isAcceptedByParent();
		boolean isCancelled = app.isCancelledOrRejectedByParent();

		//			
		//			app.getProviderId(),
		//			app.getOwner().getID(),
		//			app.getChoiceNumber()
		//				+ ": "
		//				+ app.getProvider().getName()
		//				+ _page.getDebugInfo(app),
		//			offer ? GRANTED + app.getFromDate() : "",
		//			app.getPrognosis() != null ? app.getPrognosis() : "",
		//			offer && !offerPresented,
		//			offerPresented
		//				|| app.getApplicationStatus()
		//					== _page.childCarebusiness.getStatusRejected(),
		//			iwc.getSessionAttribute(_page.REQ_BUTTON + app.getNodeID())	!= null,
		//			app.isAcceptedByParent(),
		//			app.isCancelledOrRejectedByParent(),

		int index = row - 1;

		//row=2 for first row because of heading is in row 1
		add(new HiddenInput(CCConstants.APPID + index, "" + app.getNodeID()));
		String textColor = "black";
		if (isCancelled) {
			textColor = "red";
		} else if (disable) {
			textColor = "grey";
		}

		if (name != null) {
			Text t = _page.getSmallText(name);
			if (isAccepted) {
				t.setBold();
			}
			t.setStyleAttribute("color:" + textColor);
			add(t, 1, row);
		}

		if (offerText != null) {
			Text t = _page.getSmallText(offerText);
			t.setStyleAttribute("color:" + textColor);
			add(t, 2, row);
		}

		if (!disableReqBtn && !isCancelled) {
			Link reqBtn = new Link(_page.localize(REQUEST_INFO));
			reqBtn.addParameter(
				CCConstants.ACTION,
				CCConstants.ACTION_REQUEST_INFO);
			reqBtn.addParameter(CCConstants.APPID, app.getNodeID());

			reqBtn.setName(REQUEST_INFO[0]);
			reqBtn.setAsImageButton(true);
			add(reqBtn, 3, row);
		}

		//		System.out.println("DATE ID" + date.getID());		

		//TextInput ti = new TextInput(CCConstants.NEW_DATE + index);
		//ti.setLength(8);

		//The trigger is called when the radio button is turned on

		//		ti.setWidth("10");

		//        if (!disable){

		//        }

		String validateDateScript = "false";
		String alertTerminateContractScript = "false";

		if (presentOffer) {
			RadioButton rb1 =
				new RadioButton(
					CCConstants.ACCEPT_OFFER + index,
					CCConstants.YES);
			RadioButton rb2 =
				new RadioButton(
					CCConstants.ACCEPT_OFFER + index,
					CCConstants.NO_NEW_DATE);
			RadioButton rb3 =
				new RadioButton(
					CCConstants.ACCEPT_OFFER + index,
					CCConstants.NO);
			/*	rb1.setOnChange(CCConstants.NEW_DATE + index + ".disabled=true;"); //NewDate" + index + ".value=''
				rb2.setOnChange(CCConstants.NEW_DATE + index + ".disabled=false;");
				rb3.setOnChange(CCConstants.NEW_DATE + index + ".disabled=true;"); //NewDate" + index + ".value=''
			*/
			if (disableAccept) {
				rb1.setDisabled(true);
				rb2.setSelected();
			} else {
				rb1.setSelected();
			}
			
			DateInput date =
				(DateInput) _page.getStyledInterface(
					new DateInput(CCConstants.NEW_DATE + index, true));
			date.setStyleAttribute("style", _page.getSmallTextFontStyle());

			//			System.out.println("DATE ID" + date.getIDForDay());

			//			b.setWindowToOpen(ChildCareProviderQueueWindow.class);

			add(rb1, 4, row);
			add(new Text("<NOBR>"), 5, row);
			add(rb2, 5, row);
			add(date, 5, row);
			add(new Text("</NOBR>"), 5, row);
			add(rb3, 6, row);

			validateDateScript =
				"(document.getElementById('"
					+ rb2.getID()
					+ "').checked && "
					+ "(document.getElementById('"
					+ date.getIDForDay()
					+ "').value == '00' || "
					+ "document.getElementById('"
					+ date.getIDForMonth()
					+ "').value == '00' || "
					+ "document.getElementById('"
					+ date.getIDForYear()
					+ "').value == 'YY'))";
					
			alertTerminateContractScript = 
				"(document.getElementById('"
					+ rb1.getID()
					+ "').checked)";	

		}

		if (!isCancelled)  {
			int col = hasOffer ? 7 : 4;
	
			Link popup = new Link();
			//		popup.setImage(new Image());
			//		popup.setAsImageButton(true);
			popup.setImage(_page.getEditIcon(_page.localize(EDIT_TOOLTIP)));
			popup.setWindowToOpen(ChildCareProviderQueueWindow.class);
			popup.addParameter(CCConstants.PROVIDER_ID, "" + providerId);
			popup.addParameter(CCConstants.APPID, "" + app.getNodeID());
			popup.addParameter(CCConstants.USER_ID, "" + ownerId);
	
			add(popup, col++, row);
	
			Link delete = new Link();
			//		delete.setAsImageButton(true);
			delete.setImage(_page.getDeleteIcon(_page.localize(DELETE_TOOLTIP)));
			//		popup.setImage(new Image());
			delete.setOnClick("return confirm('" + CONFIRM_DELETE + "')");
			delete.addParameter(CCConstants.ACTION, CCConstants.ACTION_DELETE);
			delete.addParameter(CCConstants.APPID, app.getNodeID());
			add(delete, col++, row);
		}

		if (row % 2 == 0) {
			setRowColor(row++, _page.getZebraColor1());
		} else {
			setRowColor(row++, _page.getZebraColor2());
		}

		return new String[] {validateDateScript, alertTerminateContractScript};

	}

	/**
	 * Method createTable.
	 * @return Table
	 */
	private void initTable(boolean hasOffer) {
		//		Table table = new Table(7, rows + 1); //Heading
		//		setBorder(1);
		//		setBorderColor("GREEN");

		setCellspacing(2);
		setCellpadding(4);

		//Heading
		int col = 1;

		add(HEADER_YOUR_CHOICE, col++, 1);
		add(HEADER_OFFER, col++, 1);
		add(HEADER_QUEUE_INFO, col++, 1);
		if (hasOffer) {
			add(HEADER_YES, col++, 1);
			add(HEADER_YES_BUT, col++, 1);
			add(HEADER_NO, col++, 1);
		}

		setRowColor(1, _page.getHeaderColor());
	}

}
