package is.idega.idegaweb.member.business;

import is.idega.idegaweb.member.data.GroupApplication;
import is.idega.idegaweb.member.data.GroupApplicationHome;
import is.idega.idegaweb.member.presentation.GroupApplicationOverView;
import is.idega.idegaweb.member.presentation.GroupApplicationTab;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneHome;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.AddressType;
import com.idega.data.IDOAddRelationshipException;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;


/**
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 *
 */
public class GroupApplicationBusinessBean extends IBOServiceBean implements GroupApplicationBusiness, UserGroupPlugInBusiness{
	
	
	private static final String GENDER_MALE = "m"; 

	 
	
	public GroupApplication createGroupApplication(Group applicationGroup, String name, String pin , String gender, String email, String email2, String address, String postal, String phone, String phone2, String comment, String adminComment, String[] groupIds) throws RemoteException, FinderException, CreateException ,IDOAddRelationshipException{
		UserBusiness userBiz = this.getUserBusiness();
		EmailHome eHome = userBiz.getEmailHome();
		AddressHome addressHome = userBiz.getAddressHome();
		AddressBusiness addressBiz = getAddressBusiness();
		PhoneHome phoneHome = userBiz.getPhoneHome();		
	
		//user
		User user = userBiz.createUserByPersonalIDIfDoesNotExist(name,pin,getGender(gender), getBirthDateFromPin(pin));
		
		//gender
		user.setGender((Integer) this.getGender(gender).getPrimaryKey() );
		
		user.store(); 
			

		
		//email
		//both this and phones is very much a stupid hack in my part. I should have used findMethods etc. or make a useful getOrCreateIfNonExisting...bleh! -Eiki
		if( email!=null || email2!=null){
			Collection emails = user.getEmails();		
			boolean addEmail1 = true;
			boolean addEmail2 = true;
					
			Iterator iter = emails.iterator();
			//@todo do this with an equals method in a comparator?
			while (iter.hasNext()) {
				Email mail = (Email) iter.next();
				String tempAddress = mail.getEmailAddress();
				
				if( tempAddress.equals(email) ) addEmail1 = false;	
						
				if( tempAddress.equals(email2) ) addEmail2 = false;	
				
			}
			
			if( addEmail1 && email != null){
				Email uEmail = eHome.create();
				uEmail.setEmailAddress(email);
				uEmail.store();
				user.addEmail(uEmail);
			}
			
			if( addEmail2 && email2 != null){
				Email uEmail = eHome.create();
				uEmail.setEmailAddress(email2);
				uEmail.store();
				user.addEmail(uEmail);
			}
			

			
		}
		
		
		//address
		if( address!=null ){			
			
			//nytt find fall og breyta interfacinu
			
			
			Address uAddress = getUserBusiness().getUsersMainAddress(user);
			boolean add = false;
			if( uAddress == null ){	
				add = true;	
				AddressType mainAddress = addressHome.getAddressType1();
				uAddress = addressHome.create();
				uAddress.setAddressType(mainAddress);
			}
			
			uAddress.setStreetName(addressBiz.getStreetNameFromAddressString(address));
			uAddress.setStreetNumber(addressBiz.getStreetNumberFromAddressString(address));
			
			if( postal!=null ) uAddress.setPostalCodeID(Integer.parseInt(postal));
			
			uAddress.store();
			if ( add ) user.addAddress(uAddress);
		}
		
		// phone
		//@todo look for the phone first to avoid duplicated
		if( phone!=null || phone2!=null ){
			Collection phones = user.getPhones();
						
			boolean addPhone1 = true;
			boolean addPhone2 = true;
					
			Iterator iter = phones.iterator();
			//@todo do this with an equals method in a comparator?
			while (iter.hasNext()) {
				Phone tempPhone = (Phone) iter.next();
				String temp = tempPhone.getNumber();
				
				if( temp.equals(phone) ) addPhone1 = false;	
						
				if( temp.equals(phone2) ) addPhone2 = false;	
				
			}
  
			if( addPhone1 && phone != null){
				Phone uPhone = phoneHome.create();
				uPhone.setNumber(phone);
				uPhone.setPhoneTypeId(1);//weeeeee...svindl
				uPhone.store();
				user.addPhone(uPhone);
			}
			
			if( addPhone2 && phone2 != null){
				Phone uPhone = phoneHome.create();
				uPhone.setNumber(phone2);
				uPhone.setPhoneTypeId(3);//weeeeee...svindl
				uPhone.store();
				user.addPhone(uPhone);
			}
			
			
		}
		
		
		
		//groups	
		List groups = null;

		if( groupIds!=null ){
			GroupBusiness groupBiz = getGroupBusiness();
			groups = ListUtil.convertCollectionToList(groupBiz.getGroups(groupIds));
		}else{
			System.out.println("GROUPS ARE NULL!!!!!!");
		}
		
		//status			
		String status = getPendingStatusString();	
		
						
		
	
		
		return createGroupApplication(applicationGroup, user, status, comment,adminComment, groups);
		
	}
	
	public GroupApplication createGroupApplication(Group applicationGroup, User user, String status, String userComment, String adminComment, List groups) throws RemoteException, CreateException, IDOAddRelationshipException{
		
		GroupApplication appl = getGroupApplicationHome().create();
		appl.setApplicationGroupId(((Integer)applicationGroup.getPrimaryKey()).intValue());
		appl.setUserId(((Integer)user.getPrimaryKey()).intValue());
		appl.setStatus(status);
		appl.setUserComment(userComment);
		appl.setAdminComment(adminComment);
		appl.setCreated(IWTimestamp.getTimestampRightNow());
		appl.store();
		
		appl.addGroups(groups);
		appl.store();

		
		
		return appl;
 
	}
	
	public boolean changeGroupApplicationAdminCommentAndGroups(GroupApplication app, String adminComment, String[] groupIds){
		try {
			app.setAdminComment(adminComment);		
			
			if( groupIds!=null ){
				app.removeAllGroups();
				app.addGroups(ListUtil.convertCollectionToList(getGroupBusiness().getGroups(groupIds)));
			}
			//else app.removeAllGroups();
		
			app.store();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean changeGroupApplicationStatus(GroupApplication app, String status){
		try{
			GroupApplicationHome gHome = getGroupApplicationHome();
	    	String approved = gHome.getApprovedStatusString();
	    	String pending = gHome.getPendingStatusString();
	    	String denied = gHome.getDeniedStatusString();
			
			
			app.setModified(IWTimestamp.getTimestampRightNow());
			
			if( approved.equals(status) ){
				System.out.println("Approving application nr."+app.getPrimaryKey().toString());
				
				Collection groups = app.getGroups();
				User user = app.getUser();
				
				Iterator iter = groups.iterator();
				
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					group.addGroup(user);
				}
				
				app.setStatus(status);
				
			}
			else if( pending.equals(status) ){
				//extra stuff?
				app.setStatus(status);
			}
			else if( denied.equals(status) ){
				//extra stuff?	
				app.setStatus(status);			
			}
			
			
			
			
			
			app.store();		
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String getPendingStatusString() throws RemoteException{
		return getGroupApplicationHome().getPendingStatusString();
	}
	
	public String getApprovedStatusString()throws RemoteException{
		return getGroupApplicationHome().getApprovedStatusString();
	}
	
	public String getDeniedStatusString()throws RemoteException{
		return getGroupApplicationHome().getDeniedStatusString();
	}
	
	public boolean changeGroupApplicationStatus(int groupApplicationId, String status){
		try{
			GroupApplicationHome gHome = getGroupApplicationHome();
			GroupApplication app = gHome.findByPrimaryKey(new Integer(groupApplicationId));
			return changeGroupApplicationStatus(app,status);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
			
	}
	
	public Collection getGroupApplicationsByStatusAndApplicationGroup(String status, Group applicationGroup){
		Collection apps = null;
		try {
			GroupApplicationHome grHome = getGroupApplicationHome();
			apps =  grHome.findAllApplicationsByStatusAndApplicationGroup(status, applicationGroup);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return apps;
	
	}
	
	public Collection getGroupApplicationsByStatusAndUserOrderedByCreationDate(String status, User user){
		Collection apps = null;
		try {
			GroupApplicationHome grHome = getGroupApplicationHome();
			apps =  grHome.findAllApplicationsByStatusAndUserOrderedByCreationDate(status, user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return apps;
	
	}

	public UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) getServiceInstance(UserBusiness.class);	
	}
	
	public AddressBusiness getAddressBusiness() throws RemoteException {
		return (AddressBusiness) getServiceInstance(AddressBusiness.class);	
	}
	
	public GroupBusiness getGroupBusiness() throws RemoteException {
		return (GroupBusiness) getServiceInstance(GroupBusiness.class);	
	}

	public GroupApplicationHome getGroupApplicationHome() throws RemoteException {
		return (GroupApplicationHome) getIDOHome(GroupApplication.class);	
	}	

	 private Gender getGender(String gender){
	    try {
	      GenderHome home = (GenderHome) getIDOHome(Gender.class);
      
	        if( gender.equals(this.GENDER_MALE) ){
	          return home.getMaleGender();
	        }
	        else return home.getFemaleGender();

	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      return null;//if something happened
	    }
	  }
	  

	  
	 private IWTimestamp getBirthDateFromPin(String pin){
	    //pin format = 2502785279 yyyymmddxxxx
		int dd = Integer.parseInt(pin.substring(0,2));
		int mm = Integer.parseInt(pin.substring(2,4));
		int yyyy = Integer.parseInt(pin.substring(4,6));
	
		if(pin.endsWith("9")) yyyy += 1900;
	    else yyyy += 2000;
	    
	    IWTimestamp dob = new IWTimestamp(dd,mm,yyyy);
	    return dob;
	  }
	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreateOrUpdate(Group)
	 */
	public void afterGroupCreateOrUpdate(Group group)
		throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreateOrUpdate(User)
	 */
	public void afterUserCreateOrUpdate(User user)
		throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(Group)
	 */
	public void beforeGroupRemove(Group group)
		throws RemoveException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(User)
	 */
	public void beforeUserRemove(User user)
		throws RemoveException, RemoteException {
	}

	
	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(Group)
	 */
	public PresentationObject instanciateEditor(Group group)
		throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(Group)
	 */
	public PresentationObject instanciateViewer(Group group)throws RemoteException {
		GroupApplicationOverView viewer =  new GroupApplicationOverView();
		viewer.initialize(group);
		return viewer;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs()
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs()
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		ArrayList list = new ArrayList();
		list.add(new GroupApplicationTab(user));	
				
		return list;	
	}
  
  public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)  {
    return null;
  }

  public String isUserSuitedForGroup(User user, Group targetGroup)  {
    return null;
  }

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
 */
public List getMainToolbarElements() throws RemoteException {
	return null;
}

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
 */
public List getGroupToolbarElements(Group group) throws RemoteException {
	return null;
}

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group,java.lang.String)
 */
public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException {
	// TODO Auto-generated method stub
	return null;
}

}
