/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Startingtime;
import is.idega.idegaweb.golf.entity.StartingtimeHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class ModifyStartingtime extends GolfBlock {

	public void setTournament(IWContext modinfo, Tournament tournament) {
		modinfo.setSessionAttribute("golf_modifystartingtime_tournament", tournament);
	}

	public Tournament getTournament(IWContext modinfo) {
		return (Tournament) modinfo.getSessionAttribute("golf_modifystartingtime_tournament");
	}

	public void main(IWContext modinfo) throws Exception {

		try {
			Tournament tournament = null;

			String tournament_id = modinfo.getParameter("tournament_id");
			if (tournament_id != null) {
				tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
				setTournament(modinfo, tournament);
			}
			else {
				tournament = getTournament(modinfo);
			}

			if (tournament != null) {

				String action = modinfo.getParameter("action");
				String tournament_round_id = modinfo.getParameter("tournament_round");
				if (action != null) {
					if (action.equals("update")) {
						executeUpdate(modinfo);
						add(getEditableStartingtimeForm(modinfo, tournament, tournament_round_id));
					}
					else if (action.equals("getSearch")) {
						add(getEditableStartingtimeForm(modinfo, tournament, tournament_round_id));
					}
					else if (action.equals("addMember")) {
						add(drawLeftOuts(modinfo, tournament, tournament_round_id));
					}
				}

			}
			else {
				add("Ekkert mót valið");
			}

		}
		catch (Exception ex) {
			System.err.println("{ Aron debugging catching errors}");
			ex.printStackTrace();
		}

	}

	public Form getEditableStartingtimeForm(IWContext modinfo, Tournament tournament, String tournament_round_id) throws SQLException, FinderException {

		Form form = new Form();
		Table table = new Table();
		form.add(table);
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(1);
		table.setAlignment("center");
		table.setWidth("85%");

		int row = 0;
		String color1 = "#ADC9D0";

		int tournamentRoundId = 0;
		TournamentRound[] tRounds = tournament.getTournamentRounds();

		if (tournament_round_id != null) {
			tournamentRoundId = Integer.parseInt(tournament_round_id);
		}
		else {
			if (tRounds.length > 0) {
				tournamentRoundId = tRounds[0].getID();
			}
		}

		++row;
		table.mergeCells(1, row, 5, row);
		table.setAlignment(1, row, "right");
		IWTimestamp tourDay;
		DropdownMenu rounds = new DropdownMenu("tournament_round");
		rounds.setStyleAttribute("font-size: 8pt");

		for (int i = 0; i < tRounds.length; i++) {
			tourDay = new IWTimestamp(tRounds[i].getRoundDate());
			rounds.addMenuElement(tRounds[i].getID(), tRounds[i].getName() + " " + tourDay.getLocaleDate(modinfo.getCurrentLocale()));
		}

		if (tournamentRoundId != 0) {
			rounds.setSelectedElement(Integer.toString(tournamentRoundId));
		}
		rounds.setToSubmit();
		table.add(rounds, 1, row);

		++row;
		Text rast = new Text("Rástimar");
		rast.setFontSize(2);
		rast.setBold();
		rast.setFontColor("#FFFFFF");
		Text move = new Text("Færa");
		move.setFontSize(2);
		move.setBold();
		move.setFontColor("#FFFFFF");
		Text name = new Text("Nafn");
		name.setFontSize(2);
		name.setBold();
		name.setFontColor("#FFFFFF");
		Text club = new Text("Klúbbur");
		club.setFontSize(2);
		club.setBold();
		club.setFontColor("#FFFFFF");
		Text hand = new Text("Forgjöf");
		hand.setFontSize(2);
		hand.setBold();
		hand.setFontColor("#FFFFFF");
		Text del = new Text("Eyða");
		del.setFontSize(2);
		del.setBold();
		del.setFontColor("#FFFFFF");
		Text tee = new Text("Rásteigur");
		tee.setFontSize(2);
		tee.setBold();
		tee.setFontColor("#FFFFFF");

		table.add(rast, 1, row);
		table.add(move, 2, row);
		table.add(name, 3, row);
		table.setAlignment(3, row, "center");
		table.add(club, 4, row);
		table.add(hand, 5, row);
		table.add(del, 6, row);
		table.setRowColor(row, "#336661");
		table.setHeight(1, row, "10");

		if (tournamentRoundId != 0) {
			TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournamentRoundId);

			boolean displayTee = false;
			if (tournamentRound.getStartingtees() > 1) {
				displayTee = true;
			}

			Text tournamentName = new Text(tournament.getName());
			tournamentName.setFontSize(3);
			tournamentName.setBold();

			add("<br><center>");
			add(tournamentName);
			add("</center><p>");

			DropdownMenu availableGrupNums = getAvailableGrupNums(tournament, tournamentRound);
			availableGrupNums.setStyleAttribute("font-size: 8pt");
			DropdownMenu clonedMenu;

			CheckBox memberCheck;
			CheckBox delMember;

			for (int y = 1; y <= tournamentRound.getStartingtees(); y++) {
				// HARÐKÓÐUN DAUÐANS
				int tee_number = 1;
				if (y == 2)
					tee_number = 10;
				//
				int interval = tournament.getInterval();
				int numberInGroup = tournament.getNumberInGroup();
				IWTimestamp start = new IWTimestamp(tournamentRound.getRoundDate());
				start.addMinutes(-interval);
				IWTimestamp end = new IWTimestamp(tournamentRound.getRoundEndDate());
				java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
				Startingtime[] sTimes;
				Text time;
				int grupNum = 0;

				if (displayTee) {
					++row;
					Text startTee = new Text("Rásteigur : " + tee_number);
					startTee.setFontColor("#FFFFFF");
					startTee.setBold();
					table.add(startTee, 1, row);
					table.setRowColor(row, "#336661");
					table.mergeCells(1, row, 5, row);
					table.setAlignment(1, row, "center");
				}

				Text errorText = new Text("Ekki pláss");
				errorText.setFontColor("RED");

				while (end.isLaterThan(start)) {

					++grupNum;
					++row;

					if (color1.equals("#ADC9D0")) {
						color1 = "#CEDFCF";
					}
					else {
						color1 = "#ADC9D0";
					}

					start.addMinutes(interval);

					sTimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("Select startingtime.* from startingtime, tournament_round_startingtime where tournament_round_startingtime.startingtime_id = startingtime.startingtime_id AND tournament_round_startingtime.tournament_round_id = " + tournamentRoundId + " AND grup_num = " + grupNum + " AND tee_number = " + tee_number + " order by startingtime.grup_num, tee_number");
					clonedMenu = (DropdownMenu) availableGrupNums.clone();
					clonedMenu.setName("grup_num_dropdown_grup_num_" + grupNum + "_tee_num_" + tee_number);
					table.add(new HiddenInput("tee_number", Integer.toString(tee_number)), 1, row);

					time = new Text();
					time.setText(extraZero.format(start.getHour()) + ":" + extraZero.format(start.getMinute()));
					time.setFontStyle("letter-spacing:1px;font-family:Arial,Helvetica,sans-serif;background-color:#FFFFFF;font-size:18px;color:#2C4E3B;border-width:1px;font-weight:bold;border-style:solid;");
					//time = new
					// Flash("http://jgenerator.sidan.is/time.swt?grc=true&time="+extraZero.format(start.getHour())+":"+extraZero.format(start.getMinute()),extraZero.format(start.getHour())+":"+extraZero.format(start.getMinute()));
					//time.setWidth("60");
					//time.setHeight("40");
					//                            time = new
					// Image("http://jgenerator.sidan.is/time.swt?grc=true&time="+extraZero.format(start.getHour())+":"+extraZero.format(start.getMinute()),extraZero.format(start.getHour())+":"+extraZero.format(start.getMinute()));
					//table.add(extraZero.format(start.getHour())+":"+extraZero.format(start.getMinute()),1,row);
					table.add(time, 1, row);
					//table.setAlignment(1,row,"center");
					table.add("<br><br>", 1, row);
					table.setVerticalAlignment(1, row, "top");
					table.setHeight(1, row, "90");
					table.mergeCells(1, row, 1, row + numberInGroup - 1);

					if (sTimes.length > 0) {
						table.add(clonedMenu, 1, row);
						table.add(new HiddenInput("grup_num", Integer.toString(grupNum)), 1, row);
					}

					table.setColor(1, row, color1);

					for (int i = 0; i < sTimes.length; i++) {
						table.setColor(1, row, color1);
						table.setColor(2, row, color1);
						table.setColor(3, row, color1);
						table.setColor(4, row, color1);
						table.setColor(5, row, color1);
						table.setColor(6, row, color1);

						memberCheck = new CheckBox("grup_num_" + grupNum + "_member_checkbox_member_id_" + sTimes[i].getMemberID());
						delMember = new CheckBox("remove_startingtime", "" + sTimes[i].getMemberID());

						clonedMenu = (DropdownMenu) availableGrupNums.clone();
						clonedMenu.setName("grup_num_dropdown_member_id_" + sTimes[i].getMemberID());

						table.add(memberCheck, 2, row);
						table.add(sTimes[i].getPlayerName(), 3, row);
						table.add(sTimes[i].getClubName(), 4, row);
						table.add(TextSoap.singleDecimalFormat(sTimes[i].getHandicap()), 5, row);
						table.add(new HiddenInput("startingtime_id_member_id_" + sTimes[i].getMemberID(), Integer.toString(sTimes[i].getID())), 3, row);
						table.add(new HiddenInput("member_id", Integer.toString(sTimes[i].getMemberID())), 3, row);
						table.add(delMember, 6, row);

						/*
						 * if ( (i+1) > numberInGroup) { table.add(errorText,1,row);
						 * table.setAlignment(1,row,"right"); table.setColor(1,row,color1); }
						 */
						++row;
					}

					for (int i = sTimes.length; i < numberInGroup; i++) {
						table.add("&nbsp;", 3, row);
						table.setColor(2, row, color1);
						table.setColor(3, row, color1);
						table.setColor(4, row, color1);
						table.setColor(5, row, color1);
						table.setColor(6, row, color1);
						++row;
					}

					--row;
				}
			}

		}
		else {
			add("Mótið er ekki sett upp rétt");
		}

		++row;
		table.mergeCells(1, row, 2, row);
		table.mergeCells(3, row, 5, row);

		Link link = new Link("Bæta við rástimum");
		link.addParameter("action", "addMember");
		link.addParameter("tournament_round", tournamentRoundId + "");
		link.addParameter("tournament", tournament.getID() + "");

		table.add(link, 1, row);

		table.add(TournamentController.getAheadButton(modinfo, "", ""), 3, row);

		table.add(new HiddenInput("action", "update"), 3, row);
		table.add(new HiddenInput("tournament_round", tournamentRoundId + ""), 3, row);

		table.setAlignment(1, row, "right");
		table.setColumnAlignment(1, "center");
		table.setColumnAlignment(2, "center");
		table.setColumnAlignment(4, "center");
		table.setColumnAlignment(5, "center");
		table.setAlignment(1, 1, "right");
		table.setAlignment(1, row, "left");
		table.setAlignment(3, row, "right");

		return form;
	}

	public DropdownMenu getAvailableGrupNums(Tournament tournament, TournamentRound tRound) throws SQLException {
		DropdownMenu menu = new DropdownMenu("grup_num_dropdown");

		int interval = tournament.getInterval();
		int grupNum = 0;
		IWTimestamp start = new IWTimestamp(tRound.getRoundDate());
		start.addMinutes(-interval);
		IWTimestamp end = new IWTimestamp(tRound.getRoundEndDate());
		java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
		menu.addMenuElement(0, "");

		boolean displayTee = false;
		if (tRound.getStartingtees() > 1) {
			displayTee = true;
		}

		while (end.isLaterThan(start)) {
			++grupNum;
			start.addMinutes(interval);
			if (displayTee) {
				menu.addMenuElement(grupNum, extraZero.format(start.getHour()) + ":" + extraZero.format(start.getMinute()) + "&nbsp;&nbsp; (1)");
				menu.addMenuElement(grupNum + "_", extraZero.format(start.getHour()) + ":" + extraZero.format(start.getMinute()) + "&nbsp;&nbsp;  (10)");
			}
			else {
				menu.addMenuElement(grupNum, extraZero.format(start.getHour()) + ":" + extraZero.format(start.getMinute()));
			}
		}

		return menu;
	}

	public void executeUpdate(IWContext modinfo) throws SQLException, FinderException {
		String[] member_id = modinfo.getParameterValues("member_id");
		String[] grup_nums = modinfo.getParameterValues("grup_num");

		String[] tee_number = modinfo.getParameterValues("tee_number");
		String[] to_remove = modinfo.getParameterValues("remove_startingtime");

		String tournament_round_id = modinfo.getParameter("tournament_round");

		if (tournament_round_id != null) {
			TournamentRound trou = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
			TournamentController.invalidateStartingTimeCache(modinfo, trou.getTournamentID(), tournament_round_id);

			if (to_remove != null) {
				String[] flipp;
				Startingtime sTime;
				TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
				for (int i = 0; i < to_remove.length; i++) {
					try {
						flipp = com.idega.data.SimpleQuerier.executeStringQuery("Select s.startingtime_id from startingtime s, tournament_round_startingtime trs WHERE trs.startingtime_id = s.startingtime_id AND trs.tournament_round_id = " + tournament_round_id + " AND s.member_id = " + to_remove[i] + "");
						if (flipp.length > 0) {
							sTime = ((StartingtimeHome) IDOLookup.getHomeLegacy(Startingtime.class)).findByPrimaryKey(Integer.parseInt(flipp[0]));
							sTime.removeFrom(tournamentRound);
							sTime.delete();
						}
					}
					catch (Exception e) {
					}
				}
			}

			String startingtime_id;
			String grup_num;
			Startingtime sTime;

			if (grup_nums != null) {
				Startingtime[] sTimes;
				TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
				Tournament tournament = tournamentRound.getTournament();
				Member member;
				for (int i = 0; i < grup_nums.length; i++) {
					String grup_num1 = modinfo.getParameter("grup_num_dropdown_grup_num_" + grup_nums[i] + "_tee_num_1");
					String grup_num10 = modinfo.getParameter("grup_num_dropdown_grup_num_" + grup_nums[i] + "_tee_num_10");
					grup_num = modinfo.getParameter("grup_num_dropdown_grup_num_" + grup_nums[i] + "_tee_num_" + tee_number[i]);
					//		System.out.println("[-=Gimmi=-] GrupNum["+i+"] TeeNum 1 =
					// "+grup_num1);
					//		System.out.println("[-=Gimmi=-] GrupNum["+i+"] TeeNum 10=
					// "+grup_num10);
					//		System.out.println("[-=Gimmi=-] GrupNum["+i+"] TeeNum["+i+"] =
					// "+grup_num);
					if (grup_num1 != null) {
						if (!"0".equals(grup_num1))
							grup_num = grup_num1;
					}
					if (grup_num10 != null) {
						if (!"0".equals(grup_num10))
							grup_num = grup_num10;
					}
					//		System.out.println("[-=Gimmi=-] GrupNum after fix = "+grup_num);
					if (grup_num != null) {
						if (!grup_num.equals("0")) {
							List members = TournamentController.getMembersInStartingGroup(tournament, tournamentRound, Integer.parseInt(grup_nums[i]));
							if (members != null) {
								for (int g = 0; g < members.size(); g++) {
									member = (Member) members.get(g);
									String memberId = modinfo.getParameter("grup_num_" + grup_nums[i] + "_member_checkbox_member_id_" + member.getID());
									if (memberId != null) {
										sTimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("select startingtime.* from startingtime, tournament_round_startingtime where startingtime.member_id = " + member.getID() + " AND tournament_round_startingtime.startingtime_id = startingtime.startingtime_id AND tournament_round_startingtime.tournament_round_id = " + tournamentRound.getID() + " AND startingtime.grup_num = " + grup_nums[i]);
										for (int j = 0; j < sTimes.length; j++) {
											if (grup_num.indexOf("_") > 0) {
												//System.out.println("[-=Gimmi=-] setting scorecard to
												// tee 10");
												sTimes[j].setTeeNumber(10);
												sTimes[j].setGroupNum(Integer.parseInt(grup_num.substring(0, (grup_num.length() - 1))));
											}
											else {
												//System.out.println("[-=Gimmi=-] setting scorecard to
												// tee 1");
												sTimes[j].setTeeNumber(1);
												sTimes[j].setGroupNum(Integer.parseInt(grup_num));
											}
											sTimes[j].update();
										}
									}
								}
							}
						}
					}
				}
			}

			String sub_action = modinfo.getParameter("sub_action");
			if (sub_action != null) {
				if (sub_action.equals("addNew")) {
					if (member_id != null) {
						Member member;
						TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
						Tournament tournament = tournamentRound.getTournament();
						for (int i = 0; i < member_id.length; i++) {
							startingtime_id = modinfo.getParameter("startingtime_id_member_id_" + member_id[i]);
							grup_num = modinfo.getParameter("grup_num_dropdown_member_id_" + member_id[i]);

							if ((startingtime_id != null) && (grup_num != null)) {

								if (!grup_num.equals("0")) {
									sTime = ((StartingtimeHome) IDOLookup.getHomeLegacy(Startingtime.class)).findByPrimaryKey(Integer.parseInt(startingtime_id));
									try {
										sTime.setGroupNum(Integer.parseInt(grup_num));
										sTime.setTeeNumber(1);
									}
									catch (NumberFormatException n) {
										sTime.setGroupNum(Integer.parseInt(TextSoap.findAndCut(grup_num, "_")));
										sTime.setTeeNumber(10);
									}
									sTime.update();
								}
							}
							else if ((startingtime_id == null) && (grup_num != null)) {
								if (!grup_num.equals("0")) {
									member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id[i]));
									try {
										TournamentController.setupStartingtime(modinfo, member, tournament, Integer.parseInt(tournament_round_id), Integer.parseInt(grup_num), 1);
									}
									catch (NumberFormatException n) {
										TournamentController.setupStartingtime(modinfo, member, tournament, Integer.parseInt(tournament_round_id), Integer.parseInt(TextSoap.findAndCut(grup_num, "_")), 10);
									}
								}
							}
						}
					}
				}
			}

		}

	}

	public int getGrupNum(TournamentRound tRound, IWTimestamp stamp) {
		int returner = 0;

		Tournament tournament = tRound.getTournament();
		IWTimestamp startStamp = new IWTimestamp(tRound.getRoundDate());
		stamp.addMinutes(1);

		int interval = tournament.getInterval();

		if (stamp.isLaterThan(startStamp)) {
			while (stamp.isLaterThan(startStamp)) {
				startStamp.addMinutes(interval);
				++returner;
			}
		}
		else {
			returner = -10;
		}

		return returner;
	}

	public Form drawLeftOuts(IWContext modinfo, Tournament tournament, String tournament_round_id) throws SQLException, FinderException {

		Form form = new Form();
		Table table = new Table();
		form.add(table);
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(1);
		table.setAlignment("center");
		table.setWidth("85%");
		table.add(new HiddenInput("tournament_round", tournament_round_id));
		table.add(new HiddenInput("action", "update"));
		table.add(new HiddenInput("sub_action", "addNew"));

		int tournamentRoundId = 0;
		TournamentRound[] tRounds = tournament.getTournamentRounds();

		if (tournament_round_id != null) {
			tournamentRoundId = Integer.parseInt(tournament_round_id);
		}
		else {
			if (tRounds.length > 0) {
				tournamentRoundId = tRounds[0].getID();
			}
		}

		if (tournamentRoundId != 0) {
			TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournamentRoundId);

			int row = 0;

			DropdownMenu availableGrupNums = getAvailableGrupNums(tournament, tournamentRound);
			availableGrupNums.setStyleAttribute("font-size: 8pt");
			DropdownMenu clonedMenu;

			++row;
			table.add("Án rástíma", 1, row);
			Member[] leftOut = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll("select * from member where member_id in (Select member_id from tournament_member where tournament_id = " + tournamentRound.getTournamentID() + ") AND member_id not in (select s.member_id from startingtime s, tournament_round_startingtime trs where trs.startingtime_id = s.startingtime_id and trs.tournament_round_id = " + tournamentRound.getID() + " )    ");
			table.add(" : " + leftOut.length, 1, row);
			for (int i = 0; i < leftOut.length; i++) {
				++row;
				clonedMenu = (DropdownMenu) availableGrupNums.clone();
				clonedMenu.setName("grup_num_dropdown_member_id_" + leftOut[i].getID());

				table.add(new HiddenInput("member_id", leftOut[i].getID() + ""));
				table.add(clonedMenu, 2, row);
				table.add(leftOut[i].getName(), 3, row);
			}
			++row;
			table.mergeCells(3, row, 5, row);
			table.setAlignment(3, row, "right");
			table.add(TournamentController.getAheadButton(modinfo, "", ""), 3, row);
		}

		return form;

	}

}