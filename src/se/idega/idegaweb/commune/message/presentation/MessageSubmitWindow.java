package se.idega.idegaweb.commune.message.presentation;

import se.idega.idegaweb.commune.message.event.MessageListener;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author laddi
 */
public class MessageSubmitWindow extends Window {

	private final static String PARAM_DELETE_MESSAGE = MessageListener.PARAM_DELETE_MESSAGE;
	private final static String PARAM_SAVE_SETTINGS = MessageListener.PARAM_SAVE_SETTINGS;

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	public MessageSubmitWindow() {
		setWidth(10);
		setHeight(10);
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = iwc.getApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		if (iwc.isParameterSet(PARAM_DELETE_MESSAGE)) {
			close();
			setParentToReload();
		}
		if (iwc.isParameterSet(PARAM_SAVE_SETTINGS)) {
			close();
			setAlertOnUnLoad(iwrb.getLocalizedString("message.settings_saved", "Settings saved."));
		}
	}
}