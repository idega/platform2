package is.idega.idegaweb.golf.handicap.presentation;

/**
 * Title: Description: Copyright: idega Copyright (c) 2001 Company:
 * 
 * @author @version 1.0
 */

import is.idega.idegaweb.golf.business.ScorecardBusiness;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.util.GolfConstants;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

public class HandicapInfo extends GolfBlock {

	private String iMemberID;
	private boolean isAdmin = false;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	public HandicapInfo() {
	}

	public HandicapInfo(String memberID) {
		this.iMemberID = memberID;
	}

	public HandicapInfo(int member_id) {
		this.iMemberID = String.valueOf(member_id);
	}

	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle();
		iwb = getBundle();

		this.isAdmin = isAdministrator(modinfo);

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
			Table noTable = new Table();
			noTable.setAlignment("center");
			noTable.setCellpadding(12);
			noTable.setCellspacing(12);

			Text texti = getHeader(iwrb.getLocalizedString("handicap.member_no_handicap", "Member does not have a registered handicap."));
			texti.addBreak();
			texti.addBreak();
			texti.addToText(iwrb.getLocalizedString("handicap.handicap_help", "Contact your club to get your handicap."));

			noTable.add(texti);
			add(noTable);
		}
	}

	private void drawTable(IWContext iwc) throws IOException, SQLException, FinderException {
		IWTimestamp date = new IWTimestamp();
		date.setDay(1);
		date.setMonth(1);

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
		Union mainUnion = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(member.getMainUnionID());
		int clubOrder = memberInfo.getNumberOfRecords("select count(*) from union_,union_member,member_info where union_.union_id='" + mainUnion.getID() + "' and union_.union_id=union_member.union_id and union_member.member_id=member_info.member_id and handicap<'" + member.getHandicap() + "'") + 1;
		int numberOfRounds = getScorecardBusiness(iwc).getNumberOfRoundsAfterDate(Integer.parseInt(iMemberID), date.getDate());
		double pointsAverage = getScorecardBusiness(iwc).getPointsAverage(Integer.parseInt(iMemberID));
		Scorecard lastRound = getScorecardBusiness(iwc).getLastPlayedRound(Integer.parseInt(iMemberID));
		Scorecard bestRound = getScorecardBusiness(iwc).getBestRoundAfterDate(Integer.parseInt(iMemberID), date.getDate());
		
		Text name = getHeader(iwrb.getLocalizedString("handicap.member_name", "Member name"));
		Text mainUnionText = getHeader(iwrb.getLocalizedString("handicap.union_name", "Club name"));
		Text cardTotal = getHeader(iwrb.getLocalizedString("handicap.rounds_played", "Number of rounds played this year"));
		Text scoreText = getHeader(iwrb.getLocalizedString("handicap.last_round", "Last round played"));
		Text points = getHeader(iwrb.getLocalizedString("handicap.best_round", "Best round played this year"));
		Text averagepoints = getHeader(iwrb.getLocalizedString("handicap.average", "Average sum of points"));
		Text clubOrderText = getHeader(iwrb.getLocalizedString("handicap.club_ranking", "Club ranking"));

		Text memberText = getText(member.getName());
		Text unionText = getText(mainUnion.getAbbrevation() + " - " + mainUnion.getName());

		String cardText = String.valueOf(numberOfRounds);
		String noRounds = iwrb.getLocalizedString("handicap.no_round", "No rounds registered");
		Text cardTotalText = getText(cardText);
		if (numberOfRounds > 0) {
			if (cardText.substring(cardText.length() - 1, cardText.length()).equals("1")) {
				cardTotalText.addToText(" " + iwrb.getLocalizedString("handicap.round", "round"));
			}
			else {
				cardTotalText.addToText(" " + iwrb.getLocalizedString("handicap.rounds", "rounds"));
			}
		}
		if (numberOfRounds < 1) {
			cardTotalText = getText(noRounds);
		}

		Text scoreCardsText = getText(noRounds);
		if (lastRound  != null) {
			IWTimestamp scoreTime = new IWTimestamp(lastRound.getScorecardDate());
			Field field = lastRound.getField();
			scoreCardsText = getText(scoreTime.getLocaleDate(iwc.getCurrentLocale()) + "  -  " + field.getName());
		}
		else {
			scoreCardsText = getText(noRounds);
		}

		Text pointsText;
		if (bestRound != null) {
			IWTimestamp scoreTime = new IWTimestamp(bestRound.getScorecardDate());
			Field field = bestRound.getField();
			pointsText = getText(String.valueOf(bestRound.getTotalPoints()) + " " + iwrb.getLocalizedString("handicap.points", "points") + "  -  " + field.getName() + ", " + scoreTime.getLocaleDate(iwc.getCurrentLocale()));
		}
		else {
			pointsText = getText(noRounds);
		}

		Text averageText;
		if (pointsAverage > 0) {
			String averagePoints = TextSoap.decimalFormat(pointsAverage, 2);
			averageText = getText(averagePoints + " " + iwrb.getLocalizedString("handicap.points", "points"));
		}
		else {
			averageText = getText(noRounds);
		}

		Text clubText = getText("" + clubOrder);
		if ((int) memberInfo.getHandicap() == 100) {
			clubText = getText(iwrb.getLocalizedString("handicap.no_handicap", "No handicap"));
		}
		if (clubOrder == 0) {
			clubText = getText(iwrb.getLocalizedString("handicap.no_club", "Not registered"));
		}

		GenericButton selectMember = getButton(new GenericButton("select_member", iwrb.getLocalizedString("handicap.select", "Select member")));
		selectMember.setWindowToOpen(HandicapFindMember.class);
		
		Table textTable = new Table();
		textTable.setCellpadding(0);
		textTable.setCellspacing(0);
		int row = 1;

		textTable.add(name, 1, row++);
		textTable.add(memberText, 1, row);
		if (isAdmin) {
			textTable.add(getSmallText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE), 1, row);
			textTable.add(selectMember, 1, row);
		}
		row++;
		textTable.setHeight(row++, 12);
		
		textTable.add(mainUnionText, 1, row++);
		textTable.add(unionText, 1, row++);
		textTable.setHeight(row++, 12);
		
		textTable.add(cardTotal, 1, row++);
		textTable.add(cardTotalText, 1, row++);
		textTable.setHeight(row++, 12);
		
		textTable.add(scoreText, 1, row++);
		textTable.add(scoreCardsText, 1, row++);
		textTable.setHeight(row++, 12);
		
		textTable.add(points, 1, row++);
		textTable.add(pointsText, 1, row++);
		textTable.setHeight(row++, 12);
		
		textTable.add(averagepoints, 1, row++);
		textTable.add(averageText, 1, row++);
		textTable.setHeight(row++, 12);
		
		textTable.add(clubOrderText, 1, row++);
		textTable.add(clubText, 1, row);
		
		add(textTable);
	}
	
	private ScorecardBusiness getScorecardBusiness(IWApplicationContext iwac) {
		try {
			return (ScorecardBusiness) IBOLookup.getServiceInstance(iwac, ScorecardBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}