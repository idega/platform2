/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Startingtime;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.moduleobject.GolfDialog;
import is.idega.idegaweb.golf.tournament.business.ResultComparator;
import is.idega.idegaweb.golf.tournament.business.ResultDataHandler;
import is.idega.idegaweb.golf.tournament.business.ResultsCollector;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author gimmi
 */
public class TournamentStartingtimeSetup extends TournamentBlock {

	int howManyMembersInEachStartingGroup = 3;

	SubmitButton startingTimeB3;
	SubmitButton startingTimeB4;
	SubmitButton startingTimeB5;
	SubmitButton startingTimeB6;
	SubmitButton startingTimeB7;
	SubmitButton startingTimeB8;
	SubmitButton startingTimeB9;

	protected boolean tournamentMustBeSet() {
		return true;
	}

	/*
	 * public void setTournament(ModuleInfo modinfo, Tournament tournament) {
	 * //modinfo.setSessionAttribute("golf_setupstartingtime_tournament",tournament); }
	 */
	public Tournament getTournament(IWContext modinfo) {
		try {
			return ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt((String) modinfo.getSessionAttribute("tournament_id")));
		} catch (FinderException e) {
			return null;
		}
	}

	public void main(IWContext modinfo) throws Exception {

		initialize(modinfo, getResourceBundle());

		Tournament tournament = getTournament(modinfo);
		String control = modinfo.getParameter("stt_action");
		if (control == null) {
			control = "tournRound";
			//	   selectTournament(modinfo, iwrb);
		}
		//else {

		if (tournament != null) {
			if (control.equals("tournRound")) {
				selectTournamentRound(modinfo, getResourceBundle());
			} else if (control.equals("tourngroups")) {
				if (getTournament(modinfo).getIfGroupTournament()) {
					selectTournamentGroups(modinfo, getResourceBundle());
				} else {
					selectTournamentGroups(modinfo, getResourceBundle());
				}
			} else if (control.equals("arrangement_chosen")) {
				useArrangement(modinfo, getResourceBundle());
			} else if (control.equals("tournament_groups_chosen")) {
				arrangeTournamentGroups(modinfo, getResourceBundle());
			} else if (control.equals("manualArrangementForMembersInGroupsChosen")) {
				manualArrangementForMembersInGroups(modinfo);
			} else if (control.equals("tournament_groups_ordered")) {
				selectArrangementForGroups(modinfo, getResourceBundle());
				//arrangeMembersInGroups(modinfo);
			} else if (control.equals("arrangement_chosen_for_groups")) {
				arrangementChosenForGroups(modinfo, getResourceBundle());
			} else if (control.equals("arrangeManualled")) {
				arrangeManualRegistraion(modinfo);
			} else if (control.equals("tournamentRoundIdChosenForArrangement")) {
				arrangeByPreviousScore(modinfo, getResourceBundle());
			} else if (control.equals("tournamentRoundIdChosenForStartingtime")) {
				arrangeByPreviousStartingtime(modinfo, getResourceBundle());
			} else {
				//add("Action \""+control+"\" is not handled");
			}

			add(getBlockForm(tournament, modinfo));
		}

	}

	private void initialize(IWContext modinfo, IWResourceBundle iwrb) {
		if (startingTimeB3 == null) {
			String goForward = iwrb.getLocalizedString("tournament.continue", "Continue");
			String save = iwrb.getLocalizedString("tournament.save", "Save");
			startingTimeB3 = new SubmitButton(goForward, "stt_action", "tournday");
			startingTimeB4 = new SubmitButton(goForward, "stt_action", "tourngroups");

			startingTimeB5 = new SubmitButton(goForward, "stt_action", "tourngroups");
			///new SubmitButton("stb5",goForward);
			startingTimeB6 = new SubmitButton("stb6", goForward);
			startingTimeB7 = new SubmitButton("stb7", save);
			startingTimeB8 = new SubmitButton("stb8", goForward);
			startingTimeB9 = new SubmitButton("stb9", goForward);
		}

	}

	private void selectTournament(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		Table table = new Table(1, 3);
		table.setAlignment(1, 3, "right");
		Form form = new Form();
		form.add(table);
		table.add(iwrb.getLocalizedString("tournament.choose_tournament", "Choose a tournament"), 1, 1);
		DropdownMenu menu = TournamentController.getDropdownOrderedByUnion(new DropdownMenu("tournament"), modinfo);
		menu.setMarkupAttribute("size", "10");
		table.add(menu, 1, 2);
		table.add(TournamentController.getAheadButton(modinfo, "", ""), 1, 3);
		table.add(new HiddenInput("stt_action", "tournRound"));

		add(form);
	}

	private void selectTournamentRound(IWContext modinfo, IWResourceBundle iwrb) throws Exception {

		Tournament tournament = getTournament(modinfo);
		DropdownMenu theMenu = new DropdownMenu("tournament_round");
		Form form = new Form();
		Table table = new Table();
		table.setNoWrap();
		table.setStyleClass(1, 1, getHeaderRowClass());
		table.setStyleClass(3, 1, getHeaderRowClass());
		table.setStyleClass(4, 1, getHeaderRowClass());
		table.setStyleClass(5, 1, getHeaderRowClass());
		table.setStyleClass(6, 1, getHeaderRowClass());
		table.setCellspacing(0);
		form.add(table);
		table.setWidth(600);
		table.setBorder(0);
		int row = 1;

		try {
			String sub_action = modinfo.getParameter("sub_action");
			if (sub_action != null) {
				if (sub_action.equals("changeVisibility")) {
					String tournament_round_id = modinfo.getParameter("tournament_round_id_to_change");
					if (tournament_round_id != null) {
						TournamentRound tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
						if (tRound.getVisibleStartingtimes()) {
							tRound.setVisibleStartingtimes(false);
						} else {
							tRound.setVisibleStartingtimes(true);
						}
						TournamentController.invalidateStartingTimeCache(modinfo, tournament.getID(), String.valueOf(tournament_round_id));
						tRound.update();
					}
				}
			}
		} catch (Exception e) {
		}

		TournamentRound[] tourRounds = tournament.getTournamentRounds();
		if (tournament.isDirectRegistration()) {
			if (tourRounds.length > 1) {
				for (int i = 0; i < tourRounds.length; i++) {
					if (i != 0) {
						theMenu.addMenuElement(tourRounds[i].getID(), iwrb.getLocalizedString("tournament.round", "Round") + " " + tourRounds[i].getRoundNumber() + "&nbsp;&nbsp;&nbsp;" + (new com.idega.util.IWTimestamp(tourRounds[i].getRoundDate())).getISLDate(".", true));
					}
				}
				add(iwrb.getLocalizedString("tournament.tee_times_for_first_round_are_chosen_in_tee_times_registration", "Tee times for the first round are arrangenged in Register player"));
				add("<br><br>");
				add(form);
			} else {
				add(iwrb.getLocalizedString("tournament.tee_times_are_chosen_in_tee_times_registration", "Tee times  in this tournament are arrangerd in Register player"));
				add("<br><br>");

				GenericButton theEdit1 = getButton(new GenericButton(localize("tournament.edit_teetimes", "Edit Teetimes")));
				theEdit1.setWindowToOpen(ModifyStartingtimeWindow.class);
				theEdit1.addParameterToWindow("tournament_id", tournament.getID());
				theEdit1.addParameterToWindow("action", "getSearch");
				add(theEdit1);
			}
		} else {
			theMenu = new DropdownMenu(tournament.getTournamentRounds());
			add(form);
		}

		//setTournament(modinfo,tournament);

		Text header = getSmallHeader(iwrb.getLocalizedString("tournament.pick_a_round", "Pick a round"));

		table.add(header, 1, row);

		table.setWidth(1, "350");
		table.setWidth(2, "20");

		Link change = getLink(iwrb.getLocalizedString("tournament.change", "Change"));
		change.addParameter("sub_action", "changeVisibility");
		change.addParameter("stt_action", modinfo.getParameter("stt_action"));

		Link print;

		Link changeClone;

		Text notVisible = getMessageText(iwrb.getLocalizedString("tournament.not_visible", "Not visible"));
		notVisible.setFontColor("red");

		Text header2 = getSmallHeader(iwrb.getLocalizedString("tournament.tee_times", "Tee times"));
		table.add(header2, 3, row);
		table.mergeCells(3, row, 5, row);

		for (int i = 0; i < tourRounds.length; i++) {
			table.add(tourRounds[i].getName(iwrb), 3, row + i + 1);
			table.setWidth(3, row + i + 1, "80");
			table.setWidth(4, row + i + 1, "70");
			if (tourRounds[i].getVisibleStartingtimes()) {
				table.add(iwrb.getLocalizedString("tournament.visible", "Visible"), 4, row + i + 1);
			} else {
				table.add(notVisible, 4, row + i + 1);
			}
			change = new Link(iwrb.getLocalizedString("tournament.change", "Change"));
			change.addParameter("sub_action", "changeVisibility");
			change.addParameter("stt_action", modinfo.getParameter("stt_action"));
			change.addParameter("tournament_round_id_to_change", "" + tourRounds[i].getID());
			table.add(change, 5, row + i + 1);

			print = getLink(iwrb.getLocalizedString("tournament.print", "print"));
			print.addParameter(TournamentStartingtimeWindow.PARAMETER_TOURNAMENT_ID, tournament.getID());
			print.addParameter(TournamentStartingtimeWindow.PARAMETER_TOURNAMENT_ROUND_ID, tourRounds[i].getID());
			print.setWindowToOpen(TournamentStartingtimeWindow.class);
			table.add(print, 6, row + i + 1);
			if (i % 2 == 0) {
				table.setStyleClass(3, row + i + 1, getDarkRowClass());
				table.setStyleClass(4, row + i + 1, getDarkRowClass());
				table.setStyleClass(5, row + i + 1, getDarkRowClass());
				table.setStyleClass(6, row + i + 1, getDarkRowClass());
			} else {
				table.setStyleClass(3, row + i + 1, getLightRowClass());
				table.setStyleClass(4, row + i + 1, getLightRowClass());
				table.setStyleClass(5, row + i + 1, getLightRowClass());
				table.setStyleClass(6, row + i + 1, getLightRowClass());
			}
		}

		//	table.setBorder(1);
		++row;
		table.add(theMenu, 1, row);
		table.add(TournamentController.getAheadButton(modinfo, "", ""), 1, row);
		table.add(new HiddenInput("stt_action", "tourngroups"));

		++row;
		++row;
		//	    table.setAlignment(1,row,"center");
		++row;
		//   table.setAlignment(1,row,"center");
		table.add(iwrb.getLocalizedString("tournament.to_edit_tee_times", "To edit tee times push the button below"), 1, 4);

		GenericButton theEdit = getButton(new GenericButton(localize("tournament.edit_tee_times", "Edit Teetimes")));
		theEdit.setWindowToOpen(ModifyStartingtimeWindow.class);
		theEdit.addParameter("tournament_id", tournament.getID());
		theEdit.addParameter("action", "getSearch");
		table.add(theEdit, 1, 5);

		Paragraph par = new Paragraph();
		par.setAlign("center");
		par.add("<br>&nbsp;&nbsp;&nbsp;");
		par.add(TournamentController.getBackLink(modinfo));
		add(par);

	}

	private void selectTournamentGroups(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		Tournament tournament = getTournament(modinfo);

		if (tournament.getIfGroupTournament()) {

			Form form = new Form();
			Table table = new Table();
			form.add(table);
			table.setBorder(0);
			table.mergeCells(1, 1, 2, 1);
			table.mergeCells(1, 2, 2, 2);
			table.setAlignment(1, 2, "center");
			table.setAlignment(1, 3, "left");
			table.setAlignment(2, 3, "right");
			table.setVerticalAlignment(1, 3, "top");
			table.setVerticalAlignment(2, 3, "top");

			table.add(iwrb.getLocalizedString("tournament.choose_tournament_group_for_round", "Choose groups to play this round"), 1, 1);
			table.add(new SelectionBox(tournament.getTournamentGroups()), 1, 2);
			table.add(new HiddenInput("stt_action", "tournament_groups_chosen"));
			form.maintainParameter("tournament_round");
			table.add(TournamentController.getAheadButton(modinfo, "", ""), 2, 3);
			table.add(TournamentController.getBackLink(modinfo), 1, 3);
			add(form);
		} else {
			selectArrangement(modinfo, iwrb);

		}
	}

	private void arrangeTournamentGroups(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		String[] tournament_group_id = modinfo.getParameterValues("tournament_group");

		if (tournament_group_id != null) {
			TournamentGroup tGroup;
			SelectionBox tournamentGroups = new SelectionBox("tournament_group");
			for (int i = 0; i < tournament_group_id.length; i++) {
				tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_id[i]));
				tournamentGroups.addMenuElement(tGroup.getID(), tGroup.getName());
			}
			tournamentGroups.addUpAndDownMovers();
			tournamentGroups.setHeight(8);
			tournamentGroups.selectAllOnSubmit();

			Form form = new Form();
			Table table = new Table(2, 3);
			form.add(table);
			table.setBorder(0);
			table.mergeCells(1, 1, 2, 1);
			table.mergeCells(1, 2, 2, 2);
			table.setAlignment(1, 2, "center");
			table.setAlignment(1, 3, "left");
			table.setAlignment(2, 3, "right");
			table.setVerticalAlignment(1, 3, "top");
			table.setVerticalAlignment(2, 3, "top");

			table.add(iwrb.getLocalizedString("tournament.choose_order", "Select the order of the groups"), 1, 1);
			table.add(tournamentGroups, 1, 2);
			table.add(TournamentController.getBackLink(modinfo), 1, 3);
			table.add(TournamentController.getAheadButton(modinfo, "", ""), 2, 3);

			form.add(new HiddenInput("stt_action", "tournament_groups_ordered"));
			form.maintainParameter("tournament_round");
			add(form);
		} else {
			add(iwrb.getLocalizedString("tournament.must_select_groups", "You must pick at least one group"));
			add("<br><br>");
			add(TournamentController.getBackLink(modinfo));

		}
	}

	private void selectArrangementForGroups(IWContext modinfo, IWResourceBundle iwrb) throws Exception {

		Tournament tournament = getTournament(modinfo);

		String[] tournamentGroups = modinfo.getParameterValues("tournament_group");

		if (tournamentGroups.length > 0) {
			int row = 3;

			Form form = new Form();
			form.maintainParameter("tournament_round");
			form.maintainParameter("tournament_group");
			Table table = new Table();
			form.add(table);
			table.setBorder(0);
			table.mergeCells(1, 1, 3, 1);

			table.add(iwrb.getLocalizedString("tournament.choose_ordering_method_for_groups", "Select ordering method for groups"), 1, 1);
			table.add(iwrb.getLocalizedString("tournament.group", "Group"), 1, row);
			table.add(iwrb.getLocalizedString("tournament.order", "Order"), 3, row);
			table.add(iwrb.getLocalizedString("tournament.count", "Count"), 5, row);

			String tournament_round_id = modinfo.getParameter("tournament_round");
			TournamentRound tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));

			DropdownMenu menu = getArrangementMenu(tournament, tRound, modinfo, iwrb);
			DropdownMenu menuAll = getArrangementMenuForAll(tournament, tRound, modinfo, iwrb);

			List members;
			String manyMembers[] = SimpleQuerier.executeStringQuery("Select member_id from tournament_member where tournament_id = " + tournament.getID());
			TournamentGroup tGroup;
			for (int i = 0; i < tournamentGroups.length; i++) {
				++row;
				tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournamentGroups[i]));
				//members =
				// TournamentController.getMembersInTournamentGroup(tournament,tGroup);
				table.add(tGroup.getName(), 1, row);
				table.add(new HiddenInput("tournament_group_id", "" + tGroup.getID()), 3, row);
				table.add(menu, 3, row);
				/*
				 * if (members != null) { table.add(members.size()+"",5,row); }
				 * else { table.add("0",5,row); }
				 */

			}
			++row;
			++row;
			table.add(iwrb.getLocalizedString("tournament.all_groups", "All groups"), 1, row);
			table.add(menuAll, 3, row);
			if (manyMembers != null) {
				table.add("" + manyMembers.length, 5, row);
			} else {
				table.add("0", 5, row);
			}
			++row;

			Text warning1 = new Text(iwrb.getLocalizedString("tournament.all_groups_warning_1", "NB! If ordering method is chosen here it affact ALL groups,"));
			warning1.addToText("<br>");
			warning1.addToText(iwrb.getLocalizedString("tournament.all_groups_warning_2", "even if something else has been chosen elsewhere."));
			warning1.setFontColor("red");
			table.add(warning1, 1, row);
			table.mergeCells(1, row, 5, row);
			++row;
			++row;
			table.mergeCells(1, row, 5, row);
			if (tRound.getStartingtees() > 1) {
				table.add(new HiddenInput("overwrite_startingtimes", "unspecified"), 1, row);
			} else {
				Text warning = new Text(iwrb.getLocalizedString("tournament.overwrite_startingtimes", "Overwrite previous startingtimes"));
				warning.addToText("&nbsp;?&nbsp;&nbsp;");
				CheckBox cBox = new CheckBox("overwrite_startingtimes");
				table.add(warning, 1, row);
				table.add(cBox, 1, row);
				++row;
			}
			++row;

			table.add(new HiddenInput("stt_action", "arrangement_chosen_for_groups"));
			table.add(TournamentController.getBackLink(modinfo), 1, row);
			table.mergeCells(3, row, 5, row);
			table.add(TournamentController.getAheadButton(modinfo, "", ""), 3, row);
			table.setAlignment(3, row, "right");
			++row;
			++row;

			add(form);

		}
	}

	public void deleteStartingtimes(TournamentRound tRound) throws SQLException {
		Startingtime[] sTimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("select s.* from startingtime s, tournament_round_startingtime trs where trs.startingtime_id = s.startingtime_id AND trs.tournament_round_id = " + tRound.getID());
		for (int i = 0; i < sTimes.length; i++) {
			sTimes[i].removeFrom(tRound);
			sTimes[i].delete();
		}
	}

	private void arrangementChosenForGroups(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		String[] tournament_group_ids = modinfo.getParameterValues("tournament_group_id");
		String[] arrangements = modinfo.getParameterValues("arrangement");
		String arrangementAll = modinfo.getParameter("arrangementAll");
		if (arrangementAll == null) arrangementAll = "null";

		String tRoundId = modinfo.getParameter("tournament_round");
		TournamentRound tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tRoundId));

		if (modinfo.getParameter("overwrite_startingtimes") != null) {
			deleteStartingtimes(tRound);
		}

		Tournament tournament = getTournament(modinfo);

		//checkIfNull
		boolean arrangementsNull = false;
		boolean arrangementsAllNull = true;

		for (int i = 0; i < arrangements.length; i++) {
			if (arrangements[i].equals("null")) {
				arrangementsNull = true;
				break;
			}
		}

		if (arrangementAll.equals("null")) {
			if (!arrangementsNull) {
				List members;
				Form form = new Form();
				form.maintainParameter("tournament_round");
				Table table = new Table();
				table.setBorder(0);
				table.mergeCells(1, 1, 3, 1);
				table.setAlignment(1, 1, "center");
				//table.add("<b>Handvirk röðun innan mótshópa</b>",1,1);
				Table tabel = new Table();
				tabel.setBorder(0);
				boolean addTabel = false;
				boolean addTable = false;
				int row = 2;
				int column = 0;
				int teljari = 0;

				TournamentGroup tGroup;
				if (tournament_group_ids.length > 0) {
					for (int i = 0; i < tournament_group_ids.length; i++) {
						tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_ids[i]));
						if (!arrangements[i].equals("null")) {
							members = TournamentController.getMembersInTournamentGroup(tournament, tGroup);
							if (members != null) {
								if (arrangements[i].equals("manual")) {
									/*
									 * table.add(new
									 * HiddenInput("tournament_group_id",tournament_group_ids[i]),1,row);
									 * ++column; ++teljari; if (column == 3) {
									 * column = 1; ++row; } table.add("
									 * <u>"+tGroup.getName()+ " </u>
									 * &nbsp;&nbsp;(hópur "+teljari+")"
									 * ,column,row); table.add(" <br>
									 * ",column,row);
									 * table.add(getArrangeManualSelectionBox(members,"member_"+tournament_group_ids[i]),column,row);
									 * table.setAlignment(column,row,"left");
									 * table.setVerticalAlignment(column,row,"top");
									 * addTable = true;
									 */
								}

								else {
									useArrangement(modinfo, members, arrangements[i], iwrb);
									Text texti = new Text(iwrb.getLocalizedString("tournament.ordering_finished_for", "Ordering complete for") + " \"" + tGroup.getName() + "\"");
									texti.setFontColor("black");
									add(texti);
									add("<br>");
								}
							}
						} else {
						}
					}
				}

				if (addTabel) {
					form.add(tabel);
					add(form);
				}

				if (addTable) {
					form.add(table);
					++row;
					table.add(TournamentController.getAheadButton(modinfo, "", ""), 2, row);
					table.add(new HiddenInput("stt_action", "manualArrangementForMembersInGroupsChosen"), 2, row);
					table.setAlignment(2, row, "right");

					add(form);
				}
			} else {
				add(iwrb.getLocalizedString("tournament.ording_method_missing_for_group", "Ordering method missing for one group or more"));
				add("<p>");
				add(TournamentController.getBackLink(modinfo));
			}
		} else {
			arrangementsAllNull = false;
			List members = TournamentController.getMembersInTournamentList(tournament);

			if (arrangementAll.indexOf("previousRounds") != -1) {
				arrangementsAllNull = true;
				String highLow = "high";

				if (arrangementAll.equals("previousRoundsHigh")) {
					highLow = "high";
				} else if (arrangementAll.equals("previousRoundsLow")) {
					highLow = "low";
				}

				int theRow = 1;
				Form form = new Form();
				form.maintainParameter("tournament_round");
				form.maintainParameter("tournament_group_id");
				Table table = new Table();
				table.setBorder(0);

				table.mergeCells(1, theRow, 2, theRow);
				table.add(iwrb.getLocalizedString("tournament.select_rounds_to_use", "Select rounds for calculation"));
				table.add(new HiddenInput("stt_action", "tournamentRoundIdChosenForArrangement"), 1, theRow);
				table.add(new HiddenInput("high_low", highLow), 1, theRow);
				++theRow;

				int roundNumber = tRound.getRoundNumber();
				TournamentRound[] tRounds = tournament.getTournamentRounds();
				for (int h = 0; h < roundNumber - 1; h++) {
					++theRow;
					table.add(tRounds[h].getName(), 1, theRow);
					table.add(new CheckBox("arrangement_tournament_round_id", tRounds[h].getID() + ""), 2, theRow);
				}

				++theRow;
				++theRow;
				table.add(iwrb.getLocalizedString("tournament.order_by", "Order by") + "...", 1, theRow);
				DropdownMenu order = new DropdownMenu("displayScoresOrder");
				order.addMenuElement(ResultComparator.TOTALSTROKES + "", iwrb.getLocalizedString("tournament.displayscore_strokes_wo_handicap", "Strokes without handicap (gross)"));
				order.addMenuElement(ResultComparator.TOTALSTROKESWITHHANDICAP + "", iwrb.getLocalizedString("tournament.displayscore_strokes_w_handicap", "Strokes with handicap (net)"));
				order.addMenuElement(ResultComparator.TOTALPOINTS + "", iwrb.getLocalizedString("tournament.displayscore_points", "Points"));
				table.add(order, 2, theRow);

				++theRow;
				++theRow;
				table.mergeCells(1, theRow, 2, theRow);
				table.setAlignment(1, theRow, "right");
				table.add(TournamentController.getAheadButton(modinfo, "", ""), 1, theRow);

				form.add(table);
				add(form);

			} else if (arrangementAll.equals("previousStartingtime")) {
				arrangementsAllNull = true;
				int theRow = 1;

				Form form = new Form();
				form.maintainParameter("tournament_round");
				form.maintainParameter("tournament_group_id");
				Table table = new Table();
				table.setBorder(0);
				table.mergeCells(1, theRow, 2, theRow);
				table.add(iwrb.getLocalizedString("tournament.select_rounds_to_use", "Select rounds for calculation"));

				table.add(new HiddenInput("stt_action", "tournamentRoundIdChosenForStartingtime"), 1, theRow);

				++theRow;

				int roundNumber = tRound.getRoundNumber();
				TournamentRound[] tRounds = tournament.getTournamentRounds();
				for (int h = 0; h < roundNumber - 1; h++) {
					++theRow;
					table.add(tRounds[h].getName(), 1, theRow);
					table.add(new RadioButton("startingtime_tournament_round_id", tRounds[h].getID() + ""), 2, theRow);
				}

				++theRow;
				++theRow;
				table.mergeCells(1, theRow, 2, theRow);
				table.setAlignment(1, theRow, "right");
				table.add(TournamentController.getAheadButton(modinfo, "", ""), 1, theRow);

				form.add(table);
				add(form);
			} else {
				useArrangement(modinfo, members, arrangementAll, iwrb);
				add(iwrb.getLocalizedString("tournament.ordering_finisher", "Ordering Finished"));
			}
		}

		if ((!arrangementsNull) || (!arrangementsAllNull)) {
			finalizeStartingtimes(tRound);
		}
	}

	public void finalizeStartingtimes(TournamentRound tRound) {
		if (tRound.getStartingtees() > 1) {
			try {
				String[] highestGrupNum = SimpleQuerier.executeStringQuery("Select max(grup_num) from startingtime s, tournament_round_startingtime trs where s.startingtime_id = trs.startingtime_id AND trs.tournament_round_id = " + tRound.getID());

				int total = 1;
				try {
					total = Integer.parseInt(highestGrupNum[0]);
				} catch (NumberFormatException n) {
				}

				int half = (total / 2) + (total % 2);
				Startingtime[] sTimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("Select s.* from startingtime s, tournament_round_startingtime trs where s.startingtime_id = trs.startingtime_id AND trs.tournament_round_id = " + tRound.getID() + " AND s.grup_num > " + half + " order by grup_num");
				for (int i = 0; i < sTimes.length; i++) {
					sTimes[i].setTeeNumber(10);
					sTimes[i].setGroupNum(sTimes[i].getGroupNum() - half);
					sTimes[i].update();
				}
			} catch (Exception e) {
				System.err.println("/tournament/setupstartingtime : finilizeStartingtimes : ");
				e.printStackTrace(System.err);
			}
		}
	}

	private void manualArrangementForMembersInGroups(IWContext modinfo) throws Exception {
		IWBundle bundle = getBundle(modinfo);
		IWResourceBundle iwrb = bundle.getResourceBundle(modinfo.getCurrentLocale());
		Tournament tournament = getTournament(modinfo);
		try {
			String[] tournament_group_ids = modinfo.getParameterValues("tournament_group_id");
			String[] members;
			String tournament_round_id = modinfo.getParameter("tournament_round");
			TournamentRound tourRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));

			Table contentTable = new Table(1, 3);

			int row = 1;
			Table table = new Table();
			table.add("<u>" + iwrb.getLocalizedString("tournament.already_registered", "Were already registered") + "</u>", 1, 1);
			table.setAlignment("left");
			table.setAlignment(1, 1, "left");

			int rowR = 1;
			Table tableR = new Table();
			tableR.add("<u>" + iwrb.getLocalizedString("tournament.were_registered", "Were registered") + "</u>", 1, 1);
			tableR.setAlignment("left");
			tableR.setAlignment(1, 1, "left");

			Member member;
			int startingGroup = 1;
			if (tournament_group_ids != null) {
				for (int i = 0; i < tournament_group_ids.length; i++) {
					members = modinfo.getParameterValues("member_" + tournament_group_ids[i]);
					if (members != null) {
						for (int j = 0; j < members.length; j++) {
							member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(members[j]));
							startingGroup = TournamentController.getNextAvailableStartingGroup(tournament, tourRound);

							//startingGroup =
							// getNextAvailableStartingGroup(tournament,
							// tourDay, tournament.getNumberInGroup());

							if (!TournamentController.isMemberRegisteredInTournament(tournament, tourRound, tournament.getNumberInGroup(), member)) {
								TournamentController.setupStartingtime(modinfo, member, tournament, tourRound.getID(), startingGroup);
								++rowR;
								tableR.add(member.getName(), 1, rowR);
								tableR.add("Ráshópur " + startingGroup, 4, rowR);
								tableR.setAlignment(1, rowR, "left");
								tableR.setAlignment(4, rowR, "left");
							} else {
								++row;
								table.add(member.getName(), 1, row);
								table.setAlignment(1, row, "left");
							}
						}
					}
				}
			}

			if (rowR > 1) {
				contentTable.add(tableR, 1, 1);
			}
			if (row > 1) {
				contentTable.add(table, 1, 3);
			}

			add(contentTable);

		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
	}

	/**
	 * @depricated
	 */
	private void selectArrangement(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		//	    selectArrangement(modinfo,-1);
	}

	private DropdownMenu getArrangementMenuForAll(Tournament tournament, TournamentRound tRound, IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		DropdownMenu menu = new DropdownMenu("arrangementAll");
		menu.addMenuElement("null", iwrb.getLocalizedString("tournament.start_order_by_group", "Use ordering for each group"));
		menu.addMenuElement("random", iwrb.getLocalizedString("tournament.start_order_random", "Random"));
		menu.addMenuElement("handicap_low_first", iwrb.getLocalizedString("tournament.start_order_handicap_low_first", "By handicap (lowest first)"));
		menu.addMenuElement("handicap_high_first", iwrb.getLocalizedString("tournament.start_order_handicap_high_first", "By handicap (highest first)"));
		//	        menu.addMenuElement("manual",iwrb.getLocalizedString("tournament.start_order_manual","Manual"));
		menu.addMenuElement("alphabetical", iwrb.getLocalizedString("tournament.start_order_alphabetical", "Alphabetically"));
		int roundNumber = tRound.getRoundNumber();
		if (roundNumber > 1) {
			menu.addMenuElement("previousRoundsLow", iwrb.getLocalizedString("tournament.start_order_previous_rounds_low", "By previous rounds results (lowest first)"));
			menu.addMenuElement("previousRoundsHigh", iwrb.getLocalizedString("tournament.start_order_previous_rounds_high", "By previous rounds results (highest first)"));
			menu.addMenuElement("previousStartingtime", iwrb.getLocalizedString("tournament.start_order_previous_start", "Unchanged ordering"));
		}
		return menu;
	}

	private DropdownMenu getArrangementMenu(Tournament tournament, TournamentRound tRound, IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		DropdownMenu menu = new DropdownMenu("arrangement");
		menu.addMenuElement("null", iwrb.getLocalizedString("tournament.start_order_by_all_groups", "Use ordering for all groups"));
		menu.addMenuElement("random", iwrb.getLocalizedString("tournament.start_order_random", "Random"));
		menu.addMenuElement("handicap_low_first", iwrb.getLocalizedString("tournament.start_order_handicap_low_first", "By handicap (lowest first)"));
		menu.addMenuElement("handicap_high_first", iwrb.getLocalizedString("tournament.start_order_handicap_high_first", "By handicap (highest first)"));
		//	        menu.addMenuElement("manual",iwrb.getLocalizedString("tournament.start_order_manual","Manual"));
		menu.addMenuElement("alphabetical", iwrb.getLocalizedString("tournament.start_order_alphabetical", "Alphabetically"));

		return menu;
	}

	/*
	 * private void selectArrangement(ModuleInfo modinfo, int
	 * tournamentGroupId)throws Exception{ GolfDialog dialog = null;
	 * TournamentGroup tourGroup = null; if (tournamentGroupId == -1) { dialog =
	 * new GolfDialog("Veldu röðunarmáta"); } else { tourGroup = new
	 * TournamentGroup(tournamentGroupId); dialog = new GolfDialog("Veldu
	 * röðunarmáta fyrir \""+tourGroup.getName()+"\""); } Form form=new Form();
	 * add(dialog); dialog.add(form); DropdownMenu menu = getArrangementMenu();
	 * form.add(new HiddenInput("stt_action","arrangement_chosen"));
	 * form.maintainParameter("tournament_day"); form.add(menu);
	 * form.add(startingTimeB8); }
	 */

	private void useArrangement(IWContext modinfo, List members, String arrangement, IWResourceBundle iwrb) throws Exception {
		members = orderMembers(members, arrangement);
		arrangeMembers(modinfo, members, iwrb);
	}

	private void useArrangement(IWContext modinfo, String arrangement, IWResourceBundle iwrb) throws Exception {
		Tournament tournament = getTournament(modinfo);

		List members = new Vector();
		if (arrangement.equals("handicap")) {
			members = getMembers(tournament, "handicap");
		} else if (arrangement.equals("random")) {
			members = getMembers(tournament);
		} else if (arrangement.equals("alphabetical")) {
			members = getMembers(tournament, "name");
		} else if (arrangement.equals("manual")) {
			members = getMembers(tournament, "name");
		}

		arrangeMembers(modinfo, members, iwrb);
	}

	private void useArrangement(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		String arrangement = modinfo.getParameter("arrangement");
		if (arrangement != null) {
			useArrangement(modinfo, arrangement, iwrb);
		} else {
			String stt_action = modinfo.getParameter("stt_action");
			if (stt_action != null) {
				if (stt_action.equals("arrangement_chosen")) {
					selectArrangement(modinfo, iwrb);
				} else if (stt_action.equals("arrangement_chosen_for_group")) {
					selectArrangementForGroups(modinfo, iwrb);
				}
			} else {
				selectTournament(modinfo, iwrb);
			}

		}
	}

	private void arrangeManual(IWContext modinfo, List members, IWResourceBundle iwrb) {
		if (members != null) {
			Form form = new Form();
			Table table = new Table();
			form.add(table);
			form.maintainParameter("tournament_round");
			SelectionBox memberDropdown = new SelectionBox(members);
			memberDropdown.addUpAndDownMovers();
			memberDropdown.setHeight(10);
			memberDropdown.selectAllOnSubmit();

			form.add(memberDropdown);
			form.add(new SubmitButton(iwrb.getLocalizedString("tournament.select", "Select"), "stt_action", "arrangeManualled"));

			add(form);
		} else {
			nobodyIsRegistered(iwrb);
		}
	}

	private SelectionBox getArrangeManualSelectionBox(List members, String name) {
		SelectionBox memberDropdown = new SelectionBox(members);
		if (name.equalsIgnoreCase("")) {
			memberDropdown.setName("member");
		} else {
			memberDropdown.setName(name);
		}
		memberDropdown.addUpAndDownMovers();
		memberDropdown.setHeight(10);
		memberDropdown.selectAllOnSubmit();
		return memberDropdown;
	}

	private SelectionBox getArrangeManualSelectionBox(List members) {
		return getArrangeManualSelectionBox(members, "");
	}

	private void arrangeManualRegistraion(IWContext modinfo) throws SQLException {

		add("KOMINN Í FALL SEM ÉG VIL HENDA !!!!!!");

		Tournament tournament = getTournament(modinfo);

		String[] member_ids = modinfo.getParameterValues("member");
		String s_tournament_round_id = modinfo.getParameter("tournament_round");

		if (member_ids != null) {
			Member member;

			for (int i = 0; i < member_ids.length; i++) {
				try {
					member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_ids[i]));
					//setupStartingtime(member,tournament,
					// Integer.parseInt(s_tournament_day_id));
					add("ekkert gerðist");
				} catch (Exception e) {
					add("Villa");
					e.printStackTrace(System.err);
					add(e.getMessage());
				}
			}
		} else {
			add("Enginn var valinn valinn");
		}

	}

	public void removeDismissed(List members, Tournament tournament) {
		Member member;
		try {
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				Object item = iter.next();

				member = (Member) item;
				if (tournament.getDismissal(member) != 0) {
					System.err.println("setupstartingtime : ArrangeMembers : " + tournament.getName() + " : " + member.getName() + " radast ekki, Fravisun");
					iter.remove();
				}
			}
		} catch (Exception e) {
		}
	}

	private void arrangeMembers(IWContext modinfo, List members, IWResourceBundle iwrb) {
		Member member;
		Tournament tournament = getTournament(modinfo);
		String s_tournament_round_id = modinfo.getParameter("tournament_round");

		boolean newTournamentGroups = false;
		int minimumGroupNumber = 1;

		if (members != null) {

			removeDismissed(members, tournament);

			TournamentRound tRound;
			int[] start = new int[2];

			int numberInGroup = tournament.getNumberInGroup();
			int modder = members.size() % numberInGroup;

			if (numberInGroup == 4) {
				switch (modder) {
					case 1:
						start[0] = 2;
						start[1] = 3;
						break;
					case 2:
						start[0] = 3;
						start[1] = 3;
						break;
					case 3:
						start = new int[1];
						start[0] = 3;
						break;
					default:
						start = new int[0];
						break;
				}
			} else if (numberInGroup == 3) {
				switch (modder) {
					case 1:
						start[0] = 2;
						start[1] = 2;
						break;
					case 2:
						start = new int[1];
						start[0] = 2;
						break;
					default:
						start = new int[0];
						break;
				}
			} else if (numberInGroup == 2) {
				switch (modder) {
					case 1:
						start = new int[1];
						start[0] = 1;
						break;
					default:
						start = new int[0];
						break;
				}
			} else {
				start = new int[0];
			}

			int groupNumber = 1;
			int previousStartingGroup = 0;
			int currentStartingGroup = 0;
			int dismiss = 0;

			for (int i = 0; i < members.size(); i++) {
				member = (Member) members.get(i);

				try {
					newTournamentGroups = false;

					if (i == 0) {
						newTournamentGroups = true;
					}

					tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(s_tournament_round_id));
					int startingGroup = TournamentController.getNextAvailableStartingGroup(tournament, tRound, newTournamentGroups, minimumGroupNumber);

					if (i == 0) {
						previousStartingGroup = startingGroup;
					}

					currentStartingGroup = startingGroup;

					if (currentStartingGroup > previousStartingGroup) {
						++groupNumber;
						++previousStartingGroup;
					} else if (currentStartingGroup - previousStartingGroup == 2) {
						previousStartingGroup = currentStartingGroup - 1;
					}

					TournamentController.setupStartingtime(modinfo, member, tournament, tRound.getID(), startingGroup);

					if (start.length > 1) {
						if (groupNumber <= start.length) {
							List theList = TournamentController.getMembersInStartingGroup(tournament, tRound, startingGroup);
							if (theList != null) {
								if (theList.size() == start[groupNumber - 1]) {
									minimumGroupNumber = (startingGroup + 1);
								}
							}
						}
					} else if (start.length > 0) {
						if (groupNumber == 1) {
							List theList = TournamentController.getMembersInStartingGroup(tournament, tRound, startingGroup);
							if (theList != null) {
								if (theList.size() == start[groupNumber - 1]) {
									minimumGroupNumber = (startingGroup + 1);
								}
							}

						}
					}

					if (newTournamentGroups) {
						minimumGroupNumber = startingGroup;
					}

				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		} else {
			nobodyIsRegistered(iwrb);
		}
	}

	/**
	 * Þarf að laga....
	 */
	/*
	 * public int getHowManyInCurrentStartingGroup(Tournament tournament,
	 * TournamentDay tourDay, int startingGroup) throws SQLException {
	 * com.idega.util.idegaTimestamp stamp = new
	 * com.idega.util.idegaTimestamp(tourDay.getDate()); Startingtime[]
	 * startingtimes = (Startingtime[]) (new Startingtime()).findAll("SELECT *
	 * FROM STARTINGTIME WHERE STARTINGTIME_DATE = '"+stamp.toSQLString()+"' AND
	 * field_id="+tournament.getFieldId()+" AND grup_num="+startingGroup);
	 * return startingtimes.length; }
	 */

	private void nobodyIsRegistered(IWResourceBundle iwrb) {
		GolfDialog dialog = new GolfDialog(iwrb.getLocalizedString("tournament.nobody_regstered", "No one is registered in the tournament"));
		add(dialog);
	}

	//	  mætti setja í einhvern annan klasa...
	public List randomList(List list) {
		int randomNumber = 0;
		int numberOfMembers = list.size();
		Vector vector = new Vector();

		for (int i = 0; i < numberOfMembers; i++) {
			randomNumber = ((int) (Math.random() * list.size()));
			vector.add(list.get(randomNumber));
			list.remove(randomNumber);
		}
		return vector;
	}

	//	 setja í TournamentBusines... (whatever hann heitir);
	public List getMembers(Tournament theTournament) {
		return getMembers(theTournament, "ekkert");
	}

	//	 setja í TournamentBusines... (whatever hann heitir);

	/**
	 * Order by FirstName, LastName, MiddleName => String ordered = "name";
	 * Order by Handicap => String ordered = "handicap";
	 */
	public List getMembers(Tournament theTournament, String ordered) {

		List list = null;
		try {
			if (ordered.equalsIgnoreCase("ekkert")) {
				list = com.idega.data.EntityFinder.findReverseRelated(theTournament, (Member) IDOLookup.instanciateEntity(Member.class));
			} else if (ordered.equalsIgnoreCase("name")) {
				list = com.idega.data.EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), "Select * from member,tournament_member where tournament_member.tournament_id=" + theTournament.getID() + " and member.member_id = tournament_member.member_id order by member.first_name,member.last_name, member.middle_name");
			} else if (ordered.equalsIgnoreCase("handicap")) {
				list = com.idega.data.EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), "Select * from member,tournament_member, member_info where member_info.member_id = member.member_id AND tournament_member.tournament_id=" + theTournament.getID() + " and member.member_id = tournament_member.member_id order by member_info.handicap");
			}
		} catch (Exception s) {
			s.printStackTrace(System.err);
		}
		return list;
	}

	public List orderMembers(List members, String ordered) {
		try {
			if (ordered.equalsIgnoreCase("alphabetical")) {
				java.util.Collections.sort(members, new is.idega.idegaweb.golf.member.GenericMemberComparator(is.idega.idegaweb.golf.member.GenericMemberComparator.FIRSTLASTMIDDLE));
			} else if (ordered.equalsIgnoreCase("handicap_low_first")) {
				Member member;
				String SQLString = "select m.* from member m, member_info mi where m.member_id = mi.member_id ";
				String memberString = "AND (";

				for (int i = 0; i < members.size(); i++) {
					member = (Member) members.get(i);

					memberString += " mi.member_id = " + member.getID();
					if (i < (members.size() - 1)) {
						memberString += " OR";
					}
				}
				memberString += " ) ";
				SQLString += memberString;
				SQLString += " order by mi.handicap";

				members = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), SQLString);
			} else if (ordered.equalsIgnoreCase("handicap_high_first")) {
				Member member;
				String SQLString = "select m.* from member m, member_info mi where m.member_id = mi.member_id ";
				String memberString = "AND (";

				for (int i = 0; i < members.size(); i++) {
					member = (Member) members.get(i);

					memberString += " member_id = " + member.getID();
					if (i < (members.size() - 1)) {
						memberString += " OR";
					}
				}
				memberString += " ) ";
				SQLString += memberString;
				SQLString += " order by mi.handicap desc";

				members = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), SQLString);

			} else if (ordered.equalsIgnoreCase("random")) {
				members = randomList(members);
			}
		} catch (Exception s) {
			s.printStackTrace(System.err);
		}

		return members;
	}

	public void arrangeByPreviousScore(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		String[] tournament_round_id = modinfo.getParameterValues("arrangement_tournament_round_id");
		if (tournament_round_id == null) {
			add(iwrb.getLocalizedString("tournament.must_select_rounds", "You must select at least one round"));
			add("<p>");
			add(TournamentController.getBackLink(modinfo));
		} else {

			String tournament_round = modinfo.getParameter("tournament_round");
			String displayScoresOrder = modinfo.getParameter("displayScoresOrder");
			int tournamentTypeOrder = Integer.parseInt(displayScoresOrder);
			TournamentRound tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round));
			Tournament tournament = tRound.getTournament();
			ResultComparator comparator = new ResultComparator(tournamentTypeOrder);
			String highLow = modinfo.getParameter("high_low");
			Vector members = null;
			List memberList = null;

			String[] tGroups = modinfo.getParameterValues("tournament_group_id");

			int[] intTRoundIds = new int[tournament_round_id.length];
			try {
				for (int i = 0; i < tournament_round_id.length; i++) {
					intTRoundIds[i] = Integer.parseInt(tournament_round_id[i]);
				}
			} catch (NumberFormatException n) {
				System.err.println("TournamentStartingtimeSetup : arrangeByPreviousScore : int[] creation ");
			}

			if (tGroups != null) {
				ResultDataHandler handler = null;
				int[] tGroupIds;
				for (int i = 0; i < tGroups.length; i++) {

					handler = new ResultDataHandler(tournament.getID(), tournamentTypeOrder, Integer.parseInt(tGroups[i]), intTRoundIds, null);
					members = handler.getTournamentMembers();

					java.util.Collections.sort(members, comparator);

					if (highLow.equals("high")) {
						if (members != null) {
							java.util.Collections.reverse(members);
						}
					}

					memberList = ResultsCollectorToMember(members);
					arrangeMembers(modinfo, memberList, iwrb);
				}
			} else {
				ResultDataHandler handler = new ResultDataHandler(tournament.getID(), tournamentTypeOrder, intTRoundIds, null);
				members = handler.getTournamentMembers();

				java.util.Collections.sort(members, comparator);

				if (highLow.equals("high")) {
					if (members != null) {
						java.util.Collections.reverse(members);
					}
				}

				memberList = ResultsCollectorToMember(members);
				arrangeMembers(modinfo, memberList, iwrb);
			}

			finalizeStartingtimes(tRound);
			add(iwrb.getLocalizedString("tournament.ordering_finished", "Ordering complete"));
		}
	}

	public void arrangeByPreviousStartingtime(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		String tournament_round_id = modinfo.getParameter("startingtime_tournament_round_id");
		if (tournament_round_id == null) {
			add(iwrb.getLocalizedString("tournament.must_select_rounds", "You must select at least one round"));
			add("<p>");
			add(TournamentController.getBackLink(modinfo));
		} else {
			String tournament_round = modinfo.getParameter("tournament_round");
			Startingtime[] sTimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("select s.* from startingtime s,tournament_round_startingtime trs where trs.startingtime_id = s.startingtime_id AND trs.tournament_round_id = " + tournament_round_id + " order by s.grup_num");
			Startingtime sTime;
			Member member;
			TournamentRound tourRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round));
			Tournament tournament = tourRound.getTournament();

			for (int i = 0; i < sTimes.length; i++) {
				member = sTimes[i].getMember();
				TournamentController.setupStartingtime(modinfo, member, tournament, tourRound.getID(), sTimes[i].getGroupNum(), sTimes[i].getTeeNumber());
			}

			add(iwrb.getLocalizedString("tournament.ordering_finished", "Ordering complete"));
		}
	}

	private List ResultsCollectorToMember(Vector resultCollector) throws SQLException {

		Member member;
		Vector members = new Vector();
		ResultsCollector resColl;

		if (resultCollector != null) {

			for (int i = 0; i < resultCollector.size(); i++) {
				resColl = (ResultsCollector) resultCollector.elementAt(i);
				try {
					member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(resColl.getMemberId());
				} catch (FinderException fe) {
					throw new SQLException(fe.getMessage());
				}
				members.add(member);
			}

		}

		return members;
	}

	private void save(IWContext modinfo) throws Exception {
		IWBundle bundle = getBundle(modinfo);
		IWResourceBundle iwrb = bundle.getResourceBundle(modinfo.getCurrentLocale());
		GolfDialog dialog = new GolfDialog(iwrb.getLocalizedString("tournament.startingtimes_saved", "Startingtimes were saved"));
		add(dialog);
	}

	private Form getBlockForm(Tournament tournament, IWContext modinfo) throws SQLException {
		IWBundle bundle = getBundle(modinfo);
		IWResourceBundle iwrb = bundle.getResourceBundle(modinfo.getCurrentLocale());
		Form form = new Form();
		form.setWindowToOpen(BlockStartingtimeWindow.class);

		Table table = new Table(2, 1);
		DropdownMenu menu = new DropdownMenu("tournament_round_id");
		TournamentRound[] rounds = tournament.getTournamentRounds();
		for (int a = 0; a < rounds.length; a++) {
			menu.addMenuElement(rounds[a].getID(), "Hringur " + String.valueOf(a + 1));
		}

		table.add(menu, 1, 1);
		table.add(new SubmitButton(iwrb.getLocalizedString("tournament.block", "Block")), 2, 1);
		form.add(table);
		return form;
	}

}