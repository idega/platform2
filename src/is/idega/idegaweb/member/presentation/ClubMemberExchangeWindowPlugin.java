package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.user.app.ToolbarElement;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Aug 27, 2004
 */
public class ClubMemberExchangeWindowPlugin implements ToolbarElement {
	
	private  static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getButtonImage(com.idega.presentation.IWContext)
	 */
	public Image getButtonImage(IWContext iwc) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getName(com.idega.presentation.IWContext)
	 */
	public String getName(IWContext iwc) {
		IWBundle bundle = iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		IWResourceBundle resourceBundle = bundle.getResourceBundle(iwc);
		return resourceBundle.getLocalizedString("button.club_member_exchange", "Club Exchange");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPresentationObjectClass(com.idega.presentation.IWContext)
	 */
	public Class getPresentationObjectClass(IWContext iwc) {
		return ClubMemberExchangeWindow.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getParameterMap(com.idega.presentation.IWContext)
	 */
	public Map getParameterMap(IWContext iwc) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isValid(com.idega.presentation.IWContext)
	 */
	public boolean isValid(IWContext iwc) {
		IWMainApplicationSettings settings = iwc.getApplicationSettings();
		if (settings.getProperty("temp_show_is_related_stuff") == null) {
			return false;
		}		
		try {
			return checkUsersPermission(iwc);
		}
		catch (RemoteException e) {
		      throw new RuntimeException("[ClubMemberExchangeWindowPlugin]: Can't check user's permission");
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPriority(com.idega.presentation.IWContext)
	 */
	public int getPriority(IWContext iwc) {
		return 7;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isButton(com.idega.presentation.IWContext)
	 */
	public boolean isButton(IWContext iwc) {
		return false;
	}


	private boolean checkUsersPermission(IWContext iwc) throws RemoteException {
		if (iwc.isSuperAdmin()) {
			return true;
		}
		UserBusiness userBusiness = getUserBusiness(iwc);
		User user = iwc.getCurrentUser();
		Collection groups = userBusiness.getUsersTopGroupNodesByViewAndOwnerPermissions(user, iwc);
		Iterator iterator = groups.iterator();
		while (iterator.hasNext()) {
			Group group = (Group) iterator.next();
			String groupType = group.getGroupType();
			if (IWMemberConstants.GROUP_TYPE_FEDERATION.equals(groupType) ||
				IWMemberConstants.GROUP_TYPE_LEAGUE.equals(groupType)) {
				return true;
			}
		}
		return false;
	}

	private UserBusiness getUserBusiness(IWContext iwc)	{
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (IBOLookupException ex)	{
      throw new RuntimeException("[ClubMemberExchangeWindowPlugin]: Can't retrieve UserBusiness");
		}
	}	
}