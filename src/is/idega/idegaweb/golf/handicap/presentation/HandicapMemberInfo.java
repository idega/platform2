/*
 * Created on 26.4.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.block.image.presentation.GolfImage;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.util.GolfConstants;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.text.TextSoap;


/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class HandicapMemberInfo extends GolfBlock {

	private boolean isAdmin = false;
	private String iMemberID;
	
	private Table table;
	
	private IWResourceBundle iwrb;
	private IWBundle iwb;
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle();
		iwb = getBundle();
		isAdmin = isAdministrator(modinfo);

		if (modinfo.isParameterSet(GolfConstants.MEMBER_UUID)) {
			MemberHome home = (MemberHome) IDOLookup.getHomeLegacy(Member.class);
			try {
				Member member = home.findByUniqueID(modinfo.getParameter(GolfConstants.MEMBER_UUID));
				iMemberID = member.getPrimaryKey().toString();
			}
			catch (FinderException fe) {
				UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
				try {
					User user = userHome.findUserByUniqueId(modinfo.getParameter(GolfConstants.MEMBER_UUID));
					Member member = home.findMemberByIWMemberSystemUser(user);
					iMemberID = member.getPrimaryKey().toString();
				}
				catch (FinderException e) {
					//Nothing found...
				}
			}
			
			if (iMemberID != null) {
				modinfo.setSessionAttribute("member_id", iMemberID);
			}
		}

		if (iMemberID == null) {
			iMemberID = modinfo.getRequest().getParameter("member_id");
		}
		if (iMemberID == null) {
			iMemberID = (String) modinfo.getSession().getAttribute("member_id");
		}
		if (iMemberID == null) {
			Member memberinn = (Member) modinfo.getSession().getAttribute("member_login");
			if (memberinn != null) {
				iMemberID = String.valueOf(memberinn.getID());
				if (iMemberID == null) {
					iMemberID = "1";
				}
			}
			else {
				iMemberID = "1";
			}

		}

		try {
			drawTable(modinfo);
		}
		catch (FinderException fe) {
			log(fe);
		}
		add(table);
	}

	private void drawTable(IWContext iwc) throws IOException, SQLException, FinderException {
		table = new Table(1, 9);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 12);
		table.setHeight(4, 3);
		table.setHeight(6, 12);
		table.setHeight(8, 3);
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_CENTER);

		Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(iMemberID));
		MemberInfo memberInfo = null;
		try {
			memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(Integer.parseInt(iMemberID));
		}
		catch (FinderException fe) {
			memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).createLegacy();
			memberInfo.setID(member.getID());
			memberInfo.setFirstHandicap(100f);
			memberInfo.setHandicap(100f);
			memberInfo.store();
		}
		int order = memberInfo.getNumberOfRecords("handicap", "<", "" + member.getHandicap()) + 1;

		Text handicap = getBigHeader(iwrb.getLocalizedString("handicap.handicap", "Handicap"));
		table.add(handicap, 1, 3);
		Text totalOrder = getBigHeader(iwrb.getLocalizedString("handicap.national_ranking", "National ranking"));
		table.add(totalOrder, 1, 7);

		Text handicapText = null;
		if ((int) memberInfo.getHandicap() == 100) {
			handicapText = getText(iwrb.getLocalizedString("handicap.no_handicap", "No handicap"));
		}
		else {
			handicapText = getBigText(TextSoap.singleDecimalFormat(String.valueOf(memberInfo.getHandicap())));
		}
		table.add(handicapText, 1, 5);

		if (isAdmin) {
			GenericButton handicapUpdate = getButton(new GenericButton("update_handicap", iwrb.getLocalizedString("handicap.update_handicap", "Update handicap")));
			handicapUpdate.setWindowToOpen(HandicapUpdate.class);
			handicapUpdate.addParameterToWindow("member_id", iMemberID);
			table.add(new Break(), 1, 5);
			table.add(handicapUpdate, 1, 5);
		}


		String noRounds = iwrb.getLocalizedString("handicap.no_round", "No rounds registered");
		Text orderText = getBigText(Integer.toString(order));
		if ((int) memberInfo.getHandicap() == 100) {
			orderText = getText(noRounds);
		}
		table.add(orderText, 1, 9);

		Image memberImage = null;
		if (member.getImageId() == 1) {
			memberImage = iwb.getImage("/shared/user/user.jpg");
			memberImage.setAlt(localize("handicap.no_image_found", "No image found from user"));
			memberImage.setToolTip(localize("handicap.no_image_found", "No image found from user"));
		}
		else {
			memberImage = new GolfImage(member.getImageId());
			memberImage.setAlt(member.getName());
			memberImage.setToolTip(member.getName());
		}
		memberImage.setMaxImageWidth(100);
		memberImage.setBorder(1);
		memberImage.setBorderColor(getHeaderColor());
		memberImage.setBorderStyle("solid");
		table.add(memberImage, 1, 1);
	}
}