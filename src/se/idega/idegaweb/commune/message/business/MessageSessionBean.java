package se.idega.idegaweb.commune.message.business;

import java.util.Collection;

import com.idega.business.IBOSessionBean;
import com.idega.idegaweb.IWPropertyList;
import com.idega.user.business.UserProperties;
import com.idega.user.data.User;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MessageSessionBean extends IBOSessionBean implements MessageSession {

	public static final String MESSAGE_PROPERTIES = "message_properties";
	public static final String USER_PROP_SEND_TO_MESSAGE_BOX = "msg_send_box";
	public static final String USER_PROP_SEND_TO_EMAIL = "msg_send_email";
	
	public boolean getIfUserCanReceiveEmails(User user){
	    return hasEmail(user) && getIfUserPreferesMessageByEmail();
	}
	
	private boolean hasEmail(User user) {
    Collection emails = user.getEmails();
    return emails != null && !emails.isEmpty();
	}
	
	protected UserProperties getUserPreferences() throws Exception {
		UserProperties property = getUserContext().getUserProperties();
		return property;
	}
	
	protected IWPropertyList getUserMessagePreferences() {
		try{
			return getUserPreferences().getProperties(MESSAGE_PROPERTIES);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public boolean getIfUserPreferesMessageByEmail(){
		IWPropertyList propertyList = getUserMessagePreferences();
		if (propertyList != null) {
			String property = propertyList.getProperty(USER_PROP_SEND_TO_EMAIL, String.valueOf(hasEmail(getUserContext().getCurrentUser())));
			if(property!=null) {
				return Boolean.valueOf(property).booleanValue();
			}
		}
		return false;
	}

	public boolean getIfUserPreferesMessageInMessageBox(){
		IWPropertyList propertyList = getUserMessagePreferences();
		if (propertyList != null) {
			String property = propertyList.getProperty(USER_PROP_SEND_TO_MESSAGE_BOX);
			if(property!=null)
				return Boolean.valueOf(property).booleanValue();
		}
		return true;
	}

	public void setIfUserPreferesMessageByEmail(boolean preference){
		IWPropertyList propertyList = getUserMessagePreferences();
		propertyList.setProperty(USER_PROP_SEND_TO_EMAIL, new Boolean(preference));
	}

	public void setIfUserPreferesMessageInMessageBox(boolean preference){
		IWPropertyList propertyList = getUserMessagePreferences();
		propertyList.setProperty(USER_PROP_SEND_TO_MESSAGE_BOX, new Boolean(preference));
	}

}
