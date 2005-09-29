package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;

import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.util.IWTimestamp;

class ChildCarePlaceOfferTable1 extends Table {

	private static Text HEADER_YOUR_CHOICE;
	//private static Text HEADER_OFFER;
	//private static Text HEADER_PROGNOSE;
	//private static Text HEADER_QUEUE_INFO;
	private static Text HEADER_YES;
	private static Text HEADER_YES_BUT;
	private static Text HEADER_NO;
	private static Text HEADER_FROM_DATE;
	private static Text HEADER_CREATED_DATE;
	private static Text HEADER_QUEUE_POSITION;
	private static String CONFIRM_DELETE;
	private static String CONFIRM_REQUEST;

	private static String GRANTED, VALID_UNTIL;

	private final static String[] SUBMIT_ALERT_1 = new String[] { "ccot_alert_1", "Do you want to commit your choice? This can not be undone afterwards." }, SUBMIT_UNVALID_DATE = new String[] { "ccot_valid_date", "Please select a valid date." }, EDIT_TOOLTIP = new String[] { "ccot_edit_tooltip", "View prognosis and provider queue" }, DELETE_TOOLTIP = new String[] { "ccot_delete_tooltip", "Delete" }, ALERT_TERMINATE_CONTRACT = new String[] { "ccot_terminate_contract", "After accepting this offer, remember to cancel your active contract./n" };

	private static boolean _initializeStatics = false;

	private String _onSubmitHandler = "";

	private static ChildCareCustomerApplicationTable _page;
	private List offerList;

	final static String[] REQUEST_INFO = new String[] { "ccatp1_request_info", "Request info" };
    
    private boolean containsSortedByBirthdateProvider = false;

	private void initConstants(ChildCareCustomerApplicationTable page) {
		if (!_initializeStatics) {
			_page = page;
			HEADER_YOUR_CHOICE = page.getLocalHeader("ccatp1_your_choice", "Your Choice");
			//HEADER_OFFER = page.getLocalHeader("ccatp1_offer", "Offer");
			//HEADER_PROGNOSE = page.getLocalHeader("ccatp1_prognose", "Prognoses");
			//HEADER_QUEUE_INFO = page.getLocalHeader("ccatp1_queue_info", "Request queue information");
			HEADER_YES = page.getLocalHeader("ccatp1_yes", "Yes");
			HEADER_YES_BUT = page.getLocalHeader("ccatp1_yes_but", "No, but don't delete from queue");
			HEADER_NO = page.getLocalHeader("ccatp1_no", "No");
			HEADER_FROM_DATE = page.getLocalHeader("ccatp1_from_date", "From date");
			HEADER_CREATED_DATE = page.getLocalHeader("ccatp1_created_date", "Created date");
			HEADER_QUEUE_POSITION = page.getLocalHeader("ccatp1_queue_position", "Queue pos.");

			CONFIRM_DELETE = page.localize("ccatp1_confirm_delete", "Really delete?").toString();
			CONFIRM_REQUEST = page.localize("ccatp1_confirm_request", "Do you want to send a request?").toString();
			GRANTED = page.localize("ccatp1_granted", "You have received an offer from").toString();
			VALID_UNTIL = page.localize("ccatp1_valid_until", "This offer is valid until").toString();

			_initializeStatics = true;
		}
	}

	public ChildCarePlaceOfferTable1(IWContext iwc, ChildCareCustomerApplicationTable parent, SortedSet applications, boolean hasOffer, boolean hasActivePlacement, boolean hasAcceptedApplication) throws RemoteException {

		initConstants(parent);
		Iterator i = applications.iterator();
		int row = 2;
		boolean offerPresented = false;

		//To avoid more than the first offer to be presented with accept/reject possibilities
		StringBuffer validateDateScript = new StringBuffer("false ");
		StringBuffer alertTerminateContractScript = new StringBuffer("false ");

		boolean choiceOneAccepted = false;
		while (i.hasNext()) {
			ChildCareApplication app = ((ComparableApp) i.next()).getApplication();

			if (app.isActive()) {
				continue;
			}

			//Only first offer should be presented with possibility to accept / reject
			boolean isOffer = app.getStatus().equalsIgnoreCase(ChildCareCustomerApplicationTable.STATUS_BVJD);

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

			//String[] scripts = addToTable(iwc, row, app, isOffer, offerPresented, disableAccept, iwc.getSessionAttribute(_page.REQ_BUTTON + app.getNodeID()) != null);
			String[] scripts = addToTable(iwc, row, app, isOffer, offerPresented, disableAccept, hasAcceptedApplication);

			validateDateScript.append(scripts[0]);
			alertTerminateContractScript.append(scripts[1]);

			if (isOffer) {
				offerPresented = true;
			}
            
            if (!this.isContainsSortedByBirthdateProvider()) {
                School provider = app.getProvider();
                if (provider.getSortByBirthdate()) {
                    this.setContainsSortedByBirthdateProvider(true);
                }
            }

			row++;
		}
		
		if (offerList != null) {
			setHeight(row++, 12);
			this.mergeCells(1, row, getColumns(), row);
			Iterator iter = offerList.iterator();
			while (iter.hasNext()) {
				String element = (String) iter.next();
				add(_page.getSmallText(element), 1, row);
				if (iter.hasNext())
					add(new Break(), 1, row);
			}
		}

		//Cannot use DateInput.setAsNotEmpty because we doesn't want this requirement 
		//unless the user has selected the actual radio button.		
		Page page = parent.getParentPage();
		Script script = null;
		if (page != null) {
			script = page.getAssociatedScript();
		}
		else {
			script = new Script();
			parent.add(script);
		}
		script.setFunction("validateDates", "function validateDates() { if(" + validateDateScript + ") { alert('" + _page.localize(SUBMIT_UNVALID_DATE) + "'); return false; } else {return true;}}");
		script.setFunction("alertTerminateContract", "function alertTerminateContract() { " + (!hasActivePlacement ? "return true; }" : "if(" + alertTerminateContractScript + ") { alert('" + _page.localize(ALERT_TERMINATE_CONTRACT) + "'); return true; } else {return true;}}"));

		_onSubmitHandler = "if (!validateDates()) " + "return false; " + "if (!alertTerminateContract()) " + "return false; " + "return confirm('" + _page.localize(SUBMIT_ALERT_1) + "')";

		initTable(hasOffer);
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
	//private String[] addToTable(IWContext iwc, int row, ChildCareApplication app, boolean isOffer, boolean offerPresented, boolean disableAccept, boolean disableReqBtn) throws RemoteException {
	private String[] addToTable(IWContext iwc, int row, ChildCareApplication app, boolean isOffer, boolean offerPresented, boolean disableAccept, boolean hasAcceptedApplication) throws RemoteException {

		int providerId = app.getProviderId();
		int ownerId = ((Integer)app.getOwner().getPrimaryKey()).intValue();
		boolean isAfterSchoolApplication = _page.childCarebusiness.isAfterSchoolApplication(app);

		String validUntil = app.getOfferValidUntil() != null ? VALID_UNTIL + " " + app.getOfferValidUntil() + "." : "";
		String offerText = isOffer ? app.getChoiceNumber() + ": " + GRANTED + " " + app.getFromDate() + ". " + validUntil : "";
		if (isOffer)
			addToOfferList(offerText);

		boolean presentOffer = isOffer && !offerPresented;
		boolean disable = offerPresented || app.getApplicationStatus() == _page.childCarebusiness.getStatusRejected();

		boolean isAccepted = app.isAcceptedByParent();
		boolean isCancelled = app.isCancelledOrRejectedByParent();

		int index = row - 1;
		int column = 1;

		//row=2 for first row because of heading is in row 1
		add(new HiddenInput(CCConstants.APPID + index, "" + app.getNodeID()), 1, 1);
		String textColor = "black";
		if (isCancelled) {
			textColor = "red";
		}
		else if (disable) {
			textColor = "grey";
		}
        
        //adding choice number and provider name
        add(getProviderName(app, isAccepted, textColor), column++, row);        

		String validateDateScript = "false";
		String alertTerminateContractScript = "false";

		if (presentOffer) {
			RadioButton rb1 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.YES);
			RadioButton rb2 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.NO_NEW_DATE);
			RadioButton rb3 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.NO);
			rb3.setMustBeSelected(_page.localize("child_care.must_select_offer_option", "You must select an offer option."));

			if (disableAccept)
				rb1.setDisabled(true);
			
			if (hasAcceptedApplication) {
				rb1.setOnClick("document.getElementById('" + rb1.getID() + "').checked = false; alert('" + _page.localize("child_care.must_delete_accepted_offer", "You must delete accepted offer before you can choose a new offer.") + "'); return false;");
			}

			DateInput date = (DateInput) _page.getStyledInterface(new DateInput(CCConstants.NEW_DATE + index, true));
			date.setStyleAttribute("style", _page.getSmallTextFontStyle());

			add(rb1, column++, row);
			setNoWrap(column, row);
			add(rb2, column, row);
			add(Text.getNonBrakingSpace(), column, row);
			add(Text.getNonBrakingSpace(), column, row);
			add(date, column++, row);
			add(rb3, column++, row);

			validateDateScript = "(document.getElementById('" + rb2.getID() + "').checked && " + "(document.getElementById('" + date.getIDForDay() + "').value == '00' || " + "document.getElementById('" + date.getIDForMonth() + "').value == '00' || " + "document.getElementById('" + date.getIDForYear() + "').value == 'YY'))";

			alertTerminateContractScript = "(document.getElementById('" + rb1.getID() + "').checked)";

		}
		else {
			IWTimestamp created = new IWTimestamp(app.getQueueDate());
			IWTimestamp validFrom = new IWTimestamp(app.getFromDate());
			
			setNoWrap(column, row);
			add(_page.getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			setNoWrap(column, row);
			add(_page.getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			
			int queuePosition = getChildCareBusiness(iwc).getNumberInQueue(app);
			add(_page.getSmallText(String.valueOf(queuePosition)), column++, row);
		}

		//if (!disableReqBtn && !isCancelled) {
		if (!isCancelled && !isAfterSchoolApplication) {
			Link reqBtn = new Link(_page.getQuestionIcon(_page.localize(REQUEST_INFO)));
			reqBtn.setOnClick("return confirm('" + CONFIRM_REQUEST + "')");
			reqBtn.addParameter(CCConstants.ACTION, CCConstants.ACTION_REQUEST_INFO);
			reqBtn.addParameter(CCConstants.APPID, app.getNodeID());

			reqBtn.setName(REQUEST_INFO[0]);
			add(reqBtn, column++, row);
		}

		if (!isCancelled && !isAfterSchoolApplication) {
			Link popup = new Link(_page.getEditIcon(_page.localize(EDIT_TOOLTIP)));
			popup.setWindowToOpen(ChildCareWindow.class);
			popup.setParameter(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_VIEW_PROVIDER_QUEUE));
			popup.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			popup.addParameter(CCConstants.PROVIDER_ID, "" + providerId);
			popup.addParameter(CCConstants.APPID, "" + app.getNodeID());
			popup.addParameter(CCConstants.USER_ID, "" + ownerId);
			add(popup, column++, row);
		}
		
		if (!isCancelled) {
			Link delete = new Link(_page.getDeleteIcon(_page.localize(DELETE_TOOLTIP)));
			delete.setOnClick("return confirm('" + CONFIRM_DELETE + "')");
			delete.addParameter(CCConstants.ACTION, CCConstants.ACTION_DELETE);
			delete.addParameter(CCConstants.APPID, app.getNodeID());
			add(delete, column++, row);
		}
		
	

		if (row % 2 == 0) {
			setRowColor(row++, _page.getZebraColor1());
		}
		else {
			setRowColor(row++, _page.getZebraColor2());
		}

		return new String[] { validateDateScript, alertTerminateContractScript };

	}

    private PresentationObjectContainer getProviderName(ChildCareApplication app, boolean isAccepted, String textColor) {
        PresentationObjectContainer nameContainer = new PresentationObjectContainer(); 
        
        School provider = app.getProvider();

        String choiceNumber = app.getChoiceNumber() + ": "; 
        String name =  provider.getName() + _page.getDebugInfo(app);
        
        Text t = _page.getSmallText(choiceNumber);
        if (isAccepted) {
            t.setBold();
        }
        t.setStyleAttribute("color:" + textColor);
        nameContainer.add(t);    
        
        if (provider.getSortByBirthdate()) {
            Text star = new Text("* ");
            star.setStyleClass("childcare_SmallExplanationTextStar");
            
            nameContainer.add(star);
        }        

        t = _page.getSmallText(name);
        if (isAccepted) {
            t.setBold();
        }
        t.setStyleAttribute("color:" + textColor);
        nameContainer.add(t);
        return nameContainer;
    }

	/**
	 * Method createTable.
	 * @return Table
	 */
	private void initTable(boolean hasOffer) {
		setCellspacing(2);
		setCellpadding(2);

		//Heading
		int col = 1;

		add(HEADER_YOUR_CHOICE, col++, 1);
		if (hasOffer) {
			setWidth(HUNDRED_PERCENT);
			setWidth(1, HUNDRED_PERCENT);
			setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
			setNoWrap(col, 1);
			add(HEADER_YES, col++, 1);
			setNoWrap(col, 1);
			add(HEADER_YES_BUT, col++, 1);
			setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
			setNoWrap(col, 1);
			add(HEADER_NO, col++, 1);
		}
		else {
			setWidth(HUNDRED_PERCENT);
			setWidth(1, HUNDRED_PERCENT);
			setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
			setNoWrap(col, 1);
			add(HEADER_CREATED_DATE, col++, 1);
			setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
			setNoWrap(col, 1);
			add(HEADER_FROM_DATE, col++, 1);
			setColumnAlignment(col, HORIZONTAL_ALIGN_CENTER);
			setNoWrap(col, 1);
			add(HEADER_QUEUE_POSITION, col++, 1);
		}

		setRowColor(1, _page.getHeaderColor());
	}
	
	private void addToOfferList(String offerText) {
		if (offerList == null)
			offerList = new ArrayList();
		offerList.add(offerText);
	}

	ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}

    public boolean isContainsSortedByBirthdateProvider() {
        return containsSortedByBirthdateProvider;
    }

    public void setContainsSortedByBirthdateProvider(
            boolean containsSortedByBirthdateProvider) {
        this.containsSortedByBirthdateProvider = containsSortedByBirthdateProvider;
    }
}