/*
 * Created on Jun 24, 2006
 * 
 */
package is.idega.idegaweb.member.presentation;

import java.util.Map;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.user.app.ToolbarElement;


public class GroupStatsWindowPlugin implements ToolbarElement {
	
	private  static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	
	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getButtonImage(com.idega.presentation.IWContext)
	 */
	public Image getButtonImage(IWContext iwc) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getName(com.idega.presentation.IWContext)
	 */
	public String getName(IWContext iwc) {
		IWBundle bundle = iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		IWResourceBundle resourceBundle = bundle.getResourceBundle(iwc);
		return resourceBundle.getLocalizedString("button.group_report_tool", "Group Report Tool");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPresentationObjectClass(com.idega.presentation.IWContext)
	 */
	public Class getPresentationObjectClass(IWContext iwc) {
		return GroupStatsWindow.class;
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
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPriority(com.idega.presentation.IWContext)
	 */
	public int getPriority(IWContext iwc) {
		return 13;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isButton(com.idega.presentation.IWContext)
	 */
	public boolean isButton(IWContext iwc) {
		return false;
	}
}
