package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Block;

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
	
		
	public GroupApplicationFormHandler(){
		super();	
	}



	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
	
		if( iwc.isParameterSet(USER_NAME_PARAM) ){
					
		}		
			
	}
		
		
		
	private GroupApplicationBusiness getGroupApplicationBusiness(IWApplicationContext iwac) throws RemoteException {
		return (GroupApplicationBusiness) IBOLookup.getServiceInstance(iwac, GroupApplicationBusiness.class);	
	}

}
