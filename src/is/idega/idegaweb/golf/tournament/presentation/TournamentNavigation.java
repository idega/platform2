package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.sql.SQLException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class TournamentNavigation extends GolfBlock {

	static int daysToDisplay = 14;
	private IWResourceBundle iwrb;
	private IWBundle iwb;
	
	private ICPage tournamentPage;

	public void main(IWContext modinfo) throws Exception {
		iwb = getBundle();
		iwrb = getResourceBundle();
		String view = modinfo.getParameter("i_tournament_view");
		String union_id = modinfo.getParameter("union_id");
		TournamentList.setAreResults(modinfo, false);

		if (isAdmin() || isClubAdmin()) {
			Paragraph par = new Paragraph();
			par.setAlign("left");
			add(par);
			
			Link link = new Link(iwrb.getImage("tournament/tournamentmanager.gif"));
			link.setWindowToOpen(TournamentAdministratorWindow.class);
			par.add(link);
		}

		String localeString = "";
		if (iwrb.getLocale() != null) {
			localeString = iwrb.getLocale().getCountry();
		}

		Table table = drawTable(modinfo, union_id, view);
		TournamentList list = new TournamentList(view);
		list.setTournamentPage(tournamentPage);
		list.setCacheable("tournament_table_union_id_" + union_id + "_view_" + view + "_locale_" + localeString + "_startTime_" + getStartStamp(modinfo).toSQLDateString() + "_endTime_" + getEndStamp(modinfo).toSQLDateString());
		table.add(list, 1, 2);
		
		add(table);
	}
	
	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public IWTimestamp getStartStamp(IWContext modinfo) {
		IWTimestamp stamp = null;
		try {
			stamp = new IWTimestamp(getTournamentSession(modinfo).getStartDate());
		}
		catch (Exception e) {
			stamp = IWTimestamp.RightNow();
			stamp.addDays(-7);
		}

		return stamp;
	}

	public IWTimestamp getEndStamp(IWContext modinfo) {
		IWTimestamp stamp = null;

		try {
			stamp = new IWTimestamp(getTournamentSession(modinfo).getEndDate());
		}
		catch (Exception e) {
			stamp = getStartStamp(modinfo);
			stamp.addDays(daysToDisplay);
		}

		return stamp;
	}

	public void setTournamentPage(ICPage page) {
		tournamentPage = page;
	}

	public Table drawTable(IWContext modinfo, String union_id, String view) throws SQLException {
		Table myTable = new Table(2, 2);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setWidth("100%");
		myTable.setColor(1, 1, "#FFFFFF");
		myTable.setColor(2, 1, "#FFFFFF");
		myTable.setAlignment(1, 1, "center");
		myTable.setAlignment(2, 1, "right");
		myTable.setVerticalAlignment(2, 1, "bottom");
		myTable.mergeCells(1, 2, 2, 2);

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
			TournamentList.setAreResults(modinfo, true);
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

		Link results = new Link(iResults);
		results.addParameter("i_tournament_view", "results");

		Link ollmot = new Link(iAllTournaments);
		ollmot.addParameter("i_tournament_view", "allTournaments");

		Link opinmot = new Link(iOpenTournaments);
		opinmot.addParameter("i_tournament_view", "openTournaments");

		Link kvennamot = new Link(iFemaleTournaments);
		kvennamot.addParameter("i_tournament_view", "femaleTournaments");

		Link unglingamot = new Link(iYoungTournaments);
		unglingamot.addParameter("i_tournament_view", "youngTournaments");

		Link eldrimot = new Link(iOlderTournaments);
		eldrimot.addParameter("i_tournament_view", "olderTournaments");

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
}