package is.idega.idegaweb.golf.handicap.presentation;

/**
 * Title: Description: Copyright: idega Copyright (c) 2001 Company:
 * 
 * @author @version 1.0
 */

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

public class HandicapInfo extends GolfBlock {

	private String iMemberID;
	private boolean isAdmin = false;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	private Table myTable;

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

		if (!isAdmin) {
			modinfo.getSession().removeAttribute("member_id");
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

		drawTable(modinfo);
		add(myTable);
	}

	private void drawTable(IWContext iwc) throws IOException, SQLException, FinderException {
		myTable = new Table(3, 1);
		myTable.setCellpadding(6);
		myTable.setCellspacing(6);
		myTable.setWidth(Table.HUNDRED_PERCENT);
		myTable.setAlignment(1, 1, "center");
		myTable.setAlignment(2, 1, "center");
		myTable.setAlignment(3, 1, "right");
		myTable.setVerticalAlignment(1, 1, "top");
		myTable.setVerticalAlignment(2, 1, "top");
		myTable.setVerticalAlignment(3, 1, "top");

		IWTimestamp date = new IWTimestamp();
		date.setDay(1);
		date.setMonth(1);

		Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(iMemberID));
		MemberInfo memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(Integer.parseInt(iMemberID));
		Union mainUnion = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(member.getMainUnionID());
		int clubOrder = memberInfo.getNumberOfRecords("select count(*) from union_,union_member,member_info where union_.union_id='" + mainUnion.getID() + "' and union_.union_id=union_member.union_id and union_member.member_id=member_info.member_id and handicap<'" + member.getHandicap() + "'") + 1;
		Scorecard[] scoreCards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id='" + iMemberID + "' and handicap_correction='N' and scorecard_date is not null order by scorecard_date desc");
		Scorecard[] scoreCards2 = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id='" + iMemberID + "' and scorecard_date>='" + date.toSQLDateString() + "' and scorecard_date is not null and handicap_correction='N' order by total_points desc");

		Text name = getHeader(iwrb.getLocalizedString("handicap.member_name", "Member name"));
		Text mainUnionText = getHeader(iwrb.getLocalizedString("handicap.union_name", "Club name"));
		Text cardTotal = getHeader(iwrb.getLocalizedString("handicap.rounds_played", "Number of rounds played this year"));
		Text scoreText = getHeader(iwrb.getLocalizedString("handicap.last_round", "Last round played"));
		Text points = getHeader(iwrb.getLocalizedString("handicap.best_round", "Best round played this year"));
		Text averagepoints = getHeader(iwrb.getLocalizedString("handicap.average", "Average sum of points"));
		Text clubOrderText = getHeader(iwrb.getLocalizedString("handicap.club_ranking", "Club ranking"));

		Text memberText = getText(member.getName());
		Text unionText = getText(mainUnion.getAbbrevation() + " - " + mainUnion.getName());

		String cardText = String.valueOf(scoreCards.length);
		String noRounds = iwrb.getLocalizedString("handicap.no_round", "No rounds registered");
		Text cardTotalText = getText(cardText);
		if (scoreCards2.length > 0) {
			if (cardText.substring(cardText.length() - 1, cardText.length()).equals("1")) {
				cardTotalText.addToText(" " + iwrb.getLocalizedString("handicap.round", "round"));
			}
			else {
				cardTotalText.addToText(" " + iwrb.getLocalizedString("handicap.rounds", "rounds"));
			}
		}
		if (scoreCards2.length < 1) {
			cardTotalText = getText(noRounds);
		}

		Text scoreCardsText = getText(noRounds);
		if (scoreCards.length > 0) {
			IWTimestamp scoreTime = new IWTimestamp(scoreCards[0].getScorecardDate());
			Field fieldId = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(scoreCards[0].getFieldID());
			scoreCardsText = getText(scoreTime.getLocaleDate(iwc.getCurrentLocale()) + "  -  " + fieldId.getName());
		}
		else {
			scoreCardsText = getText(noRounds);
		}

		Text pointsText;
		if (scoreCards2.length > 0) {
			IWTimestamp scoreTime = new IWTimestamp(scoreCards2[0].getScorecardDate());
			Field fieldId = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(scoreCards2[0].getFieldID());
			pointsText = getText(String.valueOf(scoreCards2[0].getTotalPoints()) + " " + iwrb.getLocalizedString("handicap.points", "points") + "  -  " + fieldId.getName() + ", " + scoreTime.getLocaleDate(iwc.getCurrentLocale()));
		}
		else {
			pointsText = getText(noRounds);
		}

		Text averageText;
		if (scoreCards.length > 0) {
			float punktar = 0;
			for (int b = 0; b < scoreCards.length; b++) {
				punktar += (float) scoreCards[b].getTotalPoints();
			}
			String averagePoints = TextSoap.decimalFormat(String.valueOf((punktar / (float) scoreCards.length)), 2);
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
		textTable.setCellpadding(4);
		textTable.setCellspacing(4);

		textTable.add(name, 1, 1);
		textTable.addBreak(1, 1);
		textTable.add(memberText, 1, 1);
		if (isAdmin) {
			textTable.add(getSmallText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE), 1, 1);
			textTable.add(selectMember, 1, 1);
		}
		textTable.add(mainUnionText, 1, 2);
		textTable.addBreak(1, 2);
		textTable.add(unionText, 1, 2);
		textTable.add(cardTotal, 1, 3);
		textTable.addBreak(1, 3);
		textTable.add(cardTotalText, 1, 3);
		textTable.add(scoreText, 1, 4);
		textTable.addBreak(1, 4);
		textTable.add(scoreCardsText, 1, 4);
		textTable.add(points, 1, 5);
		textTable.addBreak(1, 5);
		textTable.add(pointsText, 1, 5);
		textTable.add(averagepoints, 1, 6);
		textTable.addBreak(1, 6);
		textTable.add(averageText, 1, 6);
		textTable.add(clubOrderText, 1, 7);
		textTable.addBreak(1, 7);
		textTable.add(clubText, 1, 7);

		myTable.add(new HandicapMemberInfo(), 1, 1);
		myTable.add(textTable, 2, 1);

		HandicapCard hcCard = new HandicapCard(Integer.parseInt(iMemberID));
		hcCard.setWidth("215");
		hcCard.addPrintLink(true);
		myTable.setVerticalAlignment(3, 1, "top");
		myTable.add(hcCard, 3, 1);
	}
}