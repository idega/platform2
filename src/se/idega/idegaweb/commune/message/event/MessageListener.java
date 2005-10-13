package se.idega.idegaweb.commune.message.event;

import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;
import se.idega.idegaweb.commune.message.business.MessageSession;

import com.idega.event.IWPageEventListener;
import com.idega.presentation.IWContext;

/**
 * @author Laddi
*/
public class MessageListener implements IWPageEventListener {

  public final static String PARAM_MESSAGE_ID = "msg_id";
  public final static String PARAM_TO_MSG_BOX = "msg_to_msg_box";
  public final static String PARAM_TO_EMAIL = "msg_to_email";
	public final static String PARAM_DELETE_MESSAGE = "msg_delete_message";
	public final static String PARAM_SAVE_SETTINGS = "msg_save_settings";
   
	/**
	 * @see com.idega.business.IWEventListener#actionPerformed(IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAM_DELETE_MESSAGE)) {
				deleteMessages(iwc);
			}
			if (iwc.isParameterSet(PARAM_SAVE_SETTINGS)) {
				saveSettings(iwc);
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

  private void deleteMessages(IWContext iwc) throws Exception {
    String[] ids = iwc.getParameterValues(PARAM_MESSAGE_ID);
    for(int i=0; i<ids.length; i++){
      getMessageBusiness(iwc).deleteUserMessage(Integer.parseInt(ids[i]));
    }
  }

  private void saveSettings(IWContext iwc) throws Exception {
  	MessageSession session = getMessageSession(iwc);

  	if (iwc.isParameterSet(PARAM_TO_MSG_BOX)) {
  		session.setIfUserPreferesMessageInMessageBox(true);
  	}
  	else {
  		session.setIfUserPreferesMessageInMessageBox(false);
  	}
  		
  	if (iwc.isParameterSet(PARAM_TO_EMAIL))
  		session.setIfUserPreferesMessageByEmail(true);
  	else
  		session.setIfUserPreferesMessageByEmail(false);
  }
  
  private CommuneMessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
    return (CommuneMessageBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,CommuneMessageBusiness.class);
  }

  private MessageSession getMessageSession(IWContext iwc) throws Exception {
    return (MessageSession)com.idega.business.IBOLookup.getSessionInstance(iwc,MessageSession.class);
  }
}