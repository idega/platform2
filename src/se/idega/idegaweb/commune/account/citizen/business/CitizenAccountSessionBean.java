package se.idega.idegaweb.commune.account.citizen.business;

import com.idega.business.IBOSessionBean;
import com.idega.idegaweb.IWPropertyList;
import com.idega.user.business.UserProperties;

/**
 * @author alindman
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CitizenAccountSessionBean extends IBOSessionBean implements CitizenAccountSession {

	public static final String CITIZEN_ACCOUNT_PROPERTIES = "citizen_account_properties";
	public static final String USER_PROPERTY_USE_CO_ADDRESS = "msg_send_box";
	
	protected UserProperties getUserPreferences() throws Exception {
		UserProperties property = getUserContext().getUserProperties();
		return property;
	}
	
	protected IWPropertyList getUserMessagePreferences() {
		try{
			return getUserPreferences().getProperties(CITIZEN_ACCOUNT_PROPERTIES);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public boolean getIfUserUsesCOAddress(){
		IWPropertyList propertyList = getUserMessagePreferences();
		if (propertyList != null) {
			String property = propertyList.getProperty(USER_PROPERTY_USE_CO_ADDRESS);
			if (property!=null) {
				return Boolean.valueOf(property).booleanValue();
			}
		}
		return true;
	}

	public void setIfUserUsesCOAddress(boolean preference){
		IWPropertyList propertyList = getUserMessagePreferences();
		propertyList.setProperty(USER_PROPERTY_USE_CO_ADDRESS, new Boolean(preference));
	}
}
