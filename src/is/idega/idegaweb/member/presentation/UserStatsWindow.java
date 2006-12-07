/*
 * Created on Jan 3, 2005
 *
 */
package is.idega.idegaweb.member.presentation;

import com.idega.presentation.IWContext;

/**
 * @author Sigtryggur
 *
 */
public class UserStatsWindow extends GenericStatsWindow {
    
    public UserStatsWindow() {
        super();
		setHeight(480);
		setWidth(400);
		setScrollbar(true);
    }

    public void main(IWContext iwc) throws Exception {
    	iwrb = getResourceBundle(iwc);
    	windowTitle = iwrb.getLocalizedString("userstatswindow.userstats", "User Report");
		invocationFileName = "Invocation-UserStats.xml";
		layoutFileName = "Layout-UserStats.xml";
    	super.main(iwc);		
    }
}