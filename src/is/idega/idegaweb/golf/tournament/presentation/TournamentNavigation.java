package is.idega.idegaweb.golf.tournament.presentation;

import java.sql.SQLException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class TournamentNavigation extends GolfBlock {

	static int daysToDisplay = 14;
	private IWResourceBundle iwrb;
	private IWBundle iwb;

	public void main(IWContext modinfo) throws Exception {
		iwb = getBundle();
		iwrb = getResourceBundle();
		String view = modinfo.getParameter("i_tournament_view");
		setAreResults(modinfo, false);

		if (isAdmin() || isClubAdmin()) {
			Window window = new Window("TournamentAdmin", "tournamentadmin.jsp");
			window.setWidth(850);
			window.setHeight(600);
			window.setResizable(true);

			Paragraph par = new Paragraph();
			par.setAlign("left");
			add(par);
			Form form1 = new Form(window);
			SubmitButton Button1 = new SubmitButton(iwrb.getImage("tournament/tournamentmanager.gif"));
			form1.add(Button1);
			par.add(form1);
		}

		Form form = new Form();
		form.maintainParameter("i_tournament_view");

		String localeString = "";
		if (iwrb.getLocale() != null) {
			localeString = iwrb.getLocale().getCountry();
		}
		String union_id = modinfo.getParameter("union_id");
		TournamentList list = new TournamentList(view, iwrb);
		list.setCacheable("tournament_table_union_id_" + union_id + "_view_" + view + "_locale_" + localeString + "_startTime_" + getStartStamp(modinfo).toSQLDateString() + "_endTime_" + getEndStamp(modinfo).toSQLDateString());
		form.add(list);
		//form.add(getTournamentList(modinfo, view, iwrb));
		add(form);
	}

	public Table drawTable(IWContext modinfo, String union_id, String view) throws SQLException {
		Table myTable = new Table(2, 4);
		//myTable.setBorder(1);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setWidth("100%");
		myTable.setHeight("100%");
		myTable.setColor("#99CC99");
		myTable.setColor(1, 1, "#FFFFFF");
		myTable.setColor(2, 1, "#FFFFFF");
		myTable.setColor(1, 2, "#CEDFD0");
		myTable.setColor(1, 3, "#CEDFD0");
		myTable.setColor(1, 4, "#CEDFD0");
		myTable.setAlignment(1, 1, "center");
		myTable.setAlignment(2, 1, "right");
		myTable.setAlignment(1, 2, "right");
		myTable.setAlignment(1, 3, "center");
		myTable.setVerticalAlignment(2, 1, "bottom");
		myTable.mergeCells(1, 2, 2, 2);
		myTable.mergeCells(1, 3, 2, 3);
		myTable.mergeCells(1, 4, 2, 4);
		myTable.setHeight(3, "100%");
		myTable.setHeight(2, "19");

		Image mynd4 = iwrb.getImage("leftcorner.gif");
		Image iResults = iwrb.getImage("tabs/results1.gif");
		Image iAllTournaments = iwrb.getImage("tabs/alltournaments1.gif");
		Image iOpenTournaments = iwrb.getImage("tabs/opentournaments1.gif");
		Image iFemaleTournaments = iwrb.getImage("tabs/ladies1.gif");
		Image iYoungTournaments = iwrb.getImage("tabs/juniors1.gif");
		Image iOlderTournaments = iwrb.getImage("tabs/seniors1.gif");

		if (view == null) {
			iAllTournaments = iwrb.getImage("tabs/alltournaments.gif");
		}
		else if (view.equals("results")) {
			iResults = iwrb.getImage("tabs/results.gif");
			setAreResults(modinfo, true);
		}
		else if (view.equals("allTournaments")) {
			iAllTournaments = iwrb.getImage("tabs/alltournaments.gif");
		}
		else if (view.equals("openTournaments")) {
			iOpenTournaments = iwrb.getImage("tabs/opentournaments.gif");
		}
		else if (view.equals("femaleTournaments")) {
			iFemaleTournaments = iwrb.getImage("tabs/ladies.gif");
		}
		else if (view.equals("youngTournaments")) {
			iYoungTournaments = iwrb.getImage("tabs/juniors.gif");
		}
		else if (view.equals("olderTournaments")) {
			iOlderTournaments = iwrb.getImage("tabs/seniors.gif");
		}

		String syear = modinfo.getParameter("start_year");
		String smonth = modinfo.getParameter("start_month");
		String sday = modinfo.getParameter("start_day");
		String eyear = modinfo.getParameter("end_year");
		String emonth = modinfo.getParameter("end_month");
		String eday = modinfo.getParameter("end_day");

		Link results = new Link(iResults, "index.jsp");
		results.addParameter("i_tournament_view", "results");
		results.addParameter("start_year", syear);
		results.addParameter("start_month", smonth);
		results.addParameter("start_day", sday);
		results.addParameter("end_year", eyear);
		results.addParameter("end_month", emonth);
		results.addParameter("end_day", eday);

		Link ollmot = new Link(iAllTournaments, "index.jsp");
		ollmot.addParameter("i_tournament_view", "allTournaments");
		ollmot.addParameter("start_year", syear);
		ollmot.addParameter("start_month", smonth);
		ollmot.addParameter("start_day", sday);
		ollmot.addParameter("end_year", eyear);
		ollmot.addParameter("end_month", emonth);
		ollmot.addParameter("end_day", eday);
		Link opinmot = new Link(iOpenTournaments, "index.jsp");
		opinmot.addParameter("i_tournament_view", "openTournaments");
		opinmot.addParameter("start_year", syear);
		opinmot.addParameter("start_month", smonth);
		opinmot.addParameter("start_day", sday);
		opinmot.addParameter("end_year", eyear);
		opinmot.addParameter("end_month", emonth);
		opinmot.addParameter("end_day", eday);
		Link kvennamot = new Link(iFemaleTournaments, "index.jsp");
		kvennamot.addParameter("i_tournament_view", "femaleTournaments");
		kvennamot.addParameter("start_year", syear);
		kvennamot.addParameter("start_month", smonth);
		kvennamot.addParameter("start_day", sday);
		kvennamot.addParameter("end_year", eyear);
		kvennamot.addParameter("end_month", emonth);
		kvennamot.addParameter("end_day", eday);
		Link unglingamot = new Link(iYoungTournaments, "index.jsp");
		unglingamot.addParameter("i_tournament_view", "youngTournaments");
		unglingamot.addParameter("start_year", syear);
		unglingamot.addParameter("start_month", smonth);
		unglingamot.addParameter("start_day", sday);
		unglingamot.addParameter("end_year", eyear);
		unglingamot.addParameter("end_month", emonth);
		unglingamot.addParameter("end_day", eday);
		Link eldrimot = new Link(iOlderTournaments, "index.jsp");
		eldrimot.addParameter("i_tournament_view", "olderTournaments");
		eldrimot.addParameter("start_year", syear);
		eldrimot.addParameter("start_month", smonth);
		eldrimot.addParameter("start_day", sday);
		eldrimot.addParameter("end_year", eyear);
		eldrimot.addParameter("end_month", emonth);
		eldrimot.addParameter("end_day", eday);

		if (union_id != null) {
			ollmot.addParameter("union_id", union_id);
			opinmot.addParameter("union_id", union_id);
			kvennamot.addParameter("union_id", union_id);
			unglingamot.addParameter("union_id", union_id);
			eldrimot.addParameter("union_id", union_id);
		}

		myTable.add(results, 2, 1);
		myTable.add(opinmot, 2, 1);
		myTable.add(kvennamot, 2, 1);
		myTable.add(unglingamot, 2, 1);
		myTable.add(eldrimot, 2, 1);
		myTable.add(ollmot, 2, 1);

		return myTable;
	}

	private Table getFormTable(IWTimestamp startTime, IWTimestamp endTime, IWContext modinfo) throws SQLException {
		Table myTable = new Table(2, 3);
		//myTable.setBorder(1);
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setWidth("98%");
		myTable.setAlignment(2, 2, "right");
		myTable.mergeCells(1, 1, 2, 1);
		myTable.mergeCells(1, 3, 2, 3);

		IWTimestamp now = IWTimestamp.RightNow();
		IWCalendar dagatalid = new IWCalendar();
		String headerTextColor = "#000000";

		DropdownMenu start_y = new DropdownMenu("start_year");
		for (int y = 2000; y <= now.getYear() + 1; y++) {
			start_y.addMenuElement(String.valueOf(y), String.valueOf(y));
		}

		start_y.setSelectedElement(Integer.toString(startTime.getYear()));
		start_y.setStyleAttribute("font-size: 8pt");

		DropdownMenu start_m = new DropdownMenu("start_month");
		int mon = 12;
		for (int m = 1; m <= mon; m++) {
			start_m.addMenuElement(String.valueOf(m), dagatalid.getMonthName(m).toLowerCase().substring(0, 3) + ".");
		}

		start_m.setSelectedElement(Integer.toString(startTime.getMonth()));
		start_m.setStyleAttribute("font-size: 8pt");

		DropdownMenu start_d = new DropdownMenu("start_day");
		for (int d = 1; d <= 31; d++) {
			start_d.addMenuElement(String.valueOf(d), String.valueOf(d) + ".");
		}
		start_d.setSelectedElement(Integer.toString(startTime.getDay()));
		start_d.setStyleAttribute("font-size: 8pt");

		DropdownMenu end_y = new DropdownMenu("end_year");
		for (int y = 2000; y <= now.getYear() + 1; y++) {
			end_y.addMenuElement(String.valueOf(y), String.valueOf(y));
		}

		end_y.setSelectedElement(Integer.toString(endTime.getYear()));
		end_y.setStyleAttribute("font-size: 8pt");

		DropdownMenu end_m = new DropdownMenu("end_month");
		mon = 12;
		for (int m = 1; m <= mon; m++) {
			end_m.addMenuElement(String.valueOf(m), dagatalid.getMonthName(m).toLowerCase().substring(0, 3) + ".");
		}

		end_m.setSelectedElement(Integer.toString(endTime.getMonth()));
		end_m.setStyleAttribute("font-size: 8pt");

		DropdownMenu end_d = new DropdownMenu("end_day");
		for (int d = 1; d <= 31; d++) {
			end_d.addMenuElement(String.valueOf(d), String.valueOf(d) + ".");
		}

		end_d.setSelectedElement(Integer.toString(endTime.getDay()));
		end_d.setStyleAttribute("font-size: 8pt");

		SubmitButton skoda = new SubmitButton(iwrb.getImage("buttons/get.gif", iwrb.getLocalizedString("handicap.get_overview", "Get overview"), 76, 19));
		skoda.setStyleAttribute("font-size: 8pt");

		Text fra = new Text(iwrb.getLocalizedString("handicap.from", "From") + ": ");
		fra.setBold();
		fra.setFontColor(headerTextColor);
		Text til = new Text(iwrb.getLocalizedString("handicap.to", "To") + ": ");
		til.setBold();
		til.setFontColor(headerTextColor);
		Text strik = new Text("&nbsp;");
		Text bil = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		myTable.add(fra, 2, 2);
		myTable.add(start_d, 2, 2);
		myTable.add(start_m, 2, 2);
		myTable.add(start_y, 2, 2);

		myTable.add(strik, 2, 2);

		myTable.add(til, 2, 2);
		myTable.add(end_d, 2, 2);
		myTable.add(end_m, 2, 2);
		myTable.add(end_y, 2, 2);

		myTable.add(strik, 2, 2);
		myTable.add(strik, 2, 2);

		myTable.add(skoda, 2, 2);

		myTable.add(strik, 2, 2);

		String union_id = modinfo.getParameter("union_id");
		if (union_id != null) {
			if (union_id.equals("3")) {
				union_id = null;
			}
		}

		Union[] unions = (Union[]) ((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("ABBREVATION");
		DropdownMenu unionDrop = new DropdownMenu("union_id");
		unionDrop.setStyleAttribute("font-size: 8pt");
		if (unions != null) {
			for (int i = 0; i < unions.length; i++) {
				if (unions[i].getID() != 1)
					unionDrop.addMenuElement(unions[i].getID(), unions[i].getAbbrevation() + "&nbsp;&nbsp;" + unions[i].getName());
			}
		}
		if (union_id != null) {
			unionDrop.setSelectedElement(union_id);
		}
		else {
			unionDrop.setSelectedElement("3");
		}
		myTable.add(unionDrop, 1, 2);

		Text smallText = new Text("&nbsp;");
		smallText.setFontSize(1);

		myTable.setAlignment(1, 2, "left");
		myTable.add(smallText, 1, 1);
		myTable.add(smallText, 1, 3);

		return myTable;

	}

	public static IWTimestamp getStartStamp(IWContext modinfo) {
		String year = modinfo.getParameter("start_year");
		String month = modinfo.getParameter("start_month");
		String day = modinfo.getParameter("start_day");

		//	        idegaTimestamp stamp = new idegaTimestamp("2002-01-01");
		IWTimestamp stamp = IWTimestamp.RightNow();
		stamp.addDays(-7);

		if ((year != null) || (month != null) || (day != null)) {
			try {
				stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
			}
			catch (Exception e) {
			}
		}

		return stamp;
	}

	public static IWTimestamp getEndStamp(IWContext modinfo) {
		String year = modinfo.getParameter("end_year");
		String month = modinfo.getParameter("end_month");
		String day = modinfo.getParameter("end_day");

		IWTimestamp stamp = null;

		if ((year != null) || (month != null) || (day != null)) {
			try {
				stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
			}
			catch (Exception e) {
			}
		}

		if (stamp == null) {
			IWTimestamp temp = getStartStamp(modinfo);
			temp.addDays(daysToDisplay);
			stamp = new IWTimestamp(temp.toSQLDateString());
			//	              stamp = new idegaTimestamp("2002-12-31");
		}

		return stamp;
	}

	private static void setAreResults(IWContext modinfo, boolean results) {
		modinfo.setSessionAttribute("tournament_index_are_results", new Boolean(results));
	}

	private static boolean getAreResults(IWContext modinfo) {
		Boolean returner = new Boolean(false);
		returner = (Boolean) modinfo.getSessionAttribute("tournament_index_are_results");
		if (returner == null) {
			returner = new Boolean(false);
		}

		return returner.booleanValue();
	}

	public class TournamentList extends Block {

		String view;
		IWResourceBundle iwrb;

		public TournamentList(String view, IWResourceBundle iwrb) {
			this.view = view;
			this.iwrb = iwrb;
		}

		public void main(IWContext modinfo) throws SQLException {
			add(getTournamentList(modinfo, view, iwrb));
		}

		public PresentationObject getTournamentList(IWContext modinfo, String view, IWResourceBundle iwrb) throws SQLException {

			String union_id = modinfo.getParameter("union_id");
			if (union_id != null) {
				if (union_id.equals("3")) {
					union_id = null;
				}
			}

			Tournament[] tournaments;

			if (view == null)
				view = "allTournaments";

			IWTimestamp rightNowStamp = IWTimestamp.RightNow();
			IWTimestamp startStamp = getStartStamp(modinfo);
			IWTimestamp endStamp = getEndStamp(modinfo);

			Table outerTable = drawTable(modinfo, union_id, view);
			outerTable.setVerticalAlignment(1, 3, "top");
			outerTable.setAlignment(1, 2, "right");

			tournaments = getTournaments(modinfo, union_id, view);

			Table table = null;

			String localeString = "";
			if (iwrb.getLocale() != null) {
				localeString = iwrb.getLocale().getCountry();
			}

			Object tableObject = modinfo.getApplicationAttribute("tournament_table_union_id_" + union_id + "_view_" + view + "_locale_" + localeString + "_startTime_" + startStamp.toSQLDateString() + "_endTime_" + endStamp.toSQLDateString());

			if (tableObject != null) {
				table = (Table) tableObject;
			}

			if (table == null)
				if (tournaments != null) {
					int length = tournaments.length;
					if (tournaments.length > 0) {

						Text textProxy = new Text("");
						textProxy.setFontSize(1);

						table = new Table(7, length + 1);
						table.setWidth("98%");
						table.setCellpadding(1);
						table.setCellspacing(1);
						table.setAlignment("center");

						Text byrjar = (Text) textProxy.clone();
						byrjar.setText(iwrb.getLocalizedString("tournament.date", "Date"));
						byrjar.setFontFace(Text.FONT_FACE_VERDANA);
						byrjar.setBold();
						byrjar.setFontColor("#FFFFFF");
						byrjar.setFontSize(Text.FONT_SIZE_7_HTML_1);

						Text klubbur = (Text) textProxy.clone();
						klubbur.setText(iwrb.getLocalizedString("tournament.club", "Club"));
						klubbur.setFontFace(Text.FONT_FACE_VERDANA);
						klubbur.setBold();
						klubbur.setFontColor("#FFFFFF");
						klubbur.setFontSize(Text.FONT_SIZE_7_HTML_1);

						Text heiti = (Text) textProxy.clone();
						heiti.setText(iwrb.getLocalizedString("tournament.name", "Name"));
						heiti.setFontFace(Text.FONT_FACE_VERDANA);
						heiti.setBold();
						heiti.setFontColor("#FFFFFF");
						heiti.setFontSize(Text.FONT_SIZE_7_HTML_1);

						Text fyrirk = (Text) textProxy.clone();
						fyrirk.setText(iwrb.getLocalizedString("tournament.arrangement", "Arrangement"));
						fyrirk.setFontFace(Text.FONT_FACE_VERDANA);
						fyrirk.setBold();
						fyrirk.setFontColor("#FFFFFF");
						fyrirk.setFontSize(Text.FONT_SIZE_7_HTML_1);

						Text hringir = (Text) textProxy.clone();
						hringir.setText(iwrb.getLocalizedString("tournament.rounds", "Rounds"));
						hringir.setFontFace(Text.FONT_FACE_VERDANA);
						hringir.setBold();
						hringir.setFontColor("#FFFFFF");
						hringir.setFontSize(Text.FONT_SIZE_7_HTML_1);

						Text tegund = (Text) textProxy.clone();
						tegund.setText(iwrb.getLocalizedString("tournament.type", "Type"));
						tegund.setFontFace(Text.FONT_FACE_VERDANA);
						tegund.setBold();
						tegund.setFontColor("#FFFFFF");
						tegund.setFontSize(Text.FONT_SIZE_7_HTML_1);

						Text registerText = (Text) textProxy.clone();
						registerText.setText(iwrb.getLocalizedString("tournament.register_sm", "Register"));
						registerText.setFontFace(Text.FONT_FACE_VERDANA);
						registerText.setBold();
						registerText.setFontColor("#FFFFFF");
						registerText.setFontSize(Text.FONT_SIZE_7_HTML_1);

						table.add(byrjar, 1, 1);
						table.add(klubbur, 2, 1);
						table.add(heiti, 3, 1);
						table.add(fyrirk, 4, 1);
						table.add(hringir, 5, 1);
						table.add(tegund, 6, 1);
						table.add(registerText, 7, 1);
						table.setRowAlignment(1, "center");
						table.setRowColor(1, "#2C4E3B");
						table.setHeight(1, "20");

						Link linkProxy = new Link();
						linkProxy.setFontSize(1);
						linkProxy.setURL("tournamentinfo.jsp");

						String t_union_id;
						IWTimestamp start;
						IWTimestamp end;
						TournamentRound tRound;

						Image closedImage = iwb.getImage("shared/lock_closed.gif", iwrb.getLocalizedString("tournament.closed_tournament", "Closed tournament"), 15, 15);
						closedImage.setHorizontalSpacing(6);
						closedImage.setAlignment("absmiddle");
						Image openImage = iwb.getImage("shared/lock_open.gif", iwrb.getLocalizedString("tournament.open_tournament", "Open tournament"), 15, 15);
						openImage.setHorizontalSpacing(6);
						openImage.setAlignment("absmiddle");
						Image closedTournamentImage = iwb.getImage("shared/flag.gif", iwrb.getLocalizedString("tournament.tournament_closed", "Tournament is closed and updated"), 9, 13);
						closedTournamentImage.setHorizontalSpacing(4);
						closedTournamentImage.setAlignment("absmiddle");
						Image linkImage = iwb.getImage("shared/view.gif", iwrb.getLocalizedString("tournament.view_tournament", "View tournament"), 9, 18);
						linkImage.setHorizontalSpacing(4);
						linkImage.setAlignment("absmiddle");

						for (int i = 0; i < length; i++) {
							t_union_id = "" + tournaments[i].getUnionId();

							start = new IWTimestamp(tournaments[i].getStartTime());

							Text date = (Text) textProxy.clone();
							date.setText(start.getDate() + "/" + start.getMonth() + "/" + start.getYear());
							table.add(date, 1, i + 2);

							/*
							 * // wait florist.... if (tournaments[i].getNumberOfRounds() > 1) {
							 * try { tRound =
							 * tournaments[i].getTournamentRounds()[tournaments[i].getNumberOfRounds()-1] ;
							 * end = new idegaTimestamp(tRound.getRoundDate());
							 * date.addToText(" - "+end.getISLDate()); } catch
							 * (ArrayIndexOutOfBoundsException a) {} }
							 */

							Text union = (Text) textProxy.clone();
							if (t_union_id.equalsIgnoreCase("3")) {
								Field field = tournaments[i].getField();
								Union uUnion = GolfCacher.getCachedUnion(field.getUnionID());
								union.addToText(uUnion.getAbbrevation());
							}
							else {
								union.addToText(tournaments[i].getUnion().getAbbrevation() + "");
							}
							table.add(union, 2, i + 2);

							Link link = new Link(linkImage);
							link.setFontSize(1);
							link.setURL("tournamentinfo.jsp");
							link.addParameter("tournament_id", tournaments[i].getID());

							Text nameText = (Text) textProxy.clone();
							if (t_union_id.equalsIgnoreCase("3")) {
								nameText.addToText(tournaments[i].getUnion().getAbbrevation() + "&nbsp;-&nbsp;");
							}
							nameText.addToText(tournaments[i].getName());

							Link textLink = new Link(nameText);
							textLink.setFontSize(1);
							textLink.setURL("tournamentinfo.jsp");
							textLink.addParameter("tournament_id", tournaments[i].getID());

							if (tournaments[i].getIsClosed()) {
								table.add(closedTournamentImage, 3, i + 2);
							}

							table.add(link, 3, i + 2);
							table.add(textLink, 3, i + 2);

							Text name = (Text) textProxy.clone();
							name.setText(tournaments[i].getTournamentType().getName());
							table.add(name, 4, i + 2);

							Text rounds = (Text) textProxy.clone();
							rounds.setText(Integer.toString(tournaments[i].getNumberOfRounds()));
							table.add(rounds, 5, i + 2);

							if (tournaments[i].getIfOpenTournament()) {
								table.add(openImage, 6, i + 2);
							}
							else {
								table.add(closedImage, 6, i + 2);
							}

							String form = tournaments[i].getTournamentForm().getName();
							Text theForm = (Text) textProxy.clone();
							theForm.setText(form);
							table.add(theForm, 6, i + 2);

							if (TournamentController.isOnlineRegistration(tournaments[i], rightNowStamp)) {
								Image registerImage = iwb.getImage("shared/register.gif");
								registerImage.setName(iwrb.getLocalizedString("tournament.register_me", "Register me"));
								Window theWindow = new Window(iwrb.getLocalizedString("tournament.register_me", "Register me"), 700, 600, "registrationForMembers.jsp");
								theWindow.setResizable(true);
								Link register = new Link(registerImage, theWindow);
								register.addParameter("action", "open");
								register.addParameter("tournament_id", Integer.toString(tournaments[i].getID()));
								register.setFontSize(1);
								table.add(register, 7, i + 2);
							}
							table.setHeight(i + 2, "20");

						}

						table.setHorizontalZebraColored("#EAFAEC", "#DCEFDE");
						table.setRowColor(1, "#2C4E3B");
						table.setColumnAlignment(1, "center");
						table.setColumnAlignment(2, "center");
						table.setColumnAlignment(5, "center");
						table.setColumnAlignment(7, "center");

						modinfo.setApplicationAttribute("tournament_table_union_id_" + union_id + "_view_" + view + "_locale_" + localeString + "_startTime_" + startStamp.toSQLDateString() + "_endTime_" + endStamp.toSQLDateString(), table);

					}
					else {
						outerTable.add(iwrb.getLocalizedString("tournament.no_pending_tournaments", "No pending tournaments"), 1, 3);
					}

				}
				else {
					outerTable.add(iwrb.getLocalizedString("tournament.no_pending_tournaments", "No pending tournaments"), 1, 3);
				}

			outerTable.add(table, 1, 3);

			if (endStamp == null) {
				startStamp.addDays(daysToDisplay);
				endStamp = new IWTimestamp(startStamp.toSQLDateString());
				startStamp.addDays(-daysToDisplay);
			}

			outerTable.add(getFormTable(startStamp, endStamp, modinfo), 1, 2);

			return outerTable;

		}

		public Tournament[] getTournaments(IWContext modinfo, String union_id, String view) throws SQLException {

			Tournament[] tournaments = null;

			IWTimestamp startStamp = getStartStamp(modinfo);
			IWTimestamp endStamp = getEndStamp(modinfo);

			String SQLString = "SELECT * FROM tournament ";
			String andUnionSQLString = "";
			String whereUnionSQLString = "";
			boolean useAND = false;

			boolean areResults = getAreResults(modinfo);

			Field[] fields = {};
			if (union_id != null) {
				fields = (Field[]) ((Field) IDOLookup.instanciateEntity(Field.class)).findAll("Select * from field where union_id=" + union_id);
				if (fields.length > 0) {
					andUnionSQLString = "AND (union_id = " + union_id + " OR field_id =" + fields[0].getID() + ") ";
					whereUnionSQLString = "WHERE (union_id = " + union_id + " OR field_id =" + fields[0].getID() + ") ";
					useAND = true;
				}
			}

			if (view == null) {
				if (union_id != null) {
					SQLString += whereUnionSQLString;
					if (fields.length > 0) {
						useAND = true;
					}
				}
			}
			else {
				if (view.equals("femaleTournaments")) {
					SQLString = "SELECT * FROM tournament WHERE tournament_form_id=2 " + andUnionSQLString + "";
					useAND = true;
				}
				else if (view.equals("youngTournaments")) {
					SQLString = "SELECT * FROM tournament WHERE tournament_form_id=3 " + andUnionSQLString + "";
					useAND = true;
				}
				else if (view.equals("olderTournaments")) {
					SQLString = "SELECT * FROM tournament WHERE tournament_form_id=4 " + andUnionSQLString + "";
					useAND = true;
				}
				else if (view.equals("openTournaments")) {
					SQLString = "SELECT * FROM tournament WHERE OPEN_TOURNAMENT='Y' " + andUnionSQLString + "";
					useAND = true;
				}
				else {
					if (union_id != null) {
						SQLString += whereUnionSQLString;
						useAND = true;
					}
				}

			}

			if (areResults) {
				IWTimestamp rightNowStamp = IWTimestamp.RightNow();
				if (useAND) {
					SQLString += "AND start_time <= '" + rightNowStamp.toSQLDateString() + "' ";
				}
				else {
					SQLString += "WHERE start_time <= '" + rightNowStamp.toSQLDateString() + "' ";
				}
				SQLString += " ORDER BY start_time DESC";
				setAreResults(modinfo, false);
			}
			else {
				if (useAND) {
					SQLString += "AND start_time >= '" + startStamp.toSQLDateString() + "' AND start_time <= '" + endStamp.toSQLDateString() + "'";
					//SQLString += "AND ((t.start_time >=
					// '"+startStamp.toSQLDateString()+"' AND t.start_time <=
					// '"+endStamp.toSQLDateString()+"') OR (max(tr.round_end_date) >=
					// '"+startStamp.toSQLDateString()+"' AND max(tr.round_end_date) <=
					// '"+endStamp.toSQLDateString()+"' ))";
				}
				else {
					SQLString += "WHERE start_time >= '" + startStamp.toSQLDateString() + "' AND start_time <= '" + endStamp.toSQLDateString() + "' ";
					//SQLString += "WHERE tournament_id = tr.tournament_id AND
					// ((t.start_time >= '"+startStamp.toSQLDateString()+"' AND
					// t.start_time <= '"+endStamp.toSQLDateString()+"') OR
					// (max(tr.round_end_date) >= '"+startStamp.toSQLDateString()+"' AND
					// max(tr.round_end_date) <= '"+endStamp.toSQLDateString()+"')) ";
				}
				SQLString += " ORDER BY start_time";
			}

			try {
				//System.err.println("SQL : "+SQLString);
				//add("<br>"+areResults + "");
				tournaments = (Tournament[]) ((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAll(SQLString);
			}
			catch (java.sql.SQLException sql) {
				System.err.println("/tournament/index.jsp : finnst ekki golfvollur fyrir union ");
				tournaments = new Tournament[]{};
			}

			return tournaments;
		}
	}
}