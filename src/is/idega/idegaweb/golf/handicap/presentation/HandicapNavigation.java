/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.service.GolfGroup;
import is.idega.idegaweb.golf.util.GolfConstants;

import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * @author laddi
 */
public class HandicapNavigation extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();

		getParentPage().setTitle(iwrb.getLocalizedString("handicap.page_name", "Handicap Registry"));
		String action = modinfo.getParameter("handicap_action");
		String uri = modinfo.getRequestURI();

		try {
			Table contentTable = drawTable(iwrb, modinfo, action, uri);
			add(contentTable);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public Table drawTable(IWResourceBundle iwrb, IWContext modinfo, String action, String uri) throws SQLException, FinderException {
		String memberId = null;
		
		if (modinfo.isParameterSet(GolfConstants.MEMBER_UUID)) {
			MemberHome home = (MemberHome) IDOLookup.getHomeLegacy(Member.class);
			try {
				Member member = home.findByUniqueID(modinfo.getParameter(GolfConstants.MEMBER_UUID));
				memberId = member.getPrimaryKey().toString();
			}
			catch (FinderException fe) {
				try {
					UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
					try {
						User user = userHome.findUserByUniqueId(modinfo.getParameter(GolfConstants.MEMBER_UUID));
						Member member = home.findMemberByIWMemberSystemUser(user);
						memberId = member.getPrimaryKey().toString();
					}
					catch (FinderException e) {
						//Nothing found...
					}
				}
				catch (IDOLookupException ile) {
					log(ile);
				}
			}
			
			if (memberId != null) {
				modinfo.setSessionAttribute("member_id", memberId);
			}
		}

		if (memberId == null) {
			memberId = modinfo.getParameter("member_id");
		}
		if (memberId == null) {
			memberId = (String) modinfo.getSessionAttribute("member_id");
		}

		if (memberId == null) {
			Member memberinn = (Member) modinfo.getSession().getAttribute("member_login");
			if (memberinn != null) {
				memberId = String.valueOf(memberinn.getID());
				if (memberId == null) {
					memberId = "1";
				}
			}
			else {
				memberId = "1";
			}
		}

		GolfGroup golfGroup = new GolfGroup(memberId);
		boolean canWrite = true;
		if ((!AccessControl.isAdmin(modinfo)) && (memberId != "1")) {
			canWrite = golfGroup.getCanWrite();
		}

		Table myTable = new Table(1, 3);
		myTable.setBorder(0);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setWidth("100%");
		myTable.setHeight("100%");
		myTable.setColor(1, 1, "#FFFFFF");
		myTable.setColor(1, 2, "#FFFFFF");
		myTable.setColor(1, 3, "#FFFFFF");
		myTable.setAlignment(1, 1, "right");
		myTable.setAlignment(1, 3, "center");
		myTable.setVerticalAlignment(1, 3, "middle");
		myTable.setHeight(1, "15");
		myTable.setHeight(3, "100%");

		Image mynd1 = iwrb.getImage("tabs/information.gif");
		Image mynd2 = iwrb.getImage("tabs/registerscore.gif");
		Image mynd3 = iwrb.getImage("tabs/overview.gif");
		Image mynd4 = iwrb.getImage("tabs/information1.gif");
		Image mynd5 = iwrb.getImage("tabs/registerscore1.gif");
		Image mynd6 = iwrb.getImage("tabs/overview1.gif");

		Link information = new Link(mynd4, uri);
		information.addParameter("handicap_action", "information");
		information.setPage(getParentPageID());

		Link registerScore = new Link(mynd5, uri);
		registerScore.addParameter("handicap_action", "registerScore");
		registerScore.setPage(getParentPageID());

		Link overView = new Link(mynd6, uri);
		overView.addParameter("handicap_action", "overView");
		overView.setPage(getParentPageID());

		if ((action == null) || ("information".equalsIgnoreCase(action))) {
			myTable.add(mynd1, 1, 1);
			HandicapInfo info = new HandicapInfo(memberId);
			myTable.add(info, 1, 3);
		}
		else
			myTable.add(information, 1, 1);

		if (canWrite) {
			if ("registerScore".equalsIgnoreCase(action)) {
				myTable.add(mynd2, 1, 1);
				HandicapScore score = new HandicapScore(memberId);
				myTable.add(score, 1, 3);
			}
			else
				myTable.add(registerScore, 1, 1);
		}

		if ("overView".equalsIgnoreCase(action)) {
			myTable.add(mynd3, 1, 1);
			HandicapOverview hOverview = new HandicapOverview(memberId);
			myTable.add(hOverview, 1, 2);

		}
		else
			myTable.add(overView, 1, 1);

		return myTable;
	}
}