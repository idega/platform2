package is.idega.idegaweb.golf.tournament.presentation;

/**
 * Title: ResultsViewer Description: Displayes the results of a tournament
 * Copyright: Copyright (c) 2001 Company: idega co.
 * 
 * @author Laddi
 * @version 1.3
 */

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.entity.TournamentType;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.ResultComparator;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

public class ResultsViewer extends GolfBlock {

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";

	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	private int tournamentID = 0;
	private int tournamentGroupID = -1;
	private int[] tournamentRounds = null;

	private String gender = null;

	private int orderBy = -1;
	private int sortBy = -1;

	private boolean showAllGroups = false;
	private boolean showAllGenders = false;
	private boolean championship = false;

	private Tournament tournament;

	private Form myForm;
	private Table outerTable;
	private Table formTable;
	private Table resultTable;

	public ResultsViewer() {
	}

	public ResultsViewer(int tournamentID) {
		this.tournamentID = tournamentID;
	}

	public void main(IWContext modinfo) throws Exception {
		try {
			if (tournamentID > 0) {
				getTournamentSession(modinfo).setTournamentID(tournamentID);
			}
			
			myForm = new Form();
			myForm.setName("resultform");
			iwrb = getResourceBundle(modinfo);
			tournament = getTournamentSession(modinfo).getTournament();
			tournamentID = getTournamentSession(modinfo).getTournamentID();
			if (tournament.getNumberOfRounds() >= 4) {
				championship = true;
			}

			add(getResult(modinfo));
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private Form getResult(IWContext modinfo) {
		try {
			getFormValues(modinfo);
			getOuterTable();

			myForm.add(outerTable);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return myForm;
	}

	private void getFormValues(IWContext modinfo) {
		String tournamentGroupId_ = modinfo.getParameter("tournament_group_id");
		if (tournamentGroupId_ != null) {
			if (tournamentGroupId_.length() > 0) {
				int tournamentGroupId = Integer.parseInt(tournamentGroupId_);
				if (tournamentGroupId > 0) {
					tournamentGroupID = Integer.parseInt(tournamentGroupId_);
				}
				else {
					tournamentGroupID = -1;
					showAllGroups = true;
				}
			}
		}

		String tournamentRounds_ = modinfo.getParameter("tournament_round_id");
		if (tournamentRounds_ != null) {
			if (tournamentRounds_.length() > 0) {
				if (Integer.parseInt(tournamentRounds_) != 0) {
					String[] query = null;

					try {
						TournamentRound round = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournamentRounds_));
						String queryString = "select tournament_round_id from tournament_round where tournament_id = " + Integer.toString(tournamentID) + " and round_number <= " + Integer.toString(round.getRoundNumber()) + " order by round_number";
						query = SimpleQuerier.executeStringQuery(queryString);
					}
					catch (Exception e) {
						e.printStackTrace(System.err);
					}

					if (query != null) {
						tournamentRounds = new int[query.length];
						for (int a = 0; a < query.length; a++) {
							tournamentRounds[a] = Integer.parseInt(query[a]);
						}
					}
				}
			}
		}

		String gender_ = modinfo.getParameter("gender");
		if (gender_ != null) {
			if (gender_.equalsIgnoreCase("f") || gender_.equalsIgnoreCase("m")) {
				gender = gender_.toUpperCase();
			}
			if (gender_.equalsIgnoreCase("b") && !showAllGroups) {
				showAllGenders = true;
			}
		}

		String sort_ = modinfo.getParameter("sort");
		if (sort_ != null) {
			if (sort_.length() > 0) {
				sortBy = Integer.parseInt(sort_);
			}
			else {
				sortBy = this.getTournamentType();
			}
		}
		else {
			sortBy = this.getTournamentType();
		}

		String order_ = modinfo.getParameter("order");
		if (order_ != null) {
			if (order_.length() > 0) {
				if (order_.equalsIgnoreCase("0")) {
					orderBy = sortBy;
				}
				else {
					orderBy = Integer.parseInt(order_);
				}
			}
			else {
				orderBy = sortBy;
			}
		}
		else {
			orderBy = sortBy;
		}
	}

	private void getOuterTable() {
		outerTable = new Table(1, 3);
		outerTable.setWidth("100%");
		outerTable.setCellpadding(0);
		outerTable.setCellspacing(0);
		outerTable.setHeight(1, 40);
		outerTable.setCellpaddingRight(1, 1, 5);
		outerTable.setVerticalAlignment(1, 2, "top");

		getFormTable();
		getResultsTable();
		//addExcelLink();
		getHoleByHole();

		outerTable.add(formTable, 1, 1);
		outerTable.add(resultTable, 1, 2);
	}

	private void addExcelLink() {
		Link excelLink = new Link(new Image("/reports/pics/xls.gif"));
		excelLink.addParameter("xls", "true");
		excelLink.addParameter("tournament_id", tournamentID);
		excelLink.addParameter("gender", gender);
		excelLink.addParameter("tournament_group_id", tournamentGroupID);
		if (tournamentRounds != null) {
			for (int i = 0; i < tournamentRounds.length; i++) {
				excelLink.addParameter("tournament_round_id", tournamentRounds[i]);
			}
		}
		excelLink.addParameter("order", orderBy);
		excelLink.addParameter("sort", sortBy);
		outerTable.setHeight(3, 40);
		outerTable.setCellpaddingRight(1, 3, 5);
		outerTable.add(excelLink, 1, 3);
	}

	private void getFormTable() {
		formTable = new Table(6, 1);
		formTable.setAlignment("right");
		formTable.setCellpadding(2);
		formTable.setCellspacing(0);

		DropdownMenu genderMenu = (DropdownMenu) getStyledInterface(new DropdownMenu("gender"));
		genderMenu.addMenuElement("", "- " + iwrb.getLocalizedString("tournament.genders", "Genders") + " -");
		genderMenu.addMenuElement("M", iwrb.getLocalizedString("tournament.males", "Male"));
		genderMenu.addMenuElement("F", iwrb.getLocalizedString("tournament.females", "Female"));
		genderMenu.addMenuElement("B", iwrb.getLocalizedString("tournament.both", "Both"));
		genderMenu.keepStatusOnAction();

		DropdownMenu groupsMenu = (DropdownMenu) getStyledInterface(new DropdownMenu("tournament_group_id"));
		groupsMenu.addMenuElement("", "- " + iwrb.getLocalizedString("tournament.groups", "Groups") + " -");

		TournamentGroup[] groups = null;
		try {
			groups = tournament.getTournamentGroups();
			for (int a = 0; a < groups.length; a++) {
				groupsMenu.addMenuElement(groups[a].getID(), groups[a].getName());
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		groupsMenu.addMenuElement("0", iwrb.getLocalizedString("tournament.all", "All"));
		groupsMenu.keepStatusOnAction();

		String roundShort = iwrb.getLocalizedString("tournament.round", "Round");
		if (tournament.getNumberOfRounds() > 4) {
			roundShort = iwrb.getLocalizedString("tournament.day", "Day");
		}

		String round = iwrb.getLocalizedString("tournament.rounds", "Rounds");
		if (tournament.getNumberOfRounds() > 4) {
			round = iwrb.getLocalizedString("tournament.days", "Days");
		}

		DropdownMenu roundsMenu = (DropdownMenu) getStyledInterface(new DropdownMenu("tournament_round_id"));
		roundsMenu.addMenuElement("", "- " + round + " -");

		TournamentRound[] rounds = null;
		try {
			rounds = tournament.getTournamentRounds();
			for (int a = 0; a < rounds.length; a++) {
				roundsMenu.addMenuElement(rounds[a].getID(), Integer.toString(a + 1) + ". " + roundShort);
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		roundsMenu.addMenuElement("0", iwrb.getLocalizedString("tournament.all", "All"));
		roundsMenu.keepStatusOnAction();

		DropdownMenu scoreMenu = (DropdownMenu) getStyledInterface(new DropdownMenu("sort"));
		scoreMenu.addMenuElement("", "- " + iwrb.getLocalizedString("tournament.score", "Score") + " -");
		scoreMenu.addMenuElement(ResultComparator.TOTALSTROKES, iwrb.getLocalizedString("tournament.strokes_without_handicap", "Strokes"));
		scoreMenu.addMenuElement(ResultComparator.TOTALSTROKESWITHHANDICAP, iwrb.getLocalizedString("tournament.strokes_with_handicap", "Strokes w/handicap"));
		scoreMenu.addMenuElement(ResultComparator.TOTALPOINTS, iwrb.getLocalizedString("tournament.points", "Points"));
		scoreMenu.keepStatusOnAction();

		DropdownMenu orderMenu = (DropdownMenu) getStyledInterface(new DropdownMenu("order"));
		orderMenu.addMenuElement("", "- " + iwrb.getLocalizedString("tournament.order", "Order") + " -");
		orderMenu.addMenuElement(0, iwrb.getLocalizedString("tournament.by_score", "By score"));
		orderMenu.addMenuElement(ResultComparator.NAME, iwrb.getLocalizedString("tournament.by_name", "By name"));
		orderMenu.addMenuElement(ResultComparator.ABBREVATION, iwrb.getLocalizedString("tournament.by_club", "By club"));
		orderMenu.keepStatusOnAction();

		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(iwrb.getLocalizedString("tournament.get", "Get")));

		formTable.add(genderMenu, 1, 1);
		formTable.add(groupsMenu, 2, 1);
		formTable.add(roundsMenu, 3, 1);
		formTable.add(scoreMenu, 4, 1);
		formTable.add(orderMenu, 5, 1);
		formTable.add(submit, 6, 1);

	}

	private void getResultsTable() {
		try {
			resultTable = new Table(1, 1);
			resultTable.setBorder(0);
			resultTable.setCellpadding(0);
			resultTable.setCellspacing(0);
			resultTable.setWidth("100%");
			resultTable.setVerticalAlignment(1, 1, "top");

			if (showAllGroups || showAllGenders) {
				if (showAllGroups) {
					String genderString = "";
					if (gender != null) {
						genderString = "and gender = '" + gender.toUpperCase() + "'";
					}

					TournamentGroup[] groups = (TournamentGroup[]) ((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class)).findAll("select tg.* from tournament_group tg, tournament_tournament_group ttg where tg.tournament_group_id = ttg.tournament_group_id " + genderString + " and tournament_id = " + Integer.toString(tournamentID) + " order by handicap_max");

					Table groupResultsTable = new Table();
					groupResultsTable.setWidth("100%");

					int row = 1;

					for (int a = 0; a < groups.length; a++) {
						Text groupName = getHeader(groups[a].getName());

						TournamentResults tournResults = new TournamentResults(tournamentID, sortBy, groups[a].getID(), tournamentRounds, gender);
						tournResults.sortBy(orderBy);

						groupResultsTable.add(groupName, 1, row);
						groupResultsTable.add(tournResults, 1, row + 1);
						row += 3;
					}
					resultTable.add(groupResultsTable, 1, 1);
				}

				if (showAllGenders) {
					String[] genders = getGenderInTournament();

					Table groupResultsTable = new Table();
					groupResultsTable.setWidth("100%");
					groupResultsTable.setCellpadding(0);
					groupResultsTable.setCellspacing(0);

					int row = 1;

					for (int a = 0; a < genders.length; a++) {
						Text genderName = new Text();
						genderName.setFontSize(3);
						genderName.setBold();

						if (genders[a].equalsIgnoreCase("m"))
							genderName.setText(iwrb.getLocalizedString("tournament.men", "Men"));
						else
							genderName.setText(iwrb.getLocalizedString("tournament.women", "Women"));

						TournamentResults tournResults = new TournamentResults(tournamentID, sortBy, tournamentGroupID, tournamentRounds, genders[a]);
						tournResults.sortBy(orderBy);

						groupResultsTable.add(genderName, 1, row);
						groupResultsTable.add(tournResults, 1, row + 1);
						row += 3;
					}
					resultTable.add(groupResultsTable, 1, 1);
				}
			}
			else {
				if (championship && tournamentGroupID == -1) {
					resultTable.setAlignment(1, 1, "center");
					resultTable.add(Text.getBreak());
					resultTable.add(Text.getBreak());
					String stringToDisplay = iwrb.getLocalizedString("tournament.select_each_group_for_results", "Select each group from the list above to see the resuls within that group");
					resultTable.add(stringToDisplay, 1, 1);
				}
				else {
					TournamentResults tournResults = new TournamentResults(tournamentID, sortBy, tournamentGroupID, tournamentRounds, gender);
					tournResults.sortBy(orderBy);

					resultTable.add(tournResults, 1, 1);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private String[] getGenderInTournament() {
		String[] query = null;
		try {
			String queryString = "select distinct gender from tournament t,tournament_group tg,tournament_tournament_group ttg where t.tournament_id = ttg.tournament_id and ttg.tournament_group_id = tg.tournament_group_id and t.tournament_id = " + Integer.toString(tournamentID);
			query = SimpleQuerier.executeStringQuery(queryString);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return query;
	}

	private int getTournamentType() {
		int tournamentType = ResultComparator.TOTALSTROKES;
		try {
			TournamentType type = tournament.getTournamentType();
			String typeName = type.getTournamentType();

			if (typeName.equalsIgnoreCase("points")) tournamentType = ResultComparator.TOTALPOINTS;
			if (orderBy == ResultComparator.TOTALPOINTS) tournamentType = ResultComparator.TOTALPOINTS;
			if (orderBy == ResultComparator.TOTALSTROKES) tournamentType = ResultComparator.TOTALSTROKES;
			if (orderBy == ResultComparator.TOTALSTROKESWITHHANDICAP) tournamentType = ResultComparator.TOTALSTROKESWITHHANDICAP;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return tournamentType;
	}

	private void getHoleByHole() {
		try {
			//if ( tournamentGroupID != -1 || showAllGroups) {
			int tournamentRoundID = 0;
			GenericButton scoreLink = getButton(new GenericButton("hole_view", iwrb.getLocalizedString("tournament.holeview_window", "Hole view")));
			scoreLink.setWindowToOpen(HoleView.class);
			scoreLink.addParameterToWindow("tournamentID", tournamentID);
			if (tournamentGroupID != -1) {
				scoreLink.addParameterToWindow("tournamentGroupID", tournamentGroupID);
			}
			else {
				TournamentGroup[] groups = tournament.getTournamentGroups();
				for (int a = 0; a < groups.length; a++) {
					scoreLink.addParameterToWindow("tournamentGroupID", groups[a].getID());
				}
			}

			if (tournament.getNumberOfRounds() == 1 && sortBy == ResultComparator.TOTALSTROKES) {
				TournamentRound[] rounds = tournament.getTournamentRounds();
				if (rounds.length > 0) {
					tournamentRoundID = rounds[0].getID();
				}
				scoreLink.addParameterToWindow("tournamentRoundID", tournamentRoundID);

				if (tournamentRoundID != 0) {
					outerTable.setAlignment(1, 3, "right");
					outerTable.setHeight(1, 3, "40");
					outerTable.setCellpaddingRight(1, 3, 5);
					outerTable.add(scoreLink, 1, 3);
				}
			}
			else if (tournament.getNumberOfRounds() > 1 && sortBy == ResultComparator.TOTALSTROKES) {
				if (tournamentRounds != null) {
					if (tournamentRounds.length > 0) {
						tournamentRoundID = tournamentRounds[tournamentRounds.length - 1];
						scoreLink.addParameterToWindow("tournamentRoundID", tournamentRoundID);

						if (tournamentRoundID != 0) {
							outerTable.setAlignment(1, 3, "right");
							outerTable.setHeight(1, 3, "40");
							outerTable.setCellpaddingRight(1, 3, 5);
							outerTable.add(scoreLink, 1, 3);
						}
					}
				}
				else {
					IWTimestamp date = new IWTimestamp();
					IWTimestamp date2 = new IWTimestamp();
					date2.addDays(1);

					TournamentRound[] rounds = (TournamentRound[]) ((TournamentRound) IDOLookup.instanciateEntity(TournamentRound.class)).findAll("select * from tournament_round where tournament_id = " + Integer.toString(tournamentID) + " and round_date >= '" + date.getSQLDate() + "' and round_date <= '" + date2.getSQLDate() + "'");
					if (rounds.length > 0) {
						tournamentRoundID = rounds[0].getID();
					}
					scoreLink.addParameterToWindow("tournamentRoundID", tournamentRoundID);

					if (tournamentRoundID != 0) {
						outerTable.setAlignment(1, 3, "right");
						outerTable.setHeight(1, 3, "40");
						outerTable.setCellpaddingRight(1, 3, 5);
						outerTable.add(scoreLink, 1, 3);
					}
				}
			}
			//}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
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
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}