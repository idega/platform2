/*
 * $Id: SchoolAccountingCommuneBlock.java,v 1.1 2004/10/15 16:41:08 thomas Exp $
 * Created on Oct 15, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.accounting.presentation;

import java.rmi.RemoteException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.RadioButton;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock;


/**
 * 
 *  Last modified: $Date: 2004/10/15 16:41:08 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
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
		return getNavigationTable(showClass, multipleSchools, centralizedAdminChoice, getAccountingSession().getOperationalField());
	}
	
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools, boolean centralizedAdminChoice, String category) throws RemoteException {
		Table table = new Table(9,2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(3,"8");
		int row = 1;
		
		if (multipleSchools) {
			table.add(getSmallHeader(localize("school.opfield", "Operationalfield: ")), 1, row);
			table.mergeCells(2, row, 7, row);
			table.add(new OperationalFieldsMenu(), 2, row++);
			table.setHeight(row++, 15);			
			
			if (centralizedAdminChoice){
				table.resize(9, row + 2);
				table.add(getSmallHeader(localize("school.bun_adm","Show only BUN administrated schools")+":"+Text.NON_BREAKING_SPACE),1,row);
//				table.mergeCells(2, row, 8, row);
				
		
				RadioButton rb1 = new RadioButton(PARAM_BUNADM, ""+true);
				RadioButton rb2 = new RadioButton(PARAM_BUNADM, ""+false);		
						
				if (_centralAdmin){
					rb1.setSelected();
				} else{
					rb2.setSelected();
				}

				rb1.setToSubmit();
				rb2.setToSubmit();
				table.add(rb1,2,row);
				table.add(getSmallHeader(localize("school.yes","Yes")+Text.NON_BREAKING_SPACE),2,row);
					
				table.setNoWrap(3, row);
				table.add(rb2,3,row);
				table.add(getSmallHeader(localize("school.no","No")+Text.NON_BREAKING_SPACE),3,row);

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

		table.add(getSmallHeader(localize("school.season","Season")+":"+Text.NON_BREAKING_SPACE),1,row);
		DropdownMenu schSeas = getSchoolSeasons();
		table.add(schSeas,2,row);
		table.add(getSmallHeader(localize("school.year","Year")+":"+Text.NON_BREAKING_SPACE),4,row);
		DropdownMenu schYears = getSchoolYears();
			schYears.addMenuElementFirst("-1","");
		table.add(schYears,5,row);
		if (showClass) {
//			table.resize(8, row);
			table.setWidth(6, "10");
			table.add(getSmallHeader(localize("school.class","Class")+":"+Text.NON_BREAKING_SPACE),7,row);
			DropdownMenu schClasses = getSchoolClasses();
			schClasses.addMenuElementFirst("-1","");
			table.add(schClasses, 8, row);
		}
		
		table.setColumnWidth(9, "10");
		
		return table;
	}


}
