/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.GolfField;
import is.idega.idegaweb.golf.business.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.service.StartService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class StartingTimeLogin extends GolfBlock {

	private StartService service = new StartService();

	public void main(IWContext modinfo) throws Exception {
		getParentPage().setTitle("Skr· spilara");
		getParentPage().setBackgroundColor("white");

		String date = modinfo.getSession().getAttribute("date").toString();
		String field_id = modinfo.getSession().getAttribute("field_id").toString();
		IWTimestamp timestamp = new IWTimestamp(date);
		try {

			//TournamentDay tempTD = new TournamentDay();

			List Tournaments = EntityFinder.findAll((Tournament) IDOLookup.instanciateEntity(Tournament.class), "select tournament.* from tournament,tournament_day where tournament_day.tournament_id=tournament.tournament_id and tournament_day.day_date = '" + timestamp.toSQLDateString() + "' and tournament.field_id = " + field_id);

			if (Tournaments != null) {
				String closeParameterString = "closewidow";
				if ("true".equals(modinfo.getParameter(closeParameterString))) {
					//	        myWindow.setParentToReload();
					getParentPage().close();
				}
				else {
					Form myForm = new Form();
					add(myForm);
					setGraphic(myForm);
					Text dayReserved = getMessageText("Dagur frátekinn fyrir mót");
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
					Link close = getLink("Loka glugga");
					close.addParameter(closeParameterString, "true");
					AlignmentTable.add(close);
					myForm.add(AlignmentTable);
				}

			}
			else {

				Form myForm = new Form();
				myForm.maintainParameter("name");
				myForm.maintainParameter("line");
				int skraMargaInt = 0;
				String skraMarga = modinfo.getRequest().getParameter("skraMarga");
				add(myForm);

				int line = Integer.parseInt(modinfo.getRequest().getParameter("line"));
				//out.println("lina "+line+" ");
				//out.println("field "+field_id+" ");
				//out.println("date "+date+" ");

				try {
					setGraphic(myForm);

					int check = checkLine(line, field_id, date, modinfo);

					if (checkLine(line, field_id, date, modinfo) > 3) {
						setErroResponse(myForm, false);

					}
					else if (checkLine(line, field_id, date, modinfo) == -1) {
						System.out.print("Ekki nÊst samband vi gagnagrunn");

					}
					else {
						if (modinfo.getParameter("name") != null) {

							if (!setPlayers(modinfo)) {
								setErroResponse(myForm, true);
							}
							else {
								getParentPage().setParentToReload();
								getParentPage().close();
							}
						}
						else {

							skraMargaInt = Integer.parseInt(skraMarga);
							drawTable(skraMargaInt, myForm, modinfo);
						}
					}
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
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public TextInput insertEditBox(String name, Form myForm) {
		TextInput myInput = new TextInput(name);
		myInput.setParentObject(myForm);
		myInput.setAsNotEmpty();
		return myInput;
	}

	public TextInput insertEditBox(String name, String text) {
		TextInput myInput = new TextInput(name);
		myInput.setAsNotEmpty();
		myInput.setContent(text);
		return myInput;
	}

	public TextInput insertEditBox(String name, String text, int size) {
		TextInput myInput = new TextInput(name);
		myInput.setSize(size);
		myInput.setAsNotEmpty();
		myInput.setContent(text);
		return myInput;
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

	public DropdownMenu insertUnionDropdown(String name, int size) throws SQLException {
		DropdownMenu mydropdown = new DropdownMenu(name);

		Union union = (Union) IDOLookup.instanciateEntity(Union.class);
		List unions = EntityFinder.findAll(union, "Select * from " + union.getEntityName() + " order by abbrevation");
		for (int i = 0; i < unions.size(); i++) {
			union = (Union) unions.get(i);
			mydropdown.addMenuElement(union.getAbbrevation(), union.getAbbrevation());
		}
		mydropdown.setSelectedElement("");
		mydropdown.keepStatusOnAction();
		return mydropdown;
	}

	public TextInput insertEditBox(String name, int size) {
		TextInput myInput = new TextInput(name);
		myInput.setSize(size);
		return myInput;
	}

	private SubmitButton insertButton(String btnName, String Action, String Method, Form theForm) {
		SubmitButton mySubmit = new SubmitButton(btnName);
		theForm.setMethod(Method);
		theForm.setAction(Action);
		return mySubmit;
	}

	private SubmitButton insertButton(Image image, String imageName, String Action, String Method, Form theForm) {
		SubmitButton mySubmit = new SubmitButton(image, imageName);
		theForm.setMethod(Method);
		theForm.setAction(Action);
		return mySubmit;
	}
	
	private SubmitButton insertButton(Image image, String imageName, Class Action, String Method, Form theForm) {
		SubmitButton mySubmit = new SubmitButton(image, imageName);
		theForm.setMethod(Method);
		theForm.setClassToInstanciateAndSendTo(Action);
		return mySubmit;
	}

	private SubmitButton insertButton(String btnName, String Action, String Method, String onSub, Form theForm) {
		SubmitButton mySubmit = new SubmitButton(btnName);
		mySubmit.setOnSubmit(onSub);
		theForm.setMethod(Method);
		theForm.setAction(Action);
		return mySubmit;
	}

	public void drawTable(int skraMarga, Form myForm, IWContext modinfo) throws IOException {
		PrintWriter out = modinfo.getResponse().getWriter();
		//String picUrl_5 = "/pics/rastimask/Rastimaskraning/BorderTiler.gif";
		String btnSkraUrl = "/pics/rastimask/Takkar/Tskra1.gif";
		String btnCancelUrl = "/pics/rastimask/Takkar/Thaetta-vid1.gif";

		String txtNameUrl = "/pics/rastimask/Heiti-Graphic/Gnafn.gif";
		String txtTimeUrl = "/pics/rastimask/Heiti-Graphic/Gtimi.gif";
		String txtHandicapUrl = "/pics/rastimask/Heiti-Graphic/Gforgjof.gif";
		String txtClubUrl = "/pics/rastimask/Heiti-Graphic/Gklubbur.gif";
		String txtCardUrl = "/pics/rastimask/Heiti-Graphic/Gserkort.gif";
		String txtCardNoUrl = "/pics/rastimask/Heiti-Graphic/Gkortnumer.gif";

		int memberId = -1;
		boolean memberAvailable = false;
		//f· member id fyrir member til a finna hann og setja inn Ì textinputi
		// fyrir hann
		if (modinfo.getSession().getAttribute("member_id") != null) {
			memberId = Integer.parseInt((String) modinfo.getSession().getAttribute("member_id"));
			memberAvailable = true;
		}
		//out.print(memberId);

		String lines[] = new String[skraMarga];
		int Lines[] = new int[skraMarga];

		try {
			Member member = null;
			if (memberId != -1)
				member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
			String FieldID = modinfo.getSession().getAttribute("field_id").toString();
			String Date = modinfo.getSession().getAttribute("date").toString();
			String MemberId = modinfo.getSession().getAttribute("member_id").toString();
			GolfField myGolfField = getFieldInfo(Integer.parseInt(FieldID), Date);

			int Line = Integer.parseInt(modinfo.getRequest().getParameter("line"));

			int max = checkLine(Line, FieldID, Date, modinfo);
			//out.print(max);
			for (int j = 0; j < skraMarga; j++) {

				if (max > 3) {
					while (max > 3) {
						Line++;
						max = checkLine(Line, FieldID, Date, modinfo);
					}
				}
				max++;
				lines[j] = getTime(Line, myGolfField);
			}

			Table myTable = new Table(7, skraMarga + 3);
			myTable.setCellpadding(0);
			myTable.setCellspacing(0);
			myTable.setWidth(2, "40");

			/*
			 * myTable.add(new Image(txtTimeUrl), 2, 1); myTable.add(new
			 * Image(txtNameUrl), 3, 1); myTable.add(new Image(txtClubUrl), 4, 1);
			 * myTable.add(new Image(txtHandicapUrl), 5, 1); myTable.add(new
			 * Image(txtCardUrl), 6, 1);
			 */

			myTable.add(getLocalizedText("start.time","Time"), 2, 1);
			myTable.add(getLocalizedText("start.name","Name"), 3, 1);
			myTable.add(getLocalizedText("start.club","Club"), 4, 1);
			myTable.add(getLocalizedText("start.handicap","Handicap"), 5, 1);
			myTable.add(getLocalizedText("start.vip_card","VIP card"), 6, 1);
			myTable.add(getLocalizedText("start.cardnumber","Cardnumber"), 7, 1);

			//myTable.setBorder(1);

			boolean admin = false;
			boolean clubadmin = false;
			boolean clubworker = false;
			String unionAbbrevation = null;
			//int unionId = -1;

			if (memberAvailable) {
				admin = AccessControl.isAdmin(modinfo);
				clubadmin = AccessControl.isClubAdmin(modinfo);
				clubworker = AccessControl.isClubWorker(modinfo);
				unionAbbrevation = member.getMainUnion().getAbbrevation();
				//unionId = member.getMainUnion().getID();
			}

			int i = 1;
			for (; i < skraMarga + 1; i++) {
				//myTable.add(new Image(picUrl_5, "", 51, skraMarga*3), 1, i+1);
				myTable.setWidth(1, "25");
				myTable.add(getText(lines[i - 1]), 2, i + 1);
				myTable.setAlignment(2, i + 1, "left");

				if (admin || clubadmin || clubworker) {
					myTable.add(insertEditBox("name", myForm), 3, i + 1);
					myTable.add(insertUnionDropdown("club", unionAbbrevation, 5), 4, i + 1);
					myTable.add(insertEditBox("handicap", 6), 5, i + 1);
				}
				else {
					if (i == 1 && memberAvailable) {
						String handicap = new Float(member.getHandicap()).toString();
						if (handicap.equals("-1.0"))
							handicap = "100.0";
						myTable.add(insertEditBox("name", member.getName()), 3, i + 1);
						myTable.add(insertUnionDropdown("club", unionAbbrevation, 5), 4, i + 1);
						myTable.add(insertEditBox("handicap", handicap, 6), 5, i + 1);
					}
					else {
						myTable.add(insertEditBox("name", myForm), 3, i + 1);
						myTable.add(insertUnionDropdown("club", unionAbbrevation, 5), 4, i + 1);
						myTable.add(insertEditBox("handicap", 6), 5, i + 1);
					}
				}

				myTable.add(insertEditBox("card", 4), 6, i + 1);
				myTable.add(insertEditBox("cardNo", 12), 7, i + 1);

			}

			setPlayers(modinfo);

			myTable.mergeCells(4, i + 2, 7, i + 2);
			myTable.add(insertButton(getResourceBundle().getImage(btnSkraUrl), "", StartingTimeLogin.class, "post", myForm), 4, i + 2);
			myTable.add(new CloseButton(getResourceBundle().getImage(btnCancelUrl)), 4, i + 2);
			myTable.setAlignment(4, i + 2, "right");
			myForm.add(myTable);

		}
		catch (SQLException E) {
			E.printStackTrace();
		}
		catch (FinderException E) {
			E.printStackTrace();
		}
		catch (IOException E) {
			E.printStackTrace();
		}
	}

	public boolean setPlayers(IWContext modinfo) throws SQLException, IOException {
		PrintWriter out = modinfo.getResponse().getWriter();
		int i = 0;

		String FieldID = modinfo.getSession().getAttribute("field_id").toString();
		String Date = modinfo.getSession().getAttribute("date").toString();
		String MemberId = modinfo.getSession().getAttribute("member_id").toString();

		int Line = Integer.parseInt(modinfo.getRequest().getParameter("line"));
		int max = checkLine(Line, FieldID, Date, modinfo);
		int fjoldi = 4 - max;

		try {
			if (modinfo.getRequest().getParameter("name") != null) {

				String playerName[] = modinfo.getRequest().getParameterValues("name");
				String playerClub[] = modinfo.getRequest().getParameterValues("club");
				String playerHandicap[] = modinfo.getRequest().getParameterValues("handicap");
				String playerCard[] = modinfo.getRequest().getParameterValues("card");
				String playerCardNo[] = modinfo.getRequest().getParameterValues("cardNo");
				int numPlayers = playerName.length;

				if (modinfo.getRequest().getParameter("handicap") != null) {
					for (int j = 0; j < playerHandicap.length; j++) {
						if (playerHandicap[j].equals(null) || playerHandicap[j].equals(""))
							playerHandicap[j] = "-1";
						if (playerClub[j].equals(null) || playerClub[j].equals(""))
							playerClub[j] = "&nbsp";
						if (playerName[j].equals(""))
							return false;
					}
				}

				for (; i < numPlayers; i++) {
					if (max > 3) {
						while (max > 3) {
							Line++;
							max = checkLine(Line, FieldID, Date, modinfo);
						}
					}
					service.setStartingtime(Line, Date, FieldID, MemberId, playerName[i], playerHandicap[i], playerClub[i], playerCard[i], playerCardNo[i]);
					max++;
				}
			}
		}
		catch (SQLException E) {
			E.printStackTrace();
			out.print("SQLException: " + E.getMessage());
			out.print("SQLState:     " + E.getSQLState());
		}
		return true;
	}

	public int checkLine(int LineNo, String fieldID, String date, IWContext modinfo) throws SQLException, IOException {
		int totalLines = -1;
		totalLines = service.entriesInGroup(LineNo, fieldID, date);
		return totalLines;
	}

	public void output(String text, IWContext modinfo) throws IOException {
		PrintWriter out = modinfo.getResponse().getWriter();
		out.print(text);
	}

	/*
	 * public void setGraphic(Form myForm, Window myWindow) { String picUrl_1 =
	 * "/pics/rastimask/Rastimaskraning/Rastimaskraning_01.gif"; String picUrl_2 =
	 * "/pics/rastimask/Rastimaskraning/Rastimaskraning_02.gif"; String picUrl_3 =
	 * "/pics/rastimask/Rastimaskraning/Rastimaskraning_03.gif"; String picUrl_4 =
	 * "/pics/rastimask/Rastimaskraning/Rastimaskraning_04.gif"; String picUrl_6 =
	 * "/pics/rastimask/Rastimaskraning/Rastimaskraning_06.gif"; Table Header =
	 * new Table(4, 1); Header.setHeight("112"); Header.setWidth(1, "96");
	 * Header.setWidth(2, "205"); Header.setWidth(3, "112"); Header.setBorder(1);
	 * Header.setCellpadding(0); Header.setCellspacing(0);
	 * myWindow.setMarginWidth(0); myWindow.setMarginHeight(0);
	 * myWindow.setLeftMargin(0); myWindow.setTopMargin(0);
	 * myWindow.setScrollbar(true); Header.setBackgroundImage(1, 1, new
	 * Image(picUrl_1)); Header.setBackgroundImage(2, 1, new Image(picUrl_2));
	 * Header.setBackgroundImage(3, 1, new Image(picUrl_3));
	 * Header.setBackgroundImage(4, 1, new Image(picUrl_4));
	 * Header.setWidth("100%"); myForm.add(Header);
	 * myWindow.setBackgroundImage(new Image(picUrl_6, "")); }
	 */

	public void setGraphic(Form myForm) {
		String picUrl_1 = "/pics/rastima_popup/skraning.gif";
		String picUrl_2 = "/pics/rastima_popup/tile.gif";

		Table Header = new Table(2, 1);
		//Header.setBorder(1);

		Header.setHeight("90");
		Header.setWidth(1, "10");

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

		getParentPage().setBackgroundColor("#8ab490");
	}

	public void setErroResponse(Form myForm, boolean inputErr) {
		//String borderPicUrl = "/pics/rastimask/Rastimaskraning/BorderTiler.gif";
		String btnCloseUrl = "/pics/rastimask/Takkar/TLoka1.gif";
		String btnBackUrl = "/pics/rastimask/Takkar/Ttilbaka1.gif";

		Table myTable = new Table(2, 3);
		if (inputErr) {
			myTable.add(getErrorText(localize("start.error02","Nau›synlegt er a› skrá eins marga og teknir voru frá")), 2, 1);
		}
		else
			myTable.add(getErrorText(localize("start.error03","ﬁetta holl er ﬂví mi›ur fullt. Gjör›u svo vel a› velja ﬂér n‡jan tíma")), 2, 1);
		//myTable.add(new Image(borderPicUrl), 1, 1);
		//myTable.add(new Image(borderPicUrl), 1, 2);
		//myTable.add(new Image(borderPicUrl), 1, 3);
		if (inputErr) {
			myTable.add(new BackButton(getResourceBundle().getImage(btnBackUrl)), 2, 3);
		}
		else
			myTable.add(new CloseButton(getResourceBundle().getImage(btnCloseUrl)), 2, 3);
		myTable.setAlignment(2, 3, "center");
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);

		myForm.add(myTable);

	}

	public GolfField getFieldInfo(int field, String date) throws SQLException, IOException {
		StartingtimeFieldConfig FieldConfig = service.getFieldConfig(field, date);
		GolfField field_info = new GolfField(new IWTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new IWTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShown(), true);
		return field_info;
	}

	public String getTime(int end, GolfField myGolfField) {

		int interval = myGolfField.get_interval();
		int openMin = myGolfField.get_open_min();
		int openHour = myGolfField.get_open_hour();

		String Time = "";

		for (int i = 1; i <= end; i++) {

			if (openMin >= 60) {
				openMin -= 60;
				openHour++;
			}

			if (openMin < 10)
				Time = openHour + ":0" + openMin;
			else
				Time = openHour + ":" + openMin;

			openMin += interval;

		}
		return Time;
	}
}