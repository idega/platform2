package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class SchoolEditor extends SchoolBlock {

	Collection schoolTypeIds = null;

	protected void init(IWContext iwc) throws Exception {
		Form F = new Form();

		if (iwc.isParameterSet("sch_save_school")) {
			saveSchool(iwc);
			F.add(getListTable(iwc));
		}
		else if (iwc.isParameterSet("sch_delete_school")) {
			int id = Integer.parseInt(iwc.getParameter("sch_delete_school"));
			getBusiness().removeSchool(id);
			F.add(getListTable(iwc));
		}
		else if (iwc.isParameterSet("sch_school_id")) {
			int id = Integer.parseInt(iwc.getParameter("sch_school_id"));
			F.add(getInput(iwc, id));

		}
		else if (iwc.isParameterSet("sch_new_school")) {
			F.add(getInput(iwc, -1));
		}
		else
			F.add(getListTable(iwc));

		add(F);

	}

	private PresentationObject getInput(IWContext iwc, int id) throws java.rmi.RemoteException {
		return getInputTable(iwc, getBusiness().getSchool(new Integer(id)));
	}

	private void saveSchool(IWContext iwc) throws java.rmi.RemoteException {
		if (iwc.isParameterSet("sch_save_school")) {
			String id = iwc.getParameter("sch_school_id");

			String name = iwc.getParameter("sch_name");
			String address = iwc.getParameter("sch_address");
			String info = iwc.getParameter("sch_info");
			//String type = iwc.getParameter("sch_type_id");
			String area = iwc.getParameter("sch_area_id");
			String zipcode = iwc.getParameter("sch_zip_code");
			String ziparea = iwc.getParameter("sch_zip_area");
			String phone = iwc.getParameter("sch_phone");
			String keycode = iwc.getParameter("sch_keycode");
			String lon = iwc.getParameter("sch_lon");
			String lat = iwc.getParameter("sch_lat");
			String commune = iwc.getParameter("sch_commune");
			String[] type_ids = iwc.getParameterValues("sch_type_ids");
			String[] year_ids = iwc.getParameterValues("sch_year_ids");
			int[] types = new int[0];
			int[] years = new int[0];
			if (type_ids != null && type_ids.length > 0) {
				types = new int[type_ids.length];
				for (int i = 0; i < type_ids.length; i++) {
					types[i] = Integer.parseInt(type_ids[i]);
				}

			}
			if (year_ids != null && year_ids.length > 0) {
				years = new int[year_ids.length];
				for (int i = 0; i < year_ids.length; i++) {
					years[i] = Integer.parseInt(year_ids[i]);
				}

			}
			int areaId = -1, sid = -1;
			if (id != null) sid = Integer.parseInt(id);
			if (area != null) areaId = Integer.parseInt(area);

			Integer communePK = null;
			if (commune != null) {
				communePK = new Integer(commune);
			}
			//		System.err.println("school id is "+id);
			getBusiness().storeSchool(sid, name, info, address, zipcode, ziparea, phone, keycode, lat, lon, areaId, types, years, communePK);
		}
	}

	public PresentationObject getListTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(8);
		table.setWidth(8, 12);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		Collection schools = new java.util.Vector(0);
		try {
			if (schoolTypeIds == null) {
				schools = getBusiness().findAllSchools();
			}
			else {
				schools = getBusiness().findAllSchoolsByType(schoolTypeIds);
			}
		}
		catch (java.rmi.RemoteException rex) {

		}
		int col = 1;

		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("name", "Name")), col++, row);
		table.add(getSmallHeader(localize("area", "Area")), col++, row);
		table.add(getSmallHeader(localize("address", "Address")), col++, row);
		table.add(getSmallHeader(localize("zipcode", "Zipcode")), col++, row);
		table.add(getSmallHeader(localize("ziparea", "Ziparea")), col++, row);
		table.add(getSmallHeader(localize("commune", "Commune")), col++, row);
		table.add(getSmallHeader(localize("phone", "Phone")), col++, row);
		table.setRowStyleClass(row++, getHeaderRowClass());

		Iterator iter = schools.iterator();
		School school;
		SchoolArea area;
		Commune communePK;
		col = 1;
		while (iter.hasNext()) {
			school = (School) iter.next();
			try {
				Link L = new Link(getEditIcon(localize("edit", "Edit")));
				L.addParameter("sch_school_id", ((Integer) school.getPrimaryKey()).intValue());
				area = school.getSchoolArea();
				communePK = school.getCommune();
				
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
				table.add(getSmallText(school.getSchoolName()), col++, row);
				if (area != null) {
					table.add(getSmallText(area.getName()), col++, row);
				}
				else {
					table.add(getSmallText("-"), col++, row);
				}
				table.add(getSmallText(school.getSchoolAddress()), col++, row);
				table.add(getSmallText(school.getSchoolZipCode()), col++, row);
				table.add(getSmallText(school.getSchoolZipArea()), col++, row);
				if (communePK != null) {
					table.add(getSmallText(communePK.getCommuneName()), col, row);
				}
				col++;
				table.add(getSmallText(school.getSchoolPhone()), col++, row);
				table.add(L, col++, row);
				
				if (row % 2 == 0) {
					table.setRowStyleClass(row, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row, getLightRowClass());
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			row++;
			col = 1;
		}

		table.setHeight(row++, 12);
		table.setCellpaddingLeft(1, row, 12);
		table.mergeCells(1, row, table.getColumns(), row);
		GenericButton newLink = getButton(new GenericButton("new", localize("school.new", "New school")));
		newLink.setPageToOpen(iwc.getCurrentIBPageID());
		newLink.addParameterToPage("sch_new_school", "true");
		table.add(newLink, 1, row);

		return table;
	}

	public PresentationObject getInputTable(IWContext iwc, School ent) throws java.rmi.RemoteException {
		int last = 16;
		Table T = new Table(3, last);
		T.mergeCells(1, 1, 3, 1);

		TextInput inputName = (TextInput) getStyledInterface(new TextInput("sch_name"));
		TextInput inputAddress = (TextInput) getStyledInterface(new TextInput("sch_address"));
		TextArea inputInfo = (TextArea) getStyledInterface(new TextArea("sch_info"));

		TextInput inputZipCode = (TextInput) getStyledInterface(new TextInput("sch_zip_code"));
		TextInput inputZipArea = (TextInput) getStyledInterface(new TextInput("sch_zip_area"));
		TextInput inputPhone = (TextInput) getStyledInterface(new TextInput("sch_phone"));
		TextInput inputKeyCode = (TextInput) getStyledInterface(new TextInput("sch_keycode"));
		TextInput inputLON = (TextInput) getStyledInterface(new TextInput("sch_lon"));
		TextInput inputLAT = (TextInput) getStyledInterface(new TextInput("sch_lat"));
		DropdownMenu drpArea = (DropdownMenu) getStyledInterface(new DropdownMenu(getSchoolAreas(), "sch_area_id"));
		drpArea.setMenuElementFirst("-1", "");
		DropdownMenu communes = (DropdownMenu) getStyledInterface(new DropdownMenu("sch_commune"));
		SelectorUtility su = new SelectorUtility();
		su.getSelectorFromIDOEntities(communes, getCommuneBusiness(iwc).getCommunes(), "getCommuneName");

		Map schooltypes = null, schoolyears = null;
		Commune commune = null;
		int Id = -1;
		if (ent != null) {

			try {
				schooltypes = getSchoolRelatedSchoolTypes(ent);
				schoolyears = getSchoolRelatedSchoolYears(ent);
				commune = ent.getCommune();

				Id = ((Integer) ent.getPrimaryKey()).intValue();
				inputName.setContent(ent.getSchoolName());
				inputAddress.setContent(ent.getSchoolAddress());
				inputInfo.setContent(ent.getSchoolInfo());
				inputZipCode.setContent(ent.getSchoolZipCode());
				inputZipArea.setContent(ent.getSchoolZipArea());
				inputPhone.setContent(ent.getSchoolPhone());
				inputKeyCode.setContent(ent.getSchoolKeyCode());
				inputLON.setContent(ent.getSchoolLongitude());
				inputLAT.setContent(ent.getSchoolLatitude());
				drpArea.setSelectedElement(String.valueOf(ent.getSchoolAreaId()));
				if (commune != null) {
					communes.setSelectedElement(commune.getPrimaryKey().toString());
				}
			}
			catch (Exception ex) {
			}
		}
		else {
			commune = getCommuneBusiness(iwc).getDefaultCommune();
			if (commune != null) {
				communes.setSelectedElement(commune.getPrimaryKey().toString());
			}
		}

		int row = 1;

		T.add(new HiddenInput("sch_school_id", String.valueOf(Id)));
		T.add(getHeader(localize("area", "Area")), 1, row++);
		T.add(getHeader(localize("name", "Name")), 1, row++);
		T.add(getHeader(localize("address", "Address")), 1, row++);
		T.add(getHeader(localize("zipcode", "Zipcode")), 1, row++);
		T.add(getHeader(localize("ziparea", "Ziparea")), 1, row++);
		T.add(getHeader(localize("phone", "Phone")), 1, row++);
		T.add(getHeader(localize("info", "Info")), 1, row++);
		T.add(getHeader(localize("keycode", "Keycode")), 1, row++);
		T.add(getHeader(localize("latitude", "Latitude")), 1, row++);
		T.add(getHeader(localize("longitude", "Longitude")), 1, row++);
		T.add(getHeader(localize("commune", "Commune")), 1, row++);
		T.add(getHeader(localize("school_area", "SchoolArea")), 1, row++);

		row = 2;
		//T.add(drpType,3,row++);

		T.add(inputName, 3, row++);
		T.add(inputAddress, 3, row++);
		T.add(inputZipCode, 3, row++);
		T.add(inputZipArea, 3, row++);
		T.add(inputPhone, 3, row++);
		T.add(inputInfo, 3, row++);
		T.add(inputKeyCode, 3, row++);
		T.add(inputLAT, 3, row++);
		T.add(inputLON, 3, row++);
		T.add(communes, 3, row++);
		T.add(drpArea, 3, row++);

		Table typeTable = new Table();
		int row2 = 1;
		Collection types = getSchoolTypes();
		if (types != null && !types.isEmpty()) {
			java.util.Iterator iter = types.iterator();
			boolean hasMap = schooltypes != null;
			SchoolType type;
			CheckBox chk = new CheckBox("sch_type_ids");
			CheckBox tjk;
			Integer primaryKey;
			while (iter.hasNext()) {
				type = (SchoolType) iter.next();
				primaryKey = (Integer) type.getPrimaryKey();
				tjk = (CheckBox) chk.clone();
				tjk.setValue(primaryKey.intValue());
				if (hasMap && schooltypes.containsKey(primaryKey)) {
					tjk.setChecked(true);
				}
				typeTable.add(tjk, 1, row2);
				typeTable.add(type.getSchoolTypeName(), 2, row2);
				//row2++;

				Table yearTable = new Table();
				/////////////////
				Collection years = getSchoolYears(primaryKey.intValue());
				if (years != null && !years.isEmpty()) {
					java.util.Iterator yearIter = years.iterator();
					boolean yearMap = schoolyears != null;
					SchoolYear year;
					CheckBox ytjk;
					int col3 = 1;
					int row3 = 1;
					while (yearIter.hasNext()) {
						year = (SchoolYear) yearIter.next();
						ytjk =  getCheckBox("sch_year_ids", year.getPrimaryKey().toString());
						if (yearMap && schoolyears.containsKey(year.getPrimaryKey())) {
							ytjk.setChecked(true);
						}
						yearTable.add(ytjk, col3, row3++);
						yearTable.add(year.getSchoolYearName(), col3++, row3);
						row3 = 1;

					}

				}

				//typeTable.mergeCells(1,row2,2,row2);
				typeTable.setVerticalAlignment(1, row2, Table.VERTICAL_ALIGN_TOP);
				typeTable.setVerticalAlignment(2, row2, Table.VERTICAL_ALIGN_TOP);
				typeTable.setVerticalAlignment(3, row2, Table.VERTICAL_ALIGN_TOP);
				typeTable.add(yearTable, 3, row2);
				++row2;
				//////////////////
			}

		}

		T.add(typeTable, 1, 13);

		T.add(getButton(new SubmitButton(localize("save", "Save"), "sch_save_school", "true")), 3, last);
		GenericButton cancel = getButton(new GenericButton("cancel", localize("cancel", "Cancel")));
		cancel.setPageToOpen(iwc.getCurrentIBPageID());
		T.add(cancel, 3, last);
		if (Id > 0) {
			GenericButton delete = getButton(new GenericButton("delete", localize("delete", "Delete")));
			delete.setPageToOpen(iwc.getCurrentIBPageID());
			delete.addParameterToPage("sch_delete_school", Id);
			T.add(delete, 3, last);
		}

		return T;
	}

	private Map getSchoolRelatedSchoolTypes(School school) throws java.rmi.RemoteException {
		return getBusiness().getSchoolRelatedSchoolTypes(school);
	}

	private Map getSchoolRelatedSchoolYears(School school) throws java.rmi.RemoteException {
		return getBusiness().getSchoolRelatedSchoolYears(school);
	}

	private Collection getSchoolTypes() throws java.rmi.RemoteException {
		return getBusiness().findAllSchoolTypes();
	}

	private Collection getSchoolYears(int schoolTypeId) throws java.rmi.RemoteException {
		return getBusiness().findAllSchoolYearsBySchoolType(schoolTypeId);
	}

	private Collection getSchoolAreas() throws java.rmi.RemoteException {
		return getBusiness().findAllSchoolAreas();
	}

	public void setSchoolTypeCategory(String typeCategory) {
		if (typeCategory != null && !typeCategory.equals("")) {
			try {
				SchoolTypeHome sth = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
				schoolTypeIds = sth.findAllByCategory(typeCategory);
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

	}

	public CommuneBusiness getCommuneBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CommuneBusiness) IBOLookup.getServiceInstance(iwac, CommuneBusiness.class);
	}

}