package is.idega.idegaweb.golf.startingtime.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import com.idega.core.builder.data.ICPage;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.Image;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.GolfField;
import is.idega.idegaweb.golf.TableInfo;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusinessBean;
import is.idega.idegaweb.golf.startingtime.data.TeeTime;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * Title: TeeTimeSearch 
 * 
 * Description: 
 * Copyright: Copyright (c) 2004 
 * Company: idega Software
 * 
 * @author 2004 - idega team -<br>
 *         <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson </a> <br>
 * @version 1.0
 */

public class TeeTimeTable extends GolfBlock {

	TeeTimeBusinessBean service = new TeeTimeBusinessBean();
	
	private ICPage _teeTimeSearchPage = null;
	private ICPage _teeTimesPage = null;
	
	private String _blockWidth = Table.HUNDRED_PERCENT;

	public void main(IWContext modinfo) throws Exception {
		getParentPage().setTitle(localize("start.teetimes", "Tee Times"));
		IWCalendar funcDate = new IWCalendar();
		IWTimestamp stamp = new IWTimestamp();

		IWTimestamp noon = new IWTimestamp(1, 2, 1, 14, 0, 0);
		noon.setAsTime();

		try {
			boolean clubField = false;

			if (modinfo.getSessionAttribute("when") == null && modinfo.getParameter("hvenaer") == null) {
				IWTimestamp now = IWTimestamp.RightNow();
				now.setAsTime();
				//if(now.isLaterThan(afternoon)){
				//modinfo.setSessionAttribute("when","2");
				//}else
				if (now.isLaterThan(noon)) {
					modinfo.setSessionAttribute("when", "1");
				} else {
					modinfo.setSessionAttribute("when", "0");
				}
			} else if (modinfo.getParameter("hvenaer") != null) {
				modinfo.setSessionAttribute("when", modinfo.getParameter("hvenaer"));
			}

			if ((modinfo.getSessionAttribute("union_id") == null || (!(modinfo.getSessionAttribute("union_id").equals(modinfo.getParameter("club"))) && modinfo.getParameter("club") != null)) && modinfo.getParameter("search") == null) clubField = true;

			if (modinfo.getSessionAttribute("union_id") == null && modinfo.getParameter("club") == null)
				modinfo.setSessionAttribute("union_id", "1"); //  Exception
			else if (modinfo.getParameter("club") != null) modinfo.setSessionAttribute("union_id", modinfo.getParameter("club"));

			if ((clubField && modinfo.getParameter("hvar") == null) || (modinfo.getSessionAttribute("field_id") == null && modinfo.getParameter("hvar") == null))
				modinfo.setSessionAttribute("field_id", "" + service.getFirstField((String) modinfo.getSessionAttribute("union_id")));
			else if (modinfo.getParameter("hvar") != null) modinfo.setSessionAttribute("field_id", modinfo.getParameter("hvar"));

			if (modinfo.getSessionAttribute("date") == null && modinfo.getParameter("day") == null)
				modinfo.setSessionAttribute("date", new IWTimestamp().toSQLDateString());
			else if (modinfo.getParameter("day") != null) modinfo.setSessionAttribute("date", modinfo.getParameter("day"));

			if (modinfo.getSessionAttribute("member_id") == null && getMember() == null)
				modinfo.setSessionAttribute("member_id", "1"); // Exception
			else if (getMember() != null) modinfo.setSessionAttribute("member_id", "" + getMember().getID());

			if (modinfo.getSessionAttribute("member_main_union_id") == null && getMember() == null)
				modinfo.setSessionAttribute("member_main_union_id", "1"); // Exception
			else if (getMember() != null) modinfo.setSessionAttribute("member_main_union_id", "" + getMember().getMainUnionID());

			String field_id2 = (String) modinfo.getSessionAttribute("field_id");

			if (field_id2 != null && !field_id2.equals("-1")) {

				modinfo.setSessionAttribute("golf_union_id", (String) modinfo.getSessionAttribute("union_id"));


				IWTimestamp currentDay = new IWTimestamp((String) modinfo.getSessionAttribute("date"));

				if (isAdmin() || (isClubAdmin() && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))) || (isClubWorker() && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id")))) {
					//add(getConfigLinks(modinfo, "admin"));
					add(User(modinfo, funcDate));
				} else {
					//add(getConfigLinks(modinfo, "others"));
					add(User(modinfo, funcDate));
				}
			} else {
				add(getErrorText(localize("start.session_expired", "Session expired")));
			}

		} catch (SQLException E) {
			System.out.print("SQLException: " + E.getMessage());
			System.out.print("SQLState:     " + E.getSQLState());
			System.out.print("VendorError:  " + E.getErrorCode());
		} catch (Exception E) {
			E.printStackTrace();
		} finally {
		}
	}

	public Table getConfigLinks(IWContext modinfo, String AccessType) {
		/*
		 * if (getServletContext().getAttribute(AccessType+"-" + When ) != null)
		 * temp = (Table)getServletContext().getAttribute(AccessType+"-" + When );
		 * else{ temp = TableConfig(
		 * ((String)getModuleInfo().getSession().getAttribute("when")),
		 * AccessType ); getServletContext().setAttribute(AccessType + "-" +
		 * When, temp); } return temp;
		 */
		return TableConfig(modinfo, ((String) modinfo.getSessionAttribute("when")), AccessType);
	}

	//	 overwrites isUser from template
	public boolean isUser(IWContext modinfo) {
		return (getMember() != null);
	}

	public Table User(IWContext modinfo, IWCalendar funcDate) throws SQLException, IOException {
		IWTimestamp stamp = new IWTimestamp(funcDate.getCalendar());

		GolfField myField = null;
		GolfField Today = new GolfField();
		TableInfo myTableInfo = new TableInfo();
		Table mainTable = new Table(1, 3);
		Table RTTop = new Table(2, 1);
		RTTop.setCellpadding(0);
		RTTop.setCellspacing(0);
		//RTTop.setBorder(1);


		mainTable.setCellspacing(0);
		mainTable.setCellpadding(0);
		mainTable.setCellpadding(1,1,15);
		mainTable.setRowAlignment(1,Table.HORIZONTAL_ALIGN_RIGHT);
		mainTable.setWidth(_blockWidth);
		//mainTable.setBorder(1);

		String union_id = modinfo.getSessionAttribute("union_id").toString();

		myField = get_field_info(modinfo, Integer.parseInt(modinfo.getSessionAttribute("field_id").toString()), modinfo.getSessionAttribute("date").toString(), union_id);
		Today = get_field_info(modinfo, Integer.parseInt(modinfo.getSessionAttribute("field_id").toString()), stamp.toSQLDateString(), union_id);

		if (myField != null && Today != null) {
			myTableInfo = get_table_info(myField, Integer.parseInt(modinfo.getSessionAttribute("when").toString()));

			int interval = myTableInfo.get_interval();
			IWTimestamp currentDay = new IWTimestamp((String) modinfo.getSessionAttribute("date"));
			Vector tournamentGroups = new Vector(0);
			int tournamentGroupsIndex = 0;
			List TournamentRounds = EntityFinder.findAll((TournamentRound) IDOLookup.instanciateEntity(TournamentRound.class), "select tournament_round.* from tournament,tournament_round where tournament_round.tournament_id=tournament.tournament_id and tournament_round.round_date >= '" + currentDay.toSQLDateString() + " 00:00' and tournament_round.round_date <= '" + currentDay.toSQLDateString() + " 23:59' and tournament.field_id = " + modinfo.getSessionAttribute("field_id"));

			if (TournamentRounds != null) {
				for (int i = 0; i < TournamentRounds.size(); i++) {
					TournamentRound tempRound = (TournamentRound) TournamentRounds.get(i);

					IWTimestamp begin = new IWTimestamp(tempRound.getRoundDate());
					begin.setAsTime();
					IWTimestamp End_ = new IWTimestamp(tempRound.getRoundEndDate());
					End_.setAsTime();
					/**
					 *  
					 */
					End_.addMinutes(myTableInfo.get_interval());
					IWTimestamp begintime = new IWTimestamp(0, 1, 0, myField.get_open_hour(), myField.get_open_min(), 0);
					begintime.setAsTime();

					int firstGroup = IWTimestamp.getMinutesBetween(begintime, begin) / interval;
					int groupCount = IWTimestamp.getMinutesBetween(begin, End_) / interval;
					int[] tempBeginGroupAndEnd = new int[2];

					tempBeginGroupAndEnd[0] = firstGroup + 1;
					tempBeginGroupAndEnd[1] = firstGroup + groupCount;

					String tournamentName = tempRound.getTournament().getName();

					tournamentGroups.add(tournamentGroupsIndex++, tournamentName);
					tournamentGroups.add(tournamentGroupsIndex++, tempBeginGroupAndEnd);

				}

			}

			mainTable.add(draw_table(myTableInfo, tournamentGroups, modinfo), 1, 2);

			RTTop.add(TimeAndPlace(modinfo, funcDate, Today), 2, 1);

			IWTimestamp opent = new IWTimestamp(1, 1, 1, 7, 59, 59);
			opent.setAsTime();
			IWTimestamp now = IWTimestamp.RightNow();
			now.setAsTime();

			if ((isAdmin() || (isClubAdmin() && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))) || (isClubWorker() && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))))) {
				RTTop.add(entry_part(modinfo, funcDate, myTableInfo, true, tournamentGroups), 2, 1);
			} else if ((isUser(modinfo) && myField.nonMemberRegistration()) || (isMemberOfUnion(modinfo, modinfo.getSessionAttribute("union_id").toString()) && myField.publicRegistration())) {
				/** @todo implement for all unions */
				if (modinfo.getSessionAttribute("union_id").equals("100")) {
					if (now.isLaterThan(opent)) {
						RTTop.add(entry_part(modinfo, funcDate, myTableInfo, false, tournamentGroups), 2, 1);
					}
				} else {
					RTTop.add(entry_part(modinfo, funcDate, myTableInfo, false, tournamentGroups), 2, 1);
				}
			}

			mainTable.add(RTTop, 1, 1);
		} else {

			mainTable = new Table();
			mainTable.add(getErrorText(localize("start.session_expired", "Session expired")));

		}
		return mainTable;

	}

	public boolean isMemberOfUnion(IWContext modinfo, String union) {
		try {
			if (union == null) return false;

			if (getMember() == null) return false;

			int union_id = new Integer(union).intValue();
			Union unionentry = (Union) IDOLookup.createLegacy(Union.class);
			unionentry.setID(union_id);

			return getMember().isMemberInUnion(unionentry);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Table draw_table(TableInfo myTableInfo, Vector tournamentGroups, IWContext modinfo) throws SQLException, IOException {
		int end = myTableInfo.get_row_num();
		int interval = myTableInfo.get_interval();
		int pic_min = myTableInfo.get_first_pic_min();
		int pic_hour = myTableInfo.get_first_pic_hour();
		int first_group = myTableInfo.get_first_group();
		int tafla = 0;
		String time;

		Table myTable = new Table(2, end+1);
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		//myTable.setBorder(1);
		myTable.setRowStyleClass(1,getHeaderRowClass());
		myTable.add(getSmallHeader(getResourceBundle().getLocalizedString("start.time", "Time")),1,1);
		myTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_LEFT);
		myTable.setRowVerticalAlignment(1,Table.VERTICAL_ALIGN_BOTTOM);

		myTable.setWidth(_blockWidth);
		myTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_CENTER);
		myTable.setColumnWidth(1, "200");

		Table[] group1 = new Table[end];

		Table group1Template = new Table(3, 4);
		//group1Template.setBorder(1);
		
		group1Template.setWidth(Table.HUNDRED_PERCENT);
		//group1Template.setRowWidth(1,"100%");
		group1Template.setColumnWidth(2,"100");
		group1Template.setColumnWidth(3,"100");
		group1Template.setCellspacing(0);
		group1Template.setCellpaddingRight(2,1,30);
		group1Template.setCellpaddingRight(2,2,30);
		group1Template.setCellpaddingRight(2,3,30);
		group1Template.setCellpaddingRight(2,4,30);
		group1Template.setColumnAlignment(2,Table.HORIZONTAL_ALIGN_RIGHT);
		group1Template.setColumnAlignment(3,Table.HORIZONTAL_ALIGN_CENTER);
		
		
		Table headerTable = (Table) group1Template.clone();
		headerTable.setRows(1);
		headerTable.add(getText(Text.NON_BREAKING_SPACE),1,1);
		headerTable.add(getSmallHeader(getResourceBundle().getLocalizedString("start.name", "Name")),1,1);
		headerTable.add(getSmallHeader(getResourceBundle().getLocalizedString("start.club", "Club")),3,1);
		headerTable.add(getSmallHeader(getResourceBundle().getLocalizedString("start.handicap", "Handicap")),2,1);
		myTable.add(headerTable,2,1);
		
		
		group1Template.add(getText(Text.NON_BREAKING_SPACE),1,1);
		group1Template.add(getText(Text.NON_BREAKING_SPACE),1,2);
		group1Template.add(getText(Text.NON_BREAKING_SPACE),1,3);
		group1Template.add(getText(Text.NON_BREAKING_SPACE),1,4);
		
		group1Template.setRowStyleClass(1,getLightRowClass());
		group1Template.setRowStyleClass(2,getDarkRowClass());
		group1Template.setRowStyleClass(3,getLightRowClass());
		group1Template.setRowStyleClass(4,getDarkRowClass());
		
		

		Table roundGroup1Template = (Table) group1Template.clone();

		

		Text templateText = getSmallText("");

		int teljari = 0;

		for (int i = 1; i <= end; i++) {
			String TournamentName = getTournamentName(tournamentGroups, first_group + teljari++);
			if (TournamentName == null) {
				group1[tafla] = (Table) group1Template.clone();

			} else {
				Text tName = (Text) templateText.clone();
				tName.setText(TournamentName);
				group1[tafla] = (Table) roundGroup1Template.clone();
				group1[tafla].add(tName, 1, 1);
				group1[tafla].add(tName, 1, 2);
				group1[tafla].add(tName, 1, 3);
				group1[tafla].add(tName, 1, 4);
			}
			myTable.add(group1[tafla], 2, i+1);
			tafla++;
		} // stilling · tˆflum endar
		teljari = 0;



		Text tTime = getText("");
		DecimalFormat extraZero = new DecimalFormat("00");
//		tTime.setFontStyle("letter-spacing:0px;font-family:Arial,Helvetica,sans-serif;background-color:#FFFFFF;font-size:18px;color:#2C4E3B;border-width:1px;font-weight:bold;border-style:solid;");
		Text timeText;

		for (int i = 1; i <= end; i++) {
			if (pic_min >= 60) {
				pic_min -= 60;
				pic_hour++;
			}
			timeText = (Text) tTime.clone();
			timeText.setText(extraZero.format(pic_hour)+":"+extraZero.format(pic_min));
			myTable.add(timeText, 1, i+1);
			myTable.setStyleClass(1, i+1, getBigRowClass());
			pic_min += interval;
		}
		
		

		
		int row, row2, count, first_gr;
		float handic;
		int last_group = first_group + end;
		int field = myTableInfo.get_field_id();
		first_gr = first_group + 1;
		String name, club, handicap;

		TeeTime[] start = service.getTableEntries(modinfo.getSessionAttribute("date").toString(), first_group, last_group, field);

		count = 1;
		row = -1;
		row2 = -2;

		DecimalFormat handicapFormat = new DecimalFormat("###.0");
		for (int i = 0; i < start.length; i++) {
			int groupNumber = start[i].getGroupNum();
			if (!isTournamentGroup(tournamentGroups, groupNumber)) {

				row = (groupNumber - first_group + 1);

				if (row == row2)
					count++;
				else
					count = 1;

				row2 = row;

				handic = start[i].getHandicap();
				name = start[i].getPlayerName();
				club = start[i].getClubName();

				if (handic < 1)
					handicap = "-";
				else if (handic == 100)
					handicap = "-";
				else
					handicap = handicapFormat.format((double) handic);

				// Not visible on the net...
				/*
				 * try { Member member = start[i].getMember(); UnionMemberInfo
				 * uni = member.getUnionMemberInfo(member.getMainUnionID()); if (
				 * !uni.getVisible() ) "; } catch (Exception e) { name =
				 * start[i].getPlayerName();
				 */

				Text tempHandicap = (Text) templateText.clone();
				tempHandicap.setText(handicap);
				Text tempName = (Text) templateText.clone();
				tempName.setText(Text.NON_BREAKING_SPACE + name);
				Text tempClub = (Text) templateText.clone();
				tempClub.setText(club);

				switch (count) {
					case 1:
						group1[row - 1].add(tempName, 1, 1);
						group1[row - 1].add(tempHandicap, 2, 1);
						group1[row - 1].add(tempClub, 3, 1);
						break;
					case 2:
						group1[row - 1].add(tempName, 1, 2);
						group1[row - 1].add(tempHandicap, 2, 2);
						group1[row - 1].add(tempClub, 3, 2);
						break;
					case 3:
						group1[row - 1].add(tempName, 1, 3);
						group1[row - 1].add(tempHandicap, 2, 3);
						group1[row - 1].add(tempClub, 3, 3);
						break;
					case 4:
						group1[row - 1].add(tempName, 1, 4);
						group1[row - 1].add(tempHandicap, 2, 4);
						group1[row - 1].add(tempClub, 3, 4);
						break;
				}
			}
		}
		// fÊrslur komnar inn

		return myTable;
	}

	public boolean isTournamentGroup(Vector rounds, int groupNumber) {

		for (int c = 0; c < rounds.size(); c += 2) {
			int[] temp = (int[]) rounds.get(c + 1);
			if (groupNumber >= temp[0] && groupNumber <= temp[1]) {
				return true;
			}
		}
		return false;
	}

	public String getTournamentName(Vector rounds, int groupNumber) {

		for (int c = 0; c < rounds.size(); c += 2) {
			int[] temp = (int[]) rounds.get(c + 1);
			if (groupNumber >= temp[0] && groupNumber <= temp[1]) {
				return (String) rounds.get(c);
			}
		}
		return null;
	}

	public GolfField get_field_info(IWContext modinfo, int field, String date, String union_id) throws SQLException, IOException {
		StartingtimeFieldConfig FieldConfig = service.getFieldConfig(field, date);
		if (FieldConfig == null) return null;

		GolfField field_info = null;
		if (isAdmin() || isClubAdmin() || isClubWorker() || isMemberOfUnion(modinfo, union_id))
			field_info = new GolfField(new IWTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new IWTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShown(), FieldConfig.publicRegistration());
		else
			field_info = new GolfField(new IWTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new IWTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShownNonMember(), FieldConfig.publicRegistration());

		field_info.setNonMemberRegistration(FieldConfig.getNonMemberRegistration());

		return field_info;
	}

	public TableInfo get_table_info(GolfField field, int daytime) {

		int col = 10;

		int row, first_pic_hour, first_pic_min, interval, time;
		int time1, time2, time3;
		int first_group;

		interval = field.get_interval();

		switch (daytime) {
			case 0:
				time = (14 - field.get_open_hour()) * 60 - field.get_open_min();
				row = (time / interval);
				first_group = 1;
				first_pic_hour = field.get_open_hour();
				first_pic_min = field.get_open_min();
				break;
			/*
			 * case 1: 60; row = (time / interval); 60-field.get_open_min()) /
			 * interval)+1; first_pic_hour = 13; first_pic_min = 0;
			 */
			case 1:
				time = (field.get_close_hour() - 14) * 60 + field.get_close_min();
				row = (time / interval);
				first_group = (((14 - field.get_open_hour()) * 60 - field.get_open_min()) / interval) + 1;
				first_pic_hour = 14;
				first_pic_min = 0;
				break;
			default:
				time = 0;
				row = 0;
				first_pic_hour = 0;
				first_pic_min = 0;
				first_group = 0;
				interval = 0;

		}

		TableInfo info = new TableInfo(col, row, first_pic_hour, first_pic_min, first_group, interval, daytime, field.get_field_id(), field.get_date(), field.get_days_shown());

		return info;

	}

	public SubmitButton insertButton(String btnName, String Method, String Action, Form theForm) {
		SubmitButton mySubmit = new SubmitButton(btnName);
		theForm.addObject(mySubmit);

		theForm.setMethod(Method);
		theForm.setAction(Action);

		return mySubmit;
	}

	private SubmitButton insertButton(Image image, String imageName, String Method, String Action, Form theForm) {
		SubmitButton mySubmit = new SubmitButton(image, imageName);
		theForm.addObject(mySubmit);

		theForm.setMethod(Method);
		theForm.setAction(Action);
		return mySubmit;
	}

	public SubmitButton insertButton(String btnName) {
		SubmitButton mySubmit = new SubmitButton(btnName);
		return mySubmit;
	}

	public SubmitButton insertButton(Image myImage, String btnName) {
		SubmitButton mySubmit = new SubmitButton(myImage, btnName);
		return mySubmit;
	}

	public HiddenInput insertHiddenInput(String inpName, String value, Form theForm) {
		HiddenInput myObject = new HiddenInput(inpName, value);
		theForm.addObject(myObject);

		return myObject;
	}

	public DropdownMenu insertDropdown(String dropdownName, IWContext modinfo) throws IOException {
		PrintWriter out = modinfo.getResponse().getWriter();
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);
		try {
			Field[] field = service.getFields((String) modinfo.getSession().getAttribute("union_id"));

			for (int i = 0; i < field.length; i++) {
				myDropdown.addMenuElement(field[i].getID(), field[i].getName());
			}
			myDropdown.keepStatusOnAction();
			myDropdown.setToSubmit();
			myDropdown.setSelectedElement(modinfo.getSession().getAttribute("field_id").toString());

		} catch (SQLException E) {
			E.printStackTrace();
		}
		return myDropdown;
	}

	public DropdownMenu insertDropdown(String dropdownName, int countFrom, int countTo) {
		String from = Integer.toString(countFrom);
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		for (; countFrom <= countTo; countFrom++) {
			myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
		}
		myDropdown.keepStatusOnAction();

		return myDropdown;
	}

	public DropdownMenu insertDropdown(String dropdownName, IWCalendar funcDate, GolfField today, IWContext modinfo) {

		IWTimestamp stamp = new IWTimestamp();
		IWTimestamp stampNow = new IWTimestamp();
		int GKGAfter = 10;

		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		//		myDropdown.addMenuElement(funcyDateRS, getNextDays(funcDate,
		// funcyDateRS, 0));

		int daysShown = 0;
		if (isClubAdmin() || isClubWorker() || isAdmin()) {
			if (isClubWorker() && today.get_field_id() == 33) {
				daysShown = today.get_days_shown();
			} else {
				daysShown = 240;
				stamp.addDays(-30);
			}
		} else {
			daysShown = today.get_days_shown();
		}

		boolean addElement = true;
		for (int i = 0; i < daysShown; i++) {
			// SkÌtamix dauans hÈr!!!
			if (today.get_field_id() == 33) {
				if (i > 0 && !isAdmin()) {
					if (stampNow.getHour() >= GKGAfter)
						addElement = true;
					else
						addElement = false;
					//add(" + AddElement: "+addElement);
				} else {
					addElement = true;
				}
			}
			if (addElement) {
				myDropdown.addMenuElement(stamp.toSQLDateString(), stamp.getLocaleDate(modinfo.getCurrentLocale()));
				stamp.addDays(1);
			}
		}

		myDropdown.keepStatusOnAction();
		myDropdown.setToSubmit();
		myDropdown.setSelectedElement(modinfo.getSession().getAttribute("date").toString());

		return myDropdown;
	}

	public DropdownMenu insertDrowdown(String dropdownName, TableInfo myTableInfo, IWContext modinfo, Vector torunamentRounds) throws SQLException, IOException {
		//		PrintWriter out = modinfo.getResponse().getWriter();

		DropdownMenu myDropdown = new DropdownMenu(dropdownName);
		int end = myTableInfo.get_row_num();
		int interval = myTableInfo.get_interval();
		int pic_min = myTableInfo.get_first_pic_min();
		int pic_hour = myTableInfo.get_first_pic_hour();
		int first_group = myTableInfo.get_first_group();
		String Time;
		String TimeVal;
		int val = 0;

		for (int i = 1; i <= end; i++) {

			if (pic_min >= 60) {
				pic_min -= 60;
				pic_hour++;
			}

			if (pic_min < 10)
				Time = pic_hour + ":0" + pic_min;
			else
				Time = pic_hour + ":" + pic_min;

			val = (first_group + i) - 1;
			TimeVal = Integer.toString(val);

			if (!isTournamentGroup(torunamentRounds, val)) {
				myDropdown.addMenuElement(TimeVal, Time);
			}

			pic_min += interval;
		}
		myDropdown.keepStatusOnAction();
		return myDropdown;
	}

	public TextInput insertEditBox(String name) {
		TextInput myInput = new TextInput(name);
		return myInput;
	}

	public TextInput insertEditBox(String name, int size) {
		TextInput myInput = new TextInput(name);
		myInput.setSize(size);
		return myInput;
	}

	public Table TableConfig(IWContext modinfo, String day_time, String access_type) {
		Table frame = new Table(5, 1);

		frame.setCellspacing(0);
		frame.setCellpadding(0);

		int buttonWidth = 77;
		int tableWidth = 720;

		frame.setHeight("15");
		frame.setWidth(1, "" + (tableWidth - 4 * buttonWidth));
		frame.setWidth(2, "" + buttonWidth);
		frame.setWidth(3, "" + buttonWidth);
		frame.setWidth(4, "" + buttonWidth);
		frame.setWidth(5, "" + buttonWidth);

		frame.add(Text.emptyString(), 1, 1);
		frame.add(Text.emptyString(), 2, 1);
		frame.add(Text.emptyString(), 3, 1);
		frame.add(Text.emptyString(), 4, 1);
		frame.add(Text.emptyString(), 5, 1);

		if (access_type.equals("admin")) {
			frame.add(ConfigFieldLink(modinfo), 3, 1);

			int field_id = Integer.parseInt(modinfo.getSessionAttribute("field_id").toString());
			String sDate = (String) modinfo.getSessionAttribute("date");
			IWTimestamp stamp = IWTimestamp.RightNow();
			if (sDate != null) {
				stamp = new IWTimestamp(sDate);
			}
			Link reportLink = is.idega.idegaweb.golf.startingtime.presentation.TeeTimeReportWindow.getLink(stamp, field_id, getResourceBundle().getImage("/tabs/lists1.gif"));
			frame.add(reportLink, 2, 1);
			frame.setAlignment(2, 1, "right");
		} else if (access_type.equals("user")) {
			frame.add(Change(modinfo), 3, 1);
		}

		if (day_time.equals("0")) {
			frame.add(Fhd(modinfo, true), 4, 1);
			frame.add(Ehd(modinfo, false), 5, 1);
		}

		if (day_time.equals("1")) {
			frame.add(Fhd(modinfo, false), 4, 1);
			frame.add(Ehd(modinfo, true), 5, 1);
		}


		return frame;
	}

	public Link Fhd(IWContext modinfo, boolean inUse) {
		Link myLink;
		if (inUse)
			myLink = new Link(getResourceBundle().getImage("tabs/morning.gif"));
		else
			myLink = new Link(getResourceBundle().getImage("tabs/morning1.gif"));

		myLink.addParameter("hvenaer", "0");

		return myLink;
	}

	public Link Ehd(IWContext modinfo, boolean inUse) {
		Link myLink;
		if (inUse)
			myLink = new Link(getResourceBundle().getImage("tabs/afternoon.gif"));
		else
			myLink = new Link(getResourceBundle().getImage("tabs/afternoon1.gif"));

		myLink.addParameter("hvenaer", "1");

		return myLink;
	}

	public Link Sd(IWContext modinfo, boolean inUse) {
		Link myLink;
		if (inUse)
			myLink = new Link(getResourceBundle().getImage("tabs/evening.gif"));
		else
			myLink = new Link(getResourceBundle().getImage("tabs/evening1.gif"));

		myLink.addParameter("hvenaer", "2");

		return myLink;
	}

	public Link ConfigFieldLink(IWContext modinfo) {
		//		Link myLink = new
		// Link(getResourceBundle().getImage("tabs/options1.gif"), new
		// Window("Gluggi", 520, 470, "admin.jsp?"));

		GolfWindow myWindow = new GolfWindow("Gluggi", 520, 320);
		myWindow.add(new TeeTimeFieldConfiguration());
		myWindow.setContentHorizontalAlignment(Table.HORIZONTAL_ALIGN_CENTER);
		myWindow.setContentVerticalAlignment(Table.VERTICAL_ALIGN_MIDDLE);
		Link myLink = new Link(getResourceBundle().getImage("tabs/options1.gif"), myWindow);

		//		Link myLink = new
		// Link(getResourceBundle().getImage("tabs/options1.gif"),
		// StartingTimeAdmin.class);

		return myLink;
	}

	public Link Change(IWContext modinfo) {
		//Link myLink = new
		// Link(getResourceBundle().getImage("tabs/change1.gif"), new
		// Window("Gluggi", 800, 600, "admbreyting.jsp?"));

		Window myWindow = new GolfWindow("Gluggi", 800, 600);
		myWindow.add(new AdminRegisterTeeTime());
		Link myLink = new Link(getResourceBundle().getImage("tabs/change1.gif"), myWindow);

		//Link myLink = new
		// Link(getResourceBundle().getImage("tabs/change1.gif"),
		// StartingTimeAdminChange.class);

		return myLink;
	}

	public Form TimeAndPlace(IWContext modinfo, IWCalendar dateFunc, GolfField Today) throws SQLException, IOException {

		Table firstTable = new Table(2, 1);
		Form day_field = new Form();
		day_field.setMethod("get");
		firstTable.setCellpadding(0);
		firstTable.setCellspacing(0);
		firstTable.setCellpaddingLeft(2,1,10);
		firstTable.setCellpaddingLeft(1,1,10);

		firstTable.add(insertDropdown("hvar", modinfo), 2, 1);
		firstTable.add(insertDropdown("day", dateFunc, Today, modinfo), 1, 1);

		day_field.add(firstTable);

		return day_field;

	}

	public Table entry_part(IWContext modinfo, IWCalendar dateFunc, TableInfo myTableInfo, boolean admin, Vector tournamentRounds) throws SQLException, IOException {

		GolfWindow theWindow = null;
		Form mainForm = null;
		boolean plainUser = true;
		if (admin) {
			mainForm = new Form();
			mainForm.setWindowToOpen(AdminRegisterTimeWindow.class);
			//		mainForm.setAction("registeradmin.jsp");
			plainUser = false;
		} else {
			mainForm = new Form();
			mainForm.setWindowToOpen(RegisterTimeWindow.class);
			mainForm.setMethod("get");
			//		mainForm.setAction("register.jsp");
			plainUser = true;
		}

		mainForm.add(new HiddenInput("daytime", (String) modinfo.getSessionAttribute("when")));

		Window updateWindow = new Window("Gluggi", 800, 600);//,
															 // "admbreyting.jsp");
		updateWindow.add(new AdminRegisterTeeTime());

		Table fTable = new Table(2, 2);
		Table entry = new Table(7, 1);

		fTable.add(Text.emptyString(), 1, 1);
		fTable.add(Text.emptyString(), 2, 1);
		fTable.setHeight(1, "6");

		entry.setColumnAlignment(1, "right");
		entry.setColumnAlignment(2, "right");
		entry.setColumnAlignment(3, "right");
		entry.setColumnAlignment(4, "right");
		entry.setColumnAlignment(6, "right");
		entry.setColumnAlignment(7, "right");
		entry.setVerticalAlignment(6, 1, "middle");

		fTable.setRowVerticalAlignment(1, "middle");
		entry.setRowVerticalAlignment(1, "middle");

		fTable.setCellspacing(0);
		fTable.setCellpadding(0);
		entry.setCellspacing(0);
		entry.setCellpadding(0);

		entry.setWidth(1, "80");
		entry.setWidth(2, "40");
		entry.setWidth(3, "40");
		entry.setWidth(4, "65");
		entry.setWidth(5, "20");
		entry.setWidth(6, "50");
		entry.setWidth(7, "20");

		fTable.add(mainForm, 1, 2);

		if (plainUser) {
			Text fjoldi = getSmallHeader(getResourceBundle().getLocalizedString("start.search.how_many", "How many?"));
			Text timi = getSmallHeader(getResourceBundle().getLocalizedString("start.time", "Time"));

			fjoldi.setFontColor("white");
			timi.setFontColor("white");

			fjoldi.setFontSize("2");
			timi.setFontSize("2");

			fjoldi.setFontStyle("Arial");
			timi.setFontStyle("Arial");

			fjoldi.setBold();
			timi.setBold();

			entry.add(fjoldi, 1, 1);
			entry.add(timi, 3, 1);

			entry.add(insertDropdown("skraMarga", 1, 4), 2, 1);
			entry.add(insertDrowdown("line", myTableInfo, modinfo, tournamentRounds), 4, 1);
		}
		entry.add(insertButton(getResourceBundle().getImage("buttons/reserve.gif"), "Taka fr·"), 6, 1); // 80,19
		entry.setVerticalAlignment(6, 1, "middle");
		insertHiddenInput("mode", "1", mainForm);

		mainForm.add(entry);
		return fTable;
	}

	public Table lineUpTournamentDay(IWContext modinfo, List Tournaments) {
		Text dayReserved = getMessageText(localize("start.message1", "Dagur frátekinn fyrir mót"));
		dayReserved.setFontSize(4);
		Table AlignmentTable = new Table();
		AlignmentTable.setBorder(0);
		AlignmentTable.add(Text.getBreak());
		AlignmentTable.add(dayReserved);
		for (int i = 0; i < Tournaments.size(); i++) {
			AlignmentTable.add("<p>" + ((Tournament) Tournaments.get(i)).getName());
		}
		AlignmentTable.setAlignment("center");
		AlignmentTable.add(Text.getBreak());
		AlignmentTable.add(Text.getBreak());
		AlignmentTable.add(getMessageText(localize("start.message1b", "Hafi› samband vi› klúbb vegna skráninga í dag.<br>Rástíma má sjá í mótaskrá")));

		return AlignmentTable;
	}
	public ICPage getTeeTimeSearchPage() {
		return _teeTimeSearchPage;
	}
	public void setTeeTimeSearchPage(ICPage p) {
		_teeTimeSearchPage = p;
	}
	public ICPage getTeeTimesPage() {
		return _teeTimesPage;
	}
	public void setTeeTimesPage(ICPage p) {
		_teeTimesPage = p;
	}
}