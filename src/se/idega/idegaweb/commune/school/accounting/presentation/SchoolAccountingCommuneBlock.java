/*
 * $Id: SchoolAccountingCommuneBlock.java,v 1.2 2005/07/20 17:52:40 malin Exp $
 * Created on Oct 15, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.accounting.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.RadioButton;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathBusiness;
import se.idega.idegaweb.commune.care.business.AccountingSession;
import se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock;


/**
 * 
 *  Last modified: $Date: 2005/07/20 17:52:40 $ by $Author: malin $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public abstract class SchoolAccountingCommuneBlock extends SchoolCommuneBlock {
	
	private final static String PARAM_BUNADM = "PARAM_BUNADM";

	
	private boolean _centralAdmin = false;
	
	protected void initialize(IWContext iwc) throws RemoteException {
		_centralAdmin = iwc.getParameter(PARAM_BUNADM) != null && iwc.getParameter(PARAM_BUNADM).equals("true"); 
		super.initialize(iwc);
	}

	protected Table getNavigationTable(boolean showClass) throws RemoteException {
		return getNavigationTable(showClass, false, false);
	}
		
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools) throws RemoteException {
		return getNavigationTable(showClass, multipleSchools, false);
	}
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools, boolean centralizedAdminChoice) throws RemoteException {
		return getNavigationTable(showClass, multipleSchools, centralizedAdminChoice, getAccountingSession().getOperationalField(), null);
	}
	
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools, boolean centralizedAdminChoice, IWContext iwc) throws RemoteException {
		return getNavigationTable(showClass, multipleSchools, centralizedAdminChoice, getAccountingSession().getOperationalField(), iwc);
	}

	
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools, boolean centralizedAdminChoice, String category, IWContext iwc) throws RemoteException {
		//Table table = new Table(20,15);
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(3,"8");
		
		table.setBorder(0);
		
		int row = 1;
		
		if (multipleSchools) {
			table.add(getSmallHeader(localize("school.opfield", "Operationalfield: ")), 1, row);
			table.mergeCells(2, row, 7, row);
			table.add(new OperationalFieldsMenu(), 2, row++);
			table.setHeight(row++, 15);			
			
			if (centralizedAdminChoice){
				table.resize(9, row + 2);
				table.add(getSmallHeader(localize("school.bun_adm","Show only BUN administrated schools")+":"+Text.NON_BREAKING_SPACE),1,row);
				table.mergeCells(1, row, 9, row);
				
		
				RadioButton rb1 = new RadioButton(PARAM_BUNADM, ""+true);
				RadioButton rb2 = new RadioButton(PARAM_BUNADM, ""+false);		
						
				if (_centralAdmin){
					rb1.setSelected();
				} else{
					rb2.setSelected();
				}

				rb1.setToSubmit();
				rb2.setToSubmit();
//				table.add(rb1,2,row);
				table.add(Text.NON_BREAKING_SPACE,1,row);
				table.add(rb1,1,row);
				table.add(Text.NON_BREAKING_SPACE + getSmallHeader(localize("school.yes","Yes")+Text.NON_BREAKING_SPACE),1,row);
					
				table.setNoWrap(1, row);
				table.add(Text.NON_BREAKING_SPACE,1,row);
				table.add(rb2,1,row);
				table.add(Text.NON_BREAKING_SPACE + getSmallHeader(localize("school.no","No")+Text.NON_BREAKING_SPACE),1,row);

				++row;
				table.setHeight(row, "2");
				++row;			
		
			}
						
			table.resize(9, row + 2);
			table.add(getSmallHeader(localize("school.school_list","School")+":"+Text.NON_BREAKING_SPACE),1,row);
			table.mergeCells(2, row, 8, row);
			table.add(getSchools(_centralAdmin, category),2,row);
			++row;
			table.setHeight(row, "2");
			++row;
		}
		int column = 1;
		table.add(getSmallHeader(localize("school.season","Season")+":"+Text.NON_BREAKING_SPACE),column++,row);
		/*SchoolCategory schoolCateg = null;
		try{
			schoolCateg= getSchoolCategoryHome().findByPrimaryKey(category);	
		}
		 catch (FinderException fe){
		 	log (fe);
		 }
		DropdownMenu schSeas = getSchoolSeasons(true, schoolCateg);
		*/
		DropdownMenu schSeas = getSchoolSeasons();
		 
		table.add(schSeas,column++,row);
		table.setWidth(column++, "10");
		table.add(getSmallHeader(localize("school.year","Year")+":"+Text.NON_BREAKING_SPACE),column++,row);
		DropdownMenu schYears = getSchoolYears();
			schYears.addMenuElementFirst("-1","");
		table.add(schYears,column++,row);
		
		if (showClass) {
//			table.resize(8, row);
			table.setWidth(column++, "10");
			table.add(getSmallHeader(localize("school.class","Class")+":"+Text.NON_BREAKING_SPACE),column++,row);
			DropdownMenu schClasses = getSchoolClasses();
			schClasses.addMenuElementFirst("-1","");
			table.add(schClasses, column++, row);
		}
		
		column = 1;
		row++;
		if (category != null){
			if (category.equalsIgnoreCase(getSchoolBusiness().getHighSchoolSchoolCategory())){
				Collection studyPaths = getStudyPaths(iwc, category);
				if (studyPaths != null){
					//table.setColumnWidth(column++, "10");
					table.setHeight(row++, "2");
					table.add(getSmallHeader(localize("school.study_path", "Study Path")+":"+Text.NON_BREAKING_SPACE), column, row++);
					table.mergeCells(column, row, 8, row);
					table.add(getStudyPathDropdown(studyPaths, iwc), column++, row++);
					table.setHeight(row, "5");
				}
			
				
			}
		}
		
		
		
		//table.setColumnWidth(column++, "10");
		
		return table;
	}
	
	/*
	 * Returns a DropdownMenu for operational fields. 
	 */
/*	private DropdownMenu getOperationDropdownMenu(IWContext iwc, String parameter, String operation) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(parameter));
	//	menu.addMenuElement("", localize(KEY_OPERATION_SELECTOR_HEADER, "Choose operation"));
		try {
			Collection c = getStudyPathBusiness(iwc).findAllOperations();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					SchoolType st = (SchoolType) iter.next();
					String id = st.getPrimaryKey().toString();
					menu.addMenuElement(id, localize(st.getLocalizationKey(), st.getLocalizationKey()));
				}
				if (operation != null) {
					menu.setSelectedElement(operation);
				}
			}		
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
		return menu;	
	}	
	*/
	
	protected DropdownMenu getStudyPathDropdown(Collection studyPaths, IWContext iwc) throws RemoteException{
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(session.getParameterStudyPathID()));
		
		if (studyPaths != null) {
			int studyPathID = 0;
			Iterator iter = studyPaths.iterator();
			menu.addMenuElementFirst("-1", localize("school.choose", "Choose"));
			try{
				while (iter.hasNext()) {
					SchoolStudyPath sp = (SchoolStudyPath) iter.next();
					menu.addMenuElement(sp.getPrimaryKey().toString(), sp.getCode() + " - " + sp.getDescription());
				}
				if (iwc.isParameterSet(session.getParameterStudyPathID())){
					studyPathID = new Integer (iwc.getParameter(session.getParameterStudyPathID())).intValue();
					menu.setSelectedElement(studyPathID);
				}
			}
			catch (Exception e){
				log(e);
			}
			
			
		}
		else {
			menu.addMenuElement(-1, "");
		}
		
		return menu;
	}

	protected Collection getStudyPaths(IWContext iwc, String category){
		Collection studyPaths = null;
		Collection schoolTypeIds = null;
		try{
			schoolTypeIds = getBusiness().getSchoolBusiness().findAllSchoolTypesInCategory(category);
		}
		catch (RemoteException re){
			log(re);
		}
		
		try {
			StudyPathBusiness spb = getStudyPathBusiness(iwc);
			studyPaths = spb.findStudyPathsByOperations(schoolTypeIds);
				
		} catch (RemoteException e) {
			log(e);
		}
		
		return studyPaths;
			
	}
	
	/*
	 * Returns a study path business object
	 */
	public StudyPathBusiness getStudyPathBusiness(IWContext iwc) throws RemoteException {
		
		return (StudyPathBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, StudyPathBusiness.class);
	}	
	

	/**
	 * Returns the form parameter with the specified parameter name
	 * from the specified IWContext object as an integer. Returns -1 if
	 * the parameter is not set. 
	 * @param iwc the idegaWeb context object
	 * @param parameterName the name of the form parameter
	 * @author anders
	 */
	protected int getIntParameter(IWContext iwc, String parameterName) {
		int intValue = 0;
		String s = getParameter(iwc, parameterName);
		try {
			intValue = Integer.parseInt(s);
		} catch (NumberFormatException  e) {
			intValue = -1;
		}
		return intValue;
	}
	/**
	 * Returns the form parameter with the specified parameter name
	 * from the specified IWContext object. Returns an empty string
	 * if the parameter is not set instead of null. 
	 * @param iwc the idegaWeb context object
	 * @param parameterName the name of the form parameter
	 * @author anders
	 */
	protected String getParameter(IWContext iwc, String parameterName) {
		String p = iwc.getParameter(parameterName);
		if (p == null) {
			p = "";
		}
		return p;
	}
	

}
