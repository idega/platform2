/*
 * Created on 26.4.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.business.GenericMemberComparator;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.util.Collections;
import java.util.List;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;


/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentParticipantsList extends GolfBlock {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();
		
		Tournament tournament = getTournamentSession(modinfo).getTournament();
		List members = getTournamentBusiness(modinfo).getMembersInTournamentList(tournament);
		Member member;

		if (members != null) {
			Collections.sort(members, new GenericMemberComparator(GenericMemberComparator.FIRSTLASTMIDDLE));
			Text nameString = getSmallHeader(iwrb.getLocalizedString("tournament.name", "Name"));
			Text handicapString = getSmallHeader(iwrb.getLocalizedString("tournament.handicap", "Handicap"));
			Text abbString = getSmallHeader(iwrb.getLocalizedString("tournament.club", "Club"));

			int union_id = -1;
			Union union;
			String abbrevation = "-";

			float handicap = 0;
			Text tName;
			Text abb;
			Text tHandicap;
			Text tTHandicap;

			String headerColor = getHeaderColor();
			String darkColor = getZebraColor2();
			String lightColor = getZebraColor1();

			Table table2 = new Table();
			table2.setCellpadding(0);
			table2.setCellspacing(0);
			table2.setWidth("100%");

			table2.add(nameString, 1, 1);
			table2.add(abbString, 2, 1);
			table2.add(handicapString, 3, 1);
			table2.add(nameString, 4, 1);
			table2.add(abbString, 5, 1);
			table2.add(handicapString, 6, 1);
			table2.setAlignment(2, 1, "center");
			table2.setAlignment(3, 1, "center");
			table2.setAlignment(5, 1, "center");
			table2.setAlignment(6, 1, "center");
			table2.setHeight(1, "15");
			table2.setRowStyleClass(1, getHeaderRowClass());

			int half = (int) (members.size() + 1) / 2;
			int column = 1;
			int row = 1;
			int zebraRow = 1;

			for (int i = 0; i < members.size(); i++) {
				if (i == half) {
					column = 4;
					row = 2;
				}
				else {
					++row;
				}
				table2.setHeight(row, 10);
				member = (Member) members.get(i);
				//                    member = new
				// TournamentParticipants().getTournamentParticipants(((Member)
				// members.get(i)).getID(),tournament.getID() );

				handicap = member.getHandicap();
				tName = getSmallText(member.getName());

				//union_id = member.getMainUnionID();
				union_id = tournament.getTournamentMemberUnionId(member);
				abbrevation = union_id + "";

				if (union_id != -1) {
					union = GolfCacher.getCachedUnion(union_id);
					//union = new Union(union_id);
					abbrevation = union.getAbbrevation();
				}
				else {
					abbrevation = "-";
				}

				abb = getSmallText(abbrevation);
				tHandicap = getSmallText(TextSoap.singleDecimalFormat(handicap));

				table2.add(tName, column, row);
				table2.add(abb, column + 1, row);
				table2.add(tHandicap, column + 2, row);

				if (member.getGender().equalsIgnoreCase("M")) {
					if (handicap > tournament.getMaxHandicap()) {
						tTHandicap = getSmallText(" (" + tournament.getMaxHandicap() + ")");
						table2.add(tTHandicap, column + 2, row);
					}
				}
				else {
					if (handicap > tournament.getFemaleMaxHandicap()) {
						tTHandicap = getSmallText(" (" + tournament.getFemaleMaxHandicap() + ")");
						table2.add(tTHandicap, column + 2, row);
					}
				}

				table2.setAlignment(column, row, "left");
				table2.setAlignment(column + 1, row, "center");
				table2.setAlignment(column + 2, row, "center");
				
				table2.setRowPadding(row, getCellpadding());
				if (zebraRow % 2 != 0) {
					table2.setRowStyleClass(row, getLightRowClass());
				}
				else {
					table2.setRowStyleClass(row, getDarkRowClass());
				}
				zebraRow++;
			} // for ends
			
			row = table2.getRows() + 1;

			table2.mergeCells(1, row, 6, row);
			table2.setAlignment(1, row, "left");
			table2.setCellpadding(1, row, 6);
			Text notice = getHeader(iwrb.getLocalizedString("tournament.handicap_comment", ""));
			table2.add(notice, 1, row);

			add(table2);
		}
		else {
			add(getHeader(iwrb.getLocalizedString("tournament.nobody_regstered", "Nobody is registered in the tournament")));
		}
	}

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}