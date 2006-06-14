package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentTour;
import is.idega.idegaweb.golf.entity.TournamentTourHome;
import is.idega.idegaweb.golf.entity.TournamentTourScoreSystem;
import is.idega.idegaweb.golf.entity.TournamentTourScoreSystemHome;
import is.idega.idegaweb.golf.entity.TournamentTournamentTour;
import is.idega.idegaweb.golf.entity.TournamentTournamentTourHome;
import is.idega.idegaweb.golf.entity.TournamentTournamentTourPK;
import is.idega.idegaweb.golf.entity.Union;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

public class TournamentTourEditor extends TournamentBlock {

	static final String PARAMETER_TOUR_ID = "tte_p_tid";
	private static final String PARAMETER_UNION_ID = "tte_p_uid";
	private static final String PARAMETER_NAME = "tte_p_name";
	private static final String PARAMETER_TOUR_SCORING_TYPE_ID = "tte_p_tsid";
	private static final String PARAMETER_TOURNAMENT_ID = "tte_p_tourid";
	static final String ACTION = "tte_p_ac";
	private static final String ACTION_NEW = "tte_p_ac_n";
	private static final String ACTION_SAVE = "tte_p_ac_sn";
	private static final String ACTION_EDIT = "tte_p_ac_ed";
	private static final String ACTION_DELETE = "tte_p_ac_ad";
	private static final String ACTION_REMOVE_TOURNAMENT = "tte_p_rmtm";
	static final String ACTION_SELECT = "tte_p_ac_as";

	protected boolean tournamentMustBeSet() {
		return false;
	}

	public void main(IWContext iwc) throws Exception {
		String action = iwc.getParameter(ACTION);

		if (isAdmin() || isClubAdmin()) {
			if (action == null) {
				addMainMenu(iwc);
			} else if (action.equals(ACTION_NEW)) {
				addCreationMenu(iwc, -1);
			} else if (action.equals(ACTION_EDIT)) {
				int id = -1;
				if (iwc.isParameterSet(PARAMETER_TOUR_ID)) {
					id = Integer.parseInt(iwc.getParameter(PARAMETER_TOUR_ID));
					addCreationMenu(iwc, id);
				} else {
					addMainMenu(iwc);
				}
			} else if (action.equals(ACTION_SAVE)) {
				saveNewTour(iwc);
				addMainMenu(iwc);
			} else if (action.equals(ACTION_DELETE)) {
				deleteTour(iwc);
				addMainMenu(iwc);
			} else if (action.equals(ACTION_SELECT)) {
				if (iwc.isParameterSet(PARAMETER_TOUR_ID)) {
					addTournamentTourInfo(iwc);
				} else {
					addMainMenu(iwc);
				}
			} else if (action.equals(ACTION_REMOVE_TOURNAMENT)) {
				removeTournament(iwc);
				addTournamentTourInfo(iwc);
			}
		} else {
			add(localize("no_permission", "No permission"));
		}
	}

	private void removeTournament(IWContext iwc) {
		String id = iwc.getParameter(PARAMETER_TOUR_ID);
		String tid = iwc.getParameter(PARAMETER_TOURNAMENT_ID);

		try {
			TournamentTournamentTourPK pk = new TournamentTournamentTourPK(new Integer(tid), new Integer(id));
			TournamentTournamentTour ttTour = getTournamentTournamentTourHome().findByPrimaryKey(pk);
			ttTour.remove();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	private void addTournamentTourInfo(IWContext iwc) throws FinderException {
		String id = iwc.getParameter(PARAMETER_TOUR_ID);
		TournamentTour tour = getTournamentTourHome().findByPrimaryKey(id);

		Table outerTable = new Table();

		Table headerTable = new Table();
		headerTable.setWidth("100%");
		outerTable.add(headerTable, 1, 1);
		headerTable.add(localize("tournament.name", "Name")+ " : ", 1, 1);
		headerTable.add(tour.getName(), 1, 1);
		headerTable.add(localize("tournament.scoring_system", "Scoring System")+ " : ", 1, 2);
		headerTable.add(tour.getScoreSystem().getName(), 1, 2);

		Table tournamentList = new Table();
		tournamentList.setWidth("100%");
		outerTable.add(tournamentList, 1, 2);
		int row = 1;
		Locale locale = iwc.getCurrentLocale();
		Collection tournamentIDS = getTournamentTourHome().getTournamentIDs(tour);
		if (tournamentIDS != null) {
			Iterator iter = tournamentIDS.iterator();
			Tournament tournament;
			IWTimestamp stamp;
			while (iter.hasNext()) {
				tournament = getTournamentHome().findByPrimaryKey(iter.next());
				tournamentList.add(tournament.getName(), 1, row);
				stamp = new IWTimestamp(tournament.getStartTime());
				tournamentList.add(stamp.getLocaleDate(locale), 2, row);
				tournamentList.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
				try {
					TournamentTournamentTourPK pk = new TournamentTournamentTourPK(tournament.getPrimaryKey(), tour.getPrimaryKey());
					TournamentTournamentTour ttTour = getTournamentTournamentTourHome().findByPrimaryKey(pk);
					tournamentList.add(Integer.toString(ttTour.getTotalScore()), 3, row);
				} catch (FinderException f) {
					f.printStackTrace();
				}
				Link rem = new Link(localize("remove", "Remove"));
				rem.addParameter(ACTION, ACTION_REMOVE_TOURNAMENT);
				rem.addParameter(PARAMETER_TOUR_ID, id);
				rem.addParameter(PARAMETER_TOURNAMENT_ID, tournament.getPrimaryKey().toString());
				tournamentList.add(rem, 4, row);
				row++;
			}
		}

		GenericButton button = new GenericButton(localize("tournament.add_tournament", "Add tournament"));
		button.setWindowToOpen(TournamentTourTournamentAdderWindow.class);
		button.addParameter(PARAMETER_TOUR_ID, id);
		outerTable.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);
		outerTable.add(button, 1, 3);
		add(outerTable);

	}

	private void addMainMenu(IWContext iwc) throws Exception {
		Union union = ((Member) AccessControl.getMember(iwc)).getMainUnion();
		int unionID = 1;
		if (union != null) {
			unionID = union.getID();
		}
		Collection tours = getTournamentTourHome().findAllByUnionID(unionID);
		DropdownMenu dTours = new DropdownMenu(PARAMETER_TOUR_ID);
		dTours.setMarkupAttribute("size","10");
		dTours.setStyleAttribute("width:300px");
		if (tours != null) {
			Iterator iter = tours.iterator();
			String prefix = null;
			TournamentTour tour;
			if (super.isClubAdmin() && union != null) {
				prefix = union.getAbbrevation()+" ";
			}
			while (iter.hasNext()) {
				tour = (TournamentTour) iter.next();
				if (prefix != null) {
					dTours.addMenuElement(tour.getPrimaryKey().toString(), prefix + tour.getName());
				} else {
					dTours.addMenuElement(tour.getPrimaryKey().toString(), tour.getName());
				}
			}
		}

		Form form = new Form();
		form.setMethod("POST");
		Table table = new Table();
		form.add(table);
		table.add(getResourceBundle().getLocalizedString("tournament.select_a_tour","Select a tour"), 1, 1);
		table.mergeCells(1, 1, 2, 1);
		table.add(dTours, 1, 2);
		table.mergeCells(1, 2, 2, 2);

		SubmitButton create = new SubmitButton(localize("tournament.create", "Create"), ACTION, ACTION_NEW);
		SubmitButton edit = new SubmitButton(localize("tournament.edit", "Edit"), ACTION, ACTION_EDIT);
		SubmitButton del = new SubmitButton(localize("tournament.delete", "Delete"), ACTION, ACTION_DELETE);
		SubmitButton select = new SubmitButton(localize("tournament.select", "Select"), ACTION, ACTION_SELECT);

		table.add(create, 1, 3);
		table.add(edit, 1, 3);
		table.add(del, 2, 3);
		table.add(select, 2, 3);
		table.setAlignment(2, 3, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void deleteTour(IWContext iwc) {
		String id = iwc.getParameter(PARAMETER_TOUR_ID);
		if (id != null) {
			try {
				TournamentTour tour = getTournamentTourHome().findByPrimaryKey(new Integer(id));
				tour.remove();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (RemoveException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveNewTour(IWContext iwc) {
		String name = iwc.getParameter(PARAMETER_NAME);
		String union = iwc.getParameter(PARAMETER_UNION_ID);
		String score = iwc.getParameter(PARAMETER_TOUR_SCORING_TYPE_ID);
		String id = iwc.getParameter(PARAMETER_TOUR_ID);
		if (name != null && !name.trim().equals("")) {
			try {
				TournamentTour tour = null;
				if (id != null) {
					tour = getTournamentTourHome().findByPrimaryKey(new Integer(id));
				} else {
					tour = getTournamentTourHome().create();
				}
				tour.setName(name);
				tour.setUnionId(Integer.parseInt(union));
				tour.setScoreSystem(Integer.parseInt(score));
				tour.store();
			} catch (CreateException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

	}

	private void addCreationMenu(IWContext modinfo, int id) {
		Form form = new Form();
		Table table = new Table();
		form.add(table);

		TournamentTour tour = null;
		if (id > 0) {
			try {
				tour = getTournamentTourHome().findByPrimaryKey(new Integer(id));
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}


		int row = 1;
		TextInput nameI = new TextInput(PARAMETER_NAME);
		table.add(localize("tournament.name", "Name"), 1, row);
		table.add(nameI, 2, row++);

		table.add(localize("tournament.union", "Union"), 1, row);
		DropdownMenu un = getUnionDropdown(modinfo);
		table.add(un, 2, row++);
		DropdownMenu menu = null;
		try {
			Collection system = getTournamentTourScoreSystemHome().findAll();
			menu = new DropdownMenu(system, PARAMETER_TOUR_SCORING_TYPE_ID);
			table.add(localize("tournament.scoring", "Scoring"), 1, row);
			table.add(menu, 2, row++);
		} catch (FinderException e) {
			e.printStackTrace();
		}

		if (tour != null) {
			nameI.setContent(tour.getName());
			un.setSelectedElement(tour.getUnionID());
			if (menu != null) {
				menu.setSelectedElement(tour.getScoreSystem().getPrimaryKey().toString());
			}
			table.add(new HiddenInput(PARAMETER_TOUR_ID, Integer.toString(id)), 2, row);
		}

		SubmitButton sb = new SubmitButton(localize("tournament.save", "Save"), ACTION, ACTION_SAVE);
		table.add(sb, 2, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		add(form);
	}

	private DropdownMenu getUnionDropdown(IWContext modinfo) {
		Union union = null;
		DropdownMenu unions = new DropdownMenu(PARAMETER_UNION_ID);
		if (AccessControl.isClubAdmin(modinfo)) {
			try {
				union = ((Member) AccessControl.getMember(modinfo)).getMainUnion();
			}
			catch (FinderException fe) {
			} catch (SQLException e) {
			}
			unions.addMenuElement(union.getID(),union.getName());

		}
		else {
			Union[] theUnion;
			try {
				theUnion = (Union[])((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("ABBREVATION");
				unions = new DropdownMenu(theUnion, PARAMETER_UNION_ID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return unions;
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

	private TournamentTourScoreSystemHome getTournamentTourScoreSystemHome() {
		try {
			return (TournamentTourScoreSystemHome) IDOLookup.getHome(TournamentTourScoreSystem.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

}
