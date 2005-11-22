/*
 * Created on Feb 2, 2005
 */
package is.idega.idegaweb.member.presentation;

import com.idega.presentation.text.Link;
import com.idega.user.data.Group;
import com.idega.user.presentation.LinkToUserStats;

/**
 * @author Sigtryggur
 */
public class LinkToUserStatsImpl implements LinkToUserStats {

    private Group selectedGroup = null;
    private String invocationFileName = null;
    private String layoutFileName = null;
    private String localizableKeyName = null;
    
    public Link getLink() {
        Link link = new Link();
		link.addParameter(UserStatsWindow.STATS_INVOCATION_NAME_FROM_BUNDLE, invocationFileName);
		link.addParameter(UserStatsWindow.STATS_LAYOUT_NAME_FROM_BUNDLE, layoutFileName);
		link.addParameter(UserStatsWindow.STATS_LOCALIZABLE_KEY_NAME, localizableKeyName);
		link.setWindowToOpen(UserStatsWindow.class);
		if (selectedGroup!= null) {
		    link.addParameter("dr_group", selectedGroup.getNodeID());
		}
        return link;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }
    public void setInvocationFileName(String invocationFileName) {
        this.invocationFileName = invocationFileName;
    }
    public void setLayoutFileName(String layoutFileName) {
        this.layoutFileName = layoutFileName;
    }
    public void setLocalizableKeyName(String localizableKeyName) {
        this.localizableKeyName = localizableKeyName;
    }
}
