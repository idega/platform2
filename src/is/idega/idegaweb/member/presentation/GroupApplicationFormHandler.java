package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Block;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 *
 */
public class GroupApplicationFormHandler extends Block {

	private static final String USER_NAME_PARAM = "user_name";
	private static final String PIN_PARAM = "pin";
	private static final String PHONE_PARAM = "phone";
	private static final String ADDRESS_PARAM = "address";
	private static final String EMAIL_PARAM = "email";
	private static final String COMMENT_PARAM = "comment";
	private static final String GROUPS_PARAM = "groups";
	private static final String GENDER_PARAM = "gender";
	
	

	private Group applicationGroup = null;
	
		
	public GroupApplicationFormHandler(){
		super();	
	}



	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
	
		if( applicationGroup!=null ){
			if( iwc.isParameterSet(USER_NAME_PARAM) && iwc.isParameterSet(PIN_PARAM) ){
				
				GroupApplicationBusiness biz = this.getGroupApplicationBusiness(iwc);

				
				String name = iwc.getParameter(USER_NAME_PARAM);
				String pin = iwc.getParameter(PIN_PARAM);
				String gender = iwc.getParameter(GENDER_PARAM);
				String email = iwc.getParameter(EMAIL_PARAM);
				String address = iwc.getParameter(ADDRESS_PARAM);
				String phone = iwc.getParameter(PHONE_PARAM);
				String comment = iwc.getParameter(COMMENT_PARAM);

				
				String[] groups = iwc.getParameterValues(GROUPS_PARAM);
				
				biz.createGroupApplication(applicationGroup,name,pin,gender,email,address,phone,comment);
				

			
			}
			else add("Error : No name and PIN!");
			
					
		}
		else{
			add("The application group parameter has not been set");	
		}		
			
	}
	
	public void setApplicationGroup(Group group){
		this.applicationGroup = group;	
	}
		
		
		
	private GroupApplicationBusiness getGroupApplicationBusiness(IWApplicationContext iwac) throws RemoteException {
		return (GroupApplicationBusiness) IBOLookup.getServiceInstance(iwac, GroupApplicationBusiness.class);	
	}
	

	  
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return "is.idega.idegaweb.member";
	}

}
