package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentTour;
import is.idega.idegaweb.golf.entity.TournamentTourHome;
import is.idega.idegaweb.golf.entity.TournamentTournamentTour;
import is.idega.idegaweb.golf.entity.TournamentTournamentTourHome;
import is.idega.idegaweb.golf.entity.TournamentTournamentTourPK;
import is.idega.idegaweb.golf.templates.page.GolfWindow;
import is.idega.idegaweb.golf.tournament.business.TournamentBusiness;

import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

public class TournamentTourTournamentAdderWindow extends GolfWindow {

	private static final String ACTION = "tttaw_a";
	private static final String ACTION_SELECT_TOURNAMENT = "tttaw_ast";
	private static final String ACTION_SAVE = "tttaw_s";
	private static final String ACTION_CLOSE = "tttaw_ck";
	private static final String PARAMETER_TOURNAMENT_ID = "par_tid";
	private static final String PARAMETER_TOTAL_SCORE = "par_tsc";

	public TournamentTourTournamentAdderWindow() {
		super();
		setHeight(400);
		setWidth(500);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		addHeading(localize("tournament.tour_tournaments", "Tour Tournaments"));

		String id = iwc.getParameter(TournamentTourEditor.PARAMETER_TOUR_ID);
		if (id == null) {
			add(localize("tournament.no_tour_selected", "No tour selected"));
		} else {
			TournamentTour tour = getTournamentTourHome().findByPrimaryKey(new Integer(id));
			String action = iwc.getParameter(ACTION);
			if (action == null || action.equals("")) {
				addTournamentSelection(iwc, tour);
			} else if (action.equals(ACTION_SELECT_TOURNAMENT)) {
				// VELJA STIG
				if (iwc.isParameterSet(PARAMETER_TOURNAMENT_ID)) {
					addTournamentSettings(iwc, tour);
				} else {
					addTournamentSelection(iwc, tour);
				}
			} else if (action.equals(ACTION_SAVE)) {
				save(iwc, tour);
			} else if (action.equals(ACTION_CLOSE)) {
				String url = iwc.getIWMainApplication().getPublicObjectInstanciatorURI(TournamentTourEditorWindow.class)+"&"+TournamentTourEditor.ACTION+"="+TournamentTourEditor.ACTION_SELECT
		    	  + "&"+TournamentTourEditor.PARAMETER_TOUR_ID+"="+tour.getPrimaryKey().toString();
				this.setParentToReloadWithURL(url);
				this.close();
			}
		}
	}
	
	private void save(IWContext iwc, TournamentTour tour) throws FinderException {
		String totalPoint = iwc.getParameter(PARAMETER_TOTAL_SCORE);
		Tournament tournament = getTournamentHome().findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_TOURNAMENT_ID)));
		
		TournamentTournamentTourPK pk = new TournamentTournamentTourPK(tournament.getPrimaryKey(), tour.getPrimaryKey());
		try {
			TournamentTournamentTour ttTour = getTournamentTournamentTourHome().create(pk);
			ttTour.setTotalScore(Integer.parseInt(totalPoint));
			ttTour.store();
			
			Form form = new Form();
			form.maintainParameter(TournamentTourEditor.PARAMETER_TOUR_ID);
			Table table = getTableWithHeader(tour);
			form.add(table);
			
			table.mergeCells(1, 3, 2, 3);
			table.add(localize("travel.tournament_added", "Tournament added"), 1, 3);
			SubmitButton close = new SubmitButton(localize("close", "Close"), ACTION, ACTION_CLOSE);

			table.mergeCells(1, 4, 2, 4);
			table.setAlignment(1, 4, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(close, 1, 4);
			
			add(form);
			
		} catch (CreateException e) {
			add(localize("tournament.tournament_already_added", "Tournament aleady added"));
			addTournamentSelection(iwc, tour);
		}
	}

	private void addTournamentSettings(IWContext iwc, TournamentTour tour) throws FinderException {
		Form form = new Form();
		form.maintainParameter(TournamentTourEditor.PARAMETER_TOUR_ID);
		Table table = getTableWithHeader(tour);
		form.add(table);

		Tournament tournament = getTournamentHome().findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_TOURNAMENT_ID)));
		table.add(localize("tournament.tournament", "Tournament"), 1,3);
		table.add(tournament.getName(), 2, 3);
		table.add(new HiddenInput(PARAMETER_TOURNAMENT_ID, tournament.getPrimaryKey().toString()), 2, 3);
		TextInput inp = new TextInput(PARAMETER_TOTAL_SCORE);
		inp.setContent(Integer.toString(tour.getScoreSystem().getDefaultPoints()));
		table.add(localize("tournament.total_points", "Total points"), 1, 4);
		table.add(inp, 2, 4);
		
		BackButton back = new BackButton(localize("back", "Back"));
		SubmitButton save = new SubmitButton(localize("save", "Save"), ACTION, ACTION_SAVE);
		table.setAlignment(2, 5, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(back, 1, 5);
		table.add(save, 2, 5);
		add(form);
	}
	
	private void addTournamentSelection(IWContext iwc, TournamentTour tour) {
		Form form = new Form();
		form.maintainParameter(TournamentTourEditor.PARAMETER_TOUR_ID);
		Table table = getTableWithHeader(tour);
		table.mergeCells(1, 3, 2, 3);
		table.mergeCells(1, 4, 2, 4);

		try {
			DropdownMenu tournaments = null;
			int iYear = com.idega.util.IWTimestamp.RightNow().getYear();
			String sYear = iwc.getParameter("view_year");
			if (sYear != null) {
				iYear = Integer.parseInt(sYear);
			}
			if (isClubAdmin(iwc)) {
				Member member = AccessControl.getMember(iwc);
				int member_id = member.getID();
				Member golfMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(member_id);
				int main_union_id = golfMember.getMainUnionID();
				tournaments = getTournamentBusiness(iwc).getDropdownOrderedByUnion(new DropdownMenu(PARAMETER_TOURNAMENT_ID),iwc, iYear);

			}
			else if (isAdmin(iwc)) {
				tournaments = getTournamentBusiness(iwc).getDropdownOrderedByUnion(new DropdownMenu(PARAMETER_TOURNAMENT_ID),iwc, iYear);
			}
			tournaments.setMarkupAttribute("size","10");

			DropdownMenu year = new DropdownMenu("view_year");
			for (int i = 2001 ; i <= com.idega.util.IWTimestamp.RightNow().getYear(); i++) {
				year.addMenuElement(Integer.toString(i), Integer.toString(i));
			}
			year.setSelectedElement(Integer.toString(iYear));
			year.setToSubmit();
			
			table.add(localize("tournament.select_a_tournament", "Select a tournament"), 1, 3);
			table.add(tournaments, 1, 4);
			table.add(year, 1, 5);
			
			SubmitButton add = new SubmitButton(localize("tournament.add", "Add"), ACTION, ACTION_SELECT_TOURNAMENT);
			table.setAlignment(2, 5, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(add, 2, 5);
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		form.add(table);
		add(form);
	}

	private Table getTableWithHeader(TournamentTour tour) {
		Table table = new Table();
		table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
		table.add(localize("tournament.name", "Name")+ " : ", 1, 1);
		table.add(tour.getName(), 1, 1);
		table.add(localize("tournament.scoring_system", "Scoring System")+ " : ", 1, 2);
		table.add(tour.getScoreSystem().getName(), 1, 2);
		table.mergeCells(1, 1, 2, 1);
		table.mergeCells(1, 2, 2, 2);
		return table;
	}

	private boolean isAdmin(IWContext modinfo) {
		try {
			return AccessControl.isAdmin(modinfo);
		}
		catch(SQLException E) {
			return false;
		}
	}

	private boolean isClubAdmin(IWContext modinfo) {
		return AccessControl.isClubAdmin(modinfo);
	}


	private TournamentHome getTournamentHome() {
		try {
			return (TournamentHome) IDOLookup.getHome(Tournament.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	private TournamentTourHome getTournamentTourHome() {
		try {
			return (TournamentTourHome) IDOLookup.getHome(TournamentTour.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	private TournamentTournamentTourHome getTournamentTournamentTourHome() {
		try {
			return (TournamentTournamentTourHome) IDOLookup.getHome(TournamentTournamentTour.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public TournamentBusiness getTournamentBusiness(IWContext iwc) {
		try {
			return (TournamentBusiness) IBOLookup.getServiceInstance(iwc, TournamentBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

}
