/*
 * Created on 13.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.provider.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.User;
import com.idega.user.presentation.UserChooser;

/**
 * @author laddi
 */
public class SchoolGroupEditor extends ProviderBlock {

	private final String PARAMETER_ACTION = "sge_action";
	private final String PARAMETER_GROUP_ID = "sge_group_id";
	private final String PARAMETER_GROUP_NAME ="sge_group_name";
	private final String PARAMETER_SCHOOL_YEARS ="sge_school_years";
	private final String PARAMETER_TEACHERS ="sge_teachers";
	private final String PARAMETER_SEASON_ID ="sge_season_id";
	private final String PARAMETER_TYPE_ID ="sge_type_id";
	private final String PARAMETER_IS_SUBGROUP = "sge_is_subgroup";
	
	private final int ACTION_VIEW = 1;
	private final int ACTION_EDIT = 2;
	private final int ACTION_DELETE = 3;
	private final int ACTION_SAVE = 4;
	
	private int _action = ACTION_VIEW;
	private int _groupID = -1;
	private SchoolClass _group;
	private School _provider;

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#_main(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		try {
			_provider = getSession().getProvider();
			parseAction(iwc);
			
			switch (_action) {
				case ACTION_VIEW :
					add(getOverview());
					break;
				case ACTION_EDIT :
					add(getEditForm());
					break;
				case ACTION_DELETE :
					deleteGroup();
					add(getOverview());
					break;
				case ACTION_SAVE :
					saveGroup(iwc);
					add(getOverview());
					break;
			}
		}
		catch (FinderException e) {
			add(getSmallErrorText(localize("school.no_provider_found", "No provider found for this user...")));
			add(new Break(2));
			add(new UserHomeLink());
		}
	}
	
	private Table getOverview() throws RemoteException {
		Table table = new Table(1, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 12);
		table.setHeight(4, 12);
		
		table.add(getNavigationForm(), 1, 1);
		table.add(getGroupTable(), 1, 3);
		
		GenericButton button = getButton(new GenericButton("edit", localize("new_group", "New group")));
		button.setPageToOpen(getParentPageID());
		button.addParameterToPage(PARAMETER_ACTION, ACTION_EDIT);
		table.add(button, 1, 5);
		
		return table;
	}
	
	private Table getGroupTable() {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setWidth(getWidth());
		table.setColumns(7);
		table.setWidth(6, 12);
		table.setWidth(7, 12);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		
		table.add(getLocalizedSmallHeader("group_name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("group_type","Type"), column++, row);
		table.add(getLocalizedSmallHeader("school_season","Season"), column++, row);
		table.add(getLocalizedSmallHeader("school_years","Years"), column++, row);
		table.add(getLocalizedSmallHeader("teachers","Teachers"), column++, row++);
		
		Iterator iter = getSchoolGroups().iterator();
		while (iter.hasNext()) {
			column = 1;
			SchoolClass group = (SchoolClass) iter.next();
			
			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());

			table.add(getSmallText(group.getSchoolClassName()), column++, row);
			if (group.getSchoolTypeId() != -1)
				table.add(getSmallText(group.getSchoolType().getSchoolTypeName()), column++, row);
			else
				table.add(getSmallText("-"), column++, row);
			if (group.getSchoolSeasonId() != -1)
				table.add(getSmallText(group.getSchoolSeason().getSchoolSeasonName()), column++, row);
			else
				table.add(getSmallText("-"), column++, row);
			
			Collection years = null;
			try {
				years = new HashSet(group.findRelatedSchoolYears());
			}
			catch (IDORelationshipException e) {
				years = new HashSet();
			}
			if (group.getSchoolYearId() != -1) {
				years.add(group.getSchoolYear());
			}
			
			if (!years.isEmpty()) {
				Iterator iterator = years.iterator();
				StringBuffer buffer = new StringBuffer();
				while (iterator.hasNext()) {
					SchoolYear year = (SchoolYear) iterator.next();
					buffer.append(year.getSchoolYearName());
					if (iterator.hasNext())
						buffer.append(",").append(Text.NON_BREAKING_SPACE);
				}
				table.add(getSmallText(buffer.toString()), column++, row);
			}
			else {
				table.add(getSmallText("-"), column++, row);
			}

			Collection teachers = null;
			try {
				teachers = new HashSet(group.findRelatedUsers());
			}
			catch (IDORelationshipException e) {
				teachers = new HashSet();
			}
			if (group.getTeacherId() != -1) {
				teachers.add(group.getTeacher());
			}

			if (!teachers.isEmpty()) {
				Iterator iterator = teachers.iterator();
				StringBuffer buffer = new StringBuffer();
				while (iterator.hasNext()) {
					User teacher = (User) iterator.next();
					buffer.append(teacher.getLastName());
					if (iterator.hasNext())
						buffer.append(",").append(Text.NON_BREAKING_SPACE);
				}
				table.add(getSmallText(buffer.toString()), column++, row);
			}
			else {
				table.add(getSmallText("-"), column++, row);
			}
			
			Link editLink = new Link(this.getEditIcon(localize("edit_group", "Edit group")));
			editLink.addParameter(PARAMETER_ACTION, ACTION_EDIT);
			editLink.addParameter(PARAMETER_GROUP_ID, group.getPrimaryKey().toString());
			table.add(editLink, column++, row);

			Link deleteLink = new Link(this.getDeleteIcon(localize("delete_group", "Delete group")));
			deleteLink.addParameter(PARAMETER_ACTION, ACTION_DELETE);
			deleteLink.addParameter(PARAMETER_GROUP_ID, group.getPrimaryKey().toString());
			table.add(deleteLink, column++, row++);
		}
		
		return table;
	}
	
	private Form getEditForm() {
		Form form = new Form();
		form.addParameter(PARAMETER_GROUP_ID, _groupID);
		form.addParameter(PARAMETER_ACTION, -1);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setColumns(3);
		table.setWidth(2, 10);
		table.setWidth(3, Table.HUNDRED_PERCENT);
		form.add(table);
		
		int row = 1;
		SelectorUtility util = new SelectorUtility();
		
		table.add(getSmallHeader(localize("group_name", "Name") + ":"), 1, row);
		table.setNoWrap(1, row);
		TextInput name = (TextInput) getStyledInterface(new TextInput(PARAMETER_GROUP_NAME));
		if (_group != null && _group.getSchoolClassName() != null)
			name.setContent(_group.getSchoolClassName());
		table.add(name, 3, row);
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("group_type", "Type") + ":"), 1, row);
		table.setNoWrap(1, row);
		Collection providerTypes = null;
		try {
			providerTypes = _provider.findRelatedSchoolTypes();
		}
		catch (IDORelationshipException e) {
			providerTypes = new ArrayList();
		}
		DropdownMenu types = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_TYPE_ID), providerTypes, "getSchoolTypeName"));
		types.addMenuElementFirst("-1", "");
		if (_group != null && _group.getSchoolTypeId() != -1)
			types.setSelectedElement(_group.getSchoolTypeId());
		table.add(types, 3, row);
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("school_season", "Season") + ":"), 1, row);
		table.setNoWrap(1, row);
		Collection providerSeasons = null;
		try {
			providerSeasons = getSchoolBusiness().findAllSchoolSeasons();
		}
		catch (RemoteException e) {
			providerSeasons = new ArrayList();
		}
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SEASON_ID), providerSeasons, "getSchoolSeasonName"));
		seasons.addMenuElementFirst("-1", "");
		if (_group != null && _group.getSchoolSeasonId() != -1)
			seasons.setSelectedElement(_group.getSchoolSeasonId());
		table.add(seasons, 3, row++);
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("group_type", "Group type") + ":"), 1, row);
		table.setNoWrap(1, row);
		DropdownMenu subGroup = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_IS_SUBGROUP));
		subGroup.addMenuElement("false", localize("main_group", "Main group"));
		subGroup.addMenuElement("true", localize("sub_group", "Sub group"));
		if (_group != null)
			subGroup.setSelectedElement(String.valueOf(_group.getIsSubGroup()));
		table.add(subGroup, 3, row++);
		
		Collection schoolYears = null;
		try {
			schoolYears = _provider.findRelatedSchoolYears();
		}
		catch (IDORelationshipException e1) {
			schoolYears = new ArrayList();
		}
		
		Collection groupYears = new ArrayList();
		if (_group != null) {
			try {
				groupYears = _group.findRelatedSchoolYears();
			}
			catch (IDORelationshipException e2) {
				groupYears = new HashSet();
			}
			if (_group.getSchoolYearId() != -1)
				groupYears.add(_group.getSchoolYear());
		}
				
		table.setHeight(row++, 15);
		table.add(getSmallHeader(localize("school_years", "Years") + ":"), 1, row);
		table.setNoWrap(1, row);
		
		Iterator iter = schoolYears.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			CheckBox box = getCheckBox(PARAMETER_SCHOOL_YEARS, year.getPrimaryKey().toString());
			if (groupYears.contains(year))
				box.setChecked(true);
			
			table.setCellpadding(3, row, 2);
			table.add(box, 3, row);
			table.add(Text.getNonBrakingSpace(), 3, row);
			table.add(getSmallText(year.getSchoolYearName()), 3, row++);
		}
		table.setHeight(row++, 15);
		
		List groupTeachers = new ArrayList();
		if (_group != null) {
			try {
				groupTeachers = new ArrayList(_group.findRelatedUsers());
			}
			catch (IDORelationshipException e2) {
				groupTeachers = new ArrayList();
			}
		}
		
		UserChooser chooser;
		int size = groupTeachers.size();

		for (int a = 0; a < 4; a++) {
			if (a == 0) {
				table.add(getSmallHeader(localize("teacher", "Teacher") + ":"), 1, row);
				table.setNoWrap(1, row);
			}
			
			chooser = new UserChooser(PARAMETER_TEACHERS+"_"+(a+1));
			if (a < size) {
				User teacher = (User) groupTeachers.get(a);
				chooser.setSelected(teacher);
			}
			table.add(chooser, 3, row++);
			
			if ((a + 1) < 4)
				table.setHeight(row++, 3);
		}
				
		table.setHeight(row++, 12);
		table.mergeCells(1, row, 3, row);
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save_group", "Save group")));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		table.add(save, 1, row);
		
		table.add(Text.getNonBrakingSpace(), 1, row);

		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel")));
		cancel.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		table.add(cancel, 1, row);
		
		return form;
	}
	
	private void deleteGroup() {
		try {
			getSchoolBusiness().removeSchoolClass(_groupID);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void saveGroup(IWContext iwc) {
		String name = iwc.getParameter(PARAMETER_GROUP_NAME);
		String[] years = iwc.getParameterValues(PARAMETER_SCHOOL_YEARS);
		String[] teachers = new String[4];
		int typeID = -1;
		if (iwc.isParameterSet(PARAMETER_TYPE_ID))
			typeID = Integer.parseInt(iwc.getParameter(PARAMETER_TYPE_ID));
		int seasonID = -1;
		if (iwc.isParameterSet(PARAMETER_SEASON_ID))
			seasonID = Integer.parseInt(iwc.getParameter(PARAMETER_SEASON_ID));
		boolean isSubGroup = Boolean.valueOf(iwc.getParameter(PARAMETER_IS_SUBGROUP)).booleanValue();
		
		for (int a = 1; a <= 4; a++) {
			String teacher = "-1";
			if (iwc.isParameterSet(PARAMETER_TEACHERS+"_"+a))
				teacher = iwc.getParameter(PARAMETER_TEACHERS+"_"+a);
			teachers[a-1] = teacher;
		}
		
		try {
			SchoolClass schoolClass = getSchoolBusiness().storeSchoolClass(_groupID, name, getSession().getProviderID(), typeID, seasonID, years, teachers);
			schoolClass.setIsSubGroup(isSubGroup);
			schoolClass.store();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private Collection getSchoolGroups() {
		try {
			if (getSession().getSeasonID() != -1 && getSession().getYearID() != -1)
				return getSchoolBusiness().findSchoolClassesBySchoolAndSeasonAndYear(getSession().getProviderID(), getSession().getSeasonID(), getSession().getYearID());
			else if (getSession().getSeasonID() != -1 && getSession().getYearID() == -1)
				return getSchoolBusiness().findSchoolClassesBySchoolAndSeason(getSession().getProviderID(), getSession().getSeasonID());
			else if (getSession().getSeasonID() == -1 && getSession().getYearID() != -1)
				return getSchoolBusiness().findSchoolClassesBySchoolAndYear(getSession().getProviderID(), getSession().getYearID());
			else
				return getSchoolBusiness().findSchoolClassesBySchool(getSession().getProviderID());
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	/**
	 * @param iwc
	 */
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		if (iwc.isParameterSet(PARAMETER_GROUP_ID))
			_groupID = Integer.parseInt(iwc.getParameter(PARAMETER_GROUP_ID));
		
		if (_groupID != -1) {
			try {
				_group = getSchoolBusiness().findSchoolClass(new Integer(_groupID));
			}
			catch (RemoteException e) {
				_group = null;
			}
		}
	}
}