package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.block.login.business.GolfLoginBusiness;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberBMPBean;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusinessBean;
import is.idega.idegaweb.golf.startingtime.data.TeeTime;
import is.idega.idegaweb.golf.startingtime.data.TeeTimeHome;
import is.idega.idegaweb.golf.templates.page.GolfWindow;
import is.idega.idegaweb.golf.tournament.presentation.MemberSearchWindow;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * Title: Golf Description: Copyright: Copyright (c) 2001 Company: idega.is
 * 
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundssonn </a>
 * @version 1.0
 */

public class AdminRegisterTime extends GolfWindow {

	private TeeTimeBusinessBean business;
	private Form myForm;
	private Table frameTable;
	private IWTimestamp currentDay;
	private String currentField;
	private String currentUnion;
	private String MemberID;
	private int daytime;
	private StartingtimeFieldConfig fieldInfo;
	private DecimalFormat hadycapFormat;

	private static String saveParameterString = "STsave";
	private static String timeParameterString = "STtime";
	private static String nameParameterString = "STname";
	private static String unionParameterString = "STunion";
	private static String handicapParameterString = "SThadycap";
	private static String showedUpParameterString = "STshowedUp";
	private static String deleteParameterString = "STdelete";
	private static String groupNumParameterString = "STgroup";
	private static String lastGroupParameterString = "STlastGroup";
	private static String timeChangeStartIDParameterString = "STstartID";
	private static String formParmeterIDParameterString = "STfpID";

	private static String color1 = "#336661";
	private static String color2 = "#CEDFCF";
	private static String color3 = "#ADC9D0";
	private static String color5 = "#FFFFFF";
	private static String color4 = "#6E9173";
	private static String color6 = "#FF6666";

	private boolean forPrinting = false;

	public AdminRegisterTime() {
		super();
		this.setResizable(true);
		myForm = new Form();
		frameTable = new Table();
		frameTable.setAlignment("center");
		frameTable.setWidth("100%");
		myForm.add(frameTable);
		super.add(Text.getBreak());
		super.add(myForm);
		business = new TeeTimeBusinessBean();
		hadycapFormat = new DecimalFormat("###.0");
	}

