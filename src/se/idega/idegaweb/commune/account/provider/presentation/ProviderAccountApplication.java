/*
 * $Id: ProviderAccountApplication.java,v 1.2 2002/07/29 23:28:32 tryggvil Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.provider.presentation;
import com.idega.block.process.business.CaseBusiness;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.Validator;
import se.idega.idegaweb.commune.account.provider.business.ProviderAccountBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.Iterator;
/**
 * @author <a href="mail:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ProviderAccountApplication extends CommuneBlock
{
	protected final static int ACTION_VIEW_FORM = 0;
	protected final static int ACTION_SUBMIT_FORM = 1;
	private final static String PARAM_PROV_NAME = "paa_prov_name";
	private final static String PARAM_EMAIL = "paa_email";
	private final static String PARAM_PHONE = "paa_phone";
	private final static String PARAM_NUM_PLACES = "paa_num_places";
	private final static String PARAM_FORM_SUBMIT = "paa_submit";
	private final static String PARAM_ADDR = "paa_addr";
	private final static String PARAM_MAN_NAME = "paa_man_name";
	private final static String PARAM_ADD_INFO = "paa_add_info";
	//private final static String PARAM_PROV_TYPE = "paa_prov_type";
	private final static String ERROR_PROV_NAME = "paa_error_prov_name";
	private final static String ERROR_MAN_NAME = "paa_error_man_name";
	private final static String ERROR_ADDR = "paa_error_addr";
	private final static String ERROR_PHONE = "paa_error_phone_home";
	private final static String ERROR_NUM_PLACES = "paa_error_num_places";
	private final static String ERROR_NO_INSERT = "paa_no_insert";
	private final static String ERROR_NOT_EMAIL = "paa_err_email";
	private final static String TEXT_APPLICATION_SUBMITTED = "paa_app_submitted";
	private boolean _isProvNameError = false;
	private boolean _isManNameError = false;
	private boolean _isAddressError = false;
	private boolean _isPhoneError = false;
	private boolean _isEmailError = false;
	private boolean _isNumPlacesError = false;
	private boolean _isError = false;
	private Vector _errorMsg = null;
	private int applicationID = -1;
	protected String provNameString;
	protected String emailString;
	protected String phoneString;
	protected String numPlacesString;
	protected String addressString;
	protected String manNameString;
	protected String addInfoString;
	protected static final String PARAM_APPLICATION_ID = "paa_appl_id";
	int mainTableRows = 10;
	int mainTableColumns = 2;
	private Table inputTable = new Table(mainTableColumns, mainTableRows);
	public void main(IWContext iwc)
	{
		setResourceBundle(getResourceBundle(iwc));
		try
		{
			int action = parseAction(iwc);
			initData(iwc);
			performAction(action, iwc);
		}
		catch (Exception e)
		{
			super.add(new ExceptionWrapper(e, this));
		}
	}
	public Object clone(){
		ProviderAccountApplication p = (ProviderAccountApplication)super.clone();
		if(this.inputTable!=null){
			p.inputTable=(Table)this.inputTable.clone();
		}
		return p;
	}
	
	/**
	 * Can be ovverrided in subclasses
	 */
	protected void performAction(int action, IWContext iwc) throws Exception
	{
		switch (action)
		{
			case ACTION_VIEW_FORM :
				viewForm(iwc);
				break;
			case ACTION_SUBMIT_FORM :
				submitForm(iwc);
				break;
		}
	}
	/**
	 * Method initData.
	 * @param iwc
	 */
	private void initData(IWContext iwc)
	{
		String applicationIDString = iwc.getParameter(PARAM_APPLICATION_ID);
		if (getValidator().isInt(applicationIDString))
		{
			setApplicationID(Integer.parseInt(applicationIDString));
		}
		if (getApplicationID() == -1)
		{
			provNameString = iwc.getParameter(PARAM_PROV_NAME);
			emailString = iwc.getParameter(PARAM_EMAIL);
			phoneString = iwc.getParameter(PARAM_PHONE);
			numPlacesString = iwc.getParameter(PARAM_NUM_PLACES);
			addressString = iwc.getParameter(PARAM_ADDR);
			manNameString = iwc.getParameter(PARAM_MAN_NAME);
			addInfoString = iwc.getParameter(PARAM_ADD_INFO);
		}
	}
	/**
	 * Method getApplicationID.
	 * @return int
	 */
	protected int getApplicationID()
	{
		return applicationID;
	}
	private void viewForm(IWContext iwc)
	{
		Form accountForm = new Form();
		inputTable.setCellspacing(2);
		inputTable.setCellpadding(4);
		inputTable.setAlignment(2, 6, "right");
		inputTable.setColor(getBackgroundColor());
		String provName = localize(PARAM_PROV_NAME, "Shool/ChildcareCenter name");
		String email = localize(PARAM_EMAIL, "E-Mail");
		String phone = localize(PARAM_PHONE, "Phone");
		String num_places = localize(PARAM_NUM_PLACES, "Number of places");
		String address = localize(PARAM_ADDR, "Address");
		String manName = localize(PARAM_MAN_NAME, "Manager name");
		String addInfo = localize(PARAM_ADD_INFO, "Additional info");
		TextInput inputProvName = new TextInput(PARAM_PROV_NAME);
		inputProvName.setMaxlength(40);
		TextInput inputEmail = new TextInput(PARAM_EMAIL);
		inputEmail.setAsEmail(localize(ERROR_NOT_EMAIL, "Not a valid email"));
		inputEmail.setMaxlength(40);
		TextInput inputPhone = new TextInput(PARAM_PHONE);
		inputPhone.setMaxlength(20);
		TextInput inputNumPlaces = new TextInput(PARAM_NUM_PLACES);
		inputNumPlaces.setMaxlength(3);
		inputNumPlaces.setLength(3);
		TextInput inputAddress = new TextInput(PARAM_ADDR);
		inputAddress.setMaxlength(50);
		TextInput inputManName = new TextInput(PARAM_MAN_NAME);
		inputManName.setMaxlength(50);
		TextArea inputAddInfo = new TextArea(PARAM_ADD_INFO);
		inputAddInfo.setWidth("200");
		inputAddInfo.setHeight("100");
		inputProvName.setStyle(getSmallTextFontStyle());
		inputEmail.setStyle(getSmallTextFontStyle());
		inputPhone.setStyle(getSmallTextFontStyle());
		inputNumPlaces.setStyle(getSmallTextFontStyle());
		if (provNameString != null)
			inputProvName.setContent(provNameString);
		if (emailString != null)
			inputEmail.setContent(emailString);
		if (phoneString != null)
			inputPhone.setContent(phoneString);
		if (numPlacesString != null)
			inputNumPlaces.setContent(numPlacesString);
		if (addressString != null)
			inputAddress.setContent(addressString);
		if (manNameString != null)
			inputManName.setContent(manNameString);
		if (addInfoString != null)
			inputAddInfo.setContent(addInfoString);
		if (!_isProvNameError)
			inputTable.add(getSmallText(provName), 1, 1);
		else
			inputTable.add(getSmallErrorText(provName), 1, 1);
		if (!_isEmailError)
			inputTable.add(getSmallText(email), 2, 5);
		else
			inputTable.add(getSmallErrorText(email), 2, 5);
		if (!_isPhoneError)
			inputTable.add(getSmallText(phone), 1, 3);
		else
			inputTable.add(getSmallErrorText(phone), 1, 3);
		if (!_isManNameError)
			inputTable.add(getSmallText(manName), 1, 5);
		else
			inputTable.add(getSmallErrorText(manName), 1, 5);
		if (!_isAddressError)
			inputTable.add(getSmallText(address), 2, 1);
		else
			inputTable.add(getSmallErrorText(address), 2, 1);
		if (!_isNumPlacesError)
			inputTable.add(getSmallText(num_places), 2, 3);
		else
			inputTable.add(getSmallErrorText(num_places), 2, 3);
		//Text for additional info:				
		inputTable.mergeCells(1, 7, 2, 7);
		inputTable.add(getSmallText(addInfo), 1, 7);
		inputTable.add(inputProvName, 1, 2);
		inputTable.add(inputEmail, 2, 6);
		inputTable.add(inputPhone, 1, 4);
		inputTable.add(inputNumPlaces, 2, 4);
		inputTable.add(inputManName, 1, 6);
		inputTable.add(inputAddress, 2, 2);
		//Making room for textarea
		inputTable.mergeCells(1, 8, 2, 8);
		inputTable.add(inputAddInfo, 1, 8);
		addButtons(iwc);
		if (_isError)
		{
			if (_errorMsg != null)
			{
				Table errorTable = new Table(1, 1);
				errorTable.setCellspacing(2);
				errorTable.setCellpadding(4);
				Iterator it = _errorMsg.iterator();
				while (it.hasNext())
				{
					String errorMsg = (String) it.next();
					errorTable.add(getErrorText(errorMsg), 1, 1);
					errorTable.add(Text.getBreak(), 1, 1);
				}
				accountForm.add(errorTable);
			}
		}
		accountForm.add(inputTable);
		add(accountForm);
	}

	/**
	 * Method addButtons.
	 * @param iwc
	 */
	protected void addButtons(IWContext iwc)
	{
		SubmitButton submitButton =
			new SubmitButton(
				getBundle(iwc).getImageButton(localize(PARAM_FORM_SUBMIT, "Submit application")),
				PARAM_FORM_SUBMIT);
		submitButton.setStyle(getLinkFontStyle());
		//inputTable.add(submitButton, 2, 10);
		addButton(submitButton);	
	}

	private void submitForm(IWContext iwc)
	{
		String provNameString = iwc.getParameter(PARAM_PROV_NAME);
		String phoneString = iwc.getParameter(PARAM_PHONE);
		String emailString = iwc.getParameter(PARAM_EMAIL);
		String numPlacesString = iwc.getParameter(PARAM_NUM_PLACES);
		String addressString = iwc.getParameter(PARAM_ADDR);
		String manNameString = iwc.getParameter(PARAM_MAN_NAME);
		String addInfoString = iwc.getParameter(PARAM_ADD_INFO);
		String managerEmail = null;
		String address = null;
		String additionalInfo = null;
		String managerName = null;
		String provName = null;
		int numPlaces = -1;
		String telephone = null;
		_errorMsg = null;
		if (provNameString == null || provNameString.equals(""))
		{
			_isProvNameError = true;
			_isError = true;
			addErrorString(localize(ERROR_PROV_NAME, "Provider name invalid"));
		}
		if (!getValidator().isEmail(emailString))
		{
			_isEmailError = true;
			_isError = true;
			addErrorString(localize(ERROR_NOT_EMAIL, "Email invalid"));
		}
		if (phoneString == null || phoneString.equals(""))
		{
			_isPhoneError = true;
			_isError = true;
			addErrorString(localize(ERROR_PHONE, "Phone invalid"));
		}
		if (addressString == null || addressString.equals(""))
		{
			_isAddressError = true;
			_isError = true;
			addErrorString(localize(ERROR_ADDR, "Address invalid"));
		}
		if (manNameString == null || manNameString.equals(""))
		{
			_isManNameError = true;
			_isError = true;
			addErrorString(localize(ERROR_MAN_NAME, "Manager name invalid"));
		}
		if (!getValidator().isInt(numPlacesString))
		{
			_isNumPlacesError = true;
			_isError = true;
			addErrorString(localize(ERROR_NUM_PLACES, "Number of places invalid"));
		}
		if (_isError == true)
		{
			viewForm(iwc);
			return;
		}
		boolean insert = false;
		try
		{
			managerEmail = emailString;
			address = addressString;
			managerName = manNameString;
			provName = provNameString;
			numPlaces = Integer.parseInt(numPlacesString);
			telephone = phoneString;
			if (addInfoString != null)
			{
				additionalInfo = addInfoString;
			}
			//CitizenAccountBusiness business = (CitizenAccountBusiness)IBOLookup.getServiceInstance(iwc,CitizenAccountBusiness.class);
			ProviderAccountBusiness business = this.getBusiness(iwc);
			//insert = business.insertApplication(business.getUser(pidString),pidString,emailString,phoneHomeString,phoneWorkString);
			business.createApplication(
				provName,
				address,
				telephone,
				numPlaces,
				managerName,
				managerEmail,
				additionalInfo);
			insert = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			insert = false;
		}
		if (!insert)
		{
			_isError = true;
			addErrorString(localize(ERROR_NO_INSERT, "Unable to insert application"));
			viewForm(iwc);
			return;
		}
		if (getResponsePage() != null)
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		else
			add(new Text(localize(TEXT_APPLICATION_SUBMITTED, "Application submitted")));
	}
	private void addErrorString(String errorString)
	{
		if (_errorMsg == null)
			_errorMsg = new Vector();
		_errorMsg.add(errorString);
	}
	/**
	 * This method can be overrided to add new actions
	 */
	protected int parseAction(IWContext iwc)
	{
		int action = ACTION_VIEW_FORM;
		if (iwc.isParameterSet(PARAM_FORM_SUBMIT))
		{
			action = ACTION_SUBMIT_FORM;
		}
		return action;
	}
	protected ProviderAccountBusiness getBusiness(IWApplicationContext iwc) throws RemoteException
	{
		return (ProviderAccountBusiness) IBOLookup.getServiceInstance(iwc, ProviderAccountBusiness.class);
	}
	protected Validator getValidator()
	{
		return Validator.getInstance();
	}
	/**
	 * Returns the addInfoString.
	 * @return String
	 */
	public String getAdditionalInfo()
	{
		return addInfoString;
	}
	/**
	 * Returns the addressString.
	 * @return String
	 */
	public String getAddress()
	{
		return addressString;
	}
	/**
	 * Returns the emailString.
	 * @return String
	 */
	public String getEmail()
	{
		return emailString;
	}
	/**
	 * Returns the manNameString.
	 * @return String
	 */
	public String getManagerName()
	{
		return manNameString;
	}
	/**
	 * Returns the numPlacesString.
	 * @return String
	 */
	public int getNumPlaces()
	{
		try
		{
			return Integer.parseInt(numPlacesString);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
	/**
	 * Returns the phoneString.
	 * @return String
	 */
	public String getPhone()
	{
		return phoneString;
	}
	/**
	 * Returns the provNameString.
	 * @return String
	 */
	public String getProviderName()
	{
		return provNameString;
	}
	/**
	 * Sets the addInfoString.
	 * @param addInfoString The addInfoString to set
	 */
	public void setAdditionalInfo(String addInfoString)
	{
		this.addInfoString = addInfoString;
	}
	/**
	 * Sets the addressString.
	 * @param addressString The addressString to set
	 */
	public void setAddress(String addressString)
	{
		this.addressString = addressString;
	}
	/**
	 * Sets the emailString.
	 * @param emailString The emailString to set
	 */
	public void setEmail(String emailString)
	{
		this.emailString = emailString;
	}
	/**
	 * Sets the manNameString.
	 * @param manNameString The manNameString to set
	 */
	public void setManagerName(String manNameString)
	{
		this.manNameString = manNameString;
	}
	/**
	 * Sets the numPlacesString.
	 * @param numPlacesString The numPlacesString to set
	 * @throws NumberFormatException if numPlacesString is not an integer
	 */
	public void setNumberofPlaces(String numPlacesString)
	{
		Integer.parseInt(numPlacesString);
		this.numPlacesString = numPlacesString;
	}
	/**
	 * Sets the numPlacesString.
	 * @param numPlacesString The numPlacesString to set
	 */
	public void setNumberOfPlaces(int numPlaces)
	{
		this.numPlacesString = Integer.toString(numPlaces);
	}
	/**
	 * Sets the phoneString.
	 * @param phoneString The phoneString to set
	 */
	public void setPhone(String phoneString)
	{
		this.phoneString = phoneString;
	}
	/**
	 * Sets the provNameString.
	 * @param provNameString The provNameString to set
	 */
	public void setProviderName(String provNameString)
	{
		this.provNameString = provNameString;
	}
	protected void setApplicationID(Integer applicationID)
	{
		setApplicationID(applicationID.intValue());
	}
	protected void setApplicationID(int applicationID)
	{
		this.applicationID = applicationID;
	}
	protected void addButton(PresentationObject po)
	{
		inputTable.add(po, mainTableColumns, mainTableRows);
	}
}