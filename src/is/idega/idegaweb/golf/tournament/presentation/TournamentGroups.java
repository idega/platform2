/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.moduleobject.GolfDialog;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author gimmi
 */
public class TournamentGroups extends GolfBlock {

	private GolfDialog dialog;
	private static String ACTION_PARAMETER = "thisAction";
	private static String PARAMETER_CLOSE = "close";
	private static String PARAMETER_CREATE_GROUP = "createGroup";
	private static String PARAMETER_VIEW_GROUP = "viewGroup";
	private static String PARAMETER_DELETE_GROUP = "deleteGroup";

	public void add(PresentationObject modObj) {
		dialog.add(modObj);
	}

	public void main(IWContext modinfo) throws Exception {
		dialog = new GolfDialog("");

		String action = modinfo.getParameter(ACTION_PARAMETER);
		if (action == null) {
			Form form = new Form();
			GenericButton sbCreate = getButton(new SubmitButton(localize("new_tournament_group","New Tournament Group"), ACTION_PARAMETER, PARAMETER_CREATE_GROUP));
			GenericButton sbView = getButton(new SubmitButton(localize("view_tournament_group","View Tournament Group"), ACTION_PARAMETER, PARAMETER_VIEW_GROUP));
			GenericButton sbDelete = getButton(new SubmitButton(localize("delete_tournament_group","Delete Tournament Group"), ACTION_PARAMETER, PARAMETER_DELETE_GROUP));
			GenericButton close = getButton(new SubmitButton(localize("close","Close"), ACTION_PARAMETER, PARAMETER_CLOSE));

			form.add(sbCreate);
			form.add(Text.BREAK);
			form.add(sbView);
			form.add(Text.BREAK);
			form.add(sbDelete);
			form.add(Text.BREAK);
			form.add(close);
			dialog.add(form);

		}
		else if (action.equals(PARAMETER_CREATE_GROUP)) {
			fromCreateTournamentGroup(modinfo, getResourceBundle());
		}
		else if (action.equals(PARAMETER_VIEW_GROUP)) {
			fromViewTournamentGroup(modinfo, getResourceBundle());
		}
		else if (action.equals(PARAMETER_DELETE_GROUP)) {
			fromDeleteTournamentGroup(modinfo, getResourceBundle());
		}

		else if (action.equalsIgnoreCase("close")) {
			getParentPage().setToReload();
			getParentPage().close();
		}

		super.add(dialog);
	}

	public void fromCreateTournamentGroup(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		if ((isAdmin()) || (isClubAdmin())) {

			String action = modinfo.getParameter("tournament_group_action");
			if (action == null) {
				createTournamentGroup(modinfo, iwrb);
			}
			else if (action.equals("submitted")) {
				saveTournamentGroup(modinfo, iwrb);
			}

		}
		else {
			add(iwrb.getLocalizedString("tournament.access_denied", "Access denied"));
		}

	}

