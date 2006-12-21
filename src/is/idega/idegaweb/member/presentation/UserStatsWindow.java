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
    	this.iwrb = getResourceBundle(iwc);
    	this.windowTitle = this.iwrb.getLocalizedString("userstatswindow.userstats", "User Report");
		this.invocationFileName = "Invocation-UserStats.xml";
		this.layoutFileName = "Layout-UserStats.xml";
    	super.main(iwc);		
    }
}