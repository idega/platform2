/*
 * Created on Feb 2, 2005
 */
package is.idega.idegaweb.member.presentation;

import com.idega.presentation.text.Link;
import com.idega.user.presentation.LinkToUserStats;

/**
 * @author Sigtryggur
 */
public class LinkToUserStatsImpl implements LinkToUserStats {

    public Link getLink() {
        Link link = new Link();
		link.setWindowToOpen(UserStatsWindow.class);
        return link;
    }
}