	public List getTournamentRoundList() throws SQLException {

		int interval = fieldInfo.getMinutesBetweenStart();
		Vector tournamentGroups = new Vector(0);
		int tournamentGroupsIndex = 0;
		List TournamentRounds = EntityFinder.findAll((TournamentRound) IDOLookup.instanciateEntity(TournamentRound.class), "select tournament_round.* from tournament,tournament_round where tournament_round.tournament_id=tournament.tournament_id and tournament_round.round_date >= '" + currentDay.toSQLDateString() + " 00:00' and tournament_round.round_date <= '" + currentDay.toSQLDateString() + " 23:59' and tournament.field_id = " + this.currentField);

		if (TournamentRounds != null) {
			for (int i = 0; i < TournamentRounds.size(); i++) {
				TournamentRound tempRound = (TournamentRound) TournamentRounds.get(i);

				IWTimestamp begin = new IWTimestamp(tempRound.getRoundDate());
				begin.setAsTime();
				IWTimestamp End_ = new IWTimestamp(tempRound.getRoundEndDate());
				End_.setAsTime();
				IWTimestamp begintime = new IWTimestamp(fieldInfo.getOpenTime());
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
		return tournamentGroups;
	}

	public String getTournamentName(List rounds, int groupNumber) {

		for (int c = 0; c < rounds.size(); c += 2) {
			int[] temp = (int[]) rounds.get(c + 1);
			if (groupNumber >= temp[0] && groupNumber <= temp[1]) {
				return (String) rounds.get(c);
			}
		}
		return null;
	}

	public void lineUpTable(IWContext modinfo) throws SQLException, FinderException {

		Vector illegalTimes = new Vector(0);
		int illegalTimesIndex = 0;

		IWTimestamp openTime = new IWTimestamp(fieldInfo.getOpenTime());
		int minBetween = fieldInfo.getMinutesBetweenStart();

		IWTimestamp noon = new IWTimestamp(1, 2, 1, 14, 0, 0);
		noon.setAsTime();
		//idegaTimestamp afternoon = new idegaTimestamp(1,2,1,17,0,0);
		//afternoon.setAsTime();

		int groupCount = 0;
		int firstGroup = 1;
		List takenTimes = null;
		IWTimestamp firstTime = null;
		switch (daytime) {
			case 1:
				//afternoon
				groupCount = IWTimestamp.getMinutesBetween(noon, new IWTimestamp(fieldInfo.getCloseTime())) / minBetween;
				firstGroup = IWTimestamp.getMinutesBetween(openTime, noon) / minBetween + 1;
				takenTimes = business.getStartingtimeTableEntries(this.currentDay, this.currentField, firstGroup, firstGroup + groupCount - 1);
				firstTime = noon;
				break;
			/*
			 * case 2: //evening groupCount =
			 * idegaTimestamp.getMinutesBetween(afternoon,new
			 * idegaTimestamp(fieldInfo.getCloseTime()))/minBetween; firstGroup =
			 * idegaTimestamp.getMinutesBetween(openTime
			 * ,afternoon)/minBetween+1; takenTimes =
			 * business.getStartingtimeTableEntries(this.currentDay,this.currentField,firstGroup,firstGroup+groupCount-1);
			 * firstTime = afternoon; break;
			 */
			default:
				// morning
				groupCount = IWTimestamp.getMinutesBetween(openTime, noon) / minBetween;
				firstGroup = 1;
				takenTimes = business.getStartingtimeTableEntries(this.currentDay, this.currentField, firstGroup, groupCount);
				firstTime = new IWTimestamp(fieldInfo.getOpenTime());
				break;
		}

		if (forPrinting) {
			color1 = "#000000";
			color2 = "#FFFFFF";
			color3 = "#DEDEDE";
		}
		//    int groupCount = idegaTimestamp.getMinutesBetween(openTime,new
		// idegaTimestamp(fieldInfo.getCloseTime()))/minBetween;
		HorizontalRule hr = new HorizontalRule("100%");
		hr.setNoShade(true);
		hr.setWidth("100%");

		List tournamentGroups = getTournamentRoundList();

		int countInGroups = 4;
		int lines = groupCount * countInGroups;

		frameTable.empty();

		String width = "520";
		String width1 = "70";
		String width2 = "250";
		String width3 = "70";
		String width4 = "70";
		String width5 = "60";

		Table startTable = new Table();
		startTable.setRows(lines + 1);
		startTable.setColumns(6);
		Table headerTable = new Table(6, 1);
		Table illegalTable = null;

		startTable.setAlignment("center");
		startTable.setWidth(width);
		startTable.setCellspacing(1);

		headerTable.setAlignment("center");
		headerTable.setWidth(width);
		headerTable.setCellspacing(0);

		headerTable.setColor(color1);
		/*
		 * startTable.setColor(1,1,color1); startTable.setColor(2,1,color1);
		 * startTable.setColor(3,1,color1); startTable.setColor(4,1,color1);
		 * startTable.setColor(5,1,color1);
		 */
		startTable.add(Text.emptyString(), 1, 1);
		startTable.add(Text.emptyString(), 2, 1);
		startTable.add(Text.emptyString(), 3, 1);
		startTable.add(Text.emptyString(), 4, 1);
		startTable.add(Text.emptyString(), 5, 1);
		startTable.add(Text.emptyString(), 6, 1);
		startTable.setHeight(1, "1");

		headerTable.setAlignment(1, 1, "center");
		headerTable.setAlignment(3, 1, "center");
		headerTable.setAlignment(4, 1, "center");
		headerTable.setAlignment(5, 1, "center");
		headerTable.setAlignment(6, 1, "center");

		headerTable.setWidth(1, width1);
		headerTable.setWidth(2, width2);
		headerTable.setWidth(3, width3);
		headerTable.setWidth(4, width4);
		//    headerTable.setWidth(5,width5);

		startTable.setWidth(1, width1);
		startTable.setWidth(2, width2);
		startTable.setWidth(3, width3);
		startTable.setWidth(4, width4);
		//startTable.setWidth(5,width5);

		startTable.setColumnAlignment(1, "center");
		startTable.setColumnAlignment(3, "center");
		startTable.setColumnAlignment(4, "center");
		startTable.setAlignment(5, 1, "center");
		startTable.setAlignment(6, 1, "center");

		Text textProxy = getSmallText("");
		textProxy.setFontColor("#FFFFFF");

		Text time = (Text) textProxy.clone();
		time.setText(_iwrb.getLocalizedString("start.time", "Time"));
		time.setBold();
		headerTable.add(time, 1, 1);

		Text name = (Text) textProxy.clone();
		name.setText(_iwrb.getLocalizedString("start.social_nr", "Social nr.") + " (" + _iwrb.getLocalizedString("start.name", "Name") + ")");
		name.setBold();
		headerTable.add(name, 2, 1);

		Text club = (Text) textProxy.clone();
		club.setText("(" + _iwrb.getLocalizedString("start.club", "Club") + ")");
		club.setBold();
		headerTable.add(club, 3, 1);

		Text handicap = (Text) textProxy.clone();
		handicap.setText("(" + _iwrb.getLocalizedString("start.handicap", "Handicap") + ")");
		handicap.setBold();
		headerTable.add(handicap, 4, 1);

		Text showed = (Text) textProxy.clone();
		showed.setText(_iwrb.getLocalizedString("start.showed", "Showed"));
		showed.setBold();
		headerTable.add(showed, 5, 1);

		if (!forPrinting) {
			Text delete = (Text) textProxy.clone();
			delete.setText(_iwrb.getLocalizedString("start.delete", "Delete"));
			delete.setBold();
			headerTable.add(delete, 6, 1);
		}
		CheckBox delCheck = new CheckBox(deleteParameterString);
		CheckBox showedUpCheck;

		int groupCounter = 1;
		int lastGroup = -1;
		boolean insert = true;

		if (takenTimes != null) {
			for (int i = 0; i < takenTimes.size(); i++) {
				TeeTime tempStart = (TeeTime) takenTimes.get(i);
				int tempGroupNum = tempStart.getGroupNum();

				String tName = getTournamentName(tournamentGroups, tempGroupNum);

				if (tempGroupNum < 1 || tName != null) {
					insert = false;
				} else {
					if (lastGroup == tempGroupNum) {
						groupCounter++;
						if (groupCounter > countInGroups) {
							insert = false;
						}
					} else {
						groupCounter = 1;
					}
				}
				if (insert) {
					int line = (tempGroupNum - firstGroup) * countInGroups + groupCounter + 1; //(-1+1)
																							   // -groupCounter++
																							   // +headerLine
					//          openTime.addMinutes((tempGroupNum-1)*minBetween);
					//          startTable.add(TextSoap.addZero(openTime.getHour()) + ":"
					// + TextSoap.addZero(openTime.getMinute()),1,line);
					//          openTime.addMinutes(-(tempGroupNum-1)*minBetween);
					startTable.add(tempStart.getPlayerName(), 2, line);
					startTable.add(tempStart.getClubName(), 3, line);
					if (tempStart.getHandicap() >= 0) {
						startTable.add(hadycapFormat.format((double) tempStart.getHandicap()), 4, line);
					} else {
						startTable.add("-", 4, line);
					}
					if (forPrinting) {
						if (tempStart.getShowedUp()) {
							startTable.add("Y", 5, line);
						} else {
							startTable.add("N", 5, line);
						}
						startTable.setAlignment(5, line, "center");
					} else {
						CheckBox tmpShowedCheck = new CheckBox(showedUpParameterString + "_" + tempStart.getID());
						tmpShowedCheck.setChecked(tempStart.getShowedUp());
						startTable.add(tmpShowedCheck, 5, line);
					}

					CheckBox tempDelCheck = (CheckBox) delCheck.clone();
					tempDelCheck.setContent(Integer.toString(tempStart.getID()));
					startTable.add(new HiddenInput(timeChangeStartIDParameterString, Integer.toString(tempStart.getID())), 1, line);
					if (!forPrinting) {
						startTable.add(tempDelCheck, 6, line);
						startTable.setAlignment(6, line, "center");
					}
				} else {
					insert = true;
				}

				lastGroup = tempGroupNum;
			}
		}

		groupCounter = 1;
		lastGroup = -1;
		List allTakenTimes = business.getStartingtimeTableEntries(this.currentDay, this.currentField);
		int allGroupCount = IWTimestamp.getMinutesBetween(new IWTimestamp(fieldInfo.getOpenTime()), new IWTimestamp(fieldInfo.getCloseTime())) / minBetween;
		int[] freeGroups = new int[allGroupCount];

		if (allTakenTimes != null) {
			for (int i = 0; i < allTakenTimes.size(); i++) {
				TeeTime tempStart = (TeeTime) allTakenTimes.get(i);
				int tempGroupNum = tempStart.getGroupNum();
				String tName = getTournamentName(tournamentGroups, tempGroupNum);
				if (tempGroupNum < 1 || tName != null) {
					illegalTimes.insertElementAt(tempStart, illegalTimesIndex++);
				} else {
					if (lastGroup == tempGroupNum) {
						groupCounter++;
						if (groupCounter == countInGroups) {
							if (!(tempGroupNum > allGroupCount) && tempGroupNum > 0) {
								freeGroups[tempGroupNum - 1] = 1;
							} else {
								System.err.println("Error: " + this.getClassName() + " group " + tempGroupNum + " is out of range: 1-" + allGroupCount);
							}

						}
						if (groupCounter > countInGroups) {
							illegalTimes.insertElementAt(tempStart, illegalTimesIndex++);
						}
					} else {
						groupCounter = 1;
					}
				}
				lastGroup = tempGroupNum;
			}
		}

		// takes TournamentTimes out of timeDropdownMenu
		for (int c = 0; c < tournamentGroups.size(); c += 2) {
			int[] temp = (int[]) tournamentGroups.get(c + 1);
			for (int g = temp[0] - 1; g < temp[1]; g++) {
				if (g > -1 && g < freeGroups.length) freeGroups[g] = 1;
			}
		}

		DropdownMenu timeMenu = new DropdownMenu(this.timeParameterString);
		for (int i = 0; i < allGroupCount; i++) {
			if (freeGroups[i] != 1) {
				timeMenu.addMenuElement(i + 1, TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()));
			}
			openTime.addMinutes(minBetween);
		}
		openTime.addMinutes(-allGroupCount * minBetween);
		timeMenu.addMenuElement("-", "-");

		int loop = lines + 2;
		//    int takenTimesIndex = 0;
		TextInput nameInput = new TextInput(nameParameterString);
		nameInput.setSize(30);
		FloatInput handicapInput = new FloatInput(handicapParameterString);
		handicapInput.setSize(3);

		TextInput unionMenu = new TextInput(unionParameterString, "-");
		//	TextInput unionMenu = new
		// TextInput(unionParameterString,GolfCacher.getCachedUnion(currentUnion).getAbbrevation());
		unionMenu.setLength(3);
		boolean firstColor = true;
		int count = 0;
		int min = 0;
		Text templ = getSmallText("");
		for (int i = 1; i < loop; i++) {

			String tName = getTournamentName(tournamentGroups, (int) (((firstGroup - 1) * countInGroups + i - 2) / countInGroups + 1));

			if (startTable.isEmpty(5, i)) {
				if (tName == null) {
					min = ((i - 2) / countInGroups) * minBetween;
					firstTime.addMinutes(min);
					startTable.add("<b>" + TextSoap.addZero(firstTime.getHour()) + ":" + TextSoap.addZero(firstTime.getMinute()) + "</b>", 1, i);
					firstTime.addMinutes(-min);
					if (!forPrinting) {
						startTable.add(nameInput, 2, i);
						startTable.add(unionMenu, 3, i);
						startTable.add(handicapInput, 4, i);
					}
					startTable.add(new HiddenInput(groupNumParameterString, Integer.toString((int) (((firstGroup - 1) * countInGroups + i - 2) / countInGroups + 1))));
				} else {
					min = ((i - 2) / countInGroups) * minBetween;
					firstTime.addMinutes(min);
					startTable.add("<b>" + TextSoap.addZero(firstTime.getHour()) + ":" + TextSoap.addZero(firstTime.getMinute()) + "</b>", 1, i);
					firstTime.addMinutes(-min);
					Text temp = (Text) templ.clone();
					temp.setText(tName);
					startTable.add(temp, 2, i);
				}
			} else if (i > 1 && tName == null) {
				DropdownMenu tempTimeMenu = (DropdownMenu) timeMenu.clone();
				min = ((i - 2) / countInGroups) * minBetween;
				firstTime.addMinutes(min);
				tempTimeMenu.addMenuElement(Integer.toString((int) (((firstGroup - 1) * countInGroups + i - 2) / countInGroups + 1)), TextSoap.addZero(firstTime.getHour()) + ":" + TextSoap.addZero(firstTime.getMinute()));
				firstTime.addMinutes(-min);
				tempTimeMenu.setSelectedElement(Integer.toString((int) (((firstGroup - 1) * countInGroups + i - 2) / countInGroups + 1)));
				if (!forPrinting) {
					startTable.add(tempTimeMenu, 1, i);
				} else {
					startTable.add("<b>" + TextSoap.addZero(firstTime.getHour()) + ":" + TextSoap.addZero(firstTime.getMinute()) + "</b>", 1, i);
				}
				startTable.add(new HiddenInput(lastGroupParameterString, Integer.toString((int) (((firstGroup - 1) * countInGroups + i - 2) / countInGroups + 1))), 1, i);
			}
			if (i > 1) {
				if (count >= countInGroups) {
					if (firstColor) {
						firstColor = false;
					} else {
						firstColor = true;
					}
					count = 0;
				}

				// if (!forPrinting) {
				if (firstColor) {
					startTable.setColor(1, i, color2);
					startTable.setColor(2, i, color2);
					startTable.setColor(3, i, color2);
					startTable.setColor(4, i, color2);
					startTable.setColor(5, i, color2);
					startTable.setColor(6, i, color2);
				} else {
					startTable.setColor(1, i, color3);
					startTable.setColor(2, i, color3);
					startTable.setColor(3, i, color3);
					startTable.setColor(4, i, color3);
					startTable.setColor(5, i, color3);
					startTable.setColor(6, i, color3);
				}
				//}
				count++;
			}
		}

		int illegal = illegalTimes.size();
		if (illegal > 0) {
			illegalTable = new Table(5, illegal);
			illegalTable.setAlignment("center");
			illegalTable.setWidth(width);
			illegalTable.setCellspacing(1);

			illegalTable.setWidth(1, width1);
			illegalTable.setWidth(2, width2);
			illegalTable.setWidth(3, width3);
			illegalTable.setWidth(4, width4);
			//startTable.setWidth(5,width5);

			illegalTable.setColumnAlignment(1, "center");
			illegalTable.setColumnAlignment(3, "center");
			illegalTable.setColumnAlignment(4, "center");
			illegalTable.setAlignment(5, 1, "center");

			for (int i = 1; i <= illegal; i++) {
				TeeTime tempStart = (TeeTime) illegalTimes.get(i - 1);

				illegalTable.setColor(1, i, color6);
				illegalTable.setColor(2, i, color6);
				illegalTable.setColor(3, i, color6);
				illegalTable.setColor(4, i, color6);
				illegalTable.setColor(5, i, color6);

				illegalTable.add(tempStart.getPlayerName(), 2, i);
				illegalTable.add(tempStart.getClubName(), 3, i);
				if (tempStart.getHandicap() >= 0) {
					illegalTable.add(hadycapFormat.format((double) tempStart.getHandicap()), 4, i);
				} else {
					illegalTable.add("-", 4, i);
				}
				CheckBox tempDelCheck = (CheckBox) delCheck.clone();
				tempDelCheck.setContent(Integer.toString(tempStart.getID()));
				illegalTable.add(new HiddenInput(timeChangeStartIDParameterString, Integer.toString(tempStart.getID())), 1, i);
				illegalTable.add(tempDelCheck, 5, i);
				illegalTable.setAlignment(5, i, "center");

				DropdownMenu tempTimeMenu = (DropdownMenu) timeMenu.clone();
				min = (tempStart.getGroupNum() - 1) * minBetween;
				openTime.addMinutes(min);
				tempTimeMenu.addMenuElement(Integer.toString(tempStart.getGroupNum()), TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()));
				openTime.addMinutes(-min);
				tempTimeMenu.setSelectedElement(Integer.toString(tempStart.getGroupNum()));
				illegalTable.add(tempTimeMenu, 1, i);
				illegalTable.add(new HiddenInput(lastGroupParameterString, Integer.toString(tempStart.getGroupNum())), 1, i);
			}

		}

		String sDayTime;

		switch (daytime) {
			case 1:
				sDayTime = " - " + _iwrb.getLocalizedString("start.afternoon", "afternoon") + " - ";
				break;
			case 2:
				sDayTime = " - " + _iwrb.getLocalizedString("start.evening", "evening") + " - ";
				break;
			case 0:
				sDayTime = " - " + _iwrb.getLocalizedString("start.morning", "morning") + " - ";
				break;
			default:
				sDayTime = " - ";
				break;
		}

		Text dateText = getSmallHeader(business.getFieldName(Integer.parseInt(this.currentField)) + sDayTime + this.currentDay.getLocaleDate(modinfo.getCurrentLocale()));
		Table dateTable = new Table(2, 1);
		dateTable.add(dateText);
		dateTable.setAlignment("center");
		dateTable.setAlignment(1, 1, "left");
		dateTable.setAlignment(2, 1, "right");
		dateTable.setHeight(1, "25");
		dateTable.setWidth(width);

		if (!forPrinting) {
			Window popUp = new MemberSearchWindow("Leit a› me›limi");
			popUp.setWidth(600);
			popUp.setHeight(500);
			Link theSearch = getLocalizedLink("search_form_member","Search For Member");
			theSearch.setWindowToOpen(MemberSearchWindow.class);
			theSearch.addParameter("action", "getSearch");
			dateTable.add(theSearch, 2, 1);
		}

		frameTable.add(dateTable);
		frameTable.add(headerTable);

		if (illegalTable != null) {
			frameTable.add(illegalTable);
		}
		frameTable.add(startTable);

		//    SubmitButton save = new SubmitButton(" Vista ",
		// saveParameterString+".x", "do");
		//    SubmitButton save = new SubmitButton(new
		// Image("/pics/formtakks/vista.gif","Vista"), saveParameterString,
		// "do");
		GenericButton save = getButton(new SubmitButton(localize("save","Save"), saveParameterString, "do"));
		Table submSave = new Table();
		submSave.add(save);
		submSave.setAlignment("center");
		submSave.setAlignment(1, 1, "right");
		submSave.setHeight(1, "30");
		submSave.setWidth(width);
		if (!forPrinting) {
			frameTable.add(submSave);
		}
	}

