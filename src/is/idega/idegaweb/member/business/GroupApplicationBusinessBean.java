package is.idega.idegaweb.member.business;

import is.idega.idegaweb.member.data.GroupApplication;
import is.idega.idegaweb.member.data.GroupApplicationHome;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOAddRelationshipException;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;


/**
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 *
 */
public class GroupApplicationBusinessBean extends IBOServiceBean implements GroupApplicationBusiness{
	
	
	private static final String GENDER_MALE = "m"; 
	private static final String GENDER_FEMALE = "f";
	 
	
	public GroupApplication createGroupApplication(Group applicationGroup, String name, String pin , String gender, String email, String address, String phone, String comment, String[] groupIds) throws RemoteException, FinderException, CreateException ,IDOAddRelationshipException{
		UserBusiness userBiz = this.getUserBusiness();
		User user = userBiz.createUserByPersonalIDIfDoesNotExist(name,pin,getGender(gender), getBirthDateFromPin(pin));
		
		user.setGender((Integer) this.getGender(gender).getPrimaryKey() );
		
		//email, address, phone
				
		List groups = null;

		if( groupIds!=null ){
			GroupBusiness groupBiz = getGroupBusiness();
			groups = ListUtil.convertCollectionToList(groupBiz.getGroups(groupIds));
		}
			
		String status = getGroupApplicationHome().getPendingStatusString();	
		
		user.store(); 
				
		
		return createGroupApplication(applicationGroup, user, status, comment, groups);
		
	}
	
	public GroupApplication createGroupApplication(Group applicationGroup, User user, String status, String userComment, List groups) throws RemoteException, CreateException, IDOAddRelationshipException{
		
		GroupApplication appl = getGroupApplicationHome().create();
		appl.setApplicationGroupId(((Integer)applicationGroup.getPrimaryKey()).intValue());
		appl.setUserId(((Integer)user.getPrimaryKey()).intValue());
		appl.setStatus(status);
		appl.addGroups(groups);
		appl.setUserComment(userComment);
		appl.setCreated(IWTimestamp.getTimestampRightNow());
		appl.store();
		
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
				
				Collection groups = app.getGroups();
				User user = app.getUser();
				
				Iterator iter = groups.iterator();
				
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					group.addGroup(user);
				}
				
			}
			else if( pending.equals(status) ){
				//extra stuff?
			}
			else if( denied.equals(status) ){
				//extra stuff?				
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
}
