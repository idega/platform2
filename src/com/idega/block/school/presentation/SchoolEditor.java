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
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class SchoolEditor extends SchoolBlock {

	private static final String PARAMETER_ACTION = "sch_prm_action";
	private static final String PARAMETER_SCHOOL_PK = "prm_school_pk";
	private static final String PARAMETER_PROVIDER_STRING_ID = "prm_provider_string_id";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_INFO = "prm_info";
	private static final String PARAMETER_AREA = "prm_area";
	private static final String PARAMETER_ZIPCODE = "prm_zipcode";
	private static final String PARAMETER_ZIPAREA = "prm_ziparea";
	private static final String PARAMETER_PHONE = "prm_phone";
	private static final String PARAMETER_KEYCODE = "prm_keycode";
	private static final String PARAMETER_LONGITUDE = "prm_longitude";
	private static final String PARAMETER_LATITUDE = "prm_latitude";
	private static final String PARAMETER_COMMUNE = "prm_commune";
	private static final String PARAMETER_TYPE_PKS = "prm_type_pks";
	private static final String PARAMETER_YEAR_PKS = "prm_year_pks";
	private static final String PARAMETER_AFTER_SCHOOL_CARE_PROVIDER_PK = "prm_care_provider_pk";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;
	
	boolean _useProviderStringId = false;
	Collection schoolTypeIds = null;
	
	public boolean getUseProviderStringId() {
		return _useProviderStringId;
	}

	public void setUseProviderStringId(boolean b) {
		_useProviderStringId = b;
	}

	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_EDIT:
				Object schoolPK = iwc.getParameter(PARAMETER_SCHOOL_PK);
				showEditor(iwc, schoolPK);
				break;

			case ACTION_NEW:
				showEditor(iwc, null);
				break;

			case ACTION_SAVE:
				saveSchool(iwc);
				showList(iwc);
				break;

			case ACTION_DELETE:
				getBusiness().removeProvider(iwc.getParameter(PARAMETER_SCHOOL_PK));
				showList(iwc);
				break;
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}

	private void saveSchool(IWContext iwc) throws java.rmi.RemoteException {
		String pk = iwc.getParameter(PARAMETER_SCHOOL_PK);

		String providerStringId = iwc.getParameter(PARAMETER_PROVIDER_STRING_ID);
		String name = iwc.getParameter(PARAMETER_NAME);
		String address = iwc.getParameter(PARAMETER_ADDRESS);
		String info = iwc.getParameter(PARAMETER_INFO);
		String area = iwc.getParameter(PARAMETER_AREA);
		String zipcode = iwc.getParameter(PARAMETER_ZIPCODE);
		String ziparea = iwc.getParameter(PARAMETER_ZIPAREA);
		String phone = iwc.getParameter(PARAMETER_PHONE);
		String keycode = iwc.getParameter(PARAMETER_KEYCODE);
		String lon = iwc.getParameter(PARAMETER_LONGITUDE);
		String lat = iwc.getParameter(PARAMETER_LATITUDE);
		String commune = iwc.getParameter(PARAMETER_COMMUNE);
		String[] type_ids = iwc.getParameterValues(PARAMETER_TYPE_PKS);
		String[] year_ids = iwc.getParameterValues(PARAMETER_YEAR_PKS);
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
		if (pk != null) sid = Integer.parseInt(pk);
		if (area != null) areaId = Integer.parseInt(area);

		Integer communePK = null;
		if (commune != null) {
			communePK = new Integer(commune);
		}
		
		Object providerID = iwc.isParameterSet(PARAMETER_AFTER_SCHOOL_CARE_PROVIDER_PK) ? iwc.getParameter(PARAMETER_AFTER_SCHOOL_CARE_PROVIDER_PK) : null;
		
		School school = getBusiness().storeSchool(sid, name, info, address, zipcode, ziparea, phone, keycode, lat, lon, areaId, types, years, communePK, providerStringId);
		if (providerID != null) {
			school.setAfterSchoolCareProvider(providerID);
			school.store();
		}
	}

	public void showList(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass(STYLENAME_LIST_TABLE);
		
		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(7);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

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

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.createHeaderCell().add(new Text(localize("name", "Name")));
		row.createHeaderCell().add(new Text(localize("area", "Area")));
		row.createHeaderCell().add(new Text(localize("address", "Address")));
		row.createHeaderCell().add(new Text(localize("zipcode", "Zipcode")));
		row.createHeaderCell().add(new Text(localize("ziparea", "Ziparea")));
		row.createHeaderCell().add(new Text(localize("commune", "Commune")));
		row.createHeaderCell().add(new Text(localize("phone", "Phone")));
		row.createHeaderCell();
		row.createHeaderCell();
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		Iterator iter = schools.iterator();
		School school;
		SchoolArea area;
		Commune commune;
		while (iter.hasNext()) {
			row = group.createRow();
			school = (School) iter.next();
			try {
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_SCHOOL_PK, school.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
				
				Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
				delete.addParameter(PARAMETER_SCHOOL_PK, school.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				area = school.getSchoolArea();
				commune = school.getCommune();
				
				row.createCell().add(new Text(school.getSchoolName()));
				if (area != null) {
					row.createCell().add(new Text(area.getName()));
				}
				else {
					row.createCell().add(new Text("-"));
				}
				row.createCell().add(new Text(school.getSchoolAddress()));
				row.createCell().add(new Text(school.getSchoolZipCode()));
				row.createCell().add(new Text(school.getSchoolZipArea()));
				if (commune != null) {
					row.createCell().add(new Text(commune.getCommuneName()));
				}
				else {
					row.createCell().add(new Text("-"));
				}
				row.createCell().add(new Text(school.getSchoolPhone()));
				row.createCell().add(edit);
				row.createCell().add(delete);
				
				if (iRow % 2 == 0) {
					row.setStyleClass(STYLENAME_LIST_TABLE_EVEN_ROW);
				}
				else {
					row.setStyleClass(STYLENAME_LIST_TABLE_ODD_ROW);
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}

		form.add(table);
		form.add(new Break());
		
		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("school.new", "New school"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		form.add(newLink);

		add(form);
	}

	public void showEditor(IWContext iwc, Object schoolPK) throws java.rmi.RemoteException {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		TextInput inputProviderStringId = new TextInput(PARAMETER_PROVIDER_STRING_ID);
		inputProviderStringId.setAsNotEmpty(localize("sch_provider_id_not_empty", "Provider id must be entered."));

		TextInput inputName = new TextInput(PARAMETER_NAME);
		TextInput inputAddress = new TextInput(PARAMETER_ADDRESS);
		TextArea inputInfo = new TextArea(PARAMETER_INFO);

		TextInput inputZipCode = new TextInput(PARAMETER_ZIPCODE);
		TextInput inputZipArea = new TextInput(PARAMETER_ZIPAREA);
		TextInput inputPhone = new TextInput(PARAMETER_PHONE);
		TextInput inputKeyCode = new TextInput(PARAMETER_KEYCODE);
		TextInput inputLON = new TextInput(PARAMETER_LONGITUDE);
		TextInput inputLAT = new TextInput(PARAMETER_LATITUDE);
		
		DropdownMenu drpArea = new DropdownMenu(getSchoolAreas(), PARAMETER_AREA);
		drpArea.setMenuElementFirst("-1", "");
		
		DropdownMenu communes = new DropdownMenu(PARAMETER_COMMUNE);
		SelectorUtility su = new SelectorUtility();
		su.getSelectorFromIDOEntities(communes, getCommuneBusiness(iwc).getCommunes(), "getCommuneName");

		DropdownMenu providers = new DropdownMenu(PARAMETER_AFTER_SCHOOL_CARE_PROVIDER_PK);
		Collection schools = getBusiness().findAllSchoolsByCategory(getBusiness().getCategoryElementarySchool().getCategory());
		su.getSelectorFromIDOEntities(providers, schools, "getSchoolName");
		providers.setMenuElementFirst("", "");
		
		Map schooltypes = null, schoolyears = null;
		Commune commune = null;
		if (schoolPK != null) {
			School school = getBusiness().getSchool(schoolPK);
			
			try {
				schooltypes = getSchoolRelatedSchoolTypes(school);
				schoolyears = getSchoolRelatedSchoolYears(school);
				commune = school.getCommune();

				if (_useProviderStringId) {
					inputProviderStringId.setContent(school.getProviderStringId());
				}
				inputName.setContent(school.getSchoolName());
				inputAddress.setContent(school.getSchoolAddress());
				inputInfo.setContent(school.getSchoolInfo());
				inputZipCode.setContent(school.getSchoolZipCode());
				inputZipArea.setContent(school.getSchoolZipArea());
				inputPhone.setContent(school.getSchoolPhone());
				inputKeyCode.setContent(school.getSchoolKeyCode());
				inputLON.setContent(school.getSchoolLongitude());
				inputLAT.setContent(school.getSchoolLatitude());
				drpArea.setSelectedElement(String.valueOf(school.getSchoolAreaId()));
				if (commune != null) {
					communes.setSelectedElement(commune.getPrimaryKey().toString());
				}
				if (school.getAfterSchoolCareProviderPK() != null) {
					providers.setSelectedElement(school.getAfterSchoolCareProviderPK().toString());
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

		if (schoolPK != null) {
			form.add(new HiddenInput(PARAMETER_SCHOOL_PK, schoolPK.toString()));
		}
		
		Layer layer;
		Label label;
		if (_useProviderStringId) {
			layer = new Layer(Layer.DIV);
			layer.setStyleClass(STYLENAME_FORM_ELEMENT);
			label = new Label(localize("provider_id", "Provider ID"), inputProviderStringId);
			layer.add(label);
			layer.add(inputProviderStringId);
			form.add(layer);
		}

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("address", "Address"), inputAddress);
		layer.add(label);
		layer.add(inputAddress);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("zipcode", "zipcode"), inputZipCode);
		layer.add(label);
		layer.add(inputZipCode);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("ziparea", "Ziparea"), inputZipArea);
		layer.add(label);
		layer.add(inputZipArea);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("phone", "Phone"), inputPhone);
		layer.add(label);
		layer.add(inputPhone);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("info", "Info"), inputInfo);
		layer.add(label);
		layer.add(inputInfo);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("keycode", "keycode"), inputKeyCode);
		layer.add(label);
		layer.add(inputKeyCode);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("inputKeyCode", "Latitude"), inputLAT);
		layer.add(label);
		layer.add(inputLAT);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("longitude", "Longitude"), inputLON);
		layer.add(label);
		layer.add(inputLON);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("commune", "Commune"), communes);
		layer.add(label);
		layer.add(communes);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("school_area", "SchoolArea"), drpArea);
		layer.add(label);
		layer.add(drpArea);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("after_school_care_provider", "After school care provider"), providers);
		layer.add(label);
		layer.add(providers);
		form.add(layer);

		form.add(new Break());
		Lists list = new Lists();
		Collection types = getSchoolTypes();
		if (types != null && !types.isEmpty()) {
			Iterator iter = types.iterator();
			boolean hasMap = schooltypes != null;
			
			while (iter.hasNext()) {
				SchoolType type = (SchoolType) iter.next();
				ListItem item = new ListItem();

				CheckBox typeCheck = new CheckBox(PARAMETER_TYPE_PKS, type.getPrimaryKey().toString());
				if (hasMap && schooltypes.containsKey(type.getPrimaryKey())) {
					typeCheck.setChecked(true);
				}
				
				label = new Label(type.getSchoolTypeName(), typeCheck);
				item.add(typeCheck);
				item.add(label);
				list.add(item);

				Collection years = getSchoolYears(((Integer) type.getPrimaryKey()).intValue());
				if (years != null && !years.isEmpty()) {
					Lists yearList = new Lists();
					
					Iterator yearIter = years.iterator();
					boolean yearMap = schoolyears != null;
					while (yearIter.hasNext()) {
						SchoolYear year = (SchoolYear) yearIter.next();
						ListItem yearItem = new ListItem();
						
						CheckBox yearCheck = new CheckBox(PARAMETER_YEAR_PKS, year.getPrimaryKey().toString());
						if (yearMap && schoolyears.containsKey(year.getPrimaryKey())) {
							yearCheck.setChecked(true);
						}
						
						label = new Label(year.getSchoolYearName(), yearCheck);
						yearItem.add(yearCheck);
						yearItem.add(label);
						yearList.add(yearItem);
					}
					item.add(yearList);
				}
			}
			form.add(list);
		}
		form.add(new Break());

		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(cancel);
		form.add(save);

		add(form);
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