	private void createTournamentGroup(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		Form form = new Form();
		form.maintainParameter(ACTION_PARAMETER);

		TextInput name = new TextInput("name", "");
		TextInput description = new TextInput("description", "");
		TextInput maxAge = new TextInput("max_age", "100");
		maxAge.setSize(3);
		TextInput minAge = new TextInput("min_age", "0");
		minAge.setSize(3);
		TextInput maxHandicap = new TextInput("max_handicap", "50");
		maxHandicap.setSize(3);
		TextInput minHandicap = new TextInput("min_handicap", "-10");
		minHandicap.setSize(3);
		DropdownMenu teeColor = new DropdownMenu(EntityFinder.findAllOrdered((TeeColor) IDOLookup.instanciateEntity(TeeColor.class), "tee_color_name"));
		DropdownMenu sex = new DropdownMenu("sex");
		sex.addMenuElement("M", iwrb.getLocalizedString("tournament.males", "Male"));
		sex.addMenuElement("F", iwrb.getLocalizedString("tournament.females", "Female"));
		sex.addMenuElement("B", iwrb.getLocalizedString("tournament.both", "Both"));

		int row = 1;
		Table table = new Table();
		form.add(table);
		table.setBorder(0);
		table.setCellpadding(2);

		table.add("<b>" + iwrb.getLocalizedString("tournament.new_tournament_group", "New tournament group") + "</b>", 1, 1);
		++row;

		table.add(iwrb.getLocalizedString("tournament.name", "Name"), 1, row);
		table.add(name, 2, row);
		++row;

		table.add(iwrb.getLocalizedString("tournament.description", "Description"), 1, row);
		table.add(description, 2, row);
		++row;

		table.add(iwrb.getLocalizedString("tournament.tee", "Tee color"), 1, row);
		table.add(teeColor, 2, row);
		++row;

		table.add(iwrb.getLocalizedString("tournament.maximum_age", "Maximum age"), 1, row);
		table.add(maxAge, 2, row);
		++row;

		table.add(iwrb.getLocalizedString("tournament.minimum_age", "Minimum age"), 1, row);
		table.add(minAge, 2, row);
		++row;

		table.add(iwrb.getLocalizedString("tournament.maximum_handicap", "Maximum handicap"), 1, row);
		table.add(maxHandicap, 2, row);
		++row;

		table.add(iwrb.getLocalizedString("tournament.minimum_handicap", "Minumum handicap"), 1, row);
		table.add(minHandicap, 2, row);
		++row;

		table.add(iwrb.getLocalizedString("tournament.sex", "Sex"), 1, row);
		table.add(sex, 2, row);
		++row;

		if (isClubAdmin()) {
			Member member = (Member) getMember();
			table.add(new HiddenInput("union_id", "" + member.getMainUnionID()));
		}

		table.setVerticalAlignment(1, row, "top");
		table.add(TournamentController.getBackLink(modinfo), 1, row);
		table.setAlignment(2, row, "right");
		table.add(TournamentController.getAheadButton(modinfo, "tournament_group_action", "submitted"), 2, row);
		//table.add(new HiddenInput("tournament_group_action","submitted"));

		add(form);
	}

	private void saveTournamentGroup(IWContext modinfo, IWResourceBundle iwrb) {
		String name = modinfo.getParameter("name");
		String description = modinfo.getParameter("description");
		String maxAge = modinfo.getParameter("max_age");
		String minAge = modinfo.getParameter("min_age");
		String maxHandicap = modinfo.getParameter("max_handicap");
		String minHandicap = modinfo.getParameter("min_handicap");
		String teeColorId = modinfo.getParameter("tee_color");
		String sex = modinfo.getParameter("sex");
		String union_id = modinfo.getParameter("union_id");

		try {
			TournamentGroup tGroup = (TournamentGroup) IDOLookup.createLegacy(TournamentGroup.class);
			tGroup.setDescription(description);
			tGroup.setGender(sex);
			tGroup.setMaxAge(Integer.parseInt(maxAge));
			tGroup.setMinAge(Integer.parseInt(minAge));
			tGroup.setMaxHandicap(Float.parseFloat(maxHandicap));
			tGroup.setMinHandicap(Float.parseFloat(minHandicap));
			tGroup.setName(name);
			tGroup.setTeeColor(Integer.parseInt(teeColorId));
			if (union_id != null) {
				tGroup.setUnionID(Integer.parseInt(union_id));
			}
			else {
				tGroup.setUnionID(3);
			}
			tGroup.insert();

			add(iwrb.getLocalizedString("tournament.tournament_group_created", "Tournament group created"));
		}
		catch (Exception e) {
			add(iwrb.getLocalizedString("touranment.tournament_group_not_created", "Tournament group not created"));
		}

		add("<br>");
		add("<br>");
		add(TournamentController.getBackLink(modinfo));

	}

	public Form getBackButtonForm(IWResourceBundle iwrb) {
		Form form = new Form();
		form.add(new BackButton());
		return form;
	}

