/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.block.school.business.SchoolAreaBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolTypeBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
import com.idega.builder.data.IBPage;
import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.core.data.PostalCode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareApplicationForm extends CommuneBlock {
	private final static int ACTION_VIEW_CHECKS = 0;
	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_SUBMIT_FORM = 2;

	private final static String ERROR_NO_CHECKS = "cca_no_checks";

	private final static String SUBJECT = "cca_subject";
	private final static String PROVIDERS = "cca_providers";
	private final static String LAST_NAME = "cca_last_name";
	private final static String FIRST_NAME = "cca_first_name";
	private final static String ADDRESS = "cca_address";
	private final static String PO_TOWN = "cca_po_town";
	private final static String CARE_FROM = "cca_care_from";
	private final static String WANT_PROVIDER = "cca_want_provider";
	private final static String WANT_PROVIDER_LINK = "cca_want_provider_link";

	private final static String PARAM_FORM_SUBMIT = "cca_submit";
	private final static String PARAM_DATE = "cca_date";
	private final static String PARAM_AREA = "cca_area";
	private final static String PARAM_PROVIDER = "cca_provider";
	private final static String PARAM_FORM_NAME = "cca_form";
	private final static String PARAM_CHECK_ID = "cca_check_id";
	private final static String PARAM_CHILD_ID = "cca_child_id";

	private final static String NOT_LOGGED_IN = "cca_not_logged_in";
	private final static String APPLICATION_INSERTED = "cca_application_ok";
	private final static String APPLICATION_FAILURE = "cca_application_failed";

	private final static String EMAIL_PROVIDER_SUBJECT = "cca_provider_email_subject";
	private final static String EMAIL_PROVIDER_MESSAGE = "cca_provider_email_message";

	private String prefix = "cc_app_";
	private String prmPreSchool = prefix + "pre_scl";
	private String prmPreArea = prefix + "pre_ara";
	private String prmFirstSchool = prefix + "fst_scl";
	private String prmSecondSchool = prefix + "snd_scl";
	private String prmThirdSchool = prefix + "trd_scl";
	private String prmFourthSchool = prefix + "fou_scl";
	private String prmFifthSchool = prefix + "fiv_scl";
	private String prmFirstArea = prefix + "fst_ara";
	private String prmSecondArea = prefix + "snd_ara";
	private String prmThirdArea = prefix + "trd_ara";
	private String prmFourthArea = prefix + "fou_ara";
	private String prmFifthArea = prefix + "fiv_ara";
	private String prmPreType = prefix + "pre_typ";
	private String prmType = prefix + "cho_typ";

	private String prmAction = prefix + "snd_frm";
	private String prmMessage = prefix + "msg";

	private School school = null;
	private SchoolArea schoolArea = null;

	private int valPreArea = 1;
	private int valPreSchool = -1;
	private int valFirstSchool = -1;
	private int valSecondSchool = -1;
	private int valThirdSchool = -1;
	private int valFourthSchool = -1;
	private int valFifthSchool = -1;
	private int valFirstArea = -1;
	private int valSecondArea = -1;
	private int valThirdArea = -1;
	private int valFourthArea = -1;
	private int valFifthArea = -1;
	private int valPreType = 1;
	private int valType = 1;

	private boolean schoolChange = false;
	private boolean hasPreviousSchool = false;
	private boolean hasChosen = false;

	protected User _user = null;
	protected IBPage _presentationPage = null;
	protected IWBundle _iwb;
	protected IWResourceBundle _iwrb;

	protected int _valProvider[] = { -1, -1, -1, -1, -1 };
	protected int _valArea[] = { -1, -1, -1, -1, -1 };
	protected String _valDate = null;

	protected Collection _areas = null;
	protected Collection _providers = null;
	protected Collection _schoolTypes = null;
//	protected Collection _schoolType = null;

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		_iwb = getBundle(iwc);
		_iwrb = getResourceBundle(iwc);

		_user = iwc.getCurrentUser();

		if (_user != null) {
			_schoolTypes = getSchoolTypes(iwc, "CHILDCARE");
			_areas = getAreas(iwc);

			setResourceBundle(getResourceBundle(iwc));

			try {
				int action = parseAction(iwc);
				switch (action) {
					case ACTION_VIEW_CHECKS :
						viewChecks(iwc);
						break;
					case ACTION_VIEW_FORM :
						viewForm(iwc);
						break;
					case ACTION_SUBMIT_FORM :
						submitForm(iwc);
						break;
				}
			}
			catch (Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		}
		else {
			add(getErrorText(localize(NOT_LOGGED_IN, "No user logged in")));
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(this.PARAM_FORM_SUBMIT)) {
			if (checkParameters(iwc)) {
				return ACTION_SUBMIT_FORM;
			}
			else {
				return ACTION_VIEW_FORM;
			}
		}
		else if (iwc.isParameterSet(PARAM_CHECK_ID)) {
			return ACTION_VIEW_FORM;
		}
		else {
			return ACTION_VIEW_CHECKS;
		}
	}

	/**
	 * Displays a list of checks this user has got.
	 * 
	 * @param iwc The IdegaWeb context
	 */
	private void viewChecks(IWContext iwc) {
		Collection checks = null;

		try {
			checks = getCheckBusiness(iwc).findAllApprovedChecksByUser(_user);
		}
		catch (RemoteException e) {
		}
		catch (Exception e) {
		}

		if (checks != null && !checks.isEmpty()) {
			Iterator it = checks.iterator();
			while (it.hasNext()) {
				Check check = (Check) it.next();
				User child = null;
				try {
					child = getCheckBusiness(iwc).getUserById(check.getChildId());
				}
				catch (RemoteException e) {
				}
				catch (Exception e) {
				}

				Link link = null;
				//try {
				if (child != null) {
					link = new Link(child.getName());
					link.addParameter(PARAM_CHECK_ID, ((Integer) check.getPrimaryKey()).toString());
				}
				//}
				//catch (RemoteException e) {
				//}

				if (link != null) {
					add(link);
					add(Text.BREAK);
				}
			}
		}
		else {
			add(getErrorText(localize(ERROR_NO_CHECKS, "This user has no checks")));
		}
	}

	private void viewForm(IWContext iwc) {
		Form form = new Form();
		form.setName(PARAM_FORM_NAME);
		form.setOnSubmit("return checkApplication()");

		String checkId = iwc.getParameter(PARAM_CHECK_ID);
		form.addParameter(PARAM_CHECK_ID, checkId);
		Check check = null;
		try {
			check = getCheckBusiness(iwc).getCheck(new Integer(checkId).intValue());
		}
		catch (RemoteException e) {
		}
		catch (Exception e) {
		}

		User child = null;
		if (check != null) {
			try {
				child = getCheckBusiness(iwc).getUserById(check.getChildId());
			}
			catch (RemoteException e) {
			}
			catch (Exception e) {
			}
		}

		Table nameTable1 = new Table(2, 2);
		nameTable1.setCellpadding(4);
		nameTable1.setCellspacing(3);
		nameTable1.setColor(1, 1, getBackgroundColor());
		nameTable1.setColor(2, 1, getBackgroundColor());
		nameTable1.add(getSmallText(localize(LAST_NAME, "Last name")), 1, 1);
		nameTable1.add(getSmallText(localize(FIRST_NAME, "First name")), 2, 1);
		if (child != null) {
			try {
				nameTable1.add(getSmallText(child.getLastName()), 1, 2);
				nameTable1.add(getSmallText(child.getFirstName()), 2, 2);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			form.addParameter(PARAM_CHILD_ID, ((Integer) child.getPrimaryKey()).intValue());
		}

		Table nameTable2 = new Table(2, 2);
		nameTable2.setCellpadding(4);
		nameTable2.setCellspacing(3);
		nameTable2.setColor(1, 1, getBackgroundColor());
		nameTable2.setColor(2, 1, getBackgroundColor());
		nameTable2.add(getSmallText(localize(ADDRESS, "Address")), 1, 1);
		nameTable2.add(getSmallText(localize(PO_TOWN, "Postoffice and town")), 2, 1);
		if (child != null) {
			try {
				Address address = getCheckBusiness(iwc).getUserAddress(child);
				PostalCode code = getCheckBusiness(iwc).getUserPostalCode(child);
				if (address != null)
					nameTable2.add(getSmallText(address.getStreetName() + " " + address.getStreetNumber()), 1, 2);
				if (code != null)
					nameTable2.add(getSmallText(code.getPostalCode() + " " + code.getName()), 2, 2);
			}
			catch (RemoteException e) {
			}
			catch (Exception e) {
			}
		}

		Table inputTable = new Table(3, 11);
		inputTable.setCellspacing(2);
		inputTable.setCellpadding(4);
		inputTable.setAlignment(3, 11, "right");
		inputTable.setColor(getBackgroundColor());
		inputTable.mergeCells(1, 1, 3, 1);
		inputTable.mergeCells(1, 2, 3, 2);
		inputTable.mergeCells(1, 3, 3, 3);
		inputTable.mergeCells(2, 4, 3, 4);
		inputTable.mergeCells(1, 5, 3, 5);
		inputTable.mergeCells(1, 11, 3, 11);

		inputTable.add(getHeader(localize(PROVIDERS, "Providers")), 1, 2);

		SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, localize(PARAM_FORM_SUBMIT, "Submit application"));
		submit.setAsImageButton(true);
		inputTable.setAlignment(1, 11, "Right");
		inputTable.add(submit, 1, 11);

		String provider = localize(PARAM_PROVIDER, "Provider");
		String from = localize(CARE_FROM, "From");
		Text providerText = null;
		Text fromText = getSmallText(from);

		DateInput date = new DateInput(PARAM_DATE);
		date.setToCurrentDate();
		date.setStyleAttribute("style", getSmallTextFontStyle());
		inputTable.add(fromText, 1, 4);
		inputTable.add(date, 2, 4);

		/*DropdownMenu drpFirstArea = this.getAreaDrop(iwc,"area1");//getDropdown(iwc, prmFirstArea, null, prmType, prmFirstArea, prmFirstSchool, 2, _iwrb.getLocalizedString("school.area_first", "School Area...................."));
		drpFirstArea.setOnChange(getFilterCallerScript("area1", prmFirstSchool));
		
		DropdownMenu drpFirstSchool = (DropdownMenu) getStyledInterface(new DropdownMenu(prmFirstSchool));
//		drpFirstSchool.addMenuElementFirst("-1", _iwrb.getLocalizedString("school.school_first", "School........................."));

		inputTable.add(drpFirstArea,2,6);
		inputTable.add(drpFirstSchool,3,6);*/
		for (int i = 1; i < 6; i++) {
			DropdownMenu areaDrop = getAreaDrop(iwc,PARAM_AREA + "_" + i);
			areaDrop.setOnChange(getFilterCallerScript(PARAM_AREA + "_" + i, PARAM_PROVIDER + "_" + i));
			DropdownMenu providerDrop = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAM_PROVIDER + "_" + i));
			providerDrop.addMenuElementFirst("-1", _iwrb.getLocalizedString("school.school_first", "School........................."));
			areaDrop.setAttribute("style", getSmallTextFontStyle());
			providerDrop.setAttribute("style", getSmallTextFontStyle());
			providerText = getSmallText(provider + " " + i);
			inputTable.add(providerText, 1, 5 + i);
			inputTable.add(areaDrop, 2, 5 + i);
			inputTable.add(providerDrop, 3, 5 + i);
		}

		add(nameTable1);
		add(nameTable2);
		add(Text.BREAK);
		form.add(inputTable);

		Page p = this.getParentPage();
		if (p != null) {
			Script S = p.getAssociatedScript();
			S.addFunction("checkApplication", getSchoolCheckScript());
			try {
				S.addFunction("changeFilter", getFilterScript(iwc));
			}
			catch (RemoteException re) {
			}
		}

		add(form);
	}

	private void submitForm(IWContext iwc) {
		String checkId = iwc.getParameter(PARAM_CHECK_ID);
		String childId = iwc.getParameter(PARAM_CHILD_ID);
		ChildCareBusiness business = getChildCareBusiness(iwc);
		boolean done = false;
		if (business != null) {
			try {
				String subject = localize(EMAIL_PROVIDER_SUBJECT, "Child care application");
				String message = localize(EMAIL_PROVIDER_MESSAGE, "You have received a new childcare application");

				done = business.insertApplications(_user, _valProvider, _valDate, new Integer(checkId).intValue(), new Integer(childId).intValue(), subject, message, false);
			}
			catch (RemoteException e) {
				e.printStackTrace();
				done = false;
			}
		}

		if (done) {
			if (getResponsePage() != null)
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			else
				add(new Text(localize(APPLICATION_INSERTED, "Application submitted")));
		}
		else
			add(new Text(localize(APPLICATION_FAILURE, "Failed to submit application")));
	}

	public void setProviderPresentationLink(IBPage page) {
		_presentationPage = page;
	}

	public IBPage getProviderPresentationLink() {
		return _presentationPage;
	}

	private Collection getSchoolTypes(IWContext iwc, String category) {
		try {
			SchoolTypeBusiness sBuiz = (SchoolTypeBusiness) IBOLookup.getServiceInstance(iwc, SchoolTypeBusiness.class);
			return sBuiz.findAllSchoolTypesInCategory(category);
		}
		catch (Exception ex) {

		}
		return null;
	}

	/*	private Collection getAreasByType(IWContext iwc, Collection schoolTypes) {
			try {
				SchoolAreaBusiness saBuiz = (SchoolAreaBusiness) IBOLookup.getServiceInstance(iwc, SchoolAreaBusiness.class);
				return saBuiz.findAllSchoolAreasByType(type);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}*/

	private Collection getAreas(IWContext iwc) {
		try {
			SchoolAreaBusiness sBuiz = (SchoolAreaBusiness) IBOLookup.getServiceInstance(iwc, SchoolAreaBusiness.class);
			return sBuiz.findAllSchoolAreasByTypes(_schoolTypes);
		}
		catch (Exception ex) {
		}

		return null;
	}

	private Collection getProviders(IWContext iwc, int area_id) {
		try {
			SchoolBusiness sBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
			//@todo Remove hardcoding
			return sBuiz.findAllSchoolsByAreaAndTypes(area_id,this._schoolTypes);
		}
		catch (Exception ex) {
		}

		return null;
	}

	private DropdownMenu getAreaDrop(IWContext iwc, String name) {
			DropdownMenu drp = new DropdownMenu(name);
			drp.addMenuElement("-1", _iwrb.getLocalizedString("cca_area", "Area"));
			Iterator iter = _areas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				drp.addMenuElement(area.getPrimaryKey().toString(), area.getName());
			}

			return drp;
//		DropdownMenu drp = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
//		drp.addMenuElement("-1", iwrb.getLocalizedString("school.school_type_select", "School type select"));
//		Iterator iter = schoolTypes.iterator();
//		boolean changeValues = false;
//		
//		while (iter.hasNext()) {
//			SchoolType type = (SchoolType) iter.next();
//			drp.addMenuElement(type.getPrimaryKey().toString(), type.getSchoolTypeName());
//		}
//		
//		return drp;
	}


//	private DropdownMenu getAreaDrop(String name) {
//		try {
//			DropdownMenu drp = new DropdownMenu(name);
//			drp.addMenuElement("-1", _iwrb.getLocalizedString("cca_area", "Area"));
//			Iterator iter = _areas.iterator();
//			while (iter.hasNext()) {
//				SchoolArea area = (SchoolArea) iter.next();
//				drp.addMenuElement(area.getPrimaryKey().toString(), area.getName());
//			}
//
//			return drp;
//		}
//		catch (java.rmi.RemoteException e) {
//			return null;
//		}
//	}
//
//	private DropdownMenu getProviderDrop(String name) {
//		try {
//			DropdownMenu drp = new DropdownMenu(name);
//			drp.addMenuElement("-1", _iwrb.getLocalizedString("cca_provider", "Provider"));
//			Iterator iter = _providers.iterator();
//			while (iter.hasNext()) {
//				School provider = (School) iter.next();
//				drp.addMenuElement(provider.getPrimaryKey().toString(), provider.getName());
//			}
//
//			return drp;
//		}
//		catch (java.rmi.RemoteException e) {
//			return null;
//		}
//	}

	private CheckBusiness getCheckBusiness(IWContext iwc) throws Exception {
		return (CheckBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CheckBusiness.class);
	}

	private ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}

	private boolean checkParameters(IWContext iwc) {
		for (int i = 0; i < 5; i++) {
			_valProvider[i] = iwc.isParameterSet(PARAM_PROVIDER + "_" + (i + 1)) ? Integer.parseInt(iwc.getParameter(PARAM_PROVIDER + "_" + (i + 1))) : -1;
			_valArea[i] = iwc.isParameterSet(PARAM_AREA + "_" + (i + 1)) ? Integer.parseInt(iwc.getParameter(PARAM_PROVIDER + "_" + (i + 1))) : -1;
		}
		_valDate = iwc.getParameter(PARAM_DATE);
		/**
		 * @todo Setja inn tékk á þessum breytum
		 */

		return true;
	}

	private String getFilterCallerScript(String areaName, String schoolName) {
		StringBuffer script = new StringBuffer("changeFilter(");
		script.append(1);
		script.append(",");
		script.append("findObj('").append(areaName).append("')");
		script.append(",");
		script.append("findObj('").append(schoolName).append("')");
		script.append(")");
		return script.toString();
	}

	private String getFilterScript(IWContext iwc) throws java.rmi.RemoteException {
		StringBuffer s = new StringBuffer();
		s.append("function changeFilter(index,area,school){").append(" \n\t");
		s.append("var areaSelect = area;").append(" \n\t");
		s.append("var schoolSelect = school;").append(" \n\t");
		s.append("var selected = 0;").append(" \n\t");
		s.append("if(index == 1){").append(" \n\t\t");
		s.append("selected = areaSelect.options[areaSelect.selectedIndex].value;").append("\n\t\t");
		s.append("schoolSelect.options.length = 0;").append("\n\t\t");
		s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
		s.append(_iwrb.getLocalizedString("choose_school", "Choose School")).append("\",\"-1\",true,true);").append("\n\t");
		s.append("} else if(index == 2){").append("\n\t\t");
		s.append("selected = schoolSelect.options[schoolSelect.selectedIndex].value;").append("\n\t");
		s.append("}").append("\n\t\t\t");

		// Data Filling ::

//		StringBuffer t = new StringBuffer("if(index==1){\n\t");
//		StringBuffer a = new StringBuffer("else if(index==2){\n\t");
		StringBuffer t = new StringBuffer();
		StringBuffer a = new StringBuffer("if(index==1){\n\t");
		StringBuffer c = new StringBuffer("else if(index==2){\n\t");

//		Collection Types = _schoolType;
//		if (Types != null && !Types.isEmpty()) {
//			Iterator iter = Types.iterator();
//			Integer type;
			SchoolArea area;
			School school;
//			Collection areas;
			Collection schools;
//			// iterate through schooltypes
//			while (iter.hasNext()) {
//				type = (Integer) iter.next();

				//System.err.println("checking type "+tPK.toString());
//				areas = getAreas(iwc); //getSchoolAreasWithType(iwc, tPK.intValue());
				if (_areas != null && !_areas.isEmpty()) {
					Iterator iter2 = _areas.iterator();
//					t.append("if(selected == \"").append(type.toString()).append("\"){").append("\n\t\t");

					Hashtable aHash = new Hashtable();

					// iterate through areas whithin types
					while (iter2.hasNext()) {
						area = (SchoolArea) iter2.next();
						Integer aPK = (Integer) area.getPrimaryKey();
						// System.err.println("checking area "+aPK.toString());
						if (!aHash.containsKey(aPK)) {
							aHash.put(aPK, aPK);
							schools = this.getProviders(iwc, aPK.intValue()); //getSchoolByAreaAndType(iwc, aPK.intValue(), tPK.intValue());
							if (schools != null) {
								Iterator iter3 = schools.iterator();
								a.append("if(selected == \"").append(aPK.toString()).append("\"){").append("\n\t\t");
								Hashtable hash = new Hashtable();
								// iterator through schools whithin area and type
								while (iter3.hasNext()) {
									school = (School) iter3.next();
									String pk = school.getPrimaryKey().toString();
									//System.err.println("checking school "+pk.toString());
									if (!hash.containsKey(pk)) {
										a.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
										a.append(school.getSchoolName()).append("\",\"");
										a.append(pk).append("\");\n\t\t");
										hash.put(pk, pk);
									}

								}
								a.append("}\n\t\t");
							}
						}
						else {
							System.err.println("shools empty");
						}
						t.append("areaSelect.options[areaSelect.options.length] = new Option(\"");
						t.append(area.getSchoolAreaName()).append("\",\"");
						t.append(area.getPrimaryKey().toString()).append("\");").append("\n\t\t");

					}
//					t.append("}\n\t");
				}
				else
					System.err.println("areas empty");
//			}
//		}
//		else
//			System.err.println("types empty");

		s.append("\n\n");

//		t.append("\n\t }");
		a.append("\n\t }");
		c.append("\n\t }");

//		s.append(t.toString());
		s.append(a.toString());
		s.append(c.toString());
		s.append("\n}");

		return s.toString();
	}

	public String getSchoolCheckScript() {
		StringBuffer s = new StringBuffer();
		s.append("\nfunction checkApplication(){\n\t");
		s.append("\n\t var dropOne = ").append("findObj('").append(PARAM_PROVIDER+"_1").append("');");
		s.append("\n\t var dropTwo = ").append("findObj('").append(PARAM_PROVIDER+"_2").append("');");
		s.append("\n\t var dropThree = ").append("findObj('").append(PARAM_PROVIDER+"_3").append("');");
		s.append("\n\t var dropFour = ").append("findObj('").append(PARAM_PROVIDER+"_4").append("');");
		s.append("\n\t var dropFive = ").append("findObj('").append(PARAM_PROVIDER+"_5").append("');");

		s.append("\n\t var one = 0;");
		s.append("\n\t var two = 0;");
		s.append("\n\t var three = 0;");
		s.append("\n\t var four = 0;");
		s.append("\n\t var five = 0;");

		s.append("\n\n\t if (dropOne.selectedIndex > 0) one = dropOne.options[dropOne.selectedIndex].value;");
		s.append("\n\t if (dropTwo.selectedIndex > 0) two = dropTwo.options[dropTwo.selectedIndex].value;");
		s.append("\n\t if (dropThree.selectedIndex > 0) three = dropThree.options[dropThree.selectedIndex].value;");
		s.append("\n\t if (dropFour.selectedIndex > 0) four = dropFour.options[dropFour.selectedIndex].value;");
		s.append("\n\t if (dropFive.selectedIndex > 0) five = dropFive.options[dropFive.selectedIndex].value;");

		s.append("\n\t if(one > 0 && two > 0 && three > 0 && four > 0 && five > 0){");
		s.append("\n\t if(one == two || one == three || one == four || one == five){");
		String msg = _iwrb.getLocalizedString("school_school.must_not_be_the_same", "Please do not choose the same school more than once");
		s.append("\n\t\t\t alert('").append(msg).append("');");
		s.append("\n\t\t\t return false;");
		s.append("\n\t\t }");
		s.append("\n\t if(two == three || two == four || two == five){");
		msg = _iwrb.getLocalizedString("school_school.must_not_be_the_same", "Please do not choose the same school more than once");
		s.append("\n\t\t\t alert('").append(msg).append("');");
		s.append("\n\t\t\t return false;");
		s.append("\n\t\t }");
		s.append("\n\t if(three == four || three == five){");
		msg = _iwrb.getLocalizedString("school_school.must_not_be_the_same", "Please do not choose the same school more than once");
		s.append("\n\t\t\t alert('").append(msg).append("');");
		s.append("\n\t\t\t return false;");
		s.append("\n\t\t }");
		s.append("\n\t if(four == five){");
		msg = _iwrb.getLocalizedString("school_school.must_not_be_the_same", "Please do not choose the same school more than once");
		s.append("\n\t\t\t alert('").append(msg).append("');");
		s.append("\n\t\t\t return false;");
		s.append("\n\t\t }");
		s.append("\n\t }");
		s.append("\n\t else{");
		msg = _iwrb.getLocalizedString("school_school.must_fill_out", "Please fill out all choices");
		s.append("\n\t\t alert('").append(msg).append("');");
		s.append("\n\t\t return false;");
		s.append("\n\t }");

		s.append("\n\t\t findObj('").append(prmAction).append("').value='true';");
		s.append("\n\t return true;");
		s.append("\n}\n");
		return s.toString();
	}

	private DropdownMenu getDropdown(String name, String value, String area, String school, String firstElement) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElementFirst("-1", firstElement);
		menu.setOnChange(getFilterCallerScript(area, school));
		return menu;
	}
}