	public void handleFormInfo(IWContext modinfo) throws SQLException, FinderException {

		Object rfObj = modinfo.getSessionAttribute(formParmeterIDParameterString);
		String rfParam = modinfo.getParameter(formParmeterIDParameterString);

		if (!((String) rfObj).equals(rfParam)) {

			modinfo.setSessionAttribute(formParmeterIDParameterString, rfParam);

			String[] sentTimes = modinfo.getParameterValues(timeParameterString);
			String[] sentLastGroups = modinfo.getParameterValues(lastGroupParameterString);
			String[] sentStartIDs = modinfo.getParameterValues(timeChangeStartIDParameterString);

			String[] sentDeletes = modinfo.getParameterValues(deleteParameterString);

			String[] sentNames = modinfo.getParameterValues(nameParameterString);
			String[] sentGroupNums = modinfo.getParameterValues(groupNumParameterString);
			String[] sentUnions = modinfo.getParameterValues(unionParameterString);
			String[] sentHandicaps = modinfo.getParameterValues(handicapParameterString);

			if (sentTimes != null) {

				for (int i = 0; i < sentTimes.length; i++) {
					if (!sentTimes[i].equals(sentLastGroups[i]) || sentTimes[i].equals("-")) {
						try {
							TeeTime tempSt = ((TeeTimeHome) IDOLookup.getHomeLegacy(TeeTime.class)).findByPrimaryKey(Integer.parseInt(sentStartIDs[i]));
							tempSt.setGroupNum(Integer.parseInt(sentTimes[i]));

							String showed = modinfo.getParameter(showedUpParameterString + "_" + sentStartIDs[i]);
							if (showed == null) {
								tempSt.setShowedUp(false);
							} else {
								tempSt.setShowedUp(true);
							}
							tempSt.update();
						} catch (Exception e) {
							//continue
						}
					} else if (sentTimes[i].equals(sentLastGroups[i])) {
						TeeTime tempSt = ((TeeTimeHome) IDOLookup.getHomeLegacy(TeeTime.class)).findByPrimaryKey(Integer.parseInt(sentStartIDs[i]));

						String showed = modinfo.getParameter(showedUpParameterString + "_" + sentStartIDs[i]);
						if (showed == null) {
							tempSt.setShowedUp(false);
						} else {
							tempSt.setShowedUp(true);
						}
						tempSt.update();
					}
				}
			}

			/*
			 * if (showedUps != null) { for (int i = 0; i < showedUps.length;
			 * i++) { try { TeeTime teeTimes = new
			 * TeeTime(Integer.parseInt(showedUps[i]));
			 * teeTimes.setShowedUp(true); teeTimes.update(); }catch (Exception
			 * e) {
			 *  } } }
			 */

			if (sentDeletes != null) {
				for (int i = 0; i < sentDeletes.length; i++) {
					try {
						((TeeTimeHome) IDOLookup.getHomeLegacy(TeeTime.class)).findByPrimaryKey(Integer.parseInt(sentDeletes[i])).delete();
					} catch (Exception e) {
						//          System.err.println("tÛkst ekki a eya tÌma me id: "
						// + sentDeletes[i] );
						// continue
					}
				}

			}

			if (sentNames != null) {
				for (int i = 0; i < sentNames.length; i++) {
					if (sentNames[i] != null && !"".equals(sentNames[i])) {
						boolean ssn = false; // social security number
						if (sentNames[i].length() == 10) {
							try {
								Integer.parseInt(sentNames[i].substring(0, 5));
								Integer.parseInt(sentNames[i].substring(6, 9));
								ssn = true;
							} catch (NumberFormatException e) {
								ssn = false;
							}
						}
						/*
						 * if(sentNames[i].length() == 11){ try{
						 * Integer.parseInt(sentNames[i].substring(0,5));
						 * Integer.parseInt(sentNames[i].substring(7,10));
						 * String tempString; tempString =
						 * sentNames[i].substring(0,5); tempString +=
						 * sentNames[i].substring(7,10); sentNames[i] =
						 * tempString; ssn = true; }catch(NumberFormatException
						 * e){ ssn = false; } }
						 */
						if (ssn) {
							//List lMember =
							// EntityFinder.findAllByColumn((is.idega.idegaweb.golf.entity.Member)Member.getStaticInstance(),Member.getSocialSecurityNumberColumnName(),sentNames[i]);
							Member tempMemb = (is.idega.idegaweb.golf.entity.Member) MemberBMPBean.getMember(sentNames[i]);
							if (tempMemb != null) {
								//              Member tempMemb = (Member)lMember.get(0);
								business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, Integer.toString(tempMemb.getID()), MemberID, tempMemb.getName(), Float.toString(tempMemb.getHandicap()), GolfCacher.getCachedUnion(tempMemb.getMainUnionID()).getAbbrevation(), null, null);
							} else {
								//business.setStartingtime(Integer.parseInt(sentGroupNums[i]),
								// this.currentDay, this.currentField, null,
								// MemberID, sentNames[i], sentHandicaps[i],
								// GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(),
								// null, null);
								business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, MemberID, sentNames[i], sentHandicaps[i], sentUnions[i], null, null);
							}
						} else {
							//business.setStartingtime(Integer.parseInt(sentGroupNums[i]),
							// this.currentDay, this.currentField, null,
							// MemberID, sentNames[i], sentHandicaps[i],
							// GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(),
							// null, null);
							business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, MemberID, sentNames[i], sentHandicaps[i], sentUnions[i], null, null);
						}
					}
				}
			}
		}

		//this.setParentToReload();

	}

	public void noPermission() {
		Text satyOut = getErrorText(this._iwrb.getLocalizedString("start.no_permission", "No permission"));
		satyOut.setFontSize(4);
		Table AlignmentTable = new Table();
		AlignmentTable.setBorder(0);
		AlignmentTable.add(Text.getBreak());
		AlignmentTable.add(satyOut);
		AlignmentTable.setAlignment("center");
		AlignmentTable.add(Text.getBreak());
		AlignmentTable.add(Text.getBreak());
		//    Link close = new Link("Loka glugga");
		//    close.addParameter(closeParameterString, "true");
		//    AlignmentTable.add(close);
		frameTable.empty();
		frameTable.add(AlignmentTable);
	}

	public void main(IWContext modinfo) throws Exception {
		super.main(modinfo);
		String date = modinfo.getParameter("date");
		currentField = modinfo.getParameter("field_id");
		currentUnion = modinfo.getParameter("union_id");
		MemberID = modinfo.getParameter("member_id");
		String sDayTime = modinfo.getParameter("daytime");

		String rfParam = modinfo.getParameter(formParmeterIDParameterString);

		if (modinfo.getSessionAttribute(formParmeterIDParameterString) == null) {
			modinfo.setSessionAttribute(formParmeterIDParameterString, Integer.toString(myForm.hashCode() - 1));
			myForm.add(new HiddenInput(formParmeterIDParameterString, Integer.toString(myForm.hashCode())));
		} else if (rfParam != null) {
			myForm.add(new HiddenInput(formParmeterIDParameterString, Integer.toString((Integer.parseInt(rfParam) + rfParam.hashCode()) % Integer.MAX_VALUE)));
		} else {
			myForm.add(new HiddenInput(formParmeterIDParameterString, Integer.toString(myForm.hashCode())));
		}

		if (sDayTime == null) {
			Object tempObj = modinfo.getSessionAttribute("when");
			if (tempObj != null) {
				daytime = Integer.parseInt(tempObj.toString());
				myForm.add(new HiddenInput("daytime", tempObj.toString()));
			}
		} else {
			daytime = Integer.parseInt(sDayTime.toString());
			myForm.maintainParameter("daytime");
		}

		if (date == null) {
			Object tempObj = modinfo.getSessionAttribute("date");
			if (tempObj != null) {
				date = tempObj.toString();
				myForm.add(new HiddenInput("date", date));
			}
		} else {
			myForm.maintainParameter("date");
		}

		if (currentField == null) {
			Object tempObj = modinfo.getSessionAttribute("field_id");
			if (tempObj != null) {
				currentField = tempObj.toString();
				myForm.add(new HiddenInput("field_id", currentField));
			}
		} else {
			myForm.maintainParameter("field_id");
		}

		if (currentUnion == null) {
			Object tempObj = modinfo.getSessionAttribute("union_id");
			if (tempObj != null) {
				currentUnion = tempObj.toString();
				myForm.add(new HiddenInput("union_id", currentUnion));
			}
		} else {
			myForm.maintainParameter("union_id");
		}

		if (MemberID == null) {
			Object tempObj = GolfLoginBusiness.getMember(modinfo);
			if (tempObj != null) {
				MemberID = Integer.toString(((GenericEntity) tempObj).getID());
				myForm.add(new HiddenInput("member_id", MemberID));
			}
		} else {
			myForm.maintainParameter("member_id");
		}

		boolean keepOn = true;

		try {
			currentDay = new IWTimestamp(date);
		} catch (NullPointerException e) {
			keepOn = false;
			this.noPermission();
		}

		if (modinfo.getParameter(saveParameterString + ".x") != null || modinfo.getParameter(saveParameterString) != null) {
			this.handleFormInfo(modinfo);
		}

		if (keepOn) {
			String hasPermission = modinfo.getParameter("golf");
			if (hasPermission != null || AccessControl.isAdmin(modinfo) || (AccessControl.isClubAdmin(modinfo) && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))) || (AccessControl.isClubWorker(modinfo) && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id")))) {
				if (hasPermission == null) {
					myForm.add(new HiddenInput("golf", "79")); // some dummy
															   // value
				} else {
					myForm.maintainParameter("golf");
				}
				fieldInfo = business.getFieldConfig(Integer.parseInt(currentField), currentDay);

				String sDayTimeString;

				switch (daytime) {
					case 1:
						sDayTimeString = " - " + _iwrb.getLocalizedString("start.afternoon", "afternoon") + " - ";
						break;
					case 2:
						sDayTimeString = " - " + _iwrb.getLocalizedString("start.evening", "evening") + " - ";
						break;
					case 0:
						sDayTimeString = " - " + _iwrb.getLocalizedString("start.morning", "morning") + " - ";
						break;
					default:
						sDayTimeString = " - ";
						break;
				}

				this.setTitle(business.getFieldName(Integer.parseInt(this.currentField)) + sDayTimeString + this.currentDay.getLocaleDate(modinfo.getCurrentLocale()));
				lineUpTable(modinfo);
				if (!forPrinting) {
					Paragraph p = new Paragraph();
					p.setAlign("center");
					Link print = getLink(_iwrb.getLocalizedString("startingtime.print", "print"));
					print.setWindowToOpen(AdminRegisterTeeTimeWindow.class);
					print.addParameter("date", date);
					print.addParameter("field_id", currentField);
					print.addParameter("union_id", currentUnion);
					print.addParameter("member_id", MemberID);
					print.addParameter("daytime", sDayTime);
					p.add(print);
					add(p);
				}
			} else {
				noPermission();
			}

		}
	} // method main() ends

	public void setForPrinting(boolean forPrinting) {
		this.forPrinting = forPrinting;
	}

} // Class ends
