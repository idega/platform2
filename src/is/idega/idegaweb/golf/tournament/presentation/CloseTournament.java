/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.DisplayScores;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentTour;
import is.idega.idegaweb.golf.entity.TournamentTourHome;
import is.idega.idegaweb.golf.entity.TournamentTourMember;
import is.idega.idegaweb.golf.entity.TournamentTourMemberHome;
import is.idega.idegaweb.golf.entity.TournamentTourMemberPK;
import is.idega.idegaweb.golf.entity.TournamentTournamentTour;
import is.idega.idegaweb.golf.entity.TournamentTournamentTourHome;
import is.idega.idegaweb.golf.entity.TournamentTournamentTourPK;
import is.idega.idegaweb.golf.tournament.business.ResultComparator;
import is.idega.idegaweb.golf.tournament.business.ResultDataHandler;
import is.idega.idegaweb.golf.tournament.business.ResultsCollector;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class CloseTournament extends TournamentBlock {

	IWResourceBundle iwrb;

	protected boolean tournamentMustBeSet() {
		return true;
	}

	public void main(IWContext modinfo) throws Exception {
		super.setAdminView(TournamentAdministratorWindow.ADMIN_VIEW_FINISH_TOURNAMENT);
		iwrb = getResourceBundle();

		String mode = modinfo.getParameter("mode");
		if (mode == null)
			mode = "";

		String tournament_id = null;
		if (getTournamentID(modinfo) > 0) {
			tournament_id = Integer.toString(getTournamentID(modinfo));
		}

		if (tournament_id != null) {
			Tournament tournament = null;
			try {
				tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}

			if (mode.equalsIgnoreCase("")) {
				main(tournament, modinfo);
			}

			if (mode.equals("select")) {
				main2(tournament, modinfo);
			}

			if (mode.equals("update_handicap")) {
				main3(tournament, modinfo);
			}

		}

	}

	public void getTournaments(IWContext modinfo) throws NumberFormatException, RemoteException {

		DropdownMenu menu = null;
		GenericButton submit = getButton(new SubmitButton(localize("tournament.continue","Continue")));

		IWTimestamp stamp = IWTimestamp.RightNow();
		String selectedYear = modinfo.getParameter("tr_year");
		if (selectedYear == null) {
			selectedYear = Integer.toString(stamp.getYear());
		}

		DropdownMenu yearMenu = new DropdownMenu("tr_year");
		for (int i = 2001; i <= stamp.getYear(); i++) {
			yearMenu.addMenuElement(i, Integer.toString(i));
		}
		yearMenu.setSelectedElement(selectedYear);
		yearMenu.setToSubmit();

		Form myForm = new Form();
		myForm.setMethod("get");

		Table myTable = new Table(2, 3);
		myTable.setAlignment("center");
		myTable.setAlignment(2, 1, "right");
		myTable.setAlignment(1, 2, "center");
		myTable.setAlignment(1, 3, "right");
		myTable.setCellpadding(4);

		//	add("using year : "+selectedYear);
		menu = getTournamentBusiness(modinfo).getDropdownOrderedByUnion(new DropdownMenu("tournament"), modinfo, Integer.parseInt(selectedYear));
		menu.setMarkupAttribute("size", "10");

		Text tournText = new Text(iwrb.getLocalizedString("tournament.choose_tournament", "Choose tournament") + ":");
		tournText.setFontSize(3);
		tournText.setBold();

		myTable.mergeCells(1, 2, 2, 2);
		myTable.mergeCells(1, 3, 2, 3);
		myTable.add(yearMenu, 2, 1);
		myTable.add(tournText, 1, 1);
		myTable.add(menu, 1, 2);
		myTable.add(submit, 1, 3);

		myForm.add(myTable);
		add("<br>");
		add(myForm);

	}

	public void main(Tournament tournament, IWContext modinfo) throws IOException, SQLException {
		TournamentRound[] rounds = tournament.getTournamentRounds();

		int holes = rounds.length * tournament.getNumberOfHoles();

		DisplayScores[] members = getTournamentBusiness(modinfo).getDisplayScores("t.tournament_id = " + tournament.getID(), "m.member_id", "having count(stroke_count) < " + holes);

		Form myForm = new Form();
		myForm.add(new HiddenInput("mode", "select"));
		myForm.add(new HiddenInput("tournament", ""+tournament.getID()));

		Table myTable = new Table();
		myTable.mergeCells(1, 1, 2, 1);
		myTable.setCellpadding(3);

		//if ( members.length == 0 ) {

		Text selectText = new Text(iwrb.getLocalizedString("tournament.choose_calculation", "Choose how to calculate each round"));
		selectText.setFontSize(3);
		selectText.setBold();
		myTable.add(selectText, 1, 1);

		for (int a = 0; a < rounds.length; a++) {

			DropdownMenu menu = new DropdownMenu("round_" + (a + 1));
			if (!tournament.getTournamentType().getUseGroups()) {
				menu.addMenuElement(0, iwrb.getLocalizedString("tournament.handicap_increase_decrease", "Handicap to increase and decrease"));
				menu.addMenuElement(1, iwrb.getLocalizedString("tournament.handicap_decrease", "Handicap to decrease"));
				menu.addMenuElement(2, iwrb.getLocalizedString("tournament.handicap_increase", "Handicap to increase"));
			}
			menu.addMenuElement(3, iwrb.getLocalizedString("tournament.handicap_no_effect", "No change in handicap"));

			Text roundText = new Text(iwrb.getLocalizedString("tournament.round", "Round") + " " + rounds[a].getRoundNumber() + ":");

			myTable.add(roundText, 1, a + 2);
			myTable.add(menu, 2, a + 2);
			myTable.setAlignment(1, a + 2, "right");
		}

		int rows = myTable.getRows();
		myTable.mergeCells(1, rows + 1, 2, rows + 1);
		myTable.setAlignment(1, rows + 1, "right");
		myTable.add(getButton(new SubmitButton(localize("tournament.continue","Continue"))), 1, rows + 1);

		myForm.add(myTable);
		add(myForm);

	}

	public void main2(Tournament tournament, IWContext modinfo) throws IOException, SQLException {
		TournamentRound[] rounds = tournament.getTournamentRounds();

		Form myForm = new Form();
		myForm.add(new HiddenInput("mode", "update_handicap"));
		myForm.add(new HiddenInput("tournament", ""+tournament.getID()));

		Table myTable = new Table();
		myTable.setCellpadding(3);
		myTable.setAlignment(1, 3, "right");
		myTable.setWidth("60%");

		Member member;
		boolean tooHigh = false;
		float useThis = 0;

		for (int a = 0; a < rounds.length; a++) {

			String option = modinfo.getParameter("round_" + (a + 1));

			boolean increase = true;
			boolean decrease = true;

			if (option.equals("0")) {
				increase = true;
				decrease = true;

				Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where tournament_round_id = " + rounds[a].getID() + " and scorecard_date is not null");

				for (int b = 0; b < scorecard.length; b++) {
					scorecard[b].setUpdateHandicap(true);
					scorecard[b].update();
				}
			}

			else if (option.equals("1")) {
				increase = false;
				decrease = true;

				Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where total_points>36 and tournament_round_id=" + rounds[a].getID() + " and scorecard_date is not null");
				Scorecard[] scorecard2 = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where total_points<=36 and tournament_round_id=" + rounds[a].getID() + " and scorecard_date is not null");

				for (int b = 0; b < scorecard.length; b++) {
					scorecard[b].setUpdateHandicap(true);
					scorecard[b].update();
				}
				for (int b = 0; b < scorecard2.length; b++) {
					scorecard2[b].setUpdateHandicap(false);
					scorecard2[b].update();
				}
			}

			else if (option.equals("2")) {
				increase = true;
				decrease = false;

				Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where total_points<=36 and tournament_round_id=" + rounds[a].getID() + " and scorecard_date is not null");
				Scorecard[] scorecard2 = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where total_points>36 and tournament_round_id=" + rounds[a].getID() + " and scorecard_date is not null");

				for (int b = 0; b < scorecard.length; b++) {
					scorecard[b].setUpdateHandicap(true);
					scorecard[b].update();
				}
				for (int b = 0; b < scorecard2.length; b++) {
					scorecard2[b].setUpdateHandicap(false);
					scorecard2[b].update();
				}
			}

			else if (option.equals("3")) {
				increase = false;
				decrease = false;

				Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where tournament_round_id = " + rounds[a].getID() + " and scorecard_date is not null");

				for (int b = 0; b < scorecard.length; b++) {
					scorecard[b].setUpdateHandicap(false);
					scorecard[b].update();
				}
			}

			rounds[a].setDecreaseHandicap(decrease);
			rounds[a].setIncreaseHandicap(increase);
			rounds[a].update();

		}

		Text handicapText = new Text(iwrb.getLocalizedString("tournament.to_update", "Update golfers handicap") + ":");
		handicapText.setFontSize(3);
		handicapText.setBold();

		myTable.add(handicapText, 1, 1);
		myTable.addText(iwrb.getLocalizedString("tournament.update_warning", "Handicap update could take several moments."), 1, 2);
		myTable.add(getButton(new SubmitButton(localize("tournament.continue","Continue"), "calculate")), 1, 3);

		myForm.add(myTable);
		add(myForm);

	}

	public void main3(Tournament tournament, IWContext modinfo) throws IOException, SQLException {
		IWTimestamp stampur = new IWTimestamp(tournament.getStartTime());
		stampur.addDays(-2);

		boolean calculate = true;
		/*
		String query = modinfo.getParameter("calculate");
		if (query == null) {
			calculate = false;
		}
		 */
		DisplayScores[] members = getTournamentBusiness(modinfo).getDisplayScores("t.tournament_id = " + tournament.getID() + " ", "m.member_id");

		if (calculate) {
			for (int a = 0; a < members.length; a++) {
				UpdateHandicap.update(members[a].getMemberID(), stampur);
			}
		}

		// :added by Gimmi 15.06.2001
		Timestamp stamp = IWTimestamp.getTimestampRightNow();
		tournament.setIsClosed(true);
		tournament.setClosedDate(stamp);
		tournament.update();
		getTournamentBusiness(modinfo).removeTournamentBoxApplication(modinfo);
		// :done


		// TournamentTour scoring...
		try {
			TournamentTourHome ttHome = (TournamentTourHome) IDOLookup.getHome(TournamentTour.class);
			TournamentTournamentTourHome tttHome = (TournamentTournamentTourHome) IDOLookup.getHome(TournamentTournamentTour.class);
			TournamentTourMemberHome ttmHome = (TournamentTourMemberHome) IDOLookup.getHome(TournamentTourMember.class);
			Collection tours = ttHome.findByTournamentID(tournament.getPrimaryKey());
			if (tours != null && !tours.isEmpty()) {
				Iterator iter = tours.iterator();
				while (iter.hasNext()) {
					int sorter = ResultComparator.TOTALSTROKES;
					TournamentTour tour = (TournamentTour) iter.next();
					TournamentTournamentTourPK pk = new TournamentTournamentTourPK(tournament.getPrimaryKey(), tour.getPrimaryKey());
					TournamentTournamentTour ttTour = tttHome.findByPrimaryKey(pk);
					int totalPoints = ttTour.getTotalScore();
					float[] points = tour.getScoreSystem().getPointsDivision();
					TournamentGroup[] groups = tournament.getTournamentGroups();
					int tournamentId_ = tournament.getID();
					
					Object tournamentID = tournament.getPrimaryKey();
					Object tournamentTourID = tour.getPrimaryKey();
					
					HashMap map = new HashMap();
					HashMap scores = new HashMap();
					
					int tournamentType_ = tournament.getTournamentTypeId();
//					ResultDataHandler handler = new ResultDataHandler(tournamentId_, tournamentType_, tournamentGroupId_, tournamentRounds_, gender_);
					for (int i = 0; i < groups.length; i++) {
						map = new HashMap();
						scores = new HashMap();
						Object tournamentGroupID = groups[i].getPrimaryKey();
						ResultDataHandler handler = new ResultDataHandler(tournamentId_, sorter, groups[i].getID(), null, null);
						Vector result = handler.getTournamentMembers();
						ResultComparator comparator = new ResultComparator(sorter);
//						ResultComparator comparator = new ResultComparator(tournamentType_);
						Collections.sort(result, comparator);
						Iterator mIter = result.iterator();
						int counter = 0;
						boolean cont = true;
						int length = points.length;
						while (mIter.hasNext() && cont) {
							ResultsCollector r = (ResultsCollector) mIter.next();
							float score = 0;
							if (counter < length) {
								score = (float) totalPoints * (points[counter] / (float) 100);
							}
							if (r.getDismissal() > 0) {
								System.out.println("[CloseTournament : Member "+r.getName()+" has a dismissal");
								continue;
							}
							System.out.println("[CloseTournament : Member "+r.getName()+" receives the score : "+score);
							Object memberID = new Integer(r.getMemberId());
							TournamentTourMemberPK mPK = new TournamentTourMemberPK(tournamentID, tournamentTourID, tournamentGroupID, memberID);
							TournamentTourMember ttMember = null;
							try {
								 ttMember = ttmHome.findByPrimaryKey(mPK);
							} catch (FinderException f) {
								ttMember = ttmHome.create(mPK);
							}
							ttMember.setScore(score);
							ttMember.store();
							
							Collection coll = (Collection) map.get(new Integer(r.getTotalStrokes()));							
							if (coll == null) {
								// FIRST PLAYER WITH WITH SCORE.
								if (counter < length) {
									coll = new Vector();
									coll.add(ttMember);
									scores.put(new Integer(r.getTotalStrokes()), new Float(score));
									map.put(new Integer(r.getTotalStrokes()), coll);
								} else {
									ttMember.remove();
									cont = false;
								}
							} else {
								// OTHER PLAYER WITH SAME SCORE...
								coll.add(ttMember);
								float totalScore = score + ((Float) scores.get(new Integer(r.getTotalStrokes()))).floatValue();
								scores.put(new Integer(r.getTotalStrokes()), new Float(totalScore));
								int numOfPlayers = coll.size();
								Iterator pIter = coll.iterator();
								float newScore = totalScore / (float) numOfPlayers;
								while (pIter.hasNext()) {
									TournamentTourMember rr = (TournamentTourMember) pIter.next();
									rr.setScore(newScore);
									System.out.println("[CloseTournament : Resetting member "+rr.getMember().getName()+" receives the score : "+newScore);
									rr.store();
								}
							}
							++counter;
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		// TournamentTour scoring done

		Table myTable = new Table(1, 3);
		myTable.setCellpadding(3);
		myTable.setWidth("90%");

		Text selectText = new Text(iwrb.getLocalizedString("tournament.tournament_results", "Results"));
		selectText.setFontSize(3);
		selectText.setBold();
		myTable.add(selectText, 1, 1);

		ResultsViewer results = new ResultsViewer(tournament.getID());

		myTable.setAlignment(1, 3, "right");
		myTable.add(getButton(new CloseButton()), 1, 3);
		myTable.add(results, 1, 2);

		add(myTable);

	}

}