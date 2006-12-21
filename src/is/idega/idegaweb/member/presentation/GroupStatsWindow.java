/*
 * Created on Jun 24, 2006
 * 
 */
package is.idega.idegaweb.member.presentation;

import com.idega.presentation.IWContext;

/**
 * @author Sigtryggur
 *
 */
public class GroupStatsWindow extends GenericStatsWindow {
    
    public GroupStatsWindow() {
        super();
		setHeight(400);
		setWidth(400);
		setScrollbar(true);
    }

    public void main(IWContext iwc) throws Exception {
    	this.iwrb = getResourceBundle(iwc);
    	this.windowTitle = this.iwrb.getLocalizedString("groupstatswindow.groupstats", "Group Report");
    	this.invocationFileName = "Invocation-GroupStats.xml";
    	this.layoutFileName = "Layout-GroupStats.xml";
	    super.main(iwc);
    }
}