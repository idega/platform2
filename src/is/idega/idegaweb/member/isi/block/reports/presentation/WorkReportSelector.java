/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * Description: This class extends ClubSelector to add the step of selecting the year of the work report and creating the report if needed.
 * If you sub class this class you must call its main(iwc) method first in your subclass's main(iwc) method<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportSelector extends ClubSelector {
	
	protected int workReportId = -1;
	protected int year = -1;
	
	protected static final String PARAM_WORK_REPORT_ID = "iwme_wr_sel_wr_id";
	protected static final String PARAM_WORK_YEAR = "iwme_wr_sel_year";
	
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportselector.step_name";
	
	
	/**
	 * @return the work reports id. -1 if no report selected
	 */
	public int getWorkReportId() {
		return workReportId;
	}

	/**
	 * @param workReportId
	 */
	public void setWorkReportId(int workReportId) {
		this.workReportId = workReportId;
	}

	/**
	 * @return the year of the report
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}

	public WorkReportSelector() {
		super();
		addToParametersToMaintainList(PARAM_WORK_REPORT_ID);
		addToParametersToMaintainList(PARAM_WORK_YEAR);
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}
	
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getClubId()!=-1){
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
			
			if( iwc.isParameterSet(PARAM_WORK_YEAR) ){
				//regionalUnionId = Integer.parseInt(PARAM_REGION_UNION_ID);
				year = Integer.parseInt(iwc.getParameter(PARAM_WORK_YEAR));
				
				
				workReportId = reportBiz.getOrCreateWorkReportIdForClubIdByYear(getClubId(),year);
			}
			else{
				
				addWorkReportSelectionForm();
			}
		}
		
	}




	protected void addWorkReportSelectionForm() {

		Form reportSelectorForm = new Form();
		
		reportSelectorForm.maintainParameters(getParametersToMaintain());
		
		DropdownMenu dateSelector = new DropdownMenu(PARAM_WORK_YEAR);
		IWTimestamp stamp = IWTimestamp.RightNow();
		
		int currentYear = stamp.getYear();
		int beginningYear = currentYear - 1;
		
		for (int i = beginningYear; i <= currentYear; i++) {
			dateSelector.addMenuElement(i,Integer.toString(i));
		}
		
		Table table = new Table(2,3);
		table.mergeCells(1,1,2,1);
		table.mergeCells(1,3,2,3);
		table.setAlignment(1,3,Table.HORIZONTAL_ALIGN_RIGHT);
		
		
		table.add(iwrb.getLocalizedString("workreportselector.select_year_of_report","Select work report year."),1,1);
		table.add(iwrb.getLocalizedString("workreportselector.year","Year"),1,2);
		table.add(dateSelector,2,2);		
		table.add(new HiddenInput(PARAM_CLUB_ID,Integer.toString(getClubId())),2,2);	
		
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString("clubselector.continue","continue"));
		submit.setAsImageButton(true);
		
		table.add(submit,1,3);
		
		
		reportSelectorForm.add(table);
	
		add(reportSelectorForm);
	}
	

		
	}
	

