/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.GolfField;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Startingtime;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.service.StartService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class AdminRegisterTeeTime extends GolfBlock {

	private StartService service = new StartService();

	public void main(IWContext modinfo) throws Exception {
		int temp = 0;
		getParentPage().setTitle(localize("start.register_player", "Register Player"));
		getParentPage().setBackgroundColor("white");

		Form myForm = new Form();

		String MemberId = modinfo.getSessionAttribute("member_id").toString();
		String FieldID = modinfo.getSessionAttribute("field_id").toString();
		String deleteCheckBox = modinfo.getParameter("check");

		setGraphic(myForm);
		try {
			if (deleteCheckBox != null && modinfo.getParameter("btnUpdatePlayer") != null) {
				if (!updatePlayers(MemberId, modinfo)) {
					setErroResponse(myForm);
				}
				else {
					deletePlayers(MemberId, modinfo);
					getParentPage().setParentToReload();
					getParentPage().close();
				}
			}
			else if (modinfo.getParameter("btnUpdatePlayer") != null) {
				if (!updatePlayers(MemberId, modinfo)) {
					setErroResponse(myForm);
				}
				else {
					getParentPage().setParentToReload();
					getParentPage().close();
				}
			}
			else {
				if (isAdmin()) {
					adminView(MemberId, FieldID, myForm, modinfo);
				}
				else
					drawTable(MemberId, FieldID, myForm, modinfo);
			}
			add(myForm);
		}
		catch (SQLException E) {
			System.out.print("SQLException: " + E.getMessage());
			System.out.print("SQLState:     " + E.getSQLState());
			System.out.print("VendorError:  " + E.getErrorCode());
		}
		catch (Exception E) {
			E.printStackTrace();
		}
	}

	public TextInput insertEditBox(String name, String text) {
		TextInput myInput = new TextInput(name);
		myInput.setContent(text);
		return myInput;
	}

	public TextInput insertEditBox(String name, String text, int size) {
		TextInput myInput = new TextInput(name);
		myInput.setSize(size);
		myInput.setContent(text);
		return myInput;
	}

	private void insertOrderByBottons(Table myTable, PrintWriter out) {
		//ath! eins og er er þetta ekki takkar sem raðast eftir heldur bara texti,
		// og raðar ekki

		String txtDelUrl = "/pics/rastimask/Heiti-Graphic/Geyda.gif";
		String txtNameUrl = "/pics/rastimask/Heiti-Graphic/Gnafn.gif";
		String txtTimeUrl = "/pics/rastimask/Heiti-Graphic/Gtimi.gif";
		String txtHandicapUrl = "/pics/rastimask/Heiti-Graphic/Gforgjof.gif";
		String txtClubUrl = "/pics/rastimask/Heiti-Graphic/Gklubbur.gif";
		String txtCardUrl = "/pics/rastimask/Heiti-Graphic/Gserkort.gif";
		String txtCardNoUrl = "/pics/rastimask/Heiti-Graphic/Gkortnumer.gif";
		String txtDateUrl = "/pics/rastimask/Heiti-Graphic/Gdags.gif";

		myTable.add(getLocalizedText("start.delete","Delete"), 1, 1);
		myTable.add(getLocalizedText("start.name","Name"), 2, 1);
		myTable.add(getLocalizedText("start.club","Club"), 3, 1);
		myTable.add(getLocalizedText("start.handicap","Handicap"), 4, 1);
		myTable.add(getLocalizedText("start.vip_card","VIP card"), 5, 1);
		myTable.add(getLocalizedText("start.card_number","Card number"), 6, 1);
		myTable.add(getLocalizedText("start.date","Date"), 7, 1);
		myTable.add(getLocalizedText("start.time","Time"), 8, 1);

	}

	public boolean updatePlayers(String MemberId, IWContext modinfo) throws SQLException, IOException {
		PrintWriter out = modinfo.getResponse().getWriter();

		int numPlayers = 0;
		boolean admin = isAdmin(Integer.parseInt(MemberId));
		String FieldID = "";
		Startingtime[] startArray = null;
		Startingtime stime = (Startingtime) IDOLookup.createLegacy(Startingtime.class);

		if (modinfo.getParameter("drpdFieldID") != null) {
			FieldID = modinfo.getParameter("drpdFieldID");
		}
		else
			FieldID = "10";//modinfo.getSession().getAttribute("field_id").toString();

		try {
			startArray = (Startingtime[]) modinfo.getSessionAttribute("StartChangeArray");
			modinfo.removeSessionAttribute("StartChangeArray");

			String playerName[] = modinfo.getParameterValues("name");
			String playerClub[] = modinfo.getParameterValues("club");
			String playerHandicap[] = modinfo.getParameterValues("handicap");
			String playerCard[] = modinfo.getParameterValues("card");
			String playerCardNo[] = modinfo.getParameterValues("cardNo");

			System.err.println("arraylength : " + startArray.length);
			for (int i = 0; i < startArray.length; i++) {
				System.err.println(i);
				stime = startArray[i];
				stime.setPlayerName(playerName[i]);
				stime.setClubName(playerClub[i]);
				try {

					stime.setHandicap(new Float(com.idega.util.text.TextSoap.findAndReplace(playerHandicap[i], ",", ".")));
				}
				catch (NumberFormatException e) {
				}
				catch (NullPointerException e) {
					System.err.println("handicap null");
				}

				try {
					stime.update();
				}
				catch (SQLException E) {
					System.err.println("error : i = " + i);
				}
			}
		}
		catch (Exception E) {
			out.print("Exception: " + E.getMessage());
		}

		return true;
	}

	public int deletePlayers(String MemberId, IWContext modinfo) throws SQLException, IOException {
		PrintWriter out = modinfo.getResponse().getWriter();
		Startingtime stime = (Startingtime) IDOLookup.instanciateEntity(Startingtime.class);
		try {
			if (modinfo.getRequest().getParameterValues("check") != null) {
				String[] delcheck = modinfo.getRequest().getParameterValues("check");
				for (int i = 0; i < delcheck.length; i++) {
					stime.deleteMultiple("startingtime_id", delcheck[i]);
				}
			}
		}
		catch (SQLException E) {
			E.printStackTrace();
			out.print("SQLException: " + E.getMessage());
			out.print("SQLState:     " + E.getSQLState());
		}
		return 0;
	}

	public String formatDate(IWCalendar funcDate, String date) {

		int day, month, year;
		StringTokenizer Timetoken = new StringTokenizer(date, "-");

		year = Integer.parseInt(Timetoken.nextToken());
		month = Integer.parseInt(Timetoken.nextToken());
		day = Integer.parseInt(Timetoken.nextToken());

		String mon = funcDate.getMonthName(month);

		return day + ". " + mon.toLowerCase();

	}

	public void adminView(String memberId, String FieldID, Form myForm, IWContext modinfo) throws IOException {
		String btnSaekjaUrl = "../gummi/pics/rastimask/Takkar/Tsaekja1.gif";
		PrintWriter out = modinfo.getResponse().getWriter();
		Table myTable = new Table(4, 2);

		myTable.add(FieldDropdown("drpField", memberId, FieldID, modinfo), 2, 1);

		myTable.setAlignment("center");
		myForm.add(myTable);
		try {
			drawTable(memberId, FieldID, myForm, modinfo);
		}
		catch (SQLException E) {
			E.printStackTrace();
			out.print("SQLException: " + E.getMessage());
			out.print("SQLState:     " + E.getSQLState());
			out.print("VendorError:  " + E.getErrorCode());
		}
	}

	public GolfField getFieldInfo(int field, String date) throws SQLException, IOException {
		StartingtimeFieldConfig FieldConfig = service.getFieldConfig(field, date);
		GolfField field_info = new GolfField(new IWTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new IWTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShown(), FieldConfig.publicRegistration());
		return field_info;
	}

	public String getTime(int group, GolfField myGolfField) {

		int interval = myGolfField.get_interval();
		int openHour = myGolfField.get_open_hour();
		int openMin = myGolfField.get_open_min();

		int Hour = openHour + ((group - 1) * interval) / 60;
		int Min = openMin + ((group - 1) * interval) % 60;

		if (Min >= 60) {
			Min -= 60;
			Hour++;
		}

		String time;

		if (Min < 10 && Hour < 10)
			time = "0" + Hour + ":0" + Min;
		else if (Min < 10)
			time = "" + Hour + ":0" + Min;
		else if (Hour < 10)
			time = "0" + Hour + ":" + Min;
		else
			time = "" + Hour + ":" + Min;

		return time;
	}

	public void setGraphic(Form myForm) {
		String picUrl_1 = "/pics/rastima_popup/breyting.gif";
		String picUrl_2 = "/pics/rastima_popup/tile.gif";

		Table Header = new Table(2, 1);
		//Header.setBorder(1);

		Header.setHeight("90");
		Header.setWidth(1, "185");

		Header.setCellpadding(0);
		Header.setCellspacing(0);
		getParentPage().setMarginWidth(0);
		getParentPage().setMarginHeight(0);
		getParentPage().setLeftMargin(0);
		getParentPage().setTopMargin(0);

		Header.setBackgroundImage(2, 1, getResourceBundle().getImage(picUrl_1));
		Header.setBackgroundImage(1, 1, getResourceBundle().getImage(picUrl_2));

		Header.setWidth("100%");

		myForm.add(Header);

	}

	public void setErroResponse(Form myForm) {
		String borderPicUrl = "pics/rastimask/Rastimaskraning/BorderTiler.gif";
		String btnBackUrl = "pics/rastimask/Takkar/Ttilbaka1.gif";

		Table myTable = new Table(2, 3);

		myTable.add(getErrorText(localize("start.error1","NauÝsynlegt er aÝ hafa nafn. Ef ßœ vilt afturkalla skr‡ningu, veldu ß‡ \"EyÝa\" boxiÝ")), 2, 1);

		myTable.add(getResourceBundle().getImage(borderPicUrl), 1, 1);
		myTable.add(getResourceBundle().getImage(borderPicUrl), 1, 2);
		myTable.add(getResourceBundle().getImage(borderPicUrl), 1, 3);
		myTable.add(new BackButton(getResourceBundle().getImage(btnBackUrl)), 2, 3);

		myTable.setAlignment(2, 3, "center");
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);

		myForm.add(myTable);

	}

	private String getOrderByClause(IWContext modinfo) {
		String ordyerByClause = "grup_num";

		if (modinfo.getParameter("steSkraName") != null) {
			ordyerByClause = "player_name";
		}
		else if (modinfo.getParameter("hCap") != null) {
			ordyerByClause = "handicap";
		}
		else if (modinfo.getParameter("Time") != null) {
			ordyerByClause = "grup_num";
		}
		else if (modinfo.getParameter("steName") != null) {
			ordyerByClause = "player_name";
		}
		else if (modinfo.getParameter("steClub") != null) {
			ordyerByClause = "club_name";
		}
		else if (modinfo.getParameter("steCardNo") != null) {
			ordyerByClause = "card_num";
		}
		else if (modinfo.getParameter("steDate") != null) {
			ordyerByClause = "startingtime_date";
		}
		else if (modinfo.getParameter("serkort") != null) {
			ordyerByClause = "card_name";
		}

		return ordyerByClause;
	}

	public boolean drawTable(String MemberId, String FieldID, Form myForm, IWContext modinfo) throws IOException, SQLException {

		PrintWriter out = modinfo.getResponse().getWriter();
		IWCalendar funcDate = new IWCalendar();

		Table myTable = null;
		String orderby_clause = "startingtime_date";
		GolfField myGolfField = new GolfField();

		String ordyerByClause = getOrderByClause(modinfo);

		boolean admin = isAdmin(Integer.parseInt(MemberId));

		if (modinfo.getRequest().getParameter("drpField") != null)
			FieldID = modinfo.getRequest().getParameter("drpField");

		try {
			Startingtime[] startingtimemeArray = null;
			Startingtime stime = (Startingtime) IDOLookup.instanciateEntity(Startingtime.class);

			///out.print(FieldID+" member id "+MemberId);

			if (admin) {
				startingtimemeArray = service.findAllPlayersInFieldOrdered(FieldID, ordyerByClause);
			}
			else {
				startingtimemeArray = service.findAllPlayersByMemberOrdered(FieldID, MemberId, ordyerByClause);
			}
			modinfo.setSessionAttribute("StartChangeArray", startingtimemeArray);

			int rows = startingtimemeArray.length;

			//out.print(rows);
			if (rows == 0)
				return false;

			if (admin)
				myTable = new Table(9, rows + 2);
			else
				myTable = new Table(8, rows + 2);

			myTable.setAlignment("center");
			//myTable.setBorder(1);
			int i = 0;
			for (; i < startingtimemeArray.length; i++) {
				stime = startingtimemeArray[i];
				myGolfField = getFieldInfo(Integer.parseInt(FieldID), stime.getStartingtimeDate().toString());
				String group_num = getTime(stime.getGroupNum(), myGolfField);

				myTable.add(new CheckBox("check", new Integer(stime.getID()).toString()), 1, i + 2);
				myTable.add(insertEditBox("name", stime.getPlayerName()), 2, i + 2);
				if (stime.getClubName() == null || stime.getClubName().equals(""))
					myTable.add(insertUnionDropdown("club", "-", 5), 3, i + 2);
				else
					myTable.add(insertUnionDropdown("club", stime.getClubName(), 5), 3, i + 2);
				if (stime.getHandicap() == -1)
					myTable.add(insertEditBox("handicap", "-", 4), 4, i + 2);
				else
					myTable.add(insertEditBox("handicap", new Float(stime.getHandicap()).toString(), 4), 4, i + 2);
				if (stime.getCardName() == null || stime.getCardName().equals(""))
					myTable.add(insertEditBox("card", "&nbsp;", 6), 5, i + 2);
				else
					myTable.add(insertEditBox("card", stime.getCardName(), 6), 5, i + 2);
				if (stime.getCardNum() == null || stime.getCardNum().equals(""))
					myTable.add(insertEditBox("cardNo", "&nbsp;", 6), 6, i + 2);
				else
					myTable.add(insertEditBox("cardNo", stime.getCardNum(), 6), 6, i + 2);
				myTable.add(getText(formatDate(funcDate, stime.getStartingtimeDate().toString())), 7, i + 2);
				myTable.add(getText(group_num), 8, i + 2);
				if (admin)
					myTable.add(insertHyperlink(stime.getMember().getName(), stime.getMemberID(), AdminRegisterTeeTime.class), 9, i + 2);
			}
			String btnUpdateUrl = "/pics/rastimask/Takkar/Tuppfaera1.gif";
			String btnCancelUrl = "/pics/rastimask/Takkar/Thaetta-vid1.gif";

			insertOrderByBottons(myTable, out);
			if (admin) {
				//hér er ekki settur takki í bili
				//String txtSkraNameUrl =
				// "/pics/rastimask/Heiti-Graphic/Gskraningaradili.gif";
				//myTable.add(new SubmitButton(new Image(txtSkraNameUrl),
				// "steSkraName"), 9, 1);
				myTable.add(getLocalizedText("start.owner","Owner"), 9, 1);
			}

			myTable.mergeCells(5, i + 2, 7, i + 2);
			myTable.add(new SubmitButton(getResourceBundle().getImage(btnUpdateUrl), "btnUpdatePlayer"), 5, i + 2);
			myTable.add(new CloseButton(getResourceBundle().getImage(btnCancelUrl)), 5, i + 2);
			myTable.setAlignment(5, i + 2, "right");

			myForm.add(myTable);

		}
		catch (SQLException E) {
			out.print("SQLException: " + E.getMessage());
			out.print("SQLState:     " + E.getSQLState());
			out.print("VendorError:  " + E.getErrorCode());
			E.printStackTrace();
			return false;
		}
		return true;
	}

	private Link insertHyperlink(String name, int value, String action) {
		Link myLink = getLink(name);
		myLink.setURL(action);
		myLink.addParameter(name, new Integer(value).toString());
		return myLink;
	}
	
	private Link insertHyperlink(String name, int value, Class action) {
		Link myLink = getLink(name);
		myLink.setClassToInstanciate(action);
		
		myLink.addParameter(name, new Integer(value).toString());
		return myLink;
	}

	/**
	 * temp implementation (isClubAdmin() and isClubWorker())
	 */
	public boolean isAdmin(int memberId) throws SQLException {

		Member member = null;
		try {
			member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		if (member != null) {
			String groupName = null;
			Group[] access = member.getGroups();
			for (int i = 0; i < access.length; i++) {
				groupName = access[i].getName();
				if ("administrator".equals(groupName) || "club_admin".equals(groupName) || "club_worker".equals(groupName))
					return true;
			}
		}

		return false;
	}

	public SelectionBox insertSelectionBox(String SelectionBoxName, IWContext modinfo, int height) throws IOException, SQLException {
		SelectionBox mySelectionBox = new SelectionBox(SelectionBoxName);
		mySelectionBox.setHeight(height);
		Field[] field = service.getStartingEntryField();
		for (int i = 0; i < field.length; i++) {
			mySelectionBox.addElement("" + field[i].getID(), field[i].getName());
		}
		mySelectionBox.keepStatusOnAction();
		return mySelectionBox;
	}

	public DropdownMenu FieldDropdown(String dropdownName, String MemberId, String fieldId, IWContext modinfo) throws IOException {
		Field[] field = null;
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);
		try {
			if (isAdmin(Integer.parseInt(MemberId), false, false))
				field = service.getStartingEntryField();
			else
				field = service.getFields((String) modinfo.getSessionAttribute("member_main_union_id"));

			for (int i = 0; i < field.length; i++) {
				myDropdown.addMenuElement(field[i].getID(), field[i].getName());
			}
			myDropdown.keepStatusOnAction();
			myDropdown.setToSubmit();
			myDropdown.setSelectedElement(fieldId);
		}
		catch (SQLException E) {
			E.printStackTrace();
		}
		return myDropdown;
	}

	public boolean isAdmin(int memberId, boolean clubadmin, boolean clubworker) throws SQLException {

		Member member = null;
		try {
			member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		if (member != null) {
			String groupName = null;
			Group[] access = member.getGroups();
			for (int i = 0; i < access.length; i++) {
				groupName = access[i].getName();

				if ("administrator".equals(groupName))
					return true;
				if (clubadmin && "club_admin".equals(groupName))
					return true;
				if (clubworker && "club_worker".equals(groupName))
					return true;
			}
		}

		return false;
	}

	public DropdownMenu insertUnionDropdown(String name, String text, int size) throws SQLException {
		DropdownMenu mydropdown = new DropdownMenu(name);

		Union union = (Union) IDOLookup.instanciateEntity(Union.class);
		List unions = EntityFinder.findAll(union, "Select * from " + union.getEntityName() + " order by abbrevation");
		for (int i = 0; i < unions.size(); i++) {
			union = (Union) unions.get(i);
			mydropdown.addMenuElement(union.getAbbrevation(), union.getAbbrevation());
		}
		mydropdown.setSelectedElement(text);
		mydropdown.keepStatusOnAction();
		return mydropdown;
	}
}