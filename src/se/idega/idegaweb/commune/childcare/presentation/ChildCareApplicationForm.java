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
import com.idega.builder.presentation.IBFileChooserWindow;
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

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
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

	protected User _user = null;
	protected IBPage _presentationPage = null;
	protected IWBundle _iwb;
	protected IWResourceBundle _iwrb;

	protected int _valProvider[] = { -1, -1, -1, -1, -1 };
	protected int _valArea[] = { -1, -1, -1, -1, -1 };
	protected String _valDate[] = { null, null, null, null, null };

	protected Collection _areas = null;
	protected Collection _providers = null;

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		_iwb = getBundle(iwc);
		_iwrb = getResourceBundle(iwc);

		_user = iwc.getCurrentUser();

		if (_user != null) {
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

			//try {
				form.addParameter(PARAM_CHILD_ID, ((Integer) child.getPrimaryKey()).intValue());
			//}
			//catch (RemoteException e) {
			//}
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

		Table inputTable = new Table(5, 9);
		inputTable.setCellspacing(2);
		inputTable.setCellpadding(4);
		inputTable.setAlignment(5, 9, "right");
		inputTable.setColor(getBackgroundColor());
		inputTable.mergeCells(1, 1, 5, 1);
		inputTable.mergeCells(1, 2, 5, 2);
		inputTable.mergeCells(1, 3, 5, 3);
		inputTable.mergeCells(1, 9, 5, 9);

		inputTable.add(getHeader(localize(PROVIDERS, "Providers")), 1, 2);

		SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, localize(PARAM_FORM_SUBMIT, "Submit application"));
		submit.setAsImageButton(true);
		inputTable.setAlignment(1, 9, "Right");
		inputTable.add(submit, 1, 9);

		String provider = localize(PARAM_PROVIDER, "Provider");
		String from = localize(CARE_FROM, "From");
		Text providerText = null;
		Text fromText = getSmallText(from);

		for (int i = 1; i < 6; i++) {
			DropdownMenu areaDrop = getAreaDrop(PARAM_AREA + "_" + i);
			DropdownMenu providerDrop = getProviderDrop(PARAM_PROVIDER + "_" + i);
			DateInput date = new DateInput(PARAM_DATE + "_" + i);
			date.setToCurrentDate();
			areaDrop.setAttribute("style", getSmallTextFontStyle());
			//			areaDrop.setToSubmit();
			providerDrop.setAttribute("style", getSmallTextFontStyle());
			date.setStyleAttribute("style", getSmallTextFontStyle());
			providerText = getSmallText(provider + " " + i);
			inputTable.add(providerText, 1, 3 + i);
			inputTable.add(fromText, 4, 3 + i);
			inputTable.add(areaDrop, 2, 3 + i);
			inputTable.add(providerDrop, 3, 3 + i);
			inputTable.add(date, 5, 3 + i);
		}

		add(nameTable1);
		add(nameTable2);
		add(Text.BREAK);
		form.add(inputTable);

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

				done = business.insertApplications(_user, _valProvider, _valDate, new Integer(checkId).intValue(), new Integer(childId).intValue(), subject, message);
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

	private Collection getAreas(IWContext iwc, String category) {
		try {
			SchoolAreaBusiness sBuiz = (SchoolAreaBusiness) IBOLookup.getServiceInstance(iwc, SchoolAreaBusiness.class);
			return sBuiz.findAllSchoolAreas();
		}
		catch (Exception ex) {
		}

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

		return null;
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
			_valDate[i] = iwc.getParameter(PARAM_DATE + "_" + (i + 1));
		}
		/**
		 * @todo Setja inn tékk á þessum breytum
		 */

		return true;
	}

	private String getFilterCallerScript(IWContext iwc, String areaName, String schoolName) {
		StringBuffer script = new StringBuffer("changeFilter(");
		//		script.append(Type);
		//		script.append(",");
		//		script.append("this.form.elements['");
		//		script.append(typeName);
		//		script.append("'],");
		script.append("this.form.elements['");
		script.append(areaName);
		script.append("'],");
		script.append("this.form.elements['");
		script.append(schoolName);
		script.append("'])");
		return script.toString();
	}

	private String getInitFilterCallerScript(IWContext iwc, String areaName, String schoolName, int areaSel, int schoolSel) {
		StringBuffer script = new StringBuffer("initFilter(");
		//		script.append("document.forms['").append(PARAM_FORM_NAME).append("'].elements['");
		//		script.append(typeName);
		//		script.append("'],");
		script.append("document.forms['").append(PARAM_FORM_NAME).append("'].elements['");
		script.append(areaName);
		script.append("'],");
		script.append("document.forms['").append(PARAM_FORM_NAME).append("'].elements['");
		script.append(schoolName);
		script.append("'],");
		//		script.append(typeSel);
		//		script.append(",");
		script.append(areaSel);
		script.append(",");
		script.append(schoolSel);
		script.append(")");
		return script.toString();
	}

	/*
	  private String getFilterScript(IWContext iwc)throws java.rmi.RemoteException{
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
	    s.append(iwrb.getLocalizedString("choose_area","Choose Area")).append("\",\"-1\",true,true);").append("\n\t\t");
	    //s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"Veldu skóla\",\"-1\",true,true);").append("\n\t");
	    s.append("}else if(index == 2){").append(" \n\t\t");
	    s.append("selected = areaSelect.options[areaSelect.selectedIndex].value;").append("\n\t\t");
	    s.append("schoolSelect.options.length = 0;").append("\n\t\t");
	    s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
	    s.append(iwrb.getLocalizedString("choose_school","Choose School")).append("\",\"-1\",true,true);").append("\n\t");
	    s.append("} else if(index == 3){").append("\n\t\t");
	    s.append("selected = schoolSelect.options[schoolSelect.selectedIndex].value;").append("\n\t");
	    s.append("}").append("\n\t\t\t");
	
	    // Data Filling ::
	
	    StringBuffer t = new StringBuffer("if(index==1){\n\t");
	    StringBuffer a = new StringBuffer("else if(index==2){\n\t");
	    StringBuffer c = new StringBuffer("else if(index==3){\n\t");
	
	    Collection Types = this.schoolTypes;
	    if(Types!=null && !Types.isEmpty()){
	      Iterator iter = Types.iterator();
	      SchoolType type;
	      SchoolArea area;
	      School school;
	      Collection areas;
	      Collection schools;
	      // iterate through schooltypes
	      while(iter.hasNext()){
	        type = (SchoolType) iter.next();
	
	        Integer tPK = (Integer) type.getPrimaryKey();
	        //System.err.println("checking type "+tPK.toString());
	        areas = getSchoolAreasWithType(iwc,tPK.intValue());
	        if(areas!=null && !areas.isEmpty()){
	          Iterator iter2 = areas.iterator();
	          t.append("if(selected == \"").append(tPK.toString()).append("\"){").append("\n\t\t");
	
	           Hashtable aHash = new Hashtable();
	
	          // iterate through areas whithin types
	          while(iter2.hasNext()) {
	            area = (SchoolArea) iter2.next();
	            Integer aPK = (Integer)area.getPrimaryKey();
	            // System.err.println("checking area "+aPK.toString());
	            if(!aHash.containsKey(aPK)){
	            	aHash.put(aPK,aPK);
		            schools = getSchoolByAreaAndType(iwc,aPK.intValue(),tPK.intValue());
		            if(schools!=null ){
		              Iterator iter3 = schools.iterator();
		              a.append("if(selected == \"").append(aPK.toString()).append("\"){").append("\n\t\t");
		              Hashtable hash = new Hashtable();
		              // iterator through schools whithin area and type
		              while(iter3.hasNext()){
		                school = (School) iter3.next();
		                String pk = school.getPrimaryKey().toString();
		                //System.err.println("checking school "+pk.toString());
		                if(!hash.containsKey(pk)){
			                a.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
			                a.append(school.getSchoolName()).append("\",\"");
			                a.append(pk).append("\");\n\t\t");
			                hash.put(pk,pk);
		                }
	
		              }
		              a.append("}\n\t\t");
		        	}
	            }
	            else{
	            System.err.println("shools empty");
	          }
	          t.append("areaSelect.options[areaSelect.options.length] = new Option(\"");
	          t.append(area.getSchoolAreaName()).append("\",\"");
	          t.append(area.getPrimaryKey().toString()).append("\");").append("\n\t\t");;
	
	          }
	          t.append("}\n\t");
	        }
	        else
	           System.err.println("areas empty");
	      }
	    }
	    else
	      System.err.println("types empty");
	
	
	    s.append("\n\n");
	
	
	    t.append("\n\t }");
	    a.append("\n\t }");
	    c.append("\n\t }");
	
	    s.append(t.toString());
	    s.append(a.toString());
	    s.append(c.toString());
	    s.append("\n}");
	
	    return s.toString();
	  }
	*/
	public String getInitFilterScript() {
		StringBuffer s = new StringBuffer();
		s.append("function initFilter(area,school,area_sel,school_sel){ \n  ");
		//		s.append("changeFilter( 1 ,type,area,school); \n  ");
		//		s.append("type.selectedIndex = type_sel; \n  ");
		s.append("changeFilter(1,area,school); \n  ");
		s.append("area.selectedIndex = area_sel; \n  ");
		s.append("changeFilter(2,area,school); \n ");
		s.append("school.selectedIndex = school_sel; \n}");
		return s.toString();
	}
	/*
		public String getSchoolCheckScript(){
		  StringBuffer s = new StringBuffer();
			s.append("\nfunction checkApplication(){\n\t");
			//s.append("\n\t\t alert('").append("checking choices").append("');");
			s.append("\n\t var currSchool = ").append("document.").append(prmForm).append(".elements['").append(prmPreSchool).append("'];");
			s.append("\n\t var dropOne = ").append("document.").append(prmForm).append(".elements['").append(prmFirstSchool).append("'];");
			s.append("\n\t var dropTwo = ").append("document.").append(prmForm).append(".elements['").append(prmSecondSchool).append("'];");
			s.append("\n\t var dropThree = ").append("document.").append(prmForm).append(".elements['").append(prmThirdSchool).append("'];");
			s.append("\n\t var gradeDrop = ").append("document.").append(prmForm).append(".elements['").append(prmPreGrade).append("'];");
			s.append("\n\t var one = ").append("dropOne.options[dropOne.selectedIndex].value;");
			s.append("\n\t var two = ").append("dropTwo.options[dropTwo.selectedIndex].value;");
			s.append("\n\t var  three = ").append("dropThree.options[dropThree.selectedIndex].value;");
			s.append("\n\t var  year = gradeDrop.options?").append("gradeDrop.options[gradeDrop.selectedIndex].value").append(":")
			.append("document.sch_app_the_frm.elements['").append(prmPreGrade).append("'].value;");
			s.append("\n\t var  school = currSchool.options?").append("currSchool.options[currSchool.selectedIndex].value").append(":")
			.append("document.sch_app_the_frm.elements['").append(prmPreSchool).append("'].value;");
			// current school check
			s.append("\n\t if(school <= 0){");
			String msg1 = iwrb.getLocalizedString("school_choice.must_set_current_school","You must provide current shool");
			s.append("\n\t\t\t alert('").append(msg1).append("');");
			s.append("\n\t\t ");
			s.append("\n\t }");
	
			// year check
			s.append("\n\t else if(year <= 0 && school > 0){");
			String msg2 = iwrb.getLocalizedString("school_choice.must_set_grade","You must provide current shool year");
			s.append("\n\t\t\t alert('").append(msg2).append("');");
			s.append("\n\t\t ");
			s.append("\n\t }");
	
			// schoolchoices checked
			s.append("\n\t else if(one && two && three){");
			s.append("\n\t if(one == two || two == three || three == one){");
			String msg = iwrb.getLocalizedString("school_school.must_not_be_the_same","Please do not choose the same school more than once");
			s.append("\n\t\t\t alert('").append(msg).append("');");
			s.append("\n\t\t\t return false;");
			s.append("\n\t\t }");
			s.append("\n\t }");
			s.append("\n\t else{");
			s.append("\n\t\t alert('").append("no choices").append("');");
			s.append("\n\t\t return false;");
			s.append("\n\t }");
			//s.append("\n\t\t alert('").append("nothing wrong").append("');");
			s.append("\n\t return true;");
			s.append("\n}\n");
			return s.toString();
		}
		*/

	private String getMySubmitScript() {
		StringBuffer s = new StringBuffer();
		s.append("\n function MySubmit(){");
		s.append("\n\t if(checkApplication()) {");
		s.append("\n\t\t document.").append(PARAM_FORM_NAME).append(".elements['").append(PARAM_FORM_SUBMIT).append("'].value='true';");
		s.append("\n\t\t document.").append(PARAM_FORM_NAME).append(".submit();");
		s.append("\n\t }");
		s.append("\n}\n");

		return s.toString();
	}
}