/*
 * Created on 6.7.2003
 *
 */
package is.idega.idegaweb.member.presentation;


import java.rmi.RemoteException;
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
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.PostalCodeDropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.business.UserBusiness;
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

	private static final String prm_deceased_date = "deceased_date";
	private static final String prm_email_address = "email_address";
	private static final String prm_main_phone = "phone_number";
	private static final String prm_mainaddress_street = "addr_prim_str";
	private static final String prm_mainaddress_postal = "addr_prim_pst";
	private static final String prm_coaddress_street = "addr_co_str";
	private static final String prm_coaddress_postal = "addr_co_pst";
	private static final String prm_old_value_suffix = "_old";
	/** Parameter for user id */
	public static final String PRM_USER_ID = UserSearcher.PRM_USER_ID; //"ic_user_id";
	private static final String PRM_SAVE = "mbe_save";
	/** The userID is the handled users ID. */
	private Integer userID = null;
	/** The user currently handled */
	private User user = null;
	/** The dynamic bundle identifier*/
	private String bundleIdentifer = null;
	/** The  static bundle identifier used in this package */
	private static String BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	/** The Bundle */
	private IWBundle iwb;
	/** The resource bundle */
	private IWResourceBundle iwrb;
	/** The list of relationstyped handle by the editor */
	private List relationTypes = new Vector();

	/** Determines if we show the users relations */
	private boolean showUserRelations = true;
	/** The main layout table */
	private Table mainTable = null;
	/** the current layout table row */
	private int mainRow = 1;

	/** Header style */
	private String headerStyle = "";
	/** Header style class */
	private String headerStyleClass = "";
	/** flag for family relation types */
	boolean showAllRelationTypes = true;
	/** Class of relation connector window */
	protected Class connectorWindowClass = FamilyRelationConnector.class;

	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_HEADER = "Header";

	private String textFontStyle = "font-weight:plain;";
	private String headerFontStyle = "font-weight:bold;";
	
	private UserSearcher searcher = null;
	
	

	/**
	 * Constructs a new UserEditor with an empty list of relationtypes
	 */
	public UserEditor() {
		this(new ArrayList());
	}

	public UserEditor(List relationTypes) {
		this.relationTypes = relationTypes;
		searcher = new UserSearcher();
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) {
		debugParameters(iwc);
		// get bundles
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
				catch (FinderException e) {
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
	private void presentateUserRelations(IWContext iwc) throws RemoteException, FinderException {
		Table relationsTable = new Table();
		relationsTable.setCellspacing(4);
		int row = 1;
		Map relations = getRelations();
		UserBusiness userService = getUserService(iwc);
		User relatedUser;
		if (relationTypes != null && !relationTypes.isEmpty()) {
			for (Iterator iter = relationTypes.iterator(); iter.hasNext();) {
				String type = (String) iter.next();
				Text tTypeName = new Text(iwrb.getLocalizedString("is_"+type+"_of", "Is "+type.toLowerCase()+" of"));
				relationsTable.add(tTypeName, 1, row);
				if (relations.containsKey(type)) {
					List list = (List) relations.get(type);
					for (Iterator iterator = list.iterator(); iterator.hasNext();) {
						GroupRelation relation = (GroupRelation) iterator.next();
						relatedUser = userService.getUser(relation.getRelatedGroupPK());
						Link relatedLink = new Link(relatedUser.getName());
						relatedLink.addParameter(PRM_USER_ID, relatedUser.getPrimaryKey().toString());
						relationsTable.add(relatedUser.getPersonalID(), 2, row);
						relationsTable.add(relatedLink, 3, row);
						Link disconnectLink = getDisConnectorLink(type,(Integer)relatedUser.getPrimaryKey());
						relationsTable.add(disconnectLink,4,row);
						row++;
					}
				}
				row++;
			}

		}

		mainTable.add(relationsTable, 1, mainRow++);

	}

	public void presentateButtons(IWContext iwc) {
		Table buttonTable = new Table();
		int row = 1, col = 1;
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("mbe.save", "Save"),PRM_SAVE,user.getPrimaryKey().toString());
		SubmitButton clear = new SubmitButton(iwrb.getLocalizedImageButton("mbe.clear", "Clear"));
		
		buttonTable.add(save, col++, row);
		buttonTable.add(clear,col++,row);
		if (showUserRelations) {
			for (Iterator iter = relationTypes.iterator(); iter.hasNext();) {
				String type = (String) iter.next();
				Link registerLink = getConnectorLink(type);
				buttonTable.add(registerLink, col++, row);
			}
		}
		mainTable.add(buttonTable, 1, mainRow++);
	}

	private Link getConnectorLink(String type) {
		Link registerLink =
			new Link(
				iwb.getImageButton(
					iwrb.getLocalizedString("mbe.register_as_"+type, "Register as "+type) ));
		registerLink.setWindowToOpen(connectorWindowClass);
		registerLink.addParameter(UserRelationConnector.PARAM_USER_ID, user.getPrimaryKey().toString());
		registerLink.addParameter(UserRelationConnector.PARAM_TYPE, type);
		return registerLink;
	}
	
	private Link getDisConnectorLink(String type,Integer relatedUserID) {
			Link registerLink =
				new Link(
					iwb.getImageButton(
						iwrb.getLocalizedString("mbe.remove_"+type, "Remove "+type) ));
			registerLink.setWindowToOpen(connectorWindowClass);
			registerLink.addParameter(UserRelationConnector.PARAM_USER_ID, user.getPrimaryKey().toString());
			registerLink.addParameter(UserRelationConnector.PARAM_RELATED_USER_ID,relatedUserID.toString());
			registerLink.addParameter(UserRelationConnector.PARAM_TYPE, type);
			//registerLink.addParameter(GroupRelationConnector.PARAM_ACTION,GroupRelationConnector.ACTION_DETACH);
			return registerLink;
		}

	/**
	 * Presentates the users found by search
	 * @param iwc the context
	*/
	private void presentateUserInfo(IWContext iwc) throws RemoteException {
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
		setStyle(tDeceased,STYLENAME_HEADER);
		DateInput deceasedInput = new DateInput(prm_deceased_date);
		addressTable.add(tDeceased, 1, row);
		addressTable.add(deceasedInput, 2, row++);

		mainTable.add(addressTable, 1, mainRow++);
		mainTable.add(Text.getBreak(),1,mainRow++);

		// address layout section
		Text tAddress = new Text(iwrb.getLocalizedString("mbe.address", "Address"));
		Text tPrimary = new Text(iwrb.getLocalizedString("mbe.address.main", "Main"));
		Text tCO = new Text(iwrb.getLocalizedString("mbe.address.co", "C/O"));
		Text tStreetAddress = new Text(iwrb.getLocalizedString("mbe.address.street", "Street"));
		Text tPostalAddress = new Text(iwrb.getLocalizedString("mbe.address.postal", "Postal"));
		
		setStyle(tAddress,STYLENAME_HEADER);
		setStyle(tPrimary,STYLENAME_HEADER);
		setStyle(tCO,STYLENAME_HEADER);
		setStyle(tStreetAddress,STYLENAME_HEADER);
		setStyle(tPostalAddress,STYLENAME_HEADER);

		addressTable.add(tAddress, 1, row++);
		addressTable.add(tStreetAddress, 1, row);
		addressTable.add(Text.getNonBrakingSpace(),1,row);
		addressTable.add(Text.getNonBrakingSpace(),1,row++);
		addressTable.add(tPostalAddress, 1, row);
		addressTable.add(Text.getNonBrakingSpace(),1,row);
		addressTable.add(Text.getNonBrakingSpace(),1,row++);
		
		
		
		
		Country defaultCountry = null;
		try {
			defaultCountry = getCountryHome().findByIsoAbbreviation( iwc.getApplicationSettings().getDefaultLocale().getCountry());
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
		PostalCodeDropdownMenu primaryPostalAddressInput = new PostalCodeDropdownMenu();//new TextInput(prm_mainaddress_postal);
		primaryPostalAddressInput.setName(prm_mainaddress_postal);
		
		TextInput coStreetAddressInput = new TextInput(prm_coaddress_street);
		PostalCodeDropdownMenu coPostalAddressInput = new PostalCodeDropdownMenu();
		coPostalAddressInput.setName(prm_coaddress_postal);
		
		if(defaultCountry!=null){
			primaryPostalAddressInput.setCountry(defaultCountry);
			coPostalAddressInput.setCountry(defaultCountry);
		}

		addressTable.add(tPrimary, 2, 2);
		addressTable.add(primaryStreetAddressInput, 2, 3);
		addressTable.add(primaryPostalAddressInput, 2, 4);
		if (primaryAddress != null) {
			primaryStreetAddressInput.setContent(primaryAddress.getStreetAddress());
			addressTable.add(getOldParameter(prm_mainaddress_street,primaryAddress.getStreetAddress()));
			
			primaryPostalAddressInput.setSelectedElement(primaryAddress.getPostalCodeID());
			addressTable.add(getOldParameter(prm_mainaddress_postal,String.valueOf(primaryAddress.getPostalCodeID())));
		}
		addressTable.add(tCO, 3,2);
		addressTable.add(coStreetAddressInput, 3,3);
		addressTable.add(coPostalAddressInput, 3, 4);

		if (coAddress != null) {
			coStreetAddressInput.setContent(coAddress.getStreetAddress());
			addressTable.add(getOldParameter(prm_coaddress_street,coAddress.getStreetAddress()));
			
			coPostalAddressInput.setSelectedElement(coAddress.getPostalCodeID());
			addressTable.add(getOldParameter(prm_coaddress_postal,String.valueOf(coAddress.getPostalCodeID())));
		}

		// phone layout section
		Text tPhone = new Text(iwrb.getLocalizedString("mbe.phone", "Phone"));
		setStyle(tPhone,STYLENAME_HEADER);
		TextInput phoneInput = new TextInput(prm_main_phone);
		addressTable.add(tPhone, 1, 6);
		addressTable.add(phoneInput, 2, 6);
		try {
			Phone phone = userService.getUsersHomePhone(user);
			if (phone != null) {
				phoneInput.setContent(phone.getNumber());
				addressTable.add(getOldParameter(prm_main_phone,phone.getNumber()));
			}
		}
		catch (NoPhoneFoundException e) {

		}

		// email layout section
		Text tEmail = new Text(iwrb.getLocalizedString("mbe.email", "Email"));
		setStyle(tEmail,STYLENAME_HEADER);
		TextInput emailInput = new TextInput(prm_email_address);
		emailInput.setAsEmail();
		addressTable.add(tEmail, 1, 8);
		addressTable.add(emailInput, 2, 8);
		Email email = userService.getUserMail(user);
		if (email != null) {
			emailInput.setContent(email.getEmailAddress());
			addressTable.add(getOldParameter(prm_email_address,email.getEmailAddress()));
		}

		
	}

	/**
	 * Process parameters in the request
	 * @param iwc the context
	 */
	public void process(IWContext iwc) throws IDOLookupException, FinderException, RemoteException {
		initUser(iwc);
		initRelationTypes(iwc);
		if(iwc.isParameterSet(PRM_SAVE))
			saveUser( iwc);

	}
	
	private void saveUser(IWContext iwc)throws RemoteException{
		UserBusiness userService = getUserService(iwc);
		Integer userID = Integer.valueOf(iwc.getParameter(PRM_SAVE));
		user = userService.getUser(userID);
		try {
			// main address part
			if(isNewValue(iwc,prm_mainaddress_street) || isNewValue(iwc,prm_mainaddress_postal)){
				String street = iwc.getParameter(prm_mainaddress_street);
				Integer postal = Integer.valueOf( iwc.getParameter(prm_mainaddress_postal));
				userService.updateUsersMainAddressOrCreateIfDoesNotExist(userID,street,postal,null,null,null,null);
			}
			
			// co address part
			if(isNewValue(iwc,prm_coaddress_street) || isNewValue(iwc,prm_coaddress_postal)){
				String street = iwc.getParameter(prm_coaddress_street);
				Integer postal = Integer.valueOf( iwc.getParameter(prm_coaddress_postal));
				userService.updateUsersCoAddressOrCreateIfDoesNotExist(userID,street,postal,null,null,null,null);
			}
			
			// phone part
			if(isNewValue(iwc,prm_main_phone)){
				String number = iwc.getParameter(prm_main_phone);
				userService.updateUserPhone(userID.intValue(),PhoneTypeBMPBean.HOME_PHONE_ID,number);
			}
			
			// email part
			if(isNewValue(iwc,prm_email_address)){
				String email = iwc.getParameter(prm_email_address);
				userService.updateUserMail(userID.intValue(),email);
			}
			
			// deceased part
			if(iwc.isParameterSet(prm_deceased_date)){
				IWTimestamp deceased = new IWTimestamp(iwc.getParameter(prm_deceased_date));
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

	

	public Map getRelations() throws FinderException, RemoteException {
		Map map = new Hashtable();
		if (relationTypes == null)
			relationTypes = new Vector();
		Collection relations = getRelationHome().findGroupsRelationshipsUnder(user);
		String type;
		for (Iterator iter = relations.iterator(); iter.hasNext();) {
			GroupRelation relation = (GroupRelation) iter.next();
			type = relation.getRelationshipType();
			//only show nonpassive relations
			if(!relation.isPassive()){
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
		return map;
	}
	
	private Parameter getOldParameter(String pName,String pValue){
		return new Parameter(pName+prm_old_value_suffix,pValue);
	}
	
	private boolean isNewValue(IWContext iwc,String pName){
		if(iwc.isParameterSet(pName+prm_old_value_suffix) && iwc.isParameterSet(pName)){
			return iwc.getParameter(pName+prm_old_value_suffix).equals(iwc.getParameter(pName));
		}
		return iwc.isParameterSet(pName);
	}
	
	/**
	 * Sets the relations connector window class, that must be a subclass of GroupRelationConnector
	 * @param windowClass
	 */
	public void setGroupRelationConnectorWindow(Class windowClass){
		connectorWindowClass = windowClass;
	}
	
	public UserBusiness getUserService(IWApplicationContext iwac) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);	
	}
	
	public PhoneTypeHome getPhoneHome() throws RemoteException{
		return (PhoneTypeHome) IDOLookup.getHome(PhoneType.class);
	}
	
	public CountryHome getCountryHome() throws RemoteException{
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
		UserEditor obj = (UserEditor)super.clone();
		obj.searcher = (UserSearcher)searcher.clone();
		return obj;
	}

}
