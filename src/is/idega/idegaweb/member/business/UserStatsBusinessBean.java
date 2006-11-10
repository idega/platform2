/*
 * Created on Jan 20, 2005
 */
package is.idega.idegaweb.member.business;

import is.idega.block.nationalregister.business.NationalRegisterBusiness;
import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.idegaweb.member.presentation.GroupStatsWindowPlugin;
import is.idega.idegaweb.member.presentation.UserStatsWindowPlugin;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.datareport.util.FieldsComparator;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressType;
import com.idega.core.location.data.AddressTypeHome;
import com.idega.core.location.data.Country;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserStatus;
import com.idega.user.data.UserStatusHome;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author Sigtryggur
 *
 */
public class UserStatsBusinessBean extends IBOSessionBean  implements UserStatsBusiness, UserGroupPlugInBusiness {
    
    private UserBusiness userBiz = null;
    private GroupBusiness groupBiz = null;
    private NationalRegisterBusiness nationalRegisterBiz = null;
	private IWBundle _iwb = null;
	private IWResourceBundle _iwrb = null;
	private IWResourceBundle _userIwrb = null;
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	private final static String USER_IW_BUNDLE_IDENTIFIER = "com.idega.user";
	private static final String USR_STAT_PREFIX = "usr_stat_";
	private static final double MILLISECONDS_IN_YEAR = 31557600000d;

	private static final String LOCALIZED_CURRENT_DATE = "UserStatsBusiness.current_date";
	private static final String LOCALIZED_NAME = "UserStatsBusiness.name";
	private static final String LOCALIZED_SHORT_NAME = "UserStatsBusiness.short_name";
	private static final String LOCALIZED_ABBREVATION = "UserStatsBusiness.abbrevation";
	private static final String LOCALIZED_DISPLAY_NAME = "UserStatsBusiness.display_name";
	private static final String LOCALIZED_PERSONAL_ID = "UserStatsBusiness.personal_id";
	private static final String LOCALIZED_CUSTODIAN_NAME = "UserStatsBusiness.custodian_name";
	private static final String LOCALIZED_CUSTODIAN_PERSONAL_ID = "UserStatsBusiness.custodian_personal_id";
	private static final String LOCALIZED_CUSTODIAN_PHONE = "UserStatsBusiness.custodian_phone";
	private static final String LOCALIZED_DATE_OF_BIRTH = "UserStatsBusiness.date_of_birth";
	private static final String LOCALIZED_AGE = "UserStatsBusiness.age";
	private static final String LOCALIZED_GROUP_TYPE = "UserStatsBusiness.group_type";
	private static final String LOCALIZED_PARENT_GROUP = "UserStatsBusiness.parent_group";
	private static final String LOCALIZED_GROUP_PATH = "UserStatsBusiness.group_path";
	private static final String LOCALIZED_USER_STATUS = "UserStatsBusiness.user_status";
	private static final String LOCALIZED_STREET_ADDRESS = "UserStatsBusiness.street_address";
	private static final String LOCALIZED_POSTAL_ADDRESS = "UserStatsBusiness.postal_address";
	private static final String LOCALIZED_POST_BOX = "UserStatsBusiness.post_box";
	private static final String LOCALIZED_COUNTRY = "UserStatsBusiness.country";
	private static final String LOCALIZED_PHONE = "UserStatsBusiness.phone";
	private static final String LOCALIZED_EMAIL = "UserStatsBusiness.email";

	private static final String FIELD_NAME_NAME = "name";
	private static final String FIELD_NAME_SHORT_NAME = "short_name";
	private static final String FIELD_NAME_ABBREVATION = "abbrevation";
	private static final String FIELD_NAME_DISPLAY_NAME = "display_name";
	private static final String FIELD_NAME_PERSONAL_ID = "personal_id";
	private static final String FIELD_NAME_CUSTODIAN_NAME = "custodian_name";
	private static final String FIELD_NAME_CUSTODIAN_PERSONAL_ID = "custodian_personal_id";
	private static final String FIELD_NAME_CUSTODIAN_PHONE = "custodian_phone";
	private static final String FIELD_NAME_DATE_OF_BIRTH = "date_of_birth";
	private static final String FIELD_NAME_AGE = "age";
	private static final String FIELD_NAME_GROUP_TYPE = "group_type";
	private static final String FIELD_NAME_PARENT_GROUP = "parent_group";
	private static final String FIELD_NAME_GROUP_PATH = "group_path";
	private static final String FIELD_NAME_USER_STATUS = "user_status";
	private static final String FIELD_NAME_STREET_ADDRESS = "street_address";
	private static final String FIELD_NAME_POSTAL_ADDRESS = "postal_address";
	private static final String FIELD_NAME_POST_BOX = "post_box";
	private static final String FIELD_NAME_COUNTRY = "country";
	private static final String FIELD_NAME_PHONE = "phone";
	private static final String FIELD_NAME_EMAIL = "email";
	
	private Map cachedGroups = new HashMap();
	private Map cachedParents = new HashMap();
	
	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
		_userIwrb = this.getIWApplicationContext().getIWMainApplication().getBundle(USER_IW_BUNDLE_IDENTIFIER).getResourceBundle(this.getUserContext().getCurrentLocale());
	}

    
    public ReportableCollection getStatisticsForUsers(String groupIDFilter, String groupsRecursiveFilter, Collection groupTypesFilter, Collection userStatusesFilter, Integer yearOfBirthFromFilter, Integer yearOfBirthToFilter, String genderFilter, String dynamicLayout, String orderBy) throws RemoteException {        

        initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		
		reportCollection.addExtraHeaderParameter(
				"label_current_date", _iwrb.getLocalizedString(LOCALIZED_CURRENT_DATE, "Current date"),
				"current_date", TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale, IWTimestamp.LONG,IWTimestamp.SHORT),"GMT"));
 	
		//PARAMETERS that are also FIELDS
		 //data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		 //The name you give the field/parameter must not contain spaces or special characters		 
		 ReportableField nameField = new ReportableField(FIELD_NAME_NAME, String.class);
		 nameField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_NAME, "Name"), currentLocale);
		 reportCollection.addField(nameField);

		 ReportableField personalIDField = new ReportableField(FIELD_NAME_PERSONAL_ID, String.class);
		 personalIDField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PERSONAL_ID, "Personal ID"),currentLocale);
		 reportCollection.addField(personalIDField);

		 ReportableField dateOfBirthField = new ReportableField(FIELD_NAME_DATE_OF_BIRTH, String.class);
		 dateOfBirthField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_DATE_OF_BIRTH, "Date of birth"),currentLocale);
		 reportCollection.addField(dateOfBirthField);

		 ReportableField ageField = new ReportableField(FIELD_NAME_AGE, String.class);
		 ageField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_AGE, "Age"),currentLocale);
		 reportCollection.addField(ageField);

		 ReportableField parentGroupField = new ReportableField(FIELD_NAME_PARENT_GROUP, String.class);
		 parentGroupField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PARENT_GROUP, "Parent group"), currentLocale);
		 reportCollection.addField(parentGroupField);

		 ReportableField groupPathField = new ReportableField(FIELD_NAME_GROUP_PATH, String.class);
		 groupPathField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_GROUP_PATH, "Group Path"), currentLocale);
		 reportCollection.addField(groupPathField);
		 
		 ReportableField userStatusField = new ReportableField(FIELD_NAME_USER_STATUS, String.class);
		 userStatusField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_USER_STATUS, "User Status"), currentLocale);
		 reportCollection.addField(userStatusField);
		 
		 ReportableField streetAddressField = new ReportableField(FIELD_NAME_STREET_ADDRESS, String.class);
		 streetAddressField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_STREET_ADDRESS, "Street Address"), currentLocale);
		 reportCollection.addField(streetAddressField);
		 
		 ReportableField postalAddressField = new ReportableField(FIELD_NAME_POSTAL_ADDRESS, String.class);
		 postalAddressField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_POSTAL_ADDRESS, "Postal Address"), currentLocale);
		 reportCollection.addField(postalAddressField); 

		 ReportableField countryField = new ReportableField(FIELD_NAME_COUNTRY, String.class);
		 countryField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_COUNTRY, "Country"), currentLocale);
		 reportCollection.addField(countryField);
		 
		 ReportableField phoneField = new ReportableField(FIELD_NAME_PHONE, String.class);
		 phoneField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PHONE, "Phone"), currentLocale);
		 reportCollection.addField(phoneField);
		 
		 ReportableField emailField = new ReportableField(FIELD_NAME_EMAIL, String.class);
		 emailField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_EMAIL, "Email"), currentLocale);
		 reportCollection.addField(emailField);

		 ReportableField custodianNameField = new ReportableField(FIELD_NAME_CUSTODIAN_NAME, String.class);
		 custodianNameField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CUSTODIAN_NAME, "Custodian name"), currentLocale);
		 reportCollection.addField(custodianNameField);

		 ReportableField custodianPersonalIDField = new ReportableField(FIELD_NAME_CUSTODIAN_PERSONAL_ID, String.class);
		 custodianPersonalIDField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CUSTODIAN_PERSONAL_ID, "Custodian personal ID"),currentLocale);
		 reportCollection.addField(custodianPersonalIDField);

		 ReportableField custodianPhoneField = new ReportableField(FIELD_NAME_CUSTODIAN_PHONE, String.class);
		 custodianPhoneField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_CUSTODIAN_PHONE, "Custodian phone"), currentLocale);
		 reportCollection.addField(custodianPhoneField);
		
		Group group = null;
		Collection groups = null;
		Collection users = null;
		try {
		    if (groupIDFilter != null && !groupIDFilter.equals("")) {
		        groupIDFilter = groupIDFilter.substring(groupIDFilter.lastIndexOf("_")+1);
		        group = getGroupBusiness().getGroupByGroupID(Integer.parseInt((groupIDFilter)));
		    }
			if (group != null) {
			    if (groupsRecursiveFilter != null && groupsRecursiveFilter.equals("checked")) {
				    groups = getGroupBusiness().getChildGroupsRecursiveResultFiltered(group, groupTypesFilter, true, true, true);
				} else {
				    groups = new ArrayList();
				}
			    groups.add(group);
			}
		} catch (FinderException e) {
		    e.printStackTrace();
    	}
			users = getUserBusiness().getUsersBySpecificGroupsUserstatusDateOfBirthAndGender(groups, userStatusesFilter, yearOfBirthFromFilter,  yearOfBirthToFilter,  genderFilter);
			Collection topNodes = getUserBusiness().getUsersTopGroupNodesByViewAndOwnerPermissions(getUserContext().getCurrentUser(),getUserContext());
			Map usersByGroups = new TreeMap();
			AddressTypeHome addressHome = (AddressTypeHome) IDOLookup.getHome(AddressType.class);
			AddressType at1 = null;
			try {
			    at1 = addressHome.findAddressType2();
			} catch (FinderException e) {
			    e.printStackTrace();
			}
			Iterator iter = users.iterator();
			 while (iter.hasNext()) {
			     User user = (User) iter.next();
			     Collection parentGroupCollection = null;
			     try {
			         parentGroupCollection = getGroupHome().findParentGroups(Integer.parseInt(user.getGroup().getPrimaryKey().toString()));
			     } catch (FinderException e) {
			         System.out.println(e.getMessage());
			     }
			     parentGroupCollection.retainAll(groups);
			     Iterator parIt = parentGroupCollection.iterator();
			   
			   	String personalID = user.getPersonalID();
			   	String dateOfBirthString = null;
			   	String ageString = null;
			   	String custodianString = null;
				String custodianPersonalID = null;
				String custodianPhoneString = null;
//			 	Collection custodians = null; 
				try {
					Date date_of_birth = user.getDateOfBirth();
					if (date_of_birth != null) {
						dateOfBirthString = new IWTimestamp(date_of_birth).getDateString("dd.MM.yyyy");
						long ageInMillisecs = IWTimestamp.getMilliSecondsBetween(new IWTimestamp(date_of_birth),new IWTimestamp());
						BigDecimal age = new BigDecimal(ageInMillisecs/MILLISECONDS_IN_YEAR);
						ageString = String.valueOf(age.intValue());
						if (age.doubleValue() < 18) {
							NationalRegister userRegister = getNationalRegisterBusiness().getEntryBySSN(user.getPersonalID());
							custodianPersonalID = userRegister.getFamilyId();
							User custodian = getUserBusiness().getUser(custodianPersonalID);
							custodianString = custodian.getName();
							custodianPhoneString = getPhoneNumber(custodian);
							
						} else {
							custodianPersonalID = personalID;
						}
						if (custodianPersonalID != null && custodianPersonalID.length() == 10) {
							custodianPersonalID = custodianPersonalID.substring(0,6)+"-"+custodianPersonalID.substring(6,10);
					 	}
					}
					//custodians = getMemberFamilyLogic(getIWApplicationContext()).getCustodiansFor(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			   	if (personalID != null && personalID.length() == 10) {
			 		personalID = personalID.substring(0,6)+"-"+personalID.substring(6,10);
			 	}
			   	Collection emails =  user.getEmails();
			   	Email email = null;
			   	String emailString = null;
			   	if (!emails.isEmpty()) {
			   	    email = (Email)emails.iterator().next();
			   	    emailString = email.getEmailAddress();
			   	}
			   	Collection addresses = null;
			   	if (at1 != null) {
			   	    
					   	try {
                            addresses = user.getAddresses(at1);
                        } catch (IDOLookupException e1) {
                            e1.printStackTrace();
                        } catch (IDOCompositePrimaryKeyException e1) {
                            e1.printStackTrace();
                        } catch (IDORelationshipException e1) {
                            e1.printStackTrace();
                        }
			   	}
			    Address address = null;
			    String streetAddressString = null;
			    String postalAddressString = null;
			    String countryString = null;
			   	if (addresses != null && !addresses.isEmpty()) {
			   	    address = (Address)addresses.iterator().next();
			   	    streetAddressString = address.getStreetAddress();
			   	    postalAddressString = address.getPostalAddress();
			   	    Country country = address.getCountry();
			   	    if (country != null) {
			   	    	countryString = country.getName();
	        			Locale locale = new Locale(currentLocale.getLanguage(), country.getIsoAbbreviation());
	                    String localizedCountryName = locale.getDisplayCountry(currentLocale);
	                    if (localizedCountryName != null && !localizedCountryName.equals("")) {
	                    	countryString = localizedCountryName;
	                    }
			   	    }
			   	}
			     while (parIt.hasNext()) {
			         Group parentGroup = (Group)parIt.next();
			         List userStatuses = null;
			         String userStatusString = null;
			         try {
			             userStatuses = (List) ((UserStatusHome)IDOLookup.getHome(UserStatus.class)).findAllActiveByUserIdAndGroupId(Integer.parseInt(user.getPrimaryKey().toString()),Integer.parseInt(parentGroup.getPrimaryKey().toString()));
			         } catch (FinderException e) {
			             System.out.println(e.getMessage());
			         }
			         if (userStatuses.isEmpty()) {
			             if (userStatusesFilter != null && !userStatusesFilter.isEmpty()) {
			                 continue;
			             }
			         }
			         else {
			             UserStatus userStatus =(UserStatus)userStatuses.iterator().next();
			             String userStatusKey = userStatus.getStatus().getStatusKey();
			             if (!userStatusesFilter.isEmpty() && !userStatusesFilter.contains(userStatusKey)) {			                 
			                 continue;
			             }
			             else {
			                 userStatusString = _iwrb.getLocalizedString(USR_STAT_PREFIX+userStatusKey, userStatusKey);
			             }
			             
			         }
			         
			         String parentGroupPath = getParentGroupPath(parentGroup, topNodes);
				     // create a new ReportData for each row	    
			         ReportableData data = new ReportableData();
				     //	add the data to the correct fields/columns
			         data.addData(nameField, user.getName() );
				     data.addData(personalIDField, personalID);
				     data.addData(dateOfBirthField, dateOfBirthString);
				     data.addData(ageField, ageString);
				     data.addData(parentGroupField, parentGroup.getName());
				     data.addData(groupPathField, parentGroupPath);
				     data.addData(userStatusField, userStatusString);
				     data.addData(streetAddressField, streetAddressString);
				     data.addData(postalAddressField, postalAddressString);
				     data.addData(countryField, countryString);
				     data.addData(phoneField, getPhoneNumber(user));
				     data.addData(emailField, emailString);
				     data.addData(custodianNameField, custodianString );
				 	 data.addData(custodianPersonalIDField, custodianPersonalID );
				     data.addData(custodianPhoneField, custodianPhoneString );
				     List statsForGroup = (List) usersByGroups.get(parentGroup.getPrimaryKey());
						if (statsForGroup == null)
							statsForGroup = new Vector();
						statsForGroup.add(data);
						usersByGroups.put(parentGroup.getPrimaryKey(), statsForGroup);
				}
			}
			// iterate through the ordered map and ordered lists and add to the final collection
			Iterator statsDataIter = usersByGroups.keySet().iterator();
			while (statsDataIter.hasNext()) {
				
				List datas = (List) usersByGroups.get(statsDataIter.next());
				// don't forget to add the row to the collection
				reportCollection.addAll(datas);
			}			 	

    	ReportableField[] sortFields = null;
    	List orderByFields = new ArrayList();
    	if (dynamicLayout.equals("-1")) {
    		orderByFields.add(groupPathField);
    	}
		if (orderBy != null) {
			if (!dynamicLayout.equals("-1") && orderBy.equals(IWMemberConstants.ORDER_BY_GROUP_PATH)) {
	    		orderByFields.add(groupPathField);
	    	} else if (orderBy.equals(IWMemberConstants.ORDER_BY_USER_STATUS)) {
	    		orderByFields.add(userStatusField);
	    	} else if (orderBy.equals(IWMemberConstants.ORDER_BY_ADDRESS)) {
	    		orderByFields.add(streetAddressField);
	    	} else if (orderBy.equals(IWMemberConstants.ORDER_BY_POSTAL_ADDRESS)) {
	    		orderByFields.add(postalAddressField);
	    	}
		}
    	orderByFields.add(nameField);
		
		sortFields = new ReportableField[orderByFields.size()];
    	for (int i=0; i<orderByFields.size(); i++) {
    		sortFields[i] = (ReportableField)orderByFields.get(i);
    	}
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		return reportCollection;
    }

	public ReportableCollection getStatisticsForGroups(String groupIDFilter, String groupsRecursiveFilter, Collection groupTypesFilter, String dynamicLayout, String orderBy) throws RemoteException {
	    initializeBundlesIfNeeded();
		ReportableCollection reportCollection = new ReportableCollection();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		//PARAMETES
		//Add extra...because the inputhandlers supply the basic header texts
		
		reportCollection.addExtraHeaderParameter(
				"label_current_date", _iwrb.getLocalizedString(LOCALIZED_CURRENT_DATE, "Current date"),
				"current_date", TextSoap.findAndCut((new IWTimestamp()).getLocaleDateAndTime(currentLocale, IWTimestamp.LONG,IWTimestamp.SHORT),"GMT"));
 	
		//PARAMETERS that are also FIELDS
		 //data from entity columns, can also be defined with an entity definition, see getClubMemberStatisticsForRegionalUnions method
		 //The name you give the field/parameter must not contain spaces or special characters		
		 ReportableField nameField = new ReportableField(FIELD_NAME_NAME, String.class);
		 nameField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_NAME, "Name"), currentLocale);
		 reportCollection.addField(nameField);

		 ReportableField shortNameField = new ReportableField(FIELD_NAME_SHORT_NAME, String.class);
		 shortNameField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_SHORT_NAME, "Short Name"), currentLocale);
		 reportCollection.addField(shortNameField);

		 ReportableField abbrevationField = new ReportableField(FIELD_NAME_ABBREVATION, String.class);
		 abbrevationField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_ABBREVATION, "Abbrevation"), currentLocale);
		 reportCollection.addField(abbrevationField);

		 ReportableField displayNameField = new ReportableField(FIELD_NAME_DISPLAY_NAME, String.class);
		 displayNameField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_DISPLAY_NAME, "Display Name"), currentLocale);
		 reportCollection.addField(displayNameField);

		 ReportableField groupTypeField = new ReportableField(FIELD_NAME_GROUP_TYPE, String.class);
		 groupTypeField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_GROUP_TYPE, "Group Type"),currentLocale);
		 reportCollection.addField(groupTypeField);

		 ReportableField groupPathField = new ReportableField(FIELD_NAME_GROUP_PATH, String.class);
		 groupPathField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_GROUP_PATH, "Group Path"), currentLocale);
		 reportCollection.addField(groupPathField);
		 
		 ReportableField streetAddressField = new ReportableField(FIELD_NAME_STREET_ADDRESS, String.class);
		 streetAddressField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_STREET_ADDRESS, "Street Address"), currentLocale);
		 reportCollection.addField(streetAddressField);
		 
		 ReportableField postalAddressField = new ReportableField(FIELD_NAME_POSTAL_ADDRESS, String.class);
		 postalAddressField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_POSTAL_ADDRESS, "Postal Address"), currentLocale);
		 reportCollection.addField(postalAddressField); 

		 ReportableField pBoxField = new ReportableField(FIELD_NAME_POST_BOX, String.class);
		 pBoxField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_POST_BOX, "Postbox"), currentLocale);
		 reportCollection.addField(pBoxField);

		 ReportableField phoneField = new ReportableField(FIELD_NAME_PHONE, String.class);
		 phoneField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_PHONE, "Phone"), currentLocale);
		 reportCollection.addField(phoneField);
		 
		 ReportableField emailField = new ReportableField(FIELD_NAME_EMAIL, String.class);
		 emailField.setLocalizedName(_iwrb.getLocalizedString(LOCALIZED_EMAIL, "Email"), currentLocale);
		 reportCollection.addField(emailField);
		
		Group topGroup = null;
		Collection groups = null;
		try {
		    if (groupIDFilter != null && !groupIDFilter.equals("")) {
		        groupIDFilter = groupIDFilter.substring(groupIDFilter.lastIndexOf("_")+1);
		        topGroup = getGroupBusiness().getGroupByGroupID(Integer.parseInt((groupIDFilter)));
		    }
			if (topGroup != null) {
			    if (groupsRecursiveFilter != null && groupsRecursiveFilter.equals("checked")) {
			        groups = getGroupBusiness().getChildGroupsRecursiveResultFiltered(topGroup, groupTypesFilter, true, true, false);
				} else {
				    groups = new ArrayList();
				    groups.add(topGroup);
				}
			}
		} catch (FinderException e) {
		    e.printStackTrace();
    	}
			Collection topNodes = getUserBusiness().getUsersTopGroupNodesByViewAndOwnerPermissions(getUserContext().getCurrentUser(),getUserContext());
			Map usersByGroups = new TreeMap();
			AddressTypeHome addressHome = (AddressTypeHome) IDOLookup.getHome(AddressType.class);
			AddressType at1 = null;
			try {
			    at1 = addressHome.findAddressType1();
			} catch (FinderException e) {
			    e.printStackTrace();
			}
			Iterator iter = groups.iterator();
			while (iter.hasNext()) {
			    Group group = (Group) iter.next();
			    Collection parentGroupCollection = null;
			     try {
			         parentGroupCollection = getGroupHome().findParentGroups(Integer.parseInt(group.getPrimaryKey().toString()));
			     } catch (FinderException e) {
			         System.out.println(e.getMessage());
			     }
			     Iterator parIt = parentGroupCollection.iterator();

			    String nameString = group.getName();
			    String shortNameString = group.getShortName();
			    String abbrevation = group.getAbbrevation();
			    String displayNameString = null;
			    
			    if (abbrevation!=null && !abbrevation.equals("")) {
			    	displayNameString = abbrevation;
			    } else {
			    	displayNameString = nameString;
			    }
			    String groupTypeString = _userIwrb.getLocalizedString(group.getGroupType(), group.getGroupType());
			   	Collection emails =  group.getEmails();
			   	Email email = null;
			   	String emailString = null;
			   	if (!emails.isEmpty()) {
			   	    email = (Email)emails.iterator().next();
			   	    emailString = email.getEmailAddress();
			   	}
			   	Collection addresses = null;
			   	if (at1 != null) {			   	    
				   	try {
                        addresses = group.getAddresses(at1);
                    } catch (IDOLookupException e1) {
                        e1.printStackTrace();
                    } catch (IDOCompositePrimaryKeyException e1) {
                        e1.printStackTrace();
                    } catch (IDORelationshipException e1) {
                        e1.printStackTrace();
                    }
			   	}
			    Address address = null;
			    String streetAddressString = null;
			    String postalAddressString = null;
			    String postBoxString = null;
			   	if (addresses != null && !addresses.isEmpty()) {
			   	    address = (Address)addresses.iterator().next();
			   	    streetAddressString = address.getStreetAddress();
			   	    postalAddressString = address.getPostalAddress();
			   	    postBoxString = address.getPOBox();
			   	}
			   	while (parIt.hasNext()) {				     
			        Group parentGroup = (Group)parIt.next();
			        String parentGroupPath = getParentGroupPath(parentGroup, topNodes);
				    // create a new ReportData for each row	    
			        ReportableData data = new ReportableData();
			        //	add the data to the correct fields/columns
				    data.addData(nameField, nameString );
				    data.addData(shortNameField, shortNameString );
				    data.addData(abbrevationField, abbrevation );
   				    data.addData(displayNameField, displayNameString );
				    data.addData(groupTypeField, groupTypeString);
				    data.addData(groupPathField, parentGroupPath);
				    data.addData(emailField, emailString);
				    data.addData(streetAddressField, streetAddressString);
				    data.addData(postalAddressField, postalAddressString);
				    data.addData(pBoxField, postBoxString);
				    data.addData(phoneField, getPhoneNumber(group));
				    List statsForGroup = (List) usersByGroups.get(group.getPrimaryKey());
					if (statsForGroup == null)
						statsForGroup = new Vector();
					statsForGroup.add(data);
					usersByGroups.put(group.getPrimaryKey(), statsForGroup);
				}
			}
			// iterate through the ordered map and ordered lists and add to the final collection
			Iterator statsDataIter = usersByGroups.keySet().iterator();
			while (statsDataIter.hasNext()) {
				List datas = (List) usersByGroups.get(statsDataIter.next());
				// don't forget to add the row to the collection
				reportCollection.addAll(datas);
			}			 	

    	ReportableField[] sortFields = null;
    	List orderByFields = new ArrayList();
    	if (dynamicLayout.equals("-1")) {
    		orderByFields.add(groupPathField);
    	}
		if (orderBy != null) {
			if (!dynamicLayout.equals("-1") && orderBy.equals(IWMemberConstants.ORDER_BY_GROUP_PATH)) {
	    		orderByFields.add(groupPathField);
	    	} else if (orderBy.equals(IWMemberConstants.ORDER_BY_GROUP_TYPE)) {
	    		orderByFields.add(groupTypeField);
	    	} else if (orderBy.equals(IWMemberConstants.ORDER_BY_ADDRESS)) {
	    		orderByFields.add(streetAddressField);
	    	} else if (orderBy.equals(IWMemberConstants.ORDER_BY_POSTAL_ADDRESS)) {
	    		orderByFields.add(postalAddressField);
	    	}
		}
   		orderByFields.add(nameField);
		
		sortFields = new ReportableField[orderByFields.size()];
    	for (int i=0; i<orderByFields.size(); i++) {
    		sortFields[i] = (ReportableField)orderByFields.get(i);
    	}
		Comparator comparator = new FieldsComparator(sortFields);
		Collections.sort(reportCollection, comparator);
		return reportCollection;
	}

    private String getPhoneNumber(Group group) {
		Collection phones = group.getPhones();
		String phoneNumber = "";
		if (!phones.isEmpty()) {
			Phone phone = null;
			int tempPhoneType = 0;			
			int selectedPhoneType = 0;
			
			Iterator phIt =	phones.iterator();
			while (phIt.hasNext()) {
				phone = (Phone) phIt.next();
				if (phone != null) {
					tempPhoneType = phone.getPhoneTypeId();
					if (tempPhoneType != PhoneType.FAX_NUMBER_ID) {
						if (tempPhoneType == PhoneType.MOBILE_PHONE_ID) {							
							phoneNumber = phone.getNumber();
							break;
						}
						else if (tempPhoneType == PhoneType.HOME_PHONE_ID && selectedPhoneType != PhoneType.HOME_PHONE_ID) {
							phoneNumber = phone.getNumber();
							selectedPhoneType = phone.getPhoneTypeId();
						}
						else if (tempPhoneType == PhoneType.WORK_PHONE_ID && selectedPhoneType != PhoneType.WORK_PHONE_ID) {
							phoneNumber = phone.getNumber();
							selectedPhoneType = phone.getPhoneTypeId();
						}
					}
				}
			}
		}
		return phoneNumber;
    }
    
    private String getParentGroupPath(Group parentGroup, Collection topNodes) { 
        String parentGroupPath = parentGroup.getName();
	    Collection parentGroupCollection = null;
	    
	    while (parentGroup != null && !topNodes.contains(parentGroup)) {
	        String parentKey = parentGroup.getPrimaryKey().toString();
	        if (cachedParents.containsKey((parentKey))) {
		        Collection col = (Collection)cachedParents.get(parentKey);
		        Iterator it = col.iterator();
		        Integer parentID = null;
		        if (it.hasNext()) {
	                 parentID = (Integer)it.next();
	                 String groupKey = parentID.toString();
	                 if (cachedGroups.containsKey(groupKey)) {
	                     parentGroup = (Group)cachedGroups.get(groupKey); 
	                 }
	                 else {
	                     try {
		                     parentGroup = groupBiz.getGroupByGroupID(parentID.intValue());
			                 cachedGroups.put(groupKey, parentGroup);
	                     } catch (Exception e) {
	                         break;
	                     }
	                 }
	            }
		        else {
		        	break;
		        }
			} else {
			         parentGroupCollection = parentGroup.getParentGroups(cachedParents, cachedGroups);
			         
			     if (!parentGroupCollection.isEmpty()) {
			         parentGroup = (Group)parentGroupCollection.iterator().next();
			     }else {
			         break;
			     }
		    }
			parentGroupPath = parentGroup.getName()+"/"+parentGroupPath;
	    }
    return parentGroupPath;
    }
    
    private GroupHome getGroupHome() {
        try {
            return (GroupHome) IDOLookup.getHome(Group.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }
        return null;
    }

    private GroupBusiness getGroupBusiness() throws RemoteException {
		if (groupBiz == null) {
			groupBiz = (GroupBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), GroupBusiness.class);
		}	
		return groupBiz;
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		if (userBiz == null) {
			userBiz = (UserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), UserBusiness.class);
		}	
		return userBiz;
	}

	private NationalRegisterBusiness getNationalRegisterBusiness() throws RemoteException {
		if (nationalRegisterBiz == null) {
			nationalRegisterBiz = (NationalRegisterBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), NationalRegisterBusiness.class);
		}	
		return nationalRegisterBiz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new UserStatsWindowPlugin());
		list.add(new GroupStatsWindowPlugin());
		return list;
	}

	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	public String canCreateSubGroup(Group parentGroup, String groupTypeOfSubGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserPropertiesTabs(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}