package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class TournamentInfo extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();
		IWBundle bundle = getBundle();
		String tournament_id;
		String action = modinfo.getParameter("action");
		if (action == null) {
			action = "";
		}

		if (getTournamentSession(modinfo).getTournamentID() != -1) {
			Tournament tournament = getTournamentSession(modinfo).getTournament();
			boolean ongoing = tournament.isTournamentOngoing();
			boolean finished = tournament.isTournamentFinished();

			Table myTable = new Table(1, 2);
			myTable.setWidth("100%");
			myTable.setCellpadding(0);
			myTable.setCellspacing(0);
			myTable.setAlignment(1, 1, "right");
			myTable.setVerticalAlignment(1, 1, "bottom");
			myTable.setVerticalAlignment(1, 2, "top");

			Text header = new TournamentName();
			header.addBreak();
			header.setFontSize(3);
			header.setBold();
			add(header);

			Image participantsImage = iwrb.getImage("tabs/participants1.gif");
			Image startingtimeImage = iwrb.getImage("tabs/teetimes1.gif");
			Image informationImage = iwrb.getImage("tabs/information1.gif");
			Image iOngoing = iwrb.getImage("tabs/scoreoverview1.gif");
			Image iFinished = iwrb.getImage("tabs/results1.gif");

			if ((action.equalsIgnoreCase("")) || (action.equals("information"))) {
				action = "information";
				informationImage = iwrb.getImage("tabs/information.gif");
			}
			else if (action.equals("member_list")) {
				participantsImage = iwrb.getImage("tabs/participants.gif");
			}
			else if (action.equalsIgnoreCase("startingtime")) {
				startingtimeImage = iwrb.getImage("tabs/teetimes.gif");
			}
			else if (action.equals("viewCurrentScore")) {
				iOngoing = iwrb.getImage("tabs/scoreoverview.gif");
			}
			else if (action.equals("viewFinalScore")) {
				iFinished = iwrb.getImage("tabs/results.gif");
			}

			Link infoLink = new Link(informationImage);
			infoLink.addParameter("action", "information");
			myTable.add(infoLink, 1, 1);

			Link link2 = new Link(participantsImage);
			link2.addParameter("action", "member_list");
			myTable.add(link2, 1, 1);

			Link link = new Link(startingtimeImage);
			link.addParameter("action", "startingtime");
			myTable.add(link, 1, 1);

			Link ongoingLink = new Link(iOngoing);
			ongoingLink.addParameter("action", "viewCurrentScore");

			if (finished) {
				ongoingLink = new Link(iFinished);
				ongoingLink.addParameter("action", "viewFinalScore");
			}

			myTable.add(ongoingLink, 1, 1);

			Table table2 = new Table();
			table2.setCellpadding(0);
			table2.setCellspacing(0);
			table2.setWidth("100%");
			//table2.setHeight("100%");

			if (action.equals("member_list")) {
				table2.add(new TournamentParticipantsList());
			}
			else if (action.equalsIgnoreCase("startingtime")) {
				String tournament_round = modinfo.getParameter("tournament_round");

				TournamentStartingtimeList form = TournamentController.getStartingtimeTable(tournament, tournament_round, true, false);
				form.maintainParameter("action");
				table2.add(form);
				table2.setAlignment(1, 1, "center");
			}
			else if (action.equals("information")) {
				table2.add(getInfo(modinfo, tournament, iwrb, bundle));
			}
			else if (action.equals("viewCurrentScore")) {
				table2.setAlignment(1, 1, "left");
				try {
					String gender = modinfo.getParameter("gender");
					String t_g_id = modinfo.getParameter("tournament_group_id");
					String t_r_id = modinfo.getParameter("tournament_round_id");
					String sort = modinfo.getParameter("sort");
					String order = modinfo.getParameter("order");

					ResultsViewer result = new ResultsViewer();
					result.addHiddenInput("action", action);
					result.setCacheable("ResView1_"+action+"_"+getTournamentSession(modinfo).getTournamentID()+"_"+gender+"_"+t_g_id+"_"+t_r_id+"_"+sort+"_"+order,1800000);
					table2.add(result, 1, 1);
				}
				catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
			else if (action.equals("viewFinalScore")) {
				table2.setAlignment(1, 1, "left");
				try {
					String gender = modinfo.getParameter("gender");
					String t_g_id = modinfo.getParameter("tournament_group_id");
					String t_r_id = modinfo.getParameter("tournament_round_id");
					String sort = modinfo.getParameter("sort");
					String order = modinfo.getParameter("order");

					ResultsViewer result = new ResultsViewer();
					result.addHiddenInput("action", action);
					result.setCacheable("ResView_" + action + "_" + getTournamentSession(modinfo).getTournamentID() + "_" + gender + "_" + t_g_id + "_" + t_r_id + "_" + sort + "_" + order, 1800000);
					table2.add(result, 1, 1);
				}
				catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}

			myTable.add(table2, 1, 2);
			add(myTable);

		}
		else {
			add(iwrb.getLocalizedString("tournament.no_tournament_selected", "No tournament selected"));
		}
	}

	public String scale_decimals(String nyForgjof, int scale) throws IOException {

		BigDecimal test2 = new BigDecimal(nyForgjof);

		String nyForgjof2 = test2.setScale(scale, 5).toString();

		return nyForgjof2;

	}

	public Table getInfo(IWContext modinfo, Tournament tournament, IWResourceBundle iwrb, IWBundle iwb) throws SQLException {
		boolean ongoing = tournament.isTournamentOngoing();
		boolean finished = tournament.isTournamentFinished();

		Union union = tournament.getUnion();
		Field field = tournament.getField();

		Table table = new Table(2, 10);
		table.setColor("#CEDFD0");
		table.setCellpadding(5);
		table.setCellspacing(1);
		table.mergeCells(1, 1, 2, 1);
		table.mergeCells(1, 8, 2, 8);
		table.mergeCells(1, 9, 2, 9);
		table.setWidth("690");
		table.setAlignment("center");
		table.setAlignment(2, 10, "right");
		table.setHorizontalZebraColored("#DCEFDE", "#EAFAEC");
		table.setRowColor(1, "#2C4E3B");
		table.setRowColor(8, "#2C4E3B");
		table.setRowColor(7, "#CEDFD0");
		table.setRowColor(10, "#CEDFD0");
		table.setWidth(2, "90%");

		Text name = new Text(tournament.getName());
		name.setFontFace(Text.FONT_FACE_VERDANA);
		name.setFontSize(Text.FONT_SIZE_10_HTML_2);
		name.setFontColor("#FFFFFF");
		name.setBold();
		table.add(name, 1, 1);

		int row = 1;

		Text extraInfoText = new Text(iwrb.getLocalizedString("tournament.information", "Information"), true, false, false);
		extraInfoText.setFontFace(Text.FONT_FACE_VERDANA);
		extraInfoText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		extraInfoText.setFontColor("#FFFFFF");
		extraInfoText.setBold();
		table.add(extraInfoText, 1, 8);

		Text startDateText = new Text(iwrb.getLocalizedString("tournament.date", "Date"), true, false, false);
		startDateText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(startDateText, 1, 3);

		Text RegDateText = new Text(iwrb.getLocalizedString("tournament.registration", "Registration"), true, false, false);
		RegDateText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(RegDateText, 1, 6);

		Text tournamentTypeText = new Text(iwrb.getLocalizedString("tournament.arrangement", "Arrangement"), true, false, false);
		tournamentTypeText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(tournamentTypeText, 1, 4);

		Text unionNameText = new Text(iwrb.getLocalizedString("tournament.club", "Club"), true, false, false);
		unionNameText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(unionNameText, 1, 2);

		Text fieldNameText = new Text(iwrb.getLocalizedString("tournament.field", "Field"), true, false, false);
		fieldNameText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(fieldNameText, 1, 5);

		IWTimestamp startStamp = new IWTimestamp(tournament.getStartTime());
		Text startDate = new Text(startStamp.getLocaleDate(modinfo.getCurrentLocale()));
		startDate.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(startDate, 2, 3);

		IWTimestamp firstRegStamp = new IWTimestamp(tournament.getFirstRegistrationDate());
		IWTimestamp lastRegStamp = new IWTimestamp(tournament.getLastRegistrationDate());
		Text RegDate = new Text(firstRegStamp.getDate() + "/" + firstRegStamp.getMonth() + "/" + firstRegStamp.getYear() + " - " + lastRegStamp.getDate() + "/" + lastRegStamp.getMonth() + "/" + lastRegStamp.getYear());
		RegDate.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(RegDate, 2, 6);

		Text tournamentType = new Text(tournament.getTournamentType().getName());
		tournamentType.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(tournamentType, 2, 4);

		Text unionName = new Text(union.getName() + " (" + union.getAbbrevation() + ")");
		unionName.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(unionName, 2, 2);

		Text fieldName = new Text(field.getName());
		fieldName.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(fieldName, 2, 5);

		Text extraInfo = new Text("");
		if (tournament.getExtraText() != null) {
			String theExtraInfo = TextSoap.formatText(tournament.getExtraText());
			extraInfo.setText(theExtraInfo);
		}
		extraInfo.setFontSize(Text.FONT_SIZE_10_HTML_2);

		table.add(extraInfo, 1, 9);

		if (AccessControl.isAdmin(modinfo) || AccessControl.isClubAdmin(modinfo)) {
			if ((!ongoing) && (!finished)) {
				Form form4 = new Form(new Window(iwrb.getLocalizedString("tournament.tournament_editor", "Tournament editor"), "/tournament/deletetournament.jsp"));
				form4.add(new Parameter("tournament_id", tournament.getID() + ""));
				form4.add(new SubmitButton(getResourceBundle().getImage("buttons/delete_tournament.gif")));
				table.add(form4, 1, row);
			}
			/*
			 * Form modifyTournamentF1 = new Form(new
			 * Window(iwrb.getLocalizedString("tournament.tournament_editor","Tournament
			 * editor"),"modifytournament.jsp")); modifyTournamentF1.add(new
			 * Parameter("tournament",tournament.getID()+"" )); // vantar submittakka
			 * table.add(modifyTournamentF1,1,3);
			 */
		}
		table.setAlignment(1, row, "left");

		if (TournamentController.isOnlineRegistration(tournament)) {
			Image registerImage = getResourceBundle().getImage("buttons/skramigimotid.gif");
			registerImage.setName(iwrb.getLocalizedString("tournament.register_me", "Register me"));
			Window window = new Window("Skraning", 700, 600, "registrationForMembers.jsp");
			window.setResizable(true);
			Link register = new Link(registerImage, window);
			register.addParameter("action", "open");
			register.addParameter("tournament_id", Integer.toString(tournament.getID()));
			register.setFontSize(1);
			table.add(register, 2, 3);
		}

		return table;

	}

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}