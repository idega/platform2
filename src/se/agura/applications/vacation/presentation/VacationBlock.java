/*
 * Created on Nov 8, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package se.agura.applications.vacation.presentation;

import se.agura.applications.vacation.business.VacationBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InterfaceObject;

/**
 * @author Anna
 */
public abstract class VacationBlock extends Block {

	// ﬂessi klasi inniheldur allt sem er sameiginlegt Ì Vacation Request hluta
	// kerfisins.
	protected static final String IW_BUNDLE_IDENTIFIER = "se.agura.applications.vacation";

	protected static final String PARAMETER_ACTION = "vac_action";

	protected static final String PARAMETER_PRIMARY_KEY_VAC_TIME = "vac_time_pk";

	protected static final String PARAMETER_PRIMARY_KEY_VAC_TYPE = "vac_type_pk";

	// protected static final String PARAMETER_VACATION_TIME =
	// "vac_vacation_time";
	protected static final String PARAMETER_VACATION_TYPE = "vac_vacation_type";

	protected static final String PARAMETER_VACATION_FROM_DATE = "vac_vacation_from_date";

	protected static final String PARAMETER_VACATION_TO_DATE = "vac_vacation_to_date";

	protected static final String PARAMETER_VACATION_HOURS = "vac_vacation_hours";

	protected static final String PARAMETER_VACATION_WORKING_HOURS = "vac_vacation_working_hours";

	protected static final String PARAMETER_VACATION_EXTRA_TEXT = "vac_vacation_extra_text";
	//spurning til Ladda: Eiga ﬂeir parametrar sem tilheyra bara ö›rum undir-klasanum a› fara ﬂanga›!?
	protected static final String ACTION_NEXT = "next";

	protected static final String ACTION_CANCEL = "cancel";

	protected static final String ACTION_SEND = "send";

	protected static final String ACTION_DENIED = "denied";

	protected static final String ACTION_APPROVED = "approved";

	protected static final String ACTION_BACK = "back";

	protected static final String ACTION_PAGE_THREE = "page_three";

	protected static final String ACTION_PAGE_FOUR = "page_four";

	protected static final String ACTION_SAVE = "save";

	protected static final String ACTION_FORWARD = "forward";

	private IWBundle iwb;

	private IWResourceBundle iwrb;
	
	private String iTextStyleClass;
	private String iHeaderStyleClass;
	private String iLinkStyleClass;
	private String iInputStyleClass;
	private String iButtonStyleClass;
	private String iRadioStyleClass;

	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		present(iwc);
	}

	protected VacationBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (VacationBusiness) IBOLookup.getServiceInstance(iwac, VacationBusiness.class);
		}
		catch (IBOLookupException ible) {
			throw new IBORuntimeException(ible);
		}
	}

	public abstract void present(IWContext iwc);

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * @return Returns the iwb.
	 */
	protected IWBundle getBundle() {
		return iwb;
	}

	/**
	 * @return Returns the iwrb.
	 */
	protected IWResourceBundle getResourceBundle() {
		return iwrb;
	}
	
	protected Text getText(String string) {
		Text text = new Text(string);
		if (iTextStyleClass != null) {
			text.setStyleClass(iTextStyleClass);
		}
		return text;
	}
	
	protected Text getHeader(String string) {
		Text text = new Text(string);
		if (iHeaderStyleClass != null) {
			text.setStyleClass(iHeaderStyleClass);
		}
		return text;
	}
	
	protected Link getLink(String string) {
		Link link = new Link(string);
		if (iLinkStyleClass != null) {
			link.setStyleClass(iLinkStyleClass);
		}
		return link;
	}
	
	protected InterfaceObject getInput(InterfaceObject input) {
		if (iInputStyleClass != null) {
			input.setStyleClass(iInputStyleClass);
		}
		return input;
	}
	
	protected InterfaceObject getRadioButton(InterfaceObject radioButton) {
		if (iRadioStyleClass != null) {
			radioButton.setStyleClass(iRadioStyleClass);
		}
		return radioButton;
	}
	
	protected GenericButton getButton(GenericButton button) {
		if (iButtonStyleClass != null) {
			button.setStyleClass(iButtonStyleClass);
		}
		return button;
	}
	
	/**
	 * @param buttonStyleClass The buttonStyleClass to set.
	 */
	public void setButtonStyleClass(String buttonStyleClass) {
		iButtonStyleClass = buttonStyleClass;
	}
	/**
	 * @param headerStyleClass The headerStyleClass to set.
	 */
	public void setHeaderStyleClass(String headerStyleClass) {
		iHeaderStyleClass = headerStyleClass;
	}
	/**
	 * @param inputStyleClass The inputStyleClass to set.
	 */
	public void setInputStyleClass(String inputStyleClass) {
		iInputStyleClass = inputStyleClass;
	}
	/**
	 * @param linkStyleClass The linkStyleClass to set.
	 */
	public void setLinkStyleClass(String linkStyleClass) {
		iLinkStyleClass = linkStyleClass;
	}
	/**
	 * @param radioStyleClass The radioStyleClass to set.
	 */
	public void setRadioStyleClass(String radioStyleClass) {
		iRadioStyleClass = radioStyleClass;
	}
	/**
	 * @param textStyleClass The textStyleClass to set.
	 */
	public void setTextStyleClass(String textStyleClass) {
		iTextStyleClass = textStyleClass;
	}
}