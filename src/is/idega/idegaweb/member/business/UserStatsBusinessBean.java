/*
 * Created on Jan 20, 2005
 */
package is.idega.idegaweb.member.business;

import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
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

import javax.ejb.FinderException;

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
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
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
public class UserStatsBusinessBean extends IBOSessionBean  implements UserStatsBusiness{
    
    private UserBusiness userBiz = null;
    private GroupBusiness groupBiz = null;
	private IWBundle _iwb = null;
	private IWResourceBundle _iwrb = null;
	private IWResourceBundle _userIwrb = null;
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	private final static String USER_IW_BUNDLE_IDENTIFIER = "com.idega.user";
	private static final String USR_STAT_PREFIX = "usr_stat_";

	private static final String LOCALIZED_CURRENT_DATE = "UserStatsBusiness.current_date";
	private static final String LOCALIZED_NAME = "UserStatsBusiness.name";
	private static final String LOCALIZED_PERSONAL_ID = "UserStatsBusiness.personal_id";
	private static final String LOCALIZED_GROUP_TYPE = "UserStatsBusiness.group_type";
	private static final String LOCALIZED_GROUP_PATH = "UserStatsBusiness.group_path";
	private static final String LOCALIZED_USER_STATUS = "UserStatsBusiness.user_status";
	private static final String LOCALIZED_STREET_ADDRESS = "UserStatsBusiness.street_address";
	private static final String LOCALIZED_POSTAL_ADDRESS = "UserStatsBusiness.postal_address";
	private static final String LOCALIZED_COUNTRY = "UserStatsBusiness.country";
	private static final String LOCALIZED_PHONE = "UserStatsBusiness.phone";
	private static final String LOCALIZED_EMAIL = "UserStatsBusiness.email";

	private static final String FIELD_NAME_NAME = "name";
	private static final String FIELD_NAME_PERSONAL_ID = "personal_id";
	private static final String FIELD_NAME_GROUP_TYPE = "group_type";
	private static final String FIELD_NAME_GROUP_PATH = "group_path";
	private static final String FIELD_NAME_USER_STATUS = "user_status";
	private static final String FIELD_NAME_STREET_ADDRESS = "street_address";
	private static final String FIELD_NAME_POSTAL_ADDRESS = "postal_address";
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
				     data.addData(groupPathField, parentGroupPath);
				     data.addData(userStatusField, userStatusString);
				     data.addData(emailField, emailString);
				     data.addData(streetAddressField, streetAddressString);
				     data.addData(postalAddressField, postalAddressString);
				     data.addData(countryField, countryString);
				     data.addData(phoneField, getPhoneNumber(user));
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
				}
			    groups.add(topGroup);
			}
		} catch (FinderException e) {
		    e.printStackTrace();
    	}
			Collection topNodes = getUserBusiness().getUsersTopGroupNodesByViewAndOwnerPermissions(getUserContext().getCurrentUser(),getUserContext());
			Map usersByGroups = new TreeMap();
			AddressTypeHome addressHome = (AddressTypeHome) IDOLookup.getHome(AddressType.class);
			AddressType at1 = null;
			try {
			    at1 = addressHome.findAddressType2();
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
			   	if (addresses != null && !addresses.isEmpty()) {
			   	    address = (Address)addresses.iterator().next();
			   	    streetAddressString = address.getStreetAddress();
			   	    postalAddressString = address.getPostalAddress();
			   	}
			   	while (parIt.hasNext()) {				     
			        Group parentGroup = (Group)parIt.next();
			        String parentGroupPath = getParentGroupPath(parentGroup, topNodes);
				    // create a new ReportData for each row	    
			        ReportableData data = new ReportableData();
			        //	add the data to the correct fields/columns
				    data.addData(nameField, group.getName() );
				    data.addData(groupTypeField, groupTypeString);
				    data.addData(groupPathField, parentGroupPath);
				    data.addData(emailField, emailString);
				    data.addData(streetAddressField, streetAddressString);
				    data.addData(postalAddressField, postalAddressString);
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
}