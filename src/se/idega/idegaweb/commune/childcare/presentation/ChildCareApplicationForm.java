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
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.Converter;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

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
	private final static String PROVIDER = "cca_provider";
	private final static String AREA = "cca_area";
	private final static String CARE_FROM = "cca_care_from";
	private final static String WANT_PROVIDER = "cca_want_provider";
	private final static String WANT_PROVIDER_LINK = "cca_want_provider_link";

	private final static String PARAM_FORM_SUBMIT = "cca_submit";
	private final static String PARAM_DATE = "cca_date";
	private final static String PARAM_DATE_1 = "cca_date_1";
	private final static String PARAM_DATE_2 = "cca_date_2";
	private final static String PARAM_DATE_3 = "cca_date_3";
	private final static String PARAM_DATE_4 = "cca_date_4";
	private final static String PARAM_DATE_5 = "cca_date_5";
	private final static String PARAM_AREA = "cca_area";
	private final static String PARAM_AREA_1 = "cca_area_1";
	private final static String PARAM_AREA_2 = "cca_area_2";
	private final static String PARAM_AREA_3 = "cca_area_3";
	private final static String PARAM_AREA_4 = "cca_area_4";
	private final static String PARAM_AREA_5 = "cca_area_5";
	private final static String PARAM_PROVIDER = "cca_provider";
	private final static String PARAM_PROVIDER_1 = "cca_provider_1";
	private final static String PARAM_PROVIDER_2 = "cca_provider_2";
	private final static String PARAM_PROVIDER_3 = "cca_provider_3";
	private final static String PARAM_PROVIDER_4 = "cca_provider_4";
	private final static String PARAM_PROVIDER_5 = "cca_provider_5";
	private final static String PARAM_FORM_NAME = "cca_form";
	private final static String PARAM_TYPE_DROP = "cca_type_drop";
	private final static String PARAM_CHECK_ID = "cca_check_id";
	private final static String PARAM_GUARDIAN_APPROVES = "cca_guardian_approves";

	private final static String NOT_LOGGED_IN = "cca_not_logged_in";

	protected User _user = null;
	protected IBPage _presentationPage = null;
	protected IWBundle _iwb;
	protected IWResourceBundle _iwrb;

	protected int valProvider1 = -1;
	protected int valProvider2 = -1;
	protected int valProvider3 = -1;
	protected int valProvider4 = -1;
	protected int valProvider5 = -1;
	protected int valArea1 = -1;
	protected int valArea2 = -1;
	protected int valArea3 = -1;
	protected int valArea4 = -1;
	protected int valArea5 = -1;
	protected String valDate1 = null;
	protected String valDate2 = null;
	protected String valDate3 = null;
	protected String valDate4 = null;
	protected String valDate5 = null;
	protected int valType = -1;
	protected boolean valCustodianAgree = false;
	
	/*	protected boolean valSendCatalogue = false;
		protected boolean valSixyearCare = false;
		protected boolean valAutoAssign = false;
		protected boolean valSchoolChange = false;
		protected boolean valCustodiansAgree = false;
		protected String valMessage = "";
		protected String valLanguage = "";
		protected int valFirstSchool = -1;
		protected int valSecondSchool = -1;
		protected int valThirdSchool = -1;
		protected int valFirstArea = -1;
		protected int valSecondArea = -1;
		protected int valThirdArea = -1;
		protected int valPreGrade = -1;
		protected int valPreType = -1;
		protected int valPreArea = -1;
		protected int valPreSchool = -1;
		protected int valType = -1;
		protected int childId = -1;
		protected boolean showAgree = false;*/

	protected Collection _schoolTypes = null;
	protected Collection _areas = null;
	protected Collection _providers = null;

/*	private String prefix = "sch_app_";
	private String prmPreSchool = prefix + "pre_scl";
	private String prmPreArea = prefix + "pre_ara";
	private String prmPreType = prefix + "pre_typ";
	private String prmType = prefix + "cho_typ";
	private String prmPreGrade = prefix + "pre_grd";
	private String prmFirstSchool = prefix + "fst_scl";
	private String prmSecondSchool = prefix + "snd_scl";
	private String prmThirdSchool = prefix + "trd_scl";
	private String prmFirstArea = prefix + "fst_ara";
	private String prmSecondArea = prefix + "snd_ara";
	private String prmThirdArea = prefix + "trd_ara";*/

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		_iwb = getBundle(iwc);
		_iwrb = getResourceBundle(iwc);

		if (iwc.getUser() != null)
			_user = Converter.convertToNewUser(iwc.getUser());
		else
			_user = null;

		if (_user != null) {
			_schoolTypes = getSchoolTypes(iwc, "CHILDCARE");
			_areas = getAreas(iwc, "CHILDCARE");
			_providers = getProviders(iwc, "CHILDCARE");
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
			checks = getCheckBusiness(iwc).findAllChecksByUser(_user);
		}
		catch (RemoteException e) {
		}
		catch (Exception e) {
		}
		
		if (checks != null && !checks.isEmpty()) {
			Iterator it = checks.iterator();
			while (it.hasNext()) {
				Check check = (Check)it.next();
				User child = null;
				try {
					child = getCheckBusiness(iwc).getUserById(check.getChildId());
				}
				catch (RemoteException e) {
				}
				catch (Exception e) {
				}
				
				Link link = null;
				try {
					if (child != null) {
						link = new Link(child.getName());
						if (getResponsePage() != null)
							link.setPage(getResponsePage());
						link.addParameter(PARAM_CHECK_ID, ((Integer) check.getPrimaryKey()).toString());						
					}
				}
				catch (RemoteException e) {					
				}
				
				if (link != null) {
					add(link);
					add(Text.BREAK);
				}
			}			
		}
		else {
			add(getErrorText(localize(ERROR_NO_CHECKS,"This user has no checks")));
		}			
	}

	private void viewForm(IWContext iwc) {
		Form form = new Form();
		form.setName(PARAM_FORM_NAME);

		String checkId = iwc.getParameter(PARAM_CHECK_ID);
		form.addParameter(PARAM_CHECK_ID,checkId);
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
			catch (RemoteException e) {
			}			
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

		Table inputTable = new Table(5, 12);
		//		inputTable.setWidth("100%");
		inputTable.setCellspacing(2);
		inputTable.setCellpadding(4);
		inputTable.setAlignment(5, 12, "right");
		inputTable.setColor(getBackgroundColor());
		inputTable.mergeCells(1, 1, 5, 1);
		inputTable.mergeCells(1, 2, 5, 2);
		inputTable.mergeCells(1, 3, 5, 3);
		inputTable.mergeCells(1, 4, 5, 4);
		inputTable.mergeCells(1, 5, 5, 5);
		inputTable.mergeCells(1, 11, 5, 11);
		inputTable.mergeCells(1, 12, 5, 12);

		inputTable.add(getHeader(localize(SUBJECT, "Application subject")), 1, 1);
		inputTable.add(getHeader(localize(PROVIDERS, "Providers")), 1, 4);

		CheckBox box = new CheckBox(PARAM_GUARDIAN_APPROVES, "true");
		inputTable.add(box, 1, 11);
		inputTable.add(getSmallText(localize(PARAM_GUARDIAN_APPROVES, "Legal guardian approves")), 1, 11);
		inputTable.setAlignment(1, 11, "Left");

		/*		String url = "javascript:MySubmit()";
				Link save = new Link(getBundle(iwc).getImageButton(localize(PARAM_FORM_SUBMIT, "Submit application")), url);
				inputTable.add(save, 1, 12);
				inputTable.add(new HiddenInput(PARAM_FORM_SUBMIT, "false"));
				inputTable.setAlignment(1, 12, "Right");*/

		SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, localize(PARAM_FORM_SUBMIT, "Submit application"));
		submit.setAsImageButton(true);
		inputTable.setAlignment(1, 12, "Right");
		inputTable.add(submit, 1, 12);

		String provider = localize(PROVIDER, "Provider");
		String from = localize(CARE_FROM, "From");
		Text providerText = null;
		Text fromText = getSmallText(from);

		for (int i = 1; i < 6; i++) {
			DropdownMenu areaDrop = getAreaDrop(PARAM_AREA + "_" + i);
			DropdownMenu providerDrop = getProviderDrop(PARAM_PROVIDER + "_" + i);
			DateInput date = new DateInput(PARAM_DATE + "_" + i);
			date.setToCurrentDate();
			areaDrop.setAttribute("style", getSmallTextFontStyle());
			providerDrop.setAttribute("style", getSmallTextFontStyle());
			date.setStyleAttribute("style", getSmallTextFontStyle());
			providerText = getSmallText(provider + " " + i);
			inputTable.add(providerText, 1, 5 + i);
			inputTable.add(fromText, 4, 5 + i);
			inputTable.add(areaDrop, 2, 5 + i);
			inputTable.add(providerDrop, 3, 5 + i);
			inputTable.add(date, 5, 5 + i);
		}

		/*		Page p = this.getParentPage();
				if (p != null) {
					Script S = p.getAssociatedScript();
					Script F = new Script();
					S.addFunction("initFilter", getInitFilterScript());
					S.addFunction("submitFunction", getMySubmitScript());
					S.addFunction("checkApplication", getSchoolCheckScript());
		
					try {
						S.addFunction("changeFilter", getFilterScript(iwc));
					}
					catch (RemoteException e) {
					}
					
					if (valPreType > 0 || valPreArea > 0 || valPreSchool > 0) {
						F.addFunction("f1", getInitFilterCallerScript(iwc, prmPreType, prmPreArea, prmPreSchool, valPreType, valPreArea, valPreSchool));
					}
					if (valType > 0) {
						if (valFirstArea > 0 || valFirstSchool > 0)
							F.addFunction("f2", getInitFilterCallerScript(iwc, prmType, prmFirstArea, prmFirstSchool, valType, valFirstArea, valFirstSchool));
						if (valSecondArea > 0 || valSecondSchool > 0)
							F.addFunction("f3", getInitFilterCallerScript(iwc, prmType, prmSecondArea, prmSecondSchool, valType, valSecondArea, valSecondSchool));
						if (valThirdArea > 0 || valThirdSchool > 0)
							F.addFunction("f4", getInitFilterCallerScript(iwc, prmType, prmThirdArea, prmThirdSchool, valType, valThirdArea, valThirdSchool));
		
					}
					inputTable.add(F, 1, inputTable.getColumns());
				}*/

		DropdownMenu type = getTypeDrop(PARAM_TYPE_DROP);
		type.setAttribute("style", getSmallTextFontStyle());
		inputTable.add(type, 1, 2);

		add(nameTable1);
		add(nameTable2);
		add(Text.BREAK);
		form.add(inputTable);
		add(form);
	}

	private void submitForm(IWContext iwc) {
		String checkId = iwc.getParameter(this.PARAM_CHECK_ID);
		System.out.println("Check_id = " + checkId);
		add("Done");
	}

	public void setProviderPresentationLink(IBPage page) {
		_presentationPage = page;
	}

	public IBPage getProviderPresentationLink() {
		return _presentationPage;
	}

	/*	private String getMySubmitScript() {
			StringBuffer s = new StringBuffer();
			s.append("\n function MySubmit(){");
			s.append("\n\t if(checkApplication()) {");
			s.append("\n\t\t document.").append(PARAM_FORM_NAME).append(".elements['").append(PARAM_FORM_SUBMIT).append("'].value='true';");
			s.append("\n\t\t document.").append(PARAM_FORM_NAME).append(".submit();");
			s.append("\n\t }");
			s.append("\n}\n");
	
			return s.toString();
		}
	
		private String getInitFilterCallerScript(IWContext iwc, String typeName, String areaName, String schoolName, int typeSel, int areaSel, int schoolSel) {
			StringBuffer script = new StringBuffer("initFilter(");
			script.append("document.forms['").append(PARAM_FORM_NAME).append("'].elements['");
			script.append(typeName);
			script.append("'],");
			script.append("document.forms['").append(PARAM_FORM_NAME).append("'].elements['");
			script.append(areaName);
			script.append("'],");
			script.append("document.forms['").append(PARAM_FORM_NAME).append("'].elements['");
			script.append(schoolName);
			script.append("'],");
			script.append(typeSel);
			script.append(",");
			script.append(areaSel);
			script.append(",");
			script.append(schoolSel);
			script.append(")");
	
			return script.toString();
		}
	
		public String getInitFilterScript() {
			StringBuffer s = new StringBuffer();
			s.append("function initFilter(type,area,school,type_sel,area_sel,school_sel){ \n  ");
			s.append("changeFilter( 1 ,type,area,school); \n  ");
			s.append("type.selectedIndex = type_sel; \n  ");
			s.append("changeFilter(2,type,area,school); \n  ");
			s.append("area.selectedIndex = area_sel; \n  ");
			s.append("changeFilter(3,type,area,school); \n ");
			s.append("school.selectedIndex = school_sel; \n}");
	
			return s.toString();
		}
		
		public String getSchoolCheckScript() {
			StringBuffer s = new StringBuffer();
			s.append("\nfunction checkApplication(){\n\t");
			s.append("\n\t var currSchool = ").append("document.").append(PARAM_FORM_NAME).append(".elements['").append(prmPreSchool).append("'];");
			s.append("\n\t var dropOne = ").append("document.").append(PARAM_FORM_NAME).append(".elements['").append(prmFirstSchool).append("'];");
			s.append("\n\t var dropTwo = ").append("document.").append(PARAM_FORM_NAME).append(".elements['").append(prmSecondSchool).append("'];");
			s.append("\n\t var dropThree = ").append("document.").append(PARAM_FORM_NAME).append(".elements['").append(prmThirdSchool).append("'];");
			s.append("\n\t var gradeDrop = ").append("document.").append(PARAM_FORM_NAME).append(".elements['").append(prmPreGrade).append("'];");
			s.append("\n\t var one = ").append("dropOne.options[dropOne.selectedIndex].value;");
			s.append("\n\t var two = ").append("dropTwo.options[dropTwo.selectedIndex].value;");
			s.append("\n\t var  three = ").append("dropThree.options[dropThree.selectedIndex].value;");
			s.append("\n\t var  year = ").append("gradeDrop.options[gradeDrop.selectedIndex].value;");
			s.append("\n\t var  school = ").append("currSchool.options[currSchool.selectedIndex].value;");
	
			// current school check
			s.append("\n\t if(school <= 0){");
			String msg1 = _iwrb.getLocalizedString("school_choice.must_set_current_school", "You must provide current shool");
			s.append("\n\t\t\t alert('").append(msg1).append("');");
			s.append("\n\t\t ");
			s.append("\n\t }");
	
			// year check
			s.append("\n\t else if(year <= 0 && school > 0){");
			String msg2 = _iwrb.getLocalizedString("school_choice.must_set_grade", "You must provide current shool year");
			s.append("\n\t\t\t alert('").append(msg2).append("');");
			s.append("\n\t\t ");
			s.append("\n\t }");
	
			// schoolchoices checked
			s.append("\n\t else if(one && two && three){");
			s.append("\n\t if(one == two || two == three || three == one){");
			String msg = _iwrb.getLocalizedString("school_school.must_not_be_the_same", "Please do not choose the same school more than once");
			s.append("\n\t\t\t alert('").append(msg).append("');");
			s.append("\n\t\t\t return false;");
			s.append("\n\t\t }");
			s.append("\n\t }");
			s.append("\n\t else{");
			s.append("\n\t\t alert('").append("no choices").append("');");
			s.append("\n\t\t return false;");
			s.append("\n\t }");
			s.append("\n\t return true;");
			s.append("\n}\n");
		
			return s.toString();
		}	
		
		private String getFilterScript(IWContext iwc) throws java.rmi.RemoteException {
			StringBuffer s = new StringBuffer();
			s.append("function changeFilter(index,type,area,school){").append(" \n\t");
			s.append("var typeSelect = type;").append(" \n\t");
			s.append("var areaSelect = area;").append(" \n\t");
			s.append("var schoolSelect = school;").append(" \n\t");
			s.append("var selected = 0;").append(" \n\t");
			s.append("if(index == 1){").append(" \n\t\t");
			s.append("selected = typeSelect.options[typeSelect.selectedIndex].value;").append("\n\t\t");
			s.append("areaSelect.options.length = 0;").append("\n\t\t");
			s.append("schoolSelect.options.length = 0;").append("\n\t\t");
			s.append("areaSelect.options[areaSelect.options.length] = new Option(\"");
			s.append(_iwrb.getLocalizedString("choose_area", "Choose Area")).append("\",\"-1\",true,true);").append("\n\t\t");
			s.append("}else if(index == 2){").append(" \n\t\t");
			s.append("selected = areaSelect.options[areaSelect.selectedIndex].value;").append("\n\t\t");
			s.append("schoolSelect.options.length = 0;").append("\n\t\t");
			s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
			s.append(_iwrb.getLocalizedString("choose_school", "Choose School")).append("\",\"-1\",true,true);").append("\n\t");
			s.append("} else if(index == 3){").append("\n\t\t");
			s.append("selected = schoolSelect.options[schoolSelect.selectedIndex].value;").append("\n\t");
			s.append("}").append("\n\t\t\t");
	
			// Data Filling ::
	
			StringBuffer t = new StringBuffer("if(index==1){\n\t");
			StringBuffer a = new StringBuffer("else if(index==2){\n\t");
			StringBuffer c = new StringBuffer("else if(index==3){\n\t");
	
			Collection Types = _schoolTypes;
			if (Types != null && !Types.isEmpty()) {
				Iterator iter = Types.iterator();
				SchoolType type;
				SchoolArea area;
				School school;
				Collection areas;
				Collection schools;
				// iterate through schooltypes
				while (iter.hasNext()) {
					type = (SchoolType) iter.next();
	
					Integer tPK = (Integer) type.getPrimaryKey();
					//System.err.println("checking type "+tPK.toString());
					areas = getSchoolAreasWithType(iwc, tPK.intValue());
					if (areas != null && !areas.isEmpty()) {
						Iterator iter2 = areas.iterator();
						t.append("if(selected == \"").append(tPK.toString()).append("\"){").append("\n\t\t");
	
						Hashtable aHash = new Hashtable();
	
						// iterate through areas whithin types
						while (iter2.hasNext()) {
							area = (SchoolArea) iter2.next();
							Integer aPK = (Integer) area.getPrimaryKey();
							// System.err.println("checking area "+aPK.toString());
							if (!aHash.containsKey(aPK)) {
								aHash.put(aPK, aPK);
								schools = getSchoolByAreaAndType(iwc, aPK.intValue(), tPK.intValue());
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
						t.append("}\n\t");
					}
					else {
						System.err.println("areas empty");
					}
				}
			}
			else {
				System.err.println("types empty");
			}
	
			s.append("\n\n");
	
			t.append("\n\t }");
			a.append("\n\t }");
			c.append("\n\t }");
	
			s.append(t.toString());
			s.append(a.toString());
			s.append(c.toString());
			s.append("\n}");
	
			return s.toString();
		}*/

	/*	private Collection getSchoolAreasWithType(IWContext iwc, int type) {
			try {
				SchoolAreaBusiness saBuiz = (SchoolAreaBusiness) IBOLookup.getServiceInstance(iwc, SchoolAreaBusiness.class);
				return saBuiz.findAllSchoolAreasByType(type);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}	
		
		private Collection getSchoolByAreaAndType(IWContext iwc, int area, int type) {
			try {
				SchoolBusiness sBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
				return sBuiz.findAllSchoolsByAreaAndType(area, type);
	
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}*/

	private Collection getSchoolTypes(IWContext iwc, String category) {
		try {
			SchoolTypeBusiness sBuiz = (SchoolTypeBusiness) IBOLookup.getServiceInstance(iwc, SchoolTypeBusiness.class);
			return sBuiz.findAllSchoolTypesInCategory(category);
		}
		catch (Exception ex) {

		}
		return null;
	}

	private Collection getAreas(IWContext iwc, String category) {
		try {
			SchoolAreaBusiness sBuiz = (SchoolAreaBusiness) IBOLookup.getServiceInstance(iwc, SchoolAreaBusiness.class);
			return sBuiz.findAllSchoolAreas();
		}
		catch (Exception ex) {

		}
		System.out.println("Returning null areas");
		return null;
	}

	private Collection getProviders(IWContext iwc, String category) {
		try {
			SchoolBusiness sBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
			//@todo Remove hardcoding
			return sBuiz.findAllSchoolsByType(1);
		}
		catch (Exception ex) {

		}
		System.out.println("Returning null schools");
		return null;
	}

	private DropdownMenu getTypeDrop(String name) {
		try {
			DropdownMenu drp = new DropdownMenu(name);
			drp.addMenuElement("-1", _iwrb.getLocalizedString("cca_type", "School type"));
			Iterator iter = _schoolTypes.iterator();
			while (iter.hasNext()) {
				SchoolType type = (SchoolType) iter.next();
				drp.addMenuElement(type.getPrimaryKey().toString(), type.getSchoolTypeName());
			}

			return drp;
		}
		catch (java.rmi.RemoteException e) {
			return null;
		}
	}

	private DropdownMenu getAreaDrop(String name) {
		try {
			DropdownMenu drp = new DropdownMenu(name);
			drp.addMenuElement("-1", _iwrb.getLocalizedString("cca_area", "Area"));
			Iterator iter = _areas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				drp.addMenuElement(area.getPrimaryKey().toString(), area.getName());
			}

			return drp;
		}
		catch (java.rmi.RemoteException e) {
			return null;
		}
	}

	private DropdownMenu getProviderDrop(String name) {
		try {
			DropdownMenu drp = new DropdownMenu(name);
			drp.addMenuElement("-1", _iwrb.getLocalizedString("cca_provider", "Provider"));
			Iterator iter = _providers.iterator();
			while (iter.hasNext()) {
				School provider = (School) iter.next();
				drp.addMenuElement(provider.getPrimaryKey().toString(), provider.getName());
			}

			return drp;
		}
		catch (java.rmi.RemoteException e) {
			return null;
		}
	}
	
	private CheckBusiness getCheckBusiness(IWContext iwc) throws Exception {
		return (CheckBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CheckBusiness.class);
	}	
	
	private boolean checkParameters(IWContext iwc) {
		valProvider1 = iwc.isParameterSet(PARAM_PROVIDER_1)?Integer.parseInt(iwc.getParameter(PARAM_PROVIDER_1)):-1;
		valProvider2 = iwc.isParameterSet(PARAM_PROVIDER_2)?Integer.parseInt(iwc.getParameter(PARAM_PROVIDER_2)):-1;
		valProvider3 = iwc.isParameterSet(PARAM_PROVIDER_3)?Integer.parseInt(iwc.getParameter(PARAM_PROVIDER_3)):-1;
		valProvider4 = iwc.isParameterSet(PARAM_PROVIDER_4)?Integer.parseInt(iwc.getParameter(PARAM_PROVIDER_4)):-1;
		valProvider5 = iwc.isParameterSet(PARAM_PROVIDER_5)?Integer.parseInt(iwc.getParameter(PARAM_PROVIDER_5)):-1;
		valArea1 = iwc.isParameterSet(PARAM_AREA_1)?Integer.parseInt(iwc.getParameter(PARAM_AREA_1)):-1;
		valArea2 = iwc.isParameterSet(PARAM_AREA_2)?Integer.parseInt(iwc.getParameter(PARAM_AREA_2)):-1;
		valArea3 = iwc.isParameterSet(PARAM_AREA_3)?Integer.parseInt(iwc.getParameter(PARAM_AREA_3)):-1;
		valArea4 = iwc.isParameterSet(PARAM_AREA_4)?Integer.parseInt(iwc.getParameter(PARAM_AREA_4)):-1;
		valArea5 = iwc.isParameterSet(PARAM_AREA_5)?Integer.parseInt(iwc.getParameter(PARAM_AREA_5)):-1;
		valDate1 = iwc.getParameter(PARAM_DATE_1);
		valDate2 = iwc.getParameter(PARAM_DATE_2);
		valDate3 = iwc.getParameter(PARAM_DATE_3);
		valDate4 = iwc.getParameter(PARAM_DATE_4);
		valDate5 = iwc.getParameter(PARAM_DATE_5);
		valType = -1;
		valCustodianAgree = iwc.isParameterSet(PARAM_GUARDIAN_APPROVES);
		/**
		 * @todo Setja inn tékk á þessum breytum
		 */

		return true;	
	}
}