/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class ChangeGroup extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		String mode = modinfo.getParameter("mode");
		if (mode == null) {
			mode = "";
		}
		String tournament_id = modinfo.getParameter("tournament_id");
		String member_id = modinfo.getParameter("member_id");

		Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
		Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));

		if (mode.equals("")) {

			Table myTable = new Table(2, 3);
			myTable.mergeCells(1, 1, 2, 1);
			myTable.mergeCells(1, 2, 2, 2);
			myTable.setAlignment("center");
			myTable.setAlignment(1, 2, "center");

			Form myForm = new Form();
			myForm.add(new HiddenInput("tournament_id", tournament_id));
			myForm.add(new HiddenInput("member_id", member_id));
			myForm.add(new HiddenInput("mode", "submit"));

			DropdownMenu menu = new DropdownMenu(TournamentController.getTournamentGroups(member, tournament));
			menu.setSelectedElement("" + tournament.getTournamentGroupId(member));

			myTable.addText(localize("tournament.choose_group","Choose group")+":", 1, 1);
			myTable.add(menu, 1, 2);
			myTable.add(new SubmitButton(localize("tournament.confirm","Confirm")), 2, 3);
			myTable.add(new CloseButton(localize("tournament.cancel","Cancel")), 1, 3);

			myForm.add(myTable);
			Text breakText = new Text("<br>");

			add(breakText);
			add(myForm);
		}

		else if (mode.equals("submit")) {

			String tournament_group_id = modinfo.getParameter("tournament_group");
			TournamentGroup tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_id));

			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct slope,course_rating from tee where field_id = " + tournament.getFieldId() + " and tee_color_id = " + tGroup.getTeeColorID());

			if (tee.length > 0) {
				int newSlope = tee[0].getSlope();
				float newCR = tee[0].getCourseRating();
				int teeColorID = tGroup.getTeeColorID();
				IWTimestamp stampur;
				int errorChecker = 0;

				Scorecard[] scorecards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select s.* from scorecard s, tournament_round tr, tournament t where t.tournament_id = " + tournament.getID() + " and tr.tournament_id = t.tournament_id and tr.tournament_round_id = s.tournament_round_id and s.member_id = " + member.getID() + "");

				for (int i = 0; i < scorecards.length; i++) {
					errorChecker = setTournamentGroupId(tournament, member, tGroup.getID());
					if (errorChecker == 1) {
						scorecards[i].setSlope(newSlope);
						scorecards[i].setCourseRating(newCR);
						scorecards[i].setTeeColorID(teeColorID);

						stampur = new IWTimestamp();
						if (scorecards[i].getScorecardDate() != null) {
							stampur = new IWTimestamp(scorecards[i].getScorecardDate());
						}
						else {
							stampur = new IWTimestamp(tournament.getStartTime());
							stampur.addDays(-2);
						}

						scorecards[i].update();

						UpdateHandicap.update(scorecards[i].getMemberId());
					}
					else {
					}
				}

			}

			getParentPage().setParentToReload();
			getParentPage().close();

		}
	}

	public int setTournamentGroupId(Tournament tournament, Member member, int tournament_group_id) throws SQLException {
		Connection conn = null;
		Statement Stmt = null;
		int returner = 1;
		try {
			conn = tournament.getConnection();
			Stmt = conn.createStatement();

			returner = Stmt.executeUpdate("UPDATE tournament_member set TOURNAMENT_GROUP_ID = " + tournament_group_id + " where TOURNAMENT_ID = " + tournament.getID() + " AND MEMBER_ID =" + member.getID());

		}
		finally {
			if (Stmt != null) {
				Stmt.close();
			}
			if (conn != null) {
				freeConnection(conn);
			}
		}

		return returner;
	}

}