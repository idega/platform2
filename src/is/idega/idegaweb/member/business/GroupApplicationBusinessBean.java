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
import com.idega.core.business.AddressBusiness;
import com.idega.core.data.Address;
import com.idega.core.data.AddressHome;
import com.idega.core.data.AddressType;
import com.idega.core.data.Email;
import com.idega.core.data.EmailHome;
import com.idega.core.data.Phone;
import com.idega.core.data.PhoneHome;
import com.idega.core.data.PhoneType;
import com.idega.core.data.PhoneTypeHome;
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
	private static final String GENDER_FEMALE = "f";
	 
	
	public GroupApplication createGroupApplication(Group applicationGroup, String name, String pin , String gender, String email, String address, String phone, String comment, String[] groupIds) throws RemoteException, FinderException, CreateException ,IDOAddRelationshipException{
		UserBusiness userBiz = this.getUserBusiness();
		EmailHome eHome = userBiz.getEmailHome();
		AddressHome addressHome = userBiz.getAddressHome();
		AddressBusiness addressBiz = getAddressBusiness();
		PhoneHome phoneHome = userBiz.getPhoneHome();		
		PhoneTypeHome phoneTypeHome = (PhoneTypeHome) this.getIDOHome(PhoneType.class);
		
		
		//user
		User user = userBiz.createUserByPersonalIDIfDoesNotExist(name,pin,getGender(gender), getBirthDateFromPin(pin));
		
		//gender
		user.setGender((Integer) this.getGender(gender).getPrimaryKey() );
		
		user.store(); 
			

		
		//email
		//@todo look for the email first to avoid duplicated
		if( email!=null){
			Email uEmail = eHome.create();
			uEmail.setEmailAddress(email);
			uEmail.store();
			user.addEmail(uEmail);
		}
		//address
		//@todo look for the address first to avoid duplicated
		if( address!=null ){
			AddressType mainAddress = addressHome.getAddressType1();
			Address uAddress = addressHome.create();
			uAddress.setAddressType(mainAddress);
			uAddress.setStreetName(addressBiz.getStreetNameFromAddressString(address));
			uAddress.setStreetNumber(addressBiz.getStreetNumberFromAddressString(address));
			uAddress.store();
			user.addAddress(uAddress);
		}
		
		// phone
		//@todo look for the phone first to avoid duplicated
		if( phone!=null ){
			Phone uPhone = phoneHome.create();
			uPhone.setNumber(phone);
			//missing type of phone
			uPhone.store();
			user.addPhone(uPhone);
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
		
						
		
	
		
		return createGroupApplication(applicationGroup, user, status, comment, groups);
		
	}
	
	public GroupApplication createGroupApplication(Group applicationGroup, User user, String status, String userComment, List groups) throws RemoteException, CreateException, IDOAddRelationshipException{
		
		GroupApplication appl = getGroupApplicationHome().create();
		appl.setApplicationGroupId(((Integer)applicationGroup.getPrimaryKey()).intValue());
		appl.setUserId(((Integer)user.getPrimaryKey()).intValue());
		appl.setStatus(status);
		appl.setUserComment(userComment);
		appl.setCreated(IWTimestamp.getTimestampRightNow());
		appl.store();
		
		appl.addGroups(groups);

		
		
		return appl;
 
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
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(Group)
	 */
	public void afterGroupCreate(Group group)
		throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(User)
	 */
	public void afterUserCreate(User user)
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
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(Collection, Collection, Collection)
	 */
	public Collection findGroupsByFields(
		Collection listViewerFields,
		Collection finderOperators,
		Collection listViewerFieldValues)
		throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		return null;
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

}
