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
	protected IWTimestamp year = null;
	
	protected static final String PARAM_WORK_REPORT_ID = "iwme_wr_sel_wr_id";
	protected static final String PARAM_WORK_YEAR = "iwme_wr_sel_year";
	
	
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
	 * @return the timestamp for the year of the report
	 */
	public IWTimestamp getYear() {
		return year;
	}

	/**
	 * @param year
	 */
	public void setYear(IWTimestamp year) {
		this.year = year;
	}

	public WorkReportSelector() {
		super();
		addToParamsToMaintainList(PARAM_WORK_REPORT_ID);
		addToParamsToMaintainList(PARAM_WORK_YEAR);
	}
	
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getClubId()!=-1){
			if( iwc.isParameterSet(PARAM_WORK_YEAR) ){
				//regionalUnionId = Integer.parseInt(PARAM_REGION_UNION_ID);
				year = new IWTimestamp(iwc.getParameter(PARAM_WORK_YEAR));
				System.out.println(year.toString());
				
				workReportId = reportBiz.getOrCreateWorkReportIdForClubIdByYear(getClubId(),year.getYear());
			}
			else{
				addWorkReportSelectionForm();
			}
		}
		
	}




	protected void addWorkReportSelectionForm() {

		Form reportSelectorForm = new Form();
		
		reportSelectorForm.maintainParameters(getParamsToMaintain());
		
		DateInput dateSelector = new DateInput(PARAM_WORK_YEAR);
		IWTimestamp stamp = IWTimestamp.RightNow();
		int currentYear = stamp.getYear();
		dateSelector.setYearRange(currentYear-1,currentYear);
		dateSelector.setToShowDay(false);
		
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
	

