/*
 * Created on 20.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.data.Address;
import com.idega.core.data.Phone;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareGroupAdmin extends ChildCareBlock {

	protected static final int STATUS_ACTIVE = 1;
	protected static final int STATUS_NOT_YET_ACTIVE = 2;

	protected static final String PARAMETER_CHILD_ID = "cc_remove_child_id";
	protected static final String PARAMETER_STATUS_SORT = "cc_status_sort";
	
	private int sort = -1;
	private boolean showNotYetActive = false;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().hasPrognosis()) {
			parse(iwc);
			
			Table table = new Table(1,5);
			table.setWidth(getWidth());
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setHeight(2, 12);
			table.setHeight(4, 12);
			add(table);
			
			table.add(getNavigationTable(), 1, 1);
			table.add(getChildrenTable(iwc), 1, 3);
			
			String localized = "";
			if (getSession().getGroupID() != -1)
				localized = localize("child_care.change_group", "Change group");
			else
				localized = localize("child_care.create_group", "Create group");
	
			GenericButton createGroup = (GenericButton) getButton(new GenericButton("create_change_group", localized));
			createGroup.setWindowToOpen(ChildCareWindow.class);
			createGroup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_CREATE_GROUP);
			createGroup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			table.add(createGroup, 1, 5);
			
			if (getSession().getGroupID() != -1 && getBusiness().getSchoolBusiness().getNumberOfStudentsInClass(getSession().getGroupID()) == 0) {
				GenericButton deleteGroup = (GenericButton) getButton(new GenericButton("delete_group", localize("child_care.delete_group", "Delete group")));
				deleteGroup.setWindowToOpen(ChildCareWindow.class);
				deleteGroup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_ACTION, ChildCareAdminWindow.ACTION_DELETE_GROUP);
				deleteGroup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
				table.add(Text.getNonBrakingSpace(), 1, 5);
				table.add(deleteGroup, 1, 5);
			}
		}
		else {
			add(getSmallErrorText(localize("child_care.prognosis_must_be_set","Prognosis must be set or updated before you can continue!")));
		}
	}

	protected Table getChildrenTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		if (sort == STATUS_NOT_YET_ACTIVE)
			table.setColumns(7);
		else
			table.setColumns(6);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.address","Address"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.phone","Phone"), column++, row);
		if (sort == STATUS_NOT_YET_ACTIVE)
			table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row++);
		else
			row++;
			
		SchoolClassMember student;
		User child;
		Address address;
		Phone phone;
		Link move;
		Link delete;
		Link childInfo;
		IWTimestamp registered;
		boolean showComment = false;
		
		IWTimestamp stamp = new IWTimestamp();
		//stamp.addDays(1);

		Collection students = null;
		if (sort != -1)
			students = getBusiness().getSchoolBusiness().findStudentsInSchoolByDate(getSession().getChildCareID(), getSession().getGroupID(), stamp.getDate(), showNotYetActive);
		else
			students = getBusiness().getSchoolBusiness().findStudentsInSchoolByDate(getSession().getChildCareID(), getSession().getGroupID(), stamp.getDate());
		
		Iterator iter = students.iterator();
		while (iter.hasNext()) {
			column = 1;
			student = (SchoolClassMember) iter.next();
			child = student.getStudent();
			address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			phone = getBusiness().getUserBusiness().getChildHomePhone(child);
			registered = new IWTimestamp(student.getRegisterDate());

			move = new Link(getEditIcon(localize("child_care.move_to_another_group", "Move child to another group")));
			move.setWindowToOpen(ChildCareWindow.class);
			move.setParameter(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_MOVE_TO_GROUP));
			move.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			move.addParameter(ChildCareAdminWindow.PARAMETER_OLD_GROUP, student.getSchoolClassId());
			move.addParameter(ChildCareAdminWindow.PARAMETER_USER_ID, student.getClassMemberId());
			
			delete = new Link(getDeleteIcon(localize("child_care.delete_from_childcare", "Remove child from child care and cancel contract.")));
			delete.setWindowToOpen(ChildCareWindow.class);
			delete.setParameter(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_CANCEL_CONTRACT));
			delete.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			delete.addParameter(ChildCareAdminWindow.PARAMETER_USER_ID, student.getClassMemberId());

			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());

			if (registered.isLaterThan(stamp)) {
				showComment = true;
				table.add(getSmallErrorText("*" + Text.NON_BREAKING_SPACE), column, row);
			}

			if (getResponsePage() != null) {
				childInfo = getSmallLink(child.getNameLastFirst(true));
				childInfo.setEventListener(ChildCareEventListener.class);
				childInfo.addParameter(getSession().getParameterUserID(), student.getClassMemberId());
				childInfo.setPage(getResponsePage());
				table.add(childInfo, column++, row);
			}
			else
				table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
			table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
			if (address != null)
				table.add(getSmallText(address.getStreetAddress()), column, row);
			column++;
			if (phone != null)
				table.add(getSmallText(phone.getNumber()), column, row);
			column++;
			if (sort == STATUS_NOT_YET_ACTIVE)
				table.add(getSmallText(registered.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			
			table.setWidth(column, row, 12);
			table.add(move, column++, row);
			table.setWidth(column, row, 12);
			table.add(delete, column++, row++);
		}
		
		if (showComment) {
			table.setHeight(2, row++);
			table.mergeCells(1, row, table.getColumns(), row);
			table.add(getSmallErrorText("*"), 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.not_yet_active_placing","Placing not yet active")), 1, row++);
		}

		return table;
	}

	protected Form getNavigationTable() throws RemoteException {
		Form form = new Form();
		form.setEventListener(ChildCareEventListener.class);
		
		Table table = new Table(7,1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, 2);
		table.setWidth(4, 16);
		table.setWidth(6, 2);
		form.add(table);

		table.add(getSmallHeader(localize("child_care.group","Group")+":"),1,1);
		table.add(getChildCareGroups(), 3, 1);
		
		DropdownMenu statusSort = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_STATUS_SORT));
		statusSort.addMenuElement("-1", localize("child_care.show_all_statuses","Show all"));
		statusSort.addMenuElement(STATUS_ACTIVE, localize("child_care.show_active","Show active"));
		statusSort.addMenuElement(STATUS_NOT_YET_ACTIVE, localize("child_care.show_not_yet_active","Show not yet active"));
		statusSort.setSelectedElement(sort);
		statusSort.setToSubmit();
		
		table.add(getSmallHeader(localize("child_care.status_sort","Sorting")+":"), 5, 1);
		table.add(statusSort, 7, 1);
		
		return form;
	}
	
	protected DropdownMenu getChildCareGroups() throws RemoteException {
		DropdownMenu menu = getGroups(getSession().getGroupID(), -1);
		menu.addMenuElementFirst("-1", localize("child_care.show_all_groups","Show all"));
		menu.setToSubmit();
		
		return menu;	
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_STATUS_SORT))
			sort = Integer.parseInt(iwc.getParameter(PARAMETER_STATUS_SORT));
			
		if (sort != -1) {
			switch (sort) {
				case STATUS_ACTIVE :
					showNotYetActive = false;
					break;
				case STATUS_NOT_YET_ACTIVE :
					showNotYetActive = true;
					break;
			}
		}
	}
}