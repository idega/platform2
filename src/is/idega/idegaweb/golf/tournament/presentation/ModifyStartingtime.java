/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import java.rmi.RemoteException;
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
				add(getMessageText(localize("tournament.no_tournament_selected","No tournament selected")));
			}

		}
		catch (Exception ex) {
			System.err.println("{ Aron debugging catching errors}");
			ex.printStackTrace();
		}

	}

	public Form getEditableStartingtimeForm(IWContext modinfo, Tournament tournament, String tournament_round_id) throws SQLException, FinderException, RemoteException {

		Form form = new Form();
		Table table = new Table();
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);

		int row = 0;


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

		Table myTable = new Table();
		myTable.setCellspacing(0);
		myTable.setWidth(Table.HUNDRED_PERCENT);
		myTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		IWTimestamp tourDay;
		DropdownMenu rounds = (DropdownMenu)getStyledInterface(new DropdownMenu("tournament_round"));

		for (int i = 0; i < tRounds.length; i++) {
			tourDay = new IWTimestamp(tRounds[i].getRoundDate());
			rounds.addMenuElement(tRounds[i].getID(), tRounds[i].getName() + " " + tourDay.getLocaleDate(modinfo.getCurrentLocale()));
		}

		if (tournamentRoundId != 0) {
			rounds.setSelectedElement(Integer.toString(tournamentRoundId));
		}
		rounds.setToSubmit();
		myTable.add(rounds, 1, 1);
		form.add(myTable);

		++row;
		Text rast = getSmallHeader(localize("tournament.teetimes", "Teetimes"));
		Text move =  getSmallHeader(localize("tournament.move","Move"));
		Text name =  getSmallHeader(localize("tournament.name","Name"));
		Text club =  getSmallHeader(localize("tournament.club","Club"));
		Text hand =  getSmallHeader(localize("tournament.handicap","Handicap"));
		Text del =  getSmallHeader(localize("tournament.delete","Delete"));
		Text tee =  getSmallHeader(localize("tournament.tee","Tee"));


		table.add(rast, 1, row);
		table.add(move, 2, row);
		table.add(name, 3, row);
		table.add(club, 4, row);
		table.add(hand, 5, row);
		table.add(del, 6, row);
		table.setRowStyleClass(row,getHeaderRowClass());
		
		table.setColumnWidth(1,"100");
		table.setColumnWidth(2,"70");
		table.setColumnWidth(4,"70");
		table.setColumnWidth(5,"70");
		table.setColumnWidth(6,"70");
		

		if (tournamentRoundId != 0) {
			TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournamentRoundId);

			boolean displayTee = false;
			if (tournamentRound.getStartingtees() > 1) {
				displayTee = true;
			}
			
			addHeading(tournament.getName());

			DropdownMenu availableGrupNums = getAvailableGrupNums(tournament, tournamentRound);
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
					Text startTee = getText(localize("tournament.tee","Tee")+" : " + tee_number);
					table.add(startTee, 1, row);
					table.setRowColor(row, "#336661");
					table.mergeCells(1, row, 6, row);
					table.setAlignment(1, row, "center");
				}

				Text errorText = new Text("Ekki pl‡ss");
				errorText.setFontColor("RED");

				while (end.isLaterThan(start)) {

					++grupNum;
					++row;

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
					table.setStyleClass(1, row, getBigRowClass());
					table.mergeCells(1, row, 1, row + numberInGroup - 1);

					if (sTimes.length > 0) {
						table.add(clonedMenu, 1, row);
						table.add(new HiddenInput("grup_num", Integer.toString(grupNum)), 1, row);
					}


					for (int i = 0; i < sTimes.length; i++) {
						String rowClass =  (row%2==0)?getLightRowClass():getDarkRowClass();
						table.setStyleClass(2, row, rowClass);
						table.setStyleClass(3, row, rowClass);
						table.setStyleClass(4, row, rowClass);
						table.setStyleClass(5, row, rowClass);
						table.setStyleClass(6, row, rowClass);

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
						String rowClass =  (row%2==0)?getLightRowClass():getDarkRowClass();
						table.add("&nbsp;", 3, row);
						table.setStyleClass(2, row, rowClass);
						table.setStyleClass(3, row, rowClass);
						table.setStyleClass(4, row, rowClass);
						table.setStyleClass(5, row, rowClass);
						table.setStyleClass(6, row, rowClass);
						++row;
					}

					--row;
				}
			}

		}
		else {
			add("M—tiÝ er ekki sett upp rŽtt");
		}

		++row;
		table.mergeCells(1, row, 2, row);
		table.mergeCells(3, row, 6, row);

		Link link = getLocalizedLink("tournament.add_teetimes","Add teetimes");
		link.addParameter("action", "addMember");
		link.addParameter("tournament_round", tournamentRoundId + "");
		link.addParameter("tournament", tournament.getID() + "");

		table.add(link, 1, row);

		table.add(getTournamentBusiness(modinfo).getAheadButton(modinfo, "", ""), 3, row);

		table.add(new HiddenInput("action", "update"), 3, row);
		table.add(new HiddenInput("tournament_round", tournamentRoundId + ""), 3, row);

		table.setAlignment(1, row, "right");
		table.setColumnAlignment(1, "center");
		table.setColumnAlignment(2, "center");
		table.setColumnAlignment(4, "center");
		table.setColumnAlignment(5, "center");
		table.setColumnAlignment(6, "center");
		table.setAlignment(1, 1, "center");
		table.setAlignment(3, 1, "left");
		table.setAlignment(3, row, "right");

		Table borderTable = new Table();
		borderTable.setWidth(Table.HUNDRED_PERCENT);
		borderTable.setCellpaddingAndCellspacing(0);
		borderTable.setCellBorder(1, 1, 1, "#3A5A20", "solid");
		borderTable.add(table);
		form.add(borderTable);
		return form;
	}

	public DropdownMenu getAvailableGrupNums(Tournament tournament, TournamentRound tRound) throws SQLException {
		DropdownMenu menu = (DropdownMenu)getStyledInterface(new DropdownMenu("grup_num_dropdown"));

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

	public void executeUpdate(IWContext modinfo) throws FinderException, RemoteException, SQLException {
		String[] member_id = modinfo.getParameterValues("member_id");
		String[] grup_nums = modinfo.getParameterValues("grup_num");

		String[] tee_number = modinfo.getParameterValues("tee_number");
		String[] to_remove = modinfo.getParameterValues("remove_startingtime");

		String tournament_round_id = modinfo.getParameter("tournament_round");

		if (tournament_round_id != null) {
			TournamentRound trou = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
			getTournamentBusiness(modinfo).invalidateStartingTimeCache(modinfo, trou.getTournamentID(), tournament_round_id);

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
							List members = getTournamentBusiness(modinfo).getMembersInStartingGroup(tournament, tournamentRound, Integer.parseInt(grup_nums[i]));
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
										getTournamentBusiness(modinfo).setupStartingtime(modinfo, member, tournament, Integer.parseInt(tournament_round_id), Integer.parseInt(grup_num), 1);
									}
									catch (NumberFormatException n) {
										getTournamentBusiness(modinfo).setupStartingtime(modinfo, member, tournament, Integer.parseInt(tournament_round_id), Integer.parseInt(TextSoap.findAndCut(grup_num, "_")), 10);
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

	public Form drawLeftOuts(IWContext modinfo, Tournament tournament, String tournament_round_id) throws SQLException, FinderException, RemoteException {

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
			table.mergeCells(3, row, 6, row);
			table.setAlignment(3, row, "right");
			table.add(getTournamentBusiness(modinfo).getAheadButton(modinfo, "", ""), 3, row);
		}

		return form;

	}

}