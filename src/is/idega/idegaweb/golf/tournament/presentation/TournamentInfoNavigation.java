package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.io.IOException;
import java.math.BigDecimal;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 */
public class TournamentInfoNavigation extends GolfBlock {

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

				TournamentStartingtimeList form = getTournamentBusiness(modinfo).getStartingtimeTable(tournament, tournament_round, true, false);
				table2.add(form);
				table2.setAlignment(1, 1, "center");
			}
			else if (action.equals("information")) {
				table2.add(new TournamentInfo());
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

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}