	public void fromViewTournamentGroup(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		if ((isAdmin()) || (isClubAdmin())) {

			String action = modinfo.getParameter("view_action");
			if (action == null)
				action = "";

			if (action.equals("group_chosen")) {
				viewGroup(modinfo, iwrb);
			}
			else if (action.equals("update")) {
				update(modinfo, iwrb);
			}
			else {
				chooseTournamentGroup(modinfo, iwrb);
			}

		}
		else {
			add(iwrb.getLocalizedString("tournament.access_denied", "Access denied"));
			add(TournamentController.getBackLink(modinfo));
		}
	}

	private void chooseTournamentGroup(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		Form form = new Form();
		form.maintainParameter(ACTION_PARAMETER);
		Table table = new Table(2, 3);
		table.mergeCells(1, 1, 2, 1);
		table.mergeCells(1, 2, 2, 2);
		table.setVerticalAlignment(1, 3, "top");
		table.setVerticalAlignment(2, 3, "top");
		table.setAlignment(1, 3, "left");
		table.setAlignment(2, 3, "right");
		table.setBorder(0);

		form.add(table);

		table.add(iwrb.getLocalizedString("tournament.choose_tournament", "Choose tournament"), 1, 1);
		DropdownMenu menu = null;
		if (isClubAdmin()) {
			Member member = (Member) getMember();
			try {
				menu = new DropdownMenu(TournamentController.getUnionTournamentGroups(member.getMainUnion()));
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}
		}
		else {
			menu = new DropdownMenu(EntityFinder.findAllOrdered((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class), "name"));
		}

		menu.setMarkupAttribute("size", "7");
		table.add(menu, 1, 2);
		table.add(TournamentController.getBackLink(modinfo), 1, 3);
		table.add(TournamentController.getAheadButton(modinfo, "view_action", "group_chosen"), 2, 3);
		//table.add(new HiddenInput("view_action","group_chosen"),2,3);

		add(form);
	}

	private void viewGroup(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		String tournament_group_id = modinfo.getParameter("tournament_group");
		if (tournament_group_id != null) {
			Form form = new Form();
			Table table = new Table();
			form.maintainParameter(ACTION_PARAMETER);
			form.add(table);
			table.setBorder(0);
			table.setCellpadding(2);

			TournamentGroup tGroup = null;
			try {
				tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_id));
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}

			Union union = tGroup.getUnion();
			TextInput name = new TextInput("name", tGroup.getName());

			String desc = tGroup.getDescription();
			if (desc == null)
				desc = "";

			TextInput description = new TextInput("description", desc);
			TextInput maxAge = new TextInput("max_age", "" + tGroup.getMaxAge());
			maxAge.setSize(3);
			TextInput minAge = new TextInput("min_age", "" + tGroup.getMinAge());
			minAge.setSize(3);
			TextInput maxHandicap = new TextInput("max_handicap", "" + tGroup.getMaxHandicap());
			maxHandicap.setSize(3);
			TextInput minHandicap = new TextInput("min_handicap", "" + tGroup.getMinHandicap());
			minHandicap.setSize(3);

			int teeColorId = tGroup.getTeeColorID();
			DropdownMenu teeColor = new DropdownMenu(EntityFinder.findAllOrdered((TeeColor) IDOLookup.instanciateEntity(TeeColor.class), "tee_color_name"));
			teeColor.setSelectedElement(Integer.toString(teeColorId));

			String sexString = tGroup.getGenderString();
			DropdownMenu sex = new DropdownMenu("sex");
			sex.addMenuElement("M", iwrb.getLocalizedString("tournament.males", "Male"));
			sex.addMenuElement("F", iwrb.getLocalizedString("tournament.females", "Female"));
			sex.addMenuElement("B", iwrb.getLocalizedString("tournament.both", "Both"));
			if (sexString != null) {
				sex.setSelectedElement(sexString);
			}

			int row = 1;

