package is.idega.idegaweb.member.business;

import is.idega.idegaweb.member.data.GroupApplication;
import is.idega.idegaweb.member.data.GroupApplicationHome;
import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.business.IBOServiceBean;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 *
 */
public class GroupApplicationBusinessBean extends IBOServiceBean implements GroupApplicationBusiness{
	
	
	private static final String GENDER_MALE = "m"; 
	private static final String GENDER_FEMALE = "f";
	 
	public boolean createGroupApplication(Group applicationGroup, String name, String pin , String gender) throws RemoteException, CreateException{
		UserBusiness userBiz = this.getUserBusiness();
		User user = userBiz.createUserByPersonalIDIfDoesNotExist(name,pin,getGender(gender), getBirthDateFromPin(pin));
		user.setGender((Integer) this.getGender(gender).getPrimaryKey() );
		if( user.getCreated() == null ){
			user.setCreated(IWTimestamp.getTimestampRightNow());
		}
		
			
		user.store();
				
		
		return true;
		
	}
	
	public boolean createGroupApplication(Group applicationGroup, String name, String pin , String gender, String email, String address, String phone, String comment) throws RemoteException, CreateException{
		UserBusiness userBiz = this.getUserBusiness();
		User user = userBiz.createUserByPersonalIDIfDoesNotExist(name,pin,getGender(gender), getBirthDateFromPin(pin));
		
		user.setGender((Integer) this.getGender(gender).getPrimaryKey() );
		
		user.store();
		
		
		
		
		return true;
		
	}
	
	public GroupApplication createGroupApplication(Group applicationGroup, User user, String status, String userComment) throws RemoteException, CreateException{
		
		GroupApplication appl = getGroupApplicationHome().create();
		appl.setApplicationGroupId(((Integer)applicationGroup.getPrimaryKey()).intValue());
		appl.setUserId(((Integer)user.getPrimaryKey()).intValue());
		appl.setStatus(status);
		//appl.addGroups(java.util.List p0);
		appl.setUserComment(userComment);
		appl.setCreated(IWTimestamp.getTimestampRightNow());
		
		return appl;
 
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) getServiceInstance(UserBusiness.class);	
	}

	private GroupApplicationHome getGroupApplicationHome() throws RemoteException {
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
