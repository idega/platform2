/*
 * Created on 6.7.2003
 *
 */
package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IWEventListener;
import com.idega.core.data.Address;
import com.idega.core.data.Country;
import com.idega.core.data.CountryHome;
import com.idega.core.data.Email;
import com.idega.core.data.Phone;
import com.idega.core.data.PhoneHome;
import com.idega.core.data.PhoneType;
import com.idega.core.data.PhoneTypeBMPBean;
import com.idega.core.data.PhoneTypeHome;
import com.idega.core.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CountryDropdownMenu;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.PostalCodeDropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.data.UserStatus;
import com.idega.util.IWTimestamp;

/**
 * The <code>UserEditor</code> handles user relations and addresses.
 * It contains a configurable unstrict user search. If more than one
 * user comply to the search criteria, a window with the compliant users
 * will popup where the right user can be chosen.
 * The chosen users relations can be handled, both family and other kind
 * of relations. If the deceased date is registered the listeners listening
 * to that event should be notified.
 * 
 * @author <a href="mailto:aron@idega.is"> Aron Birkir 
 * @version 1.0
 */
public class UserEditor extends Block {

	private static final String prm_coaddress_country_id = "co_country_id";
	private static final String prm_coaddress_postal_id = "co_postal_id";
	private static final String prm_primaddress_country_id = "prim_country_id";
	private static final String prm_primaddress_postal_id = "prim_postal_id";
	private static final String prm_deceased_date = "deceased_date";
	private static final String prm_email_address = "email_address";
	private static final String prm_main_phone = "phone_number";
	private static final String prm_mainaddress_street = "addr_prim_str";
	private static final String prm_mainaddress_postal_code = "addr_prim_pst_code";
	private static final String prm_mainaddress_postal_name = "addr_prim_pst_name";
	private static final String prm_mainaddress_country ="addr_prim_country";
	private static final String prm_coaddress_street = "addr_co_str";
	private static final String prm_coaddress_postal_code = "addr_co_pst_code";
	private static final String prm_coaddress_postal_name = "addr_co_pst_name";
	private static final String prm_coaddress_country ="addr_co_country";
	private static final String prm_old_value_suffix = "_old";
	/** Parameter for user id */
	//public static final String PRM_USER_ID = UserSearcher.PRM_USER_ID; //"ic_user_id";
	protected static final String PRM_SAVE = "mbe_save";
	/** The userID is the handled users ID. */
	protected Integer userID = null;
	/** The user currently handled */
	protected User user = null;
	/** The dynamic bundle identifier*/
	private String bundleIdentifer = null;
	/** The  static bundle identifier used in this package */
	private static String BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	/** The Bundle */
	protected IWBundle iwb;
	/** The resource bundle */
	protected IWResourceBundle iwrb;
	/** The list of relationstyped handle by the editor */
	private List relationTypes = new Vector();

	/** Determines if we show the users relations */
	protected boolean showUserRelations = true;
	/** The main layout table */
	private Table mainTable = null;
	/** the current layout table row */
	private int mainRow = 1;

	/** Header style */
	private String headerStyle = "";
	/** Header style class */
	private String headerStyleClass = "";
	/** flag for family relation types */
	protected boolean showAllRelationTypes = true;
	/** Class of relation connector window */
	protected Class connectorWindowClass = FamilyRelationConnector.class;

	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_HEADER = "Header";
	public final static String STYLENAME_DECEASED = "Deceased";
	public final static String STYLENAME_BUTTON = "Button";
	public final static String STYLENAME_INTERFACE = "Interface";

	protected String textFontStyle = "font-weight:plain;";
	protected String headerFontStyle = "font-weight:bold;";
	protected String deceasedFontStyle = "font-weight:bold;font-color:red";
	protected String buttonStyle =
		"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	protected String interfaceStyle =
		"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";

	protected String textFontStyleName = null;
	protected String headerFontStyleName = null;
	protected String deceasedFontStyleName = null;
	protected String buttonStyleName = null;
	protected String interfaceStyleName = null;

	private UserSearcher searcher = null;

	private void initStyleNames() {
		if (textFontStyleName == null)
			textFontStyleName = getStyleName(STYLENAME_TEXT);
		if (headerFontStyleName == null)
			headerFontStyleName = getStyleName(STYLENAME_HEADER);
		if (buttonStyleName == null)
			buttonStyleName = getStyleName(STYLENAME_BUTTON);
		if (interfaceStyleName == null)
			interfaceStyleName = getStyleName(STYLENAME_INTERFACE);
		if (deceasedFontStyleName == null)
			deceasedFontStyleName = getStyleName(STYLENAME_DECEASED);
	}

	/**
	 * Constructs a new UserEditor with an empty list of relationtypes
	 */
	public UserEditor() {
		this(new ArrayList());
	}

	public UserEditor(List relationTypes) {
		this.relationTypes = relationTypes;
		searcher = new UserSearcher();
		searcher.setUniqueIdentifier("edt");
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) {
		//debugParameters(iwc);
		// get bundles
		initStyleNames();
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		//iwc.getApplication().getLog().info("Who is your daddy ?");
		try {
			process(iwc);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		try {
			presentate(iwc);
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		if (bundleIdentifer != null)
			return bundleIdentifer;
		return BUNDLE_IDENTIFIER;
	}

	/**
	 * Sets the dynamic bundle identifier
	 * @param string
	 */
	public void setBundleIdentifer(String string) {
		bundleIdentifer = string;
	}

	/**
	 * Appends a new relation type to be handled at the specified index
	 * @param index of which the specified element is to be inserted
	 * @param element to be inserted
	 */
	public void addRelationType(int index, String type) {
		relationTypes.add(index, type);
	}

	/**
	 * Appends a new relation type to be handled
	 * @param type to be inserted 
	 */
	public void addRelationType(String type) {
		relationTypes.add(type);
	}

	public void setToShowAllRelationTypes(boolean bool) {
		showAllRelationTypes = bool;
	}

	/**
	 * Presentates the whole UserEditor
	 * @param iwc the current context
	 */
	public void presentate(IWContext iwc) throws RemoteException {
		mainTable = new Table();

		mainTable.add((searcher), 1, mainRow++);
		searcher.setOwnFormContainer(false);
		if (user != null) {
			searcher.setUser(user);
		}
		else if (user == null) {
			try {
				searcher.process(iwc);
				user = searcher.getUser();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}

		}

		if (user != null) {
			presentateUserInfo(iwc);
			if (showUserRelations)
				try {
					presentateUserRelations(iwc);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			presentateButtons(iwc);
		}

		Form form = new Form();

		form.add(mainTable);
		add(form);
	}

	/**
	 * Presentates the users relations
	 * @param iwc the context
	 */
	protected void presentateUserRelations(IWContext iwc) throws RemoteException {
		Table relationsTable = new Table();
		relationsTable.setCellspacing(4);
		int row = 1;
		try {
			Map relations = getRelations(user);
			UserBusiness userService = getUserService(iwc);
			User relatedUser;
			if (relationTypes != null && !relationTypes.isEmpty()) {
				for (Iterator iter = relationTypes.iterator(); iter.hasNext();) {
					String type = (String) iter.next();
					Text tTypeName =
						new Text(iwrb.getLocalizedString("is_" + type + "_of", "Is " + type.toLowerCase() + " of"));
					relationsTable.add(tTypeName, 1, row);
					if (relations.containsKey(type)) {
						List list = (List) relations.get(type);
						for (Iterator iterator = list.iterator(); iterator.hasNext();) {
							GroupRelation relation = (GroupRelation) iterator.next();
							relatedUser = userService.getUser(relation.getRelatedGroupPK());
							relationsTable.add(relatedUser.getPersonalID(), 2, row);
							relationsTable.add(getRelatedUserLink(relatedUser), 3, row);
							Link disconnectLink =
								getDisConnectorLink(
									type,null,
									(Integer) user.getPrimaryKey(),
									(Integer) relatedUser.getPrimaryKey(),
									iwb.getImageButton(
										iwrb.getLocalizedString("mbe.remove_" + type, "Remove " + type)));
							relationsTable.add(disconnectLink, 4, row);
							row++;
						}
					}
					row++;
				}

			}
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		addToMainPart(relationsTable);

	}

	protected Link getRelatedUserLink(User relatedUser) {
		Link relatedLink = new Link(relatedUser.getName());
		relatedLink.addParameter(searcher.getUniqueUserParameter((Integer) relatedUser.getPrimaryKey()));
		return relatedLink;
	}

	public void presentateButtons(IWContext iwc) {
		Table buttonTable = new Table();
		int row = 1, col = 1;
		SubmitButton save =
			new SubmitButton(
				iwrb.getLocalizedImageButton("mbe.save", "Save"),
				PRM_SAVE,
				user.getPrimaryKey().toString());
		SubmitButton clear = new SubmitButton(iwrb.getLocalizedImageButton("mbe.clear", "Clear"));

		buttonTable.add(save, col++, row);
		buttonTable.add(clear, col++, row);
		if (showUserRelations) {
			for (Iterator iter = relationTypes.iterator(); iter.hasNext();) {
				String type = (String) iter.next();
				Link registerLink =
					getConnectorLink(
						(Integer) user.getPrimaryKey(),
						type,null,
						iwb.getImageButton(iwrb.getLocalizedString("mbe.register_as_" + type, "Register as " + type)));
				buttonTable.add(registerLink, col++, row);
			}
		}
		add(buttonTable);
	}

	protected Link getConnectorLink(Integer roleUserID, String type,String reverseType, PresentationObject object) {
		Link registerLink = new Link(object);

		registerLink.setWindowToOpen(connectorWindowClass);
		registerLink.addParameter(UserRelationConnector.PARAM_USER_ID, roleUserID.toString());
		if(type!=null)
			registerLink.addParameter(UserRelationConnector.PARAM_TYPE, type);
		if(reverseType!=null)
			registerLink.addParameter(UserRelationConnector.PARAM_REVERSE_TYPE,reverseType);
		return registerLink;
	}
	
	protected SubmitButton getConnectorButton(IWContext iwc,String display,Integer roleUserID, String type,String reverseType){
		SubmitButton button = new SubmitButton(display);
		String URL = Window.getWindowURL(connectorWindowClass, iwc) ;
		URL+="&"+UserRelationConnector.PARAM_USER_ID+"="+roleUserID.toString();
		if(type!=null)
			URL+="&"+UserRelationConnector.PARAM_TYPE+"="+type;
		if(reverseType!=null)
			URL+="&"+UserRelationConnector.PARAM_REVERSE_TYPE+"="+reverseType;
		button.setOnClick("javascript:" + Window.getCallingScriptString(connectorWindowClass, URL, true, iwc)+";return false;");
		button.setStyleClass(buttonStyleName);
		return button;		
	}

	protected Link getDisConnectorLink(
		String type,String reverseType,
		Integer roleUserID,
		Integer victimUserID,
		PresentationObject object) {
		Link registerLink = new Link(object);
		//new Link(	iwb.getImageButton(	iwrb.getLocalizedString("mbe.remove_"+type, "Remove "+type) ));
		registerLink.setWindowToOpen(connectorWindowClass);
		registerLink.addParameter(UserRelationConnector.PARAM_USER_ID, roleUserID.toString());
		registerLink.addParameter(UserRelationConnector.getRelatedUserParameterName(), victimUserID.toString());
		if(type!=null)
			registerLink.addParameter(UserRelationConnector.PARAM_TYPE, type);
		if(reverseType!=null)
			registerLink.addParameter(UserRelationConnector.PARAM_REVERSE_TYPE,reverseType);
		//registerLink.addParameter(GroupRelationConnector.PARAM_ACTION,GroupRelationConnector.ACTION_DETACH);
		return registerLink;
	}

	/**
	 * Presentates the users found by search
	 * @param iwc the context
	*/
	protected void presentateUserInfo(IWContext iwc) throws RemoteException {
		UserBusiness userService = getUserService(iwc);
		Table infoTable = new Table();
		Table addressTable = new Table();
		int row = 1;
		addressTable.setCellspacing(4);
		Address primaryAddress = userService.getUsersMainAddress(user);
		Address coAddress = userService.getUsersCoAddress(user);

		//		deceased layout section
		// TODO check for deceased date
		Text tDeceased = new Text(iwrb.getLocalizedString("mbe.deceased", "Deceased"));
		tDeceased.setStyleClass(deceasedFontStyleName);
		addressTable.add(tDeceased, 1, row);
		UserStatus deceasedStatus = getUserStatusService(iwc).getDeceasedUserStatus((Integer) user.getPrimaryKey());
		if (deceasedStatus != null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
			Text tDeceasedDate = new Text(df.format(deceasedStatus.getDateFrom()));
			setStyle(tDeceasedDate, STYLENAME_DECEASED);
			addressTable.add(tDeceasedDate, 2, row);
		}
		else {
			DateInput deceasedInput = new DateInput(prm_deceased_date);
			deceasedInput.setToDisplayDayLast(true);
			IWTimestamp today = IWTimestamp.RightNow();
			deceasedInput.setLatestPossibleDate(today.getDate(),iwrb.getLocalizedString("mbe.deceased_date_warning","Please do not register deceased date in the future"));
			deceasedInput.setYearRange(today.getYear()-5,today.getYear());
			deceasedInput.setStyleClass(interfaceStyleName);
			addressTable.add(deceasedInput, 2, row);
		}

		row++;
		addToMainPart(addressTable);
		addToMainPart(Text.getBreak());

		// address layout section
		Text tAddress = new Text(iwrb.getLocalizedString("mbe.address", "Address"));
		Text tPrimary = new Text(iwrb.getLocalizedString("mbe.address.main", "Main"));
		Text tCO = new Text(iwrb.getLocalizedString("mbe.address.co", "C/O"));
		Text tStreetAddress = new Text(iwrb.getLocalizedString("mbe.address.street", "Street"));
		Text tPostalName = new Text(iwrb.getLocalizedString("mbe.address.postal.name", "Postal name"));
		Text tPostalCode = new Text(iwrb.getLocalizedString("mbe.address.postal.code","Postal code"));
		Text tCountry = new Text(iwrb.getLocalizedString("mbe.address.country","Country"));

		tAddress.setStyleClass(headerFontStyleName);
		tPrimary.setStyleClass(headerFontStyleName);
		tCO.setStyleClass(headerFontStyleName);
		tStreetAddress.setStyleClass(headerFontStyleName);
		tPostalName.setStyleClass(headerFontStyleName);
		tPostalCode.setStyleClass(headerFontStyleName);
		tCountry.setStyleClass(headerFontStyleName);

		
		addressTable.add(tAddress, 2, row);
		addressTable.add(tCO,3,row);
		row++;
		int startRow = row;
		addressTable.add(tStreetAddress, 1, row++);
		addressTable.add(tPostalCode, 1, row++);
		addressTable.add(tPostalName, 1, row++);
		addressTable.add(tCountry, 1, row++);
		

		Country defaultCountry = null;
		try {
			defaultCountry =
				getCountryHome().findByIsoAbbreviation(iwc.getApplicationSettings().getDefaultLocale().getCountry());
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (MissingResourceException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}

		TextInput primaryStreetAddressInput = new TextInput(prm_mainaddress_street);
		primaryStreetAddressInput.setStyleClass(interfaceStyleName);
		
		TextInput primaryPostalCodeInput = new TextInput(prm_mainaddress_postal_code);
		primaryPostalCodeInput.setStyleClass(interfaceStyleName);
		
		TextInput primaryPostalNameInput = new TextInput(prm_mainaddress_postal_name);
		primaryPostalNameInput.setStyleClass(interfaceStyleName);
		
		CountryDropdownMenu primaryCountryInput = new CountryDropdownMenu(prm_mainaddress_country);
		primaryCountryInput.setStyleClass(interfaceStyleName);

		TextInput coStreetAddressInput = new TextInput(prm_coaddress_street);
		coStreetAddressInput.setStyleClass(interfaceStyleName);
		
		TextInput coPostalCodeInput = new TextInput(prm_coaddress_postal_code);
		coPostalCodeInput.setStyleClass(interfaceStyleName);
		
		TextInput coPostalNameInput = new TextInput(prm_coaddress_postal_name);
		coPostalNameInput.setStyleClass(interfaceStyleName);
		
		CountryDropdownMenu coCountryInput = (CountryDropdownMenu) primaryCountryInput.clone();
		coCountryInput.setName(prm_coaddress_country);
		coCountryInput.setStyleClass(interfaceStyleName);
		
		/*
		PostalCodeDropdownMenu coPostalAddressInput = new PostalCodeDropdownMenu();
		coPostalAddressInput.setName(prm_coaddress_postal);
		coPostalAddressInput.setStyleClass(interfaceStyleName);
		coPostalAddressInput.setShowCountry(true);
		*/
		if (defaultCountry != null) {
			primaryCountryInput.setSelectedCountry(defaultCountry);
			coCountryInput.setSelectedCountry(defaultCountry);
			//primaryPostalAddressInput.setCountry(defaultCountry);
			//coPostalAddressInput.setCountry(defaultCountry);
		}

		//addressTable.add(tPrimary, 2, 2);
		row  =  startRow ; 
		addressTable.add(primaryStreetAddressInput, 2, row++);
		addressTable.add(primaryPostalCodeInput,2,row++);
		addressTable.add(primaryPostalNameInput,2,row++);
		addressTable.add(primaryCountryInput,2,row++);
		
		row  =  startRow ; 
		addressTable.add(coStreetAddressInput, 3, row++);
		addressTable.add(coPostalCodeInput,3, row++);
		addressTable.add(coPostalNameInput,3, row++);
		addressTable.add(coCountryInput,3, row++);
		
		if (primaryAddress != null) {
			primaryStreetAddressInput.setContent(primaryAddress.getStreetAddress());
			addressTable.add(getOldParameter(prm_mainaddress_street, primaryAddress.getStreetAddress()));
			try {
				PostalCode postalCode = primaryAddress.getPostalCode();
				if(postalCode!=null){
					primaryPostalCodeInput.setContent(postalCode.getPostalCode());
					primaryPostalNameInput.setContent(postalCode.getName());
					addressTable.add(new Parameter(prm_primaddress_postal_id,postalCode.getPrimaryKey().toString()));
					addressTable.add(getOldParameter(prm_mainaddress_postal_code, postalCode.getPostalCode()));
					addressTable.add(getOldParameter(prm_mainaddress_postal_name, postalCode.getName()));
					
						Country country = postalCode.getCountry();
						if(country!=null){
							primaryCountryInput.setSelectedCountry(country);
							addressTable.add(getOldParameter(prm_mainaddress_country, country.getPrimaryKey().toString()));
						}
					
				}
			}
			catch (Exception e2) {
				
			}
			//primaryPostalAddressInput.setSelectedElement(primaryAddress.getPostalCodeID());
			
		}
		
		


		if (coAddress != null) {
			coStreetAddressInput.setContent(coAddress.getStreetAddress());
			addressTable.add(getOldParameter(prm_coaddress_street, coAddress.getStreetAddress()));

			try {
				PostalCode postalCode = coAddress.getPostalCode();
				if(postalCode!=null){
					coPostalCodeInput.setContent(postalCode.getPostalCode());
					coPostalNameInput.setContent(postalCode.getName());
					addressTable.add(new Parameter(prm_coaddress_postal_id,postalCode.getPrimaryKey().toString()));
					addressTable.add(getOldParameter(prm_coaddress_postal_code, postalCode.getPostalCode()));
					addressTable.add(getOldParameter(prm_coaddress_postal_name, postalCode.getName()));
					try {
						Country country = postalCode.getCountry();
						if(country!=null){
							coCountryInput.setSelectedCountry(country);
							addressTable.add(getOldParameter(prm_coaddress_country_id, country.getPrimaryKey().toString()));
						}
					}
					catch (Exception e3) {
						
					}
				}
			}
			catch (SQLException e2) {
				
			}
		}
		
		row++;

		// phone layout section
		Text tPhone = new Text(iwrb.getLocalizedString("mbe.phone", "Phone"));
		tPhone.setStyleClass(headerFontStyleName);
		TextInput phoneInput = new TextInput(prm_main_phone);
		phoneInput.setStyleClass(interfaceStyleName);
		addressTable.add(tPhone, 1, row);
		addressTable.add(phoneInput, 2, row++);
		try {
			Phone phone = userService.getUsersHomePhone(user);
			if (phone != null) {
				phoneInput.setContent(phone.getNumber());
				addressTable.add(getOldParameter(prm_main_phone, phone.getNumber()));
			}
		}
		catch (NoPhoneFoundException e) {

		}
		row++;
		// email layout section
		Text tEmail = new Text(iwrb.getLocalizedString("mbe.email", "Email"));
		tEmail.setStyleClass(headerFontStyleName);
		TextInput emailInput = new TextInput(prm_email_address);
		emailInput.setStyleClass(interfaceStyleName);
		emailInput.setAsEmail();
		addressTable.add(tEmail, 1, row);
		addressTable.add(emailInput, 2, row++);
		Email email = userService.getUserMail(user);
		if (email != null) {
			emailInput.setContent(email.getEmailAddress());
			addressTable.add(getOldParameter(prm_email_address, email.getEmailAddress()));
		}

	}

	/**
	 * Process parameters in the request
	 * @param iwc the context
	 */
	public void process(IWContext iwc) throws IDOLookupException, FinderException, RemoteException {
		initUser(iwc);
		initRelationTypes(iwc);
		if (iwc.isParameterSet(PRM_SAVE))
			saveUser(iwc);

	}

	private void saveUser(IWContext iwc) throws RemoteException {
		UserBusiness userService = getUserService(iwc);
		Integer userID = Integer.valueOf(iwc.getParameter(PRM_SAVE));
		user = userService.getUser(userID);
		try {
			// main address part
			if(isRemovedValue(iwc,prm_mainaddress_street)){
				Address address = null;
				try {
					address = userService.getUsersMainAddress(user);
					if(address!=null){
						user.removeAddress(address);
					}
				}
				catch (IDORemoveRelationshipException e1) {
					e1.printStackTrace();
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			else
			if (isNewValue(iwc, prm_mainaddress_street) 
				|| isNewValue(iwc, prm_mainaddress_postal_code)
				|| isNewValue(iwc,prm_mainaddress_postal_name)
				|| isNewValue(iwc,prm_mainaddress_country)) {
				String street = iwc.getParameter(prm_mainaddress_street);
				if (!"".equals(street)) {
					Integer postalID = null;
					Country country = null;
					if(iwc.isParameterSet(prm_mainaddress_country)){
						Integer countryID = Integer.valueOf(iwc.getParameter(prm_mainaddress_country));
						try {
							country = userService.getAddressBusiness().getCountryHome().findByPrimaryKey(countryID);	
						}
						catch (RemoteException e1) {
							e1.printStackTrace();
						}
						catch (FinderException e1) {
							e1.printStackTrace();
						}
					}
					 if(country!=null && (isNewValue(iwc,prm_mainaddress_postal_code)|| isNewValue(iwc,prm_mainaddress_postal_name))){
						String code = iwc.getParameter(prm_mainaddress_postal_code);
						String name = iwc.getParameter(prm_mainaddress_postal_name);
						try {
							PostalCode pcode = userService.getAddressBusiness().getPostalCodeAndCreateIfDoesNotExist(code,name,country);
							postalID = (Integer) pcode.getPrimaryKey();
						}
						catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
					else if (iwc.isParameterSet(prm_primaddress_postal_id)){
						postalID = Integer.valueOf(iwc.getParameter(prm_primaddress_postal_id));
					}
					
					userService.updateUsersMainAddressOrCreateIfDoesNotExist(
						userID,
						street,
						postalID,
						null,
						null,
						null,
						null);
				}
			}

			// co address part
			if(isRemovedValue(iwc,prm_coaddress_street)){
				Address address = null;
				try {
					address = userService.getUsersCoAddress(user);
					if(address!=null){
						user.removeAddress(address);
					}
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else
			if (isNewValue(iwc, prm_coaddress_street) 
				|| isNewValue(iwc,prm_coaddress_postal_code) 
				|| isNewValue(iwc,prm_coaddress_postal_name)
				|| isNewValue(iwc,prm_coaddress_country)) {
					String street = iwc.getParameter(prm_coaddress_street);
					if (!"".equals(street)) {
						Integer postalID = null;
						Country country = null;
						if(iwc.isParameterSet(prm_coaddress_country)){
							Integer countryID = Integer.valueOf(iwc.getParameter(prm_coaddress_country));
							try {
								country = userService.getAddressBusiness().getCountryHome().findByPrimaryKey(countryID);	
							}
							catch (RemoteException e1) {
								e1.printStackTrace();
							}
							catch (FinderException e1) {
								e1.printStackTrace();
							}
						}
						if(country!=null && (isNewValue(iwc,prm_coaddress_postal_code) || isNewValue(iwc,prm_coaddress_postal_name))){
							String code = iwc.getParameter(prm_coaddress_postal_code);
							String name = iwc.getParameter(prm_coaddress_postal_name);
							try {
								PostalCode pcode = userService.getAddressBusiness().getPostalCodeAndCreateIfDoesNotExist(code,name,country);
								postalID = (Integer) pcode.getPrimaryKey();
							}
							catch (RemoteException e1) {
								e1.printStackTrace();
							}
						}
						else if(iwc.isParameterSet(prm_coaddress_postal_id)){
							postalID = Integer.valueOf(iwc.getParameter(prm_coaddress_postal_id));
						}
												
						userService.updateUsersCoAddressOrCreateIfDoesNotExist(
							userID,
							street,
							postalID,
							null,
							null,
							null,
							null);
					}
				}

			// phone part
			if (isNewValue(iwc, prm_main_phone)) {
				String number = iwc.getParameter(prm_main_phone);
				userService.updateUserPhone(userID.intValue(), PhoneTypeBMPBean.HOME_PHONE_ID, number);
			}
			else if(isRemovedValue(iwc,prm_main_phone)){
				Phone phone = null;
				try {
					phone = userService.getUsersHomePhone(user);
					if(phone!=null){
						user.removePhone(phone);
					}
				}
				catch (Exception e1) {
					
				}
			}

			// email part
			if (isNewValue(iwc, prm_email_address)) {
				String email = iwc.getParameter(prm_email_address);
				userService.updateUserMail(userID.intValue(), email);
			}
			else if(isRemovedValue(iwc,prm_email_address)){
				Email email = null;
				try {
					email = userService.getUserMail(user);
					if(email!=null){
						user.removeEmail(email);
					}
				}
				catch (Exception e1) {
					
				}
			}

			// deceased part
			if (iwc.isParameterSet(prm_deceased_date)) {
				IWTimestamp deceased = new IWTimestamp(iwc.getParameter(prm_deceased_date));
				getUserStatusService(iwc).setUserAsDeceased(userID, deceased.getDate());
				//TODO use some userbusiness to inform any services that want to know about a deceased user
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		catch (EJBException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		catch (CreateException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	public void initUser(IWContext iwc) {

	}

	private void initRelationTypes(IWContext iwc) throws RemoteException {

	}

	/**
	 * Sets the current users primarykey
	 * @param integer
	 */
	public void setUserID(Integer integer) {
		userID = integer;
	}

	public void addDeceasedListener(IWEventListener listener) {

	}

	protected Map getRelations(User user) throws FinderException, RemoteException {
		Map map = new Hashtable();
		if (relationTypes == null)
			relationTypes = new Vector();
		Collection relations = getRelationHome().findGroupsRelationshipsUnder(user);
		String type;
		for (Iterator iter = relations.iterator(); iter.hasNext();) {
			GroupRelation relation = (GroupRelation) iter.next();
			type = relation.getRelationshipType();
			//only show nonpassive relations
			if (!relation.isPassive()) {
				if (showAllRelationTypes && !relationTypes.contains(type)) {
					relationTypes.add(type);
				}
				if (map.containsKey(type)) {
					((List) map.get(type)).add(relation);
				}
				else {
					List list = new Vector();
					list.add(relation);
					map.put(type, list);
				}
			}
		}
		return map;
	}

	public GroupRelationHome getRelationHome() throws RemoteException {
		return (GroupRelationHome) IDOLookup.getHome(GroupRelation.class);
	}

	/* (non-Javadoc)
		 * @see com.idega.presentation.Block#getStyleNames()
		 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		map.put(STYLENAME_HEADER, headerFontStyle);
		map.put(STYLENAME_TEXT, textFontStyle);
		map.put(STYLENAME_DECEASED, deceasedFontStyle);
		map.put(STYLENAME_BUTTON, buttonStyle);
		map.put(STYLENAME_INTERFACE, interfaceStyle);
		return map;
	}

	private Parameter getOldParameter(String pName, String pValue) {
		return new Parameter(pName + prm_old_value_suffix, pValue);
	}

	private boolean isNewValue(IWContext iwc, String pName) {
		if (iwc.isParameterSet(pName + prm_old_value_suffix) && iwc.isParameterSet(pName)) {
			return !iwc.getParameter(pName + prm_old_value_suffix).equals(iwc.getParameter(pName));
		}
		return iwc.isParameterSet(pName);
	}
	
	private boolean isRemovedValue(IWContext iwc,String pName){
		String value = iwc.getParameter(pName);
		if(iwc.isParameterSet(pName+prm_old_value_suffix) && value!=null && value.length()==0)
			return true;
		return false;
	}
	
	private String getOldValue(IWContext iwc,String pName){
		return iwc.getParameter(pName+prm_old_value_suffix);
	}

	/**
	 * Sets the relations connector window class, that must be a subclass of GroupRelationConnector
	 * @param windowClass
	 */
	public void setGroupRelationConnectorWindow(Class windowClass) {
		connectorWindowClass = windowClass;
	}

	public UserBusiness getUserService(IWApplicationContext iwac) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}

	public UserStatusBusiness getUserStatusService(IWApplicationContext iwac) throws RemoteException {
		return (UserStatusBusiness) IBOLookup.getServiceInstance(iwac, UserStatusBusiness.class);
	}

	public PhoneTypeHome getPhoneHome() throws RemoteException {
		return (PhoneTypeHome) IDOLookup.getHome(PhoneType.class);
	}

	public CountryHome getCountryHome() throws RemoteException {
		return (CountryHome) IDOLookup.getHome(Country.class);
	}

	/**
	 * @return
	 */
	public Class getConnectorWindowClass() {
		return connectorWindowClass;
	}

	/**
	 * @return
	 */
	public String getHeaderFontStyle() {
		return headerFontStyle;
	}

	/**
	 * @return
	 */
	public String getHeaderStyle() {
		return headerStyle;
	}

	/**
	 * @return
	 */
	public UserSearcher getSearcher() {
		return searcher;
	}

	/**
	 * @return
	 */
	public boolean isShowAllRelationTypes() {
		return showAllRelationTypes;
	}

	/**
	 * @return
	 */
	public boolean isShowUserRelations() {
		return showUserRelations;
	}

	/**
	 * @return
	 */
	public String getTextFontStyle() {
		return textFontStyle;
	}

	/**
	 * @param class1
	 */
	public void setConnectorWindowClass(Class class1) {
		connectorWindowClass = class1;
	}

	/**
	 * @param string
	 */
	public void setHeaderFontStyle(String string) {
		headerFontStyle = string;
	}

	/**
	 * @param string
	 */
	public void setHeaderStyle(String string) {
		headerStyle = string;
	}

	/**
	 * @param searcher
	 */
	public void setSearcher(UserSearcher searcher) {
		this.searcher = searcher;
	}

	/**
	 * @param b
	 */
	public void setShowAllRelationTypes(boolean b) {
		showAllRelationTypes = b;
	}

	/**
	 * @param b
	 */
	public void setShowUserRelations(boolean b) {
		showUserRelations = b;
	}

	/**
	 * @param string
	 */
	public void setTextFontStyle(String string) {
		textFontStyle = string;
	}

	public synchronized Object clone() {
		UserEditor obj = (UserEditor) super.clone();
		obj.searcher = (UserSearcher) searcher.clone();
		return obj;
	}

	/**
	 * @return
	 */
	public String getDeceasedFontStyle() {
		return deceasedFontStyle;
	}

	/**
	 * @param string
	 */
	public void setDeceasedFontStyle(String string) {
		deceasedFontStyle = string;
	}

	public static String getUserIDParameterName() {
		return UserSearcher.getUniqueUserParameterName("edt");
	}

	/**
	 * @param length
	 */
	public void setFirstNameLength(int length) {
		searcher.setFirstNameLength(length);
	}

	/**
	 * @param length
	 */
	public void setLastNameLength(int length) {
		searcher.setLastNameLength(length);
	}

	/**
	 * @param cols
	 */
	public void setMaxFoundUserCols(int cols) {
		searcher.setMaxFoundUserCols(cols);
	}

	/**
	 * @param rows
	 */
	public void setMaxFoundUserRows(int rows) {
		searcher.setMaxFoundUserRows(rows);
	}

	/**
	 * @param length
	 */
	public void setMiddleNameLength(int length) {
		searcher.setMiddleNameLength(length);
	}

	/**
	 * @param length
	 */
	public void setPersonalIDLength(int length) {
		searcher.setPersonalIDLength(length);
	}

	/**
	 * @param b
	 */
	public void setShowFirstNameInSearch(boolean b) {
		searcher.setShowFirstNameInSearch(b);
	}

	/**
	 * @param b
	 */
	public void setShowLastNameInSearch(boolean b) {
		searcher.setShowLastNameInSearch(b);
	}

	/**
	 * @param b
	 */
	public void setShowMiddleNameInSearch(boolean b) {
		searcher.setShowMiddleNameInSearch(b);
	}

	/**
	 * @param b
	 */
	public void setShowPersonalIDInSearch(boolean b) {
		searcher.setShowPersonalIDInSearch(b);
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserSearcher#setSkipResultsForOneFound(boolean)
	 */
	public void setSkipResultsForOneFound(boolean flag) {
		searcher.setSkipResultsForOneFound(flag);
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObjectContainer#add(com.idega.presentation.PresentationObject)
	 */
	public void addToMainPart(PresentationObject modObject) {
		mainTable.add(modObject, 1, mainRow++);
	}

	public Text getHeader(String text) {
		Text t = new Text(text);
		setStyle(t, STYLENAME_HEADER);
		return t;
	}

	public Text getText(String text) {
		Text t = new Text(text);
		setStyle(t, STYLENAME_TEXT);
		return t;
	}

	/**
	 * @return
	 */
	public String getButtonStyle() {
		return buttonStyle;
	}

	/**
	 * @return
	 */
	public String getInterfaceStyle() {
		return interfaceStyle;
	}

	/**
	 * @param string
	 */
	public void setButtonStyle(String string) {
		buttonStyle = string;
	}

	/**
	 * @param string
	 */
	public void setInterfaceStyle(String string) {
		interfaceStyle = string;
	}

}