			table.add("<b>" + tGroup.getName() + "</b>", 1, row);
			table.add(new HiddenInput("tournament_group_id", "" + tGroup.getID()), 1, row);
			table.mergeCells(1, row, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.name", "Name"), 1, row);
			table.add(name, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.description", "Description"), 1, row);
			table.add(description, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.tee", "Tee color"), 1, row);
			table.add(teeColor, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.belongs_to", "Belongs to"), 1, row);
			table.add(union.getName(), 2, row);
			table.add(new HiddenInput("union_id", "" + union.getID()), 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.maximum_age", "Maximum age"), 1, row);
			table.add(maxAge, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.minimum_age", "Minimum age"), 1, row);
			table.add(minAge, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.maximum_handicap", "Maximum handicap"), 1, row);
			table.add(maxHandicap, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.minimum_handicap", "Minumum handicap"), 1, row);
			table.add(minHandicap, 2, row);
			++row;

			table.add(iwrb.getLocalizedString("tournament.sex", "Sex"), 1, row);
			table.add(sex, 2, row);
			++row;

			table.add(TournamentController.getBackLink(modinfo), 1, row);
			table.setAlignment(2, row, "right");
			table.setVerticalAlignment(1, row, "top");
			table.setVerticalAlignment(2, row, "top");

			if (isClubAdmin()) {
				Member member = (is.idega.idegaweb.golf.entity.Member) getMember();
				int tempUnionId = member.getMainUnionID();
				if (tempUnionId == union.getID()) {
					table.add(new SubmitButton(new Image("/pics/formtakks/uppfaera.gif", ""), "view_action", "update"), 2, row);
					//table.add(new HiddenInput("view_action","update"),2,row);
				}
			}
			else if (isAdmin()) {
				if (tGroup.getUnionID() == 3) {
					table.add(getButton(new SubmitButton(localize("trounaemnt.update","Update"), "view_action", "update")), 2, row);
					//table.add(new HiddenInput("view_action","update"),2,row);
				}
			}

			add(form);
		}
		else {
			add("Enginn mótshópur valinn<br><br>");
			add(TournamentController.getBackLink(modinfo));
		}

	}

	public void update(IWContext modinfo, IWResourceBundle iwrb) {
		String name = modinfo.getParameter("name");
		String description = modinfo.getParameter("description");
		String maxAge = modinfo.getParameter("max_age");
		String minAge = modinfo.getParameter("min_age");
		String maxHandicap = modinfo.getParameter("max_handicap");
		String minHandicap = modinfo.getParameter("min_handicap");
		String teeColorId = modinfo.getParameter("tee_color");
		String sex = modinfo.getParameter("sex");
		String union_id = modinfo.getParameter("union_id");
		String tournament_group_id = modinfo.getParameter("tournament_group_id");

		try {
			maxHandicap = com.idega.util.text.TextSoap.findAndReplace(maxHandicap, ',', '.');
			minHandicap = com.idega.util.text.TextSoap.findAndReplace(minHandicap, ',', '.');

			TournamentGroup tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_id));
			tGroup.setDescription(description);
			tGroup.setGender(sex);
			tGroup.setMaxAge(Integer.parseInt(maxAge));
			tGroup.setMinAge(Integer.parseInt(minAge));
			tGroup.setMaxHandicap(Float.parseFloat(maxHandicap));
			tGroup.setMinHandicap(Float.parseFloat(minHandicap));
			tGroup.setName(name);
			tGroup.setTeeColor(Integer.parseInt(teeColorId));
			if (union_id != null) {
				tGroup.setUnionID(Integer.parseInt(union_id));
			}
			else {
				tGroup.setUnionID(3);
			}
			tGroup.update();

			add(iwrb.getLocalizedString("tournament.tournament_group_updated", "Tournament group updated"));
		}
		catch (Exception e) {
			add("Villa !!<br>");
			add(iwrb.getLocalizedString("tournament.tournament_group_not_updated", "Tournament group not updated"));
		}

		add("<br>");
		add("<br>");
		add(TournamentController.getBackLink(modinfo));

	}

	public String getModuleControlParameter() {
		return "tournament_group_delete_action";
	}

	public void fromDeleteTournamentGroup(IWContext modinfo, IWResourceBundle iwrb) throws Exception {

		if ((isAdmin()) || (isClubAdmin())) {

			String action = modinfo.getParameter(getModuleControlParameter());
			if (action == null) {
				action = "";
			}

			if (action.equalsIgnoreCase("confirm")) {
				confirm(modinfo, iwrb);
			}
			else if (action.equalsIgnoreCase("delete")) {
				delete(modinfo, iwrb);
			}
			else {
				start(modinfo, iwrb);
			}
		}
		else {
			add(iwrb.getLocalizedString("tournament.access_denied", "Access denied"));
		}

	}

	public void start(IWContext modinfo, IWResourceBundle iwrb) throws Exception {

		Form form = new Form();
		form.maintainParameter(ACTION_PARAMETER);
		dialog.add(form);

		TournamentGroup[] groups;

		if (isClubAdmin()) {

			Member member = (Member) AccessControl.getMember(modinfo);
			int union_id = member.getMainUnionID();
			groups = (TournamentGroup[]) ((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class)).findAllByColumnOrdered("union_id", Integer.toString(union_id), "name");
		}
		else {
			groups = getTournamentGroups(modinfo);
		}

		if (groups.length == 0) {

			add(iwrb.getLocalizedString("tournament.no_tournament_groups", "No tournament groups"));
		}
		else {

			DropdownMenu drop = new DropdownMenu(groups);
			form.add(drop);
		}

		form.add(getButton(new SubmitButton(localize("trounament.delete","Delete"), getModuleControlParameter(), "confirm")));
		//form.add(new HiddenInput(getModuleControlParameter(),"confirm"));

	}

	public void confirm(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		//GolfDialog dialog = new
		// GolfDialog(iwrb.getLocalizedString("tournament.warning","Warning"));
		//add(dialog);
		dialog.addMessage(iwrb.getLocalizedString("tournament.are_you_sure_you_want_to_delete_the_tournament_group", "Are you sure you want to delete the tournament group") + " ");
		Form form = new Form();
		dialog.add(form);
		form.maintainParameter("tournament_group");
		form.maintainParameter(ACTION_PARAMETER);

		form.add(new SubmitButton(iwrb.getLocalizedString("yes", "Yes"), getModuleControlParameter(), "delete"));
		form.add(new SubmitButton(iwrb.getLocalizedString("no", "No"), getModuleControlParameter(), "start"));
	}

	public void delete(IWContext modinfo, IWResourceBundle iwrb) throws Exception {
		//GolfDialog dialog = new
		// GolfDialog(iwrb.getLocalizedString("tournament.confirmation","Confirmation"));
		//add(dialog);
		List tournaments = com.idega.data.EntityFinder.findRelated(getGroup(modinfo), (Tournament) IDOLookup.instanciateEntity(Tournament.class));
		if (tournaments == null) {
			try {
				TournamentGroup group = getGroup(modinfo);
				group.delete();

				dialog.addMessage(iwrb.getLocalizedString("tournament.the_group", "The group") + " " + group.getName() + " " + iwrb.getLocalizedString("tournament.was_deleted", "was deleted"));
				dialog.add(TournamentController.getBackLink(modinfo));

			}
			catch (Exception ex) {
				ex.printStackTrace(System.err);
				dialog.addMessage(iwrb.getLocalizedString("tournament.delete_tournament_group_error", "Error. There are probable members registered in this tournament group"));
			}
		}
		else {
			dialog.addMessage(iwrb.getLocalizedString("tournament.delete_tournament_group_error", "Error. There are probable members registered in this tournament group"));
		}
	}

	public TournamentGroup[] getTournamentGroups(IWContext modinfo) throws Exception {
		return (TournamentGroup[]) ((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class)).findAllOrdered("name");
	}

	public TournamentGroup getGroup(IWContext modinfo) throws Exception {
		return ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(modinfo.getParameter("tournament_group")));
	}

	protected boolean tournamentMustBeSet() {
		return false;
	}

}