/*
 * Created on 23.4.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;
import is.idega.idegaweb.golf.tournament.even.TournamentEventListener;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentList extends GolfBlock {

	static int daysToDisplay = 14;

	private IWResourceBundle iwrb;
	
	public static final String PRM_UNION_ID = "union_id";

	private IWBundle iwb;

	String view;

	private ICPage iTournamentPage;

	public TournamentList() {
		this(null);
	}

	public TournamentList(String view) {
		this.view = view;
	}

	public void main(IWContext modinfo) throws SQLException, RemoteException {
		iwb = getBundle();
		iwrb = getResourceBundle();
		
		if (view == null) {
			view = modinfo.getParameter("view");
		}
		
		add(getTournamentList(modinfo, view, iwrb));
	}

	private Form getFormTable(IWTimestamp startTime, IWTimestamp endTime, IWContext modinfo) throws SQLException, RemoteException {
		Form form = new Form();
		form.setEventListener(TournamentEventListener.class);

		IWTimestamp now = IWTimestamp.RightNow();
		IWCalendar dagatalid = new IWCalendar();

		DateInput startDate = (DateInput) getStyledInterface(new DateInput(getTournamentSession(modinfo).getParameterNameStartDate()));
		startDate.setYearRange(2000, now.getYear());
		startDate.setDate(getStartStamp(modinfo).getDate());

		DateInput endDate = (DateInput) getStyledInterface(new DateInput(getTournamentSession(modinfo).getParameterNameEndDate()));
		endDate.setYearRange(2000, now.getYear());
		endDate.setDate(getEndStamp(modinfo).getDate());

		SubmitButton getOverView = (SubmitButton) getButton(new SubmitButton(iwrb.getLocalizedString("tournament.get_overview", "Get overview")));

		Table navigationTable = new Table(9, 3);
		navigationTable.setCellpadding(0);
		navigationTable.setCellspacing(0);
		int column = 1;
		int row = 1;

		String union_id = modinfo.getParameter("union_id");
		if (union_id != null) {
			if (union_id.equals("3")) {
				union_id = null;
			}
		}

		Union[] unions = (Union[]) ((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("ABBREVATION");
		DropdownMenu unionDrop = (DropdownMenu) getStyledInterface(new DropdownMenu("union_id"));
		if (unions != null) {
			for (int i = 0; i < unions.length; i++) {
				if (unions[i].getID() != 1) unionDrop.addMenuElement(unions[i].getID(), unions[i].getAbbrevation() + "&nbsp;&nbsp;" + unions[i].getName());
			}
		}
		if (union_id != null) {
			unionDrop.setSelectedElement(union_id);
		}
		else {
			unionDrop.setSelectedElement("3");
		}
		navigationTable.setCellpaddingLeft(column, row, 5);
		navigationTable.add(getHeader(iwrb.getLocalizedString("tournament.club", "Club") + ": "), column++, row);
		navigationTable.setWidth(column++, row, 5);
		navigationTable.mergeCells(column, row, navigationTable.getColumns(), row);
		navigationTable.add(unionDrop, column, row++);

		navigationTable.setHeight(row++, 6);
		column = 1;

		navigationTable.setCellpaddingLeft(column, row, 5);
		navigationTable.add(getHeader(iwrb.getLocalizedString("tournament.from", "From") + ": "), column++, row);
		navigationTable.setWidth(column++, row, 5);
		navigationTable.add(startDate, column++, row);
		navigationTable.setWidth(column++, row, 12);

		navigationTable.add(getHeader(iwrb.getLocalizedString("tournament.to", "To") + ": "), column++, row);
		navigationTable.setWidth(column++, row, 5);
		navigationTable.add(endDate, column++, row);
		navigationTable.setWidth(column++, row, 12);

		navigationTable.add(getOverView, column++, row);

		form.add(navigationTable);

		return form;
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

	public PresentationObject getTournamentList(IWContext modinfo, String view, IWResourceBundle iwrb) throws SQLException, RemoteException {
		String union_id = modinfo.getParameter("union_id");
		if (union_id != null) {
			if (union_id.equals("3")) {
				union_id = null;
			}
		}

		Tournament[] tournaments;
		if (view == null) {
			view = "allTournaments";
		}

		if (view.equalsIgnoreCase("results")) {
			setAreResults(modinfo, true);
		}
		else {
			setAreResults(modinfo, false);
		}

		Table outerTable = new Table(1, 3);
		outerTable.setCellpadding(0);
		outerTable.setCellspacing(0);
		outerTable.setWidth(Table.HUNDRED_PERCENT);
		outerTable.setHeight(1, 50);

		IWTimestamp rightNowStamp = IWTimestamp.RightNow();
		IWTimestamp startStamp = getStartStamp(modinfo);
		IWTimestamp endStamp = getEndStamp(modinfo);

		Table table = null;

		String localeString = "";
		if (iwrb.getLocale() != null) {
			localeString = iwrb.getLocale().getCountry();
		}

		Object tableObject = modinfo.getApplicationAttribute("tournament_table_union_id_" + union_id + "_view_" + view + "_locale_" + localeString + "_startTime_" + startStamp.toSQLDateString() + "_endTime_" + endStamp.toSQLDateString());
		if (tableObject != null) {
			table = (Table) tableObject;
		}

		if (table == null) {
			tournaments = getTournaments(modinfo, union_id, view);

			if (tournaments != null) {
				int length = tournaments.length;
				if (tournaments.length > 0) {

					Text textProxy = new Text("");
					textProxy.setFontSize(1);

					table = new Table();
					table.setWidth(Table.HUNDRED_PERCENT);
					table.setCellpadding(0);
					table.setCellspacing(0);
					int row = 1;
					int column = 1;
					int zebraRow = 1;

					table.add(getSmallHeader(iwrb.getLocalizedString("tournament.date", "Date")), column++, row);
					table.add(getSmallHeader(iwrb.getLocalizedString("tournament.club", "Club")), column++, row);
					table.add(getSmallHeader(iwrb.getLocalizedString("tournament.name", "Name")), column++, row);
					table.add(getSmallHeader(iwrb.getLocalizedString("tournament.arrangement", "Arrangement")), column++, row);
					table.add(getSmallHeader(iwrb.getLocalizedString("tournament.rounds", "Rounds")), column++, row);
					table.add(getSmallHeader(iwrb.getLocalizedString("tournament.type", "Type")), column++, row);
					table.add(getSmallHeader(iwrb.getLocalizedString("tournament.register_sm", "Register")), column++, row);
					table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
					table.setAlignment(5, row, Table.HORIZONTAL_ALIGN_CENTER);
					table.setAlignment(7, row, Table.HORIZONTAL_ALIGN_CENTER);
					table.setRowColor(row, getHeaderColor());
					table.setRowPadding(row++, getCellpadding());

					String t_union_id;
					IWTimestamp start;
					IWTimestamp end;
					TournamentRound tRound;

					Image closedImage = iwb.getImage("shared/tournament/lock_closed.gif", iwrb.getLocalizedString("tournament.closed_tournament", "Closed tournament"));
					closedImage.setToolTip(iwrb.getLocalizedString("tournament.closed_tournament", "Closed tournament"));
					closedImage.setPaddingRight(6);
					closedImage.setAlignment("absmiddle");
					Image openImage = iwb.getImage("shared/tournament/lock_open.gif", iwrb.getLocalizedString("tournament.open_tournament", "Open tournament"));
					openImage.setToolTip(iwrb.getLocalizedString("tournament.open_tournament", "Open tournament"));
					openImage.setPaddingRight(6);
					openImage.setAlignment("absmiddle");
					Image closedTournamentImage = iwb.getImage("shared/tournament/flag.gif", iwrb.getLocalizedString("tournament.tournament_closed", "Tournament is closed and updated"));
					closedTournamentImage.setToolTip(iwrb.getLocalizedString("tournament.tournament_closed", "Tournament is closed and updated"));
					closedTournamentImage.setPaddingRight(4);
					closedTournamentImage.setAlignment("absmiddle");

					for (int i = 0; i < length; i++) {
						column = 1;
						t_union_id = "" + tournaments[i].getUnionId();

						start = new IWTimestamp(tournaments[i].getStartTime());

						Text date = getSmallText(start.getDateString("dd/MM/yy"));
						table.add(date, column++, row);

						Text union = getSmallText("");
						if (t_union_id.equalsIgnoreCase("3")) {
							Field field = tournaments[i].getField();
							Union uUnion = GolfCacher.getCachedUnion(field.getUnionID());
							union.setText(uUnion.getAbbrevation());
						}
						else {
							union.setText(tournaments[i].getUnion().getAbbrevation() + "");
						}
						table.add(union, column++, row);

						if (iTournamentPage != null) {
							StringBuffer nameText = new StringBuffer();
							if (t_union_id.equalsIgnoreCase("3")) {
								nameText.append(tournaments[i].getUnion().getAbbrevation()).append(Text.NON_BREAKING_SPACE).append("-").append(Text.NON_BREAKING_SPACE);
							}
							nameText.append(tournaments[i].getName());

							Link textLink = getSmallLink(nameText.toString());
							textLink.setPage(iTournamentPage);
							textLink.setEventListener(TournamentEventListener.class);
							textLink.addParameter(getTournamentSession(modinfo).getParameterNameTournamentID(), tournaments[i].getID());

							if (tournaments[i].getIsClosed()) {
								table.add(closedTournamentImage, column, row);
							}
							table.add(textLink, column++, row);
						}
						else {
							table.add(getSmallText(tournaments[i].getName()), column++, row);
						}

						Text name = getSmallText(tournaments[i].getTournamentType().getName());
						table.add(name, column++, row);

						Text rounds = getSmallText(Integer.toString(tournaments[i].getNumberOfRounds()));
						table.add(rounds, column++, row);

						if (tournaments[i].getIfOpenTournament()) {
							table.add(openImage, column, row);
						}
						else {
							table.add(closedImage, column, row);
						}

						Text theForm = getSmallText(tournaments[i].getTournamentForm().getName());
						table.add(theForm, column++, row);

						if (TournamentController.isOnlineRegistration(tournaments[i], rightNowStamp)) {
							Image registerImage = iwb.getImage("shared/tournament/register.gif");
							registerImage.setName(iwrb.getLocalizedString("tournament.register_me", "Register me"));
							registerImage.setToolTip(iwrb.getLocalizedString("tournament.register_me", "Register me"));
							Link register = new Link(registerImage);
							register.setWindowToOpen(TournamentRegistrationWindow.class);
							register.addParameter("action", "open");
							register.addParameter("tournament_id", Integer.toString(tournaments[i].getID()));
							table.add(register, column, row);
						}

						table.setRowPadding(row, getCellpadding());
						if (zebraRow % 2 != 0) {
							table.setRowColor(row++, getZebraColor1());
						}
						else {
							table.setRowColor(row++, getZebraColor2());
						}
						zebraRow++;
						table.mergeCells(1, row, table.getColumns(), row);
						table.setRowColor(row, getLineSeperatorColor());
						table.setHeight(row++, 1);
					}

					table.setColumnAlignment(1, "center");
					table.setColumnAlignment(5, "center");
					table.setColumnAlignment(7, "center");

					modinfo.setApplicationAttribute("tournament_table_union_id_" + union_id + "_view_" + view + "_locale_" + localeString + "_startTime_" + startStamp.toSQLDateString() + "_endTime_" + endStamp.toSQLDateString(), table);

				}
				else {
					outerTable.setCellpadding(1, 3, 24);
					outerTable.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_CENTER);
					outerTable.add(getHeader(iwrb.getLocalizedString("tournament.no_pending_tournaments", "No pending tournaments")), 1, 3);
				}
			}
		}
		else {
			outerTable.setCellpadding(1, 3, 24);
			outerTable.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_CENTER);
			outerTable.add(getHeader(iwrb.getLocalizedString("tournament.no_pending_tournaments", "No pending tournaments")), 1, 3);
		}

		outerTable.add(table, 1, 2);

		if (endStamp == null) {
			startStamp.addDays(daysToDisplay);
			endStamp = new IWTimestamp(startStamp.toSQLDateString());
			startStamp.addDays(-daysToDisplay);
		}

		outerTable.add(getFormTable(startStamp, endStamp, modinfo), 1, 1);

		return outerTable;

	}

	private Tournament[] getTournaments(IWContext modinfo, String union_id, String view) throws SQLException {

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
			System.err.println("/tournament/: finnst ekki golfvollur fyrir union ");
			tournaments = new Tournament[] {};
		}

		return tournaments;
	}

	public static void setAreResults(IWContext modinfo, boolean results) {
		modinfo.setSessionAttribute("tournament_index_are_results", new Boolean(results));
	}

	public static boolean getAreResults(IWContext modinfo) {
		Boolean returner = new Boolean(false);
		returner = (Boolean) modinfo.getSessionAttribute("tournament_index_are_results");
		if (returner == null) {
			returner = new Boolean(false);
		}

		return returner.booleanValue();
	}

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * @param tournamentPage
	 *          The tournamentPage to set.
	 */
	public void setTournamentPage(ICPage tournamentPage) {
		this.iTournamentPage = tournamentPage;
	}
}