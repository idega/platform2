/*
 * Created on Dec 19, 2003
 *
 */
package is.idega.idegaweb.campus.block.application.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.business.BuildingFinder;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.util.TimePeriod;

import is.idega.idegaweb.campus.presentation.CampusBlock;

/**
 * Approver
 * @author aron 
 * @version 1.0
 */
public class Approver extends CampusBlock {
	
	static int col1 = 1,col2 = 2,col3 = 3 ,col4 = 4,col5 = 5,col6 =6;
	private int numberOfApartments = 3;
	private String lineColor = "#0000ff";
	
	public void main(IWContext iwc){
		init(iwc);
		process(iwc);
		presentate(iwc);
	}
	
	public void init(IWContext iwc){
	
	}
	
	public void process(IWContext iwc){
	
	}
	
	public void presentate(IWContext iwc){
		presentateApplicationForm(iwc);
	}
	
	private void presentateApplicationForm(IWContext iwc){
		
		Table table = new Table();
			//table.setBorder(1);
			table.setCellpadding(0);
			table.setNoWrap();
			int row =1;
			createSubjectFields(iwc,table,row);
			row =table.getRows()+1;
			setLineAtRow(table,row);
			row =table.getRows()+1;
			createApplicantFields(iwc,table,row);
			row =table.getRows()+1;
			setLineAtRow(table,row);
			row =table.getRows()+1;
			createStudyFields(iwc,table,row);
			row =table.getRows()+1;
			setLineAtRow(table,row);
			row =table.getRows()+1;
			createFamilyFields(iwc,table,row);
			row =table.getRows()+1;
			setLineAtRow(table,row);
			row =table.getRows()+1;
			createApartmentFields(iwc,table,row);
			row =table.getRows()+1;
			setLineAtRow(table,row);
			row =table.getRows()+1;
			createControlFields(iwc,table,row);
			row =table.getRows()+1;
			setLineAtRow(table,row);
			row =table.getRows()+1;
		  
		Form form = new Form();
		form.add(table);
		add(form);
			
	}
	
	private int createSubjectFields(IWContext iwc,Table table,int rowStart){
		int col = 1;
		int row = rowStart;
			table.setWidth(Table.HUNDRED_PERCENT);
			
			table.add(getSmallHeader(localize("application_subject","Subject")),col++,row);
			table.setWidth(col++,Table.HUNDRED_PERCENT);
			table.add(getEditButton(),col4,row);
			table.add(getSaveButton(),col4,row);
			table.mergeCells(col2,row,col3,row);
			table.mergeCells(col4,row,col6,row);
			table.setAlignment(col4,row,Table.HORIZONTAL_ALIGN_RIGHT);
			
			return row;
	}
	
	private int createApplicantFields(IWContext iwc,Table table,int rowStart){
		
		int start = rowStart;
		int row = start;
			table.setWidth(Table.HUNDRED_PERCENT);
			table.add(getSmallHeader(localize("name","Name")),col1,row++);
			table.add(getSmallHeader(localize("legal_residential","Legal Residential")),col1,row++);
			table.add(getSmallHeader(localize("residential","Residential")),col1,row++);
			table.add(getSmallHeader(localize("zip","Zip")),col1,row++);
			
			row = start;
			table.add(getSmallHeader(localize("personal_id","Personal ID")),col5,row++);
			table.add(getSmallHeader(localize("phone","Phone")),col5,row++);
			table.add(getSmallHeader(localize("mobile","Mobile")),col5,row++);
			table.add(getSmallHeader(localize("email","Email")),col5,row++);
			
			row = start;
			table.mergeCells(col2,row,col4,row);
			table.add(getTextInput("app_nam","",30),col2,row++);
			table.mergeCells(col2,row,col4,row);
			table.add(getTextInput("app_leg_res","",30),col2,row++);
			table.mergeCells(col2,row,col4,row);
			table.add(getTextInput("app_leg","",30),col2,row++);
			table.mergeCells(col2,row,col4,row);
			table.add(getTextInput("app_zip","",10),col2,row++);
			
			
			row = start;
			table.add(getTextInput("app_pid","",15),col6,row++);
			table.add(getTextInput("app_pho","",15),col6,row++);
			table.add(getTextInput("app_mob","",15),col6,row++);
			table.add(getTextInput("app_ema","",15),col6,row);
		
			return row;
	}
	
	private int createFamilyFields(IWContext iwc,Table table,int rowStart){
			int start = rowStart;
		    int row = start,col=1;;
		    table.mergeCells(col1,row,col2,row);
			table.add(getSmallHeader(localize("spouse","Spouse")),col1,row++);
			table.add(getSmallHeader(localize("name","Name")),col1,row++);
			table.add(getSmallHeader(localize("persona_id","Personal ID")),col1,row++);
			table.add(getSmallHeader(localize("school","School")),col1,row++);
			table.add(getSmallHeader(localize("faculty","Faculty")),col1,row++);
			table.add(getSmallHeader(localize("division","Division")),col1,row++);
			table.add(getSmallHeader(localize("study_start","Study start")),col1,row++);
			table.add(getSmallHeader(localize("study_end","Study end")),col1,row++);
		
			row = start; 
			row++;
			table.add(getTextInput("sps_nam","",30),col2,row++);
			table.add(getTextInput("sps_pid","",30),col2,row++);
			table.add(getTextInput("sps_sch","",30),col2,row++);
			table.add(getTextInput("sps_fac","",30),col2,row++);
			table.add(getTextInput("sps_div","",30),col2,row++);
			table.add(new DatePicker("sps_sch_strt",getInterfaceStyle(),iwc.getCurrentLocale()),col2,row++);
			table.add(new DatePicker("sps_sch_end",getInterfaceStyle(),iwc.getCurrentLocale()),col2,row);
			
			
			int spouserow = row;
			table.mergeCells(col3,rowStart,col3,spouserow);
			table.setColor(col3,rowStart,lineColor);
			
			// children
			row = start; 
			table.add(getSmallHeader(localize("childs","Children")),col4,row++);
			table.add(getSmallHeader(localize("name","Name")),col4,row);
			table.add(getSmallHeader(localize("personal_id","Personal ID")),col6,row++);
			
			
			table.add(getTextInput("chd_nam1","",30),col4,row);
			table.mergeCells(col4,row,col5,row);
			table.add(getTextInput("chd_pid1","",14),col6,row++);
			table.add(getTextInput("chd_nam2","",30),col4,row);
			table.mergeCells(col4,row,col5,row);
			table.add(getTextInput("chd_pid2","",14),col6,row++);
			table.add(getTextInput("chd_nam3","",30),col4,row);
			table.mergeCells(col4,row,col5,row);
			table.add(getTextInput("chd_pid3","",14),col6,row++);
			table.add(getTextInput("chd_nam4","",30),col4,row);
			table.mergeCells(col4,row,col5,row);
			table.add(getTextInput("chd_pid4","",14),col6,row++);
			table.add(getTextInput("chd_nam5","",30),col4,row);
			table.mergeCells(col4,row,col5,row);
			table.add(getTextInput("chd_pid5","",14),col6,row++);
			
			
			return Math.max(spouserow,row);
		
	}
	
	private int createApartmentFields(IWContext iwc,Table table,int rowStart){
		int row = rowStart;
		java.util.Collection types = BuildingFinder.getAllApartmentTypesComplex();
		table.mergeCells(col1,row,col2,row);
		table.add(getSmallHeader(localize("applied_apartments","Applied for apartments")),col1,row++);
		for (int i = 1; i <= numberOfApartments; i++) {
			table.add(getSmallHeader(localize("choice_"+i,i+".Choice")),col1,row);
			table.add(getTypeSelect(types,"type_sel"+i,"",i>1),col2,row);
			table.mergeCells(col2,row,col4,row);
			row++;
		}
		int typeRow = row;
		
		row = rowStart+1;
		table.add(getSmallHeader(localize("hire_from","Hire from")),col5,row++);
		table.add(getSmallHeader(localize("furniture","Furniture")),col5,row++);
		table.add(getSmallHeader(localize("waitinglist","Waitinglist")),col5,row++);
		
		row = rowStart+1;
		table.add(new DatePicker("hire_from",getInterfaceStyle(),iwc.getCurrentLocale()),col6,row++);
		table.add(getCheckBox("furniture",""),col6,row++);
		table.add(getCheckBox("waitinglist",""),col6,row++);
		row++;
		table.add(getSmallHeader(localize("other_info","Other information")),col1,row);
		table.setVerticalAlignment(col1,row,Table.VERTICAL_ALIGN_TOP);
		table.add(getTextArea("other_info","",65,3),col2,row);
		table.mergeCells(col2,row,col6,row);
		
		return Math.max(row,typeRow);
	}
	
	private int createStudyFields(IWContext iwc,Table table,int rowStart){
		int row = rowStart;
		table.add(getSmallHeader(localize("school","School")),col1,row++);
		table.add(getSmallHeader(localize("faculty","Faculty")),col1,row++);
		table.add(getSmallHeader(localize("division","Division")),col1,row++);
		
		
		row= rowStart;
		table.add(getTextInput("app_sch","",30),col2,row++);
		table.add(getTextInput("app_fac","",30),col2,row++);
		table.add(getTextInput("app_div","",30),col2,row++);
		
		
		row = rowStart;
		table.add(getSmallHeader(localize("study_start","Study start")),col5,row);
		table.add(new DatePicker("app_sch_strt",getInterfaceStyle(),iwc.getCurrentLocale()),col6,row++);
		table.add(getSmallHeader(localize("study_end","Study end")),col5,row);
		table.add(new DatePicker("app_sch_end",getInterfaceStyle(),iwc.getCurrentLocale()),col6,row++);
		
		return row;
	}
	
	private int createControlFields(IWContext iwc,Table table,int rowStart){
		int row = rowStart;
		Table priorityTable = new Table(6,2);
		char[] priorities = {'A','B','C','D','E','T'};
		for (int i = 0; i < priorities.length; i++) {
			priorityTable.add(getSmallHeader(String.valueOf(priorities[i])),i+1,1);
			priorityTable.add(getRadioButton("priority",String.valueOf(priorities[i])),i+1,2);
		}
		table.add(getSmallHeader(localize("priority_group","Priority group")),col1,row);
		table.add(priorityTable,col2,row);
		table.mergeCells(col2,row,col3,row);
		
		table.add(getSaveButton(),col4,row);
		table.add(getSubmitButton("act_deny","true","Deny","deny"),col4,row);
		table.add(getSubmitButton("act_approve","true","Approve","approve"),col4,row);
		table.mergeCells(col4,row,col6,row);
		table.setAlignment(col4,row,Table.HORIZONTAL_ALIGN_RIGHT);
		
		return row;
	}
	
	public DropdownMenu getTypeSelect(Collection types, String name, String selected, boolean firstEmpty) {
		DropdownMenu drpTypes = new DropdownMenu(name);
		drpTypes.setStyleClass(getInterfaceStyle());
		if (firstEmpty)
			drpTypes.addMenuElementFirst("-1", "-");
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			ApartmentTypeComplexHelper type = (ApartmentTypeComplexHelper) iter.next();
			drpTypes.addMenuElement(type.getKey(), type.getName());
		}
		drpTypes.setSelectedElement(selected);
		return drpTypes;
	}
	
	private void setLineAtRow(Table table,int row){
		table.mergeCells(col1,row,col6,row);
		table.setColor(col1,row,lineColor);
	}
}
