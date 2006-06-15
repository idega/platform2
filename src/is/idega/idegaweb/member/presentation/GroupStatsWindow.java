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
    }

    public void main(IWContext iwc) throws Exception {
    	iwrb = getResourceBundle(iwc);
    	windowTitle = iwrb.getLocalizedString("groupstatswindow.groupstats", "Group Report");
    	invocationFileName = "Invocation-GroupStats.xml";
    	layoutFileName = "Layout-GroupStats.xml";
	    super.main(iwc);
    }
}