/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

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
	protected String userType = null;
		
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
	 * @return
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType
	 */
	public void setUserType(String userType) {
		this.userType = userType;
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
		addToParametersToMaintainList(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_ID);
		addToParametersToMaintainList(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}
	
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getClubId()!=-1){
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
			String paramWorkReportYear = getParameterFromSessionOrRequest(iwc,WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
			String paramWorkReportId = getParameterFromSessionOrRequest(iwc,WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_ID);
			
			if(  paramWorkReportYear!=null  ){
				//regionalUnionId = Integer.parseInt(PARAM_REGION_UNION_ID);
				iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR,paramWorkReportYear);
				year = Integer.parseInt(paramWorkReportYear);
				
				
				if(paramWorkReportId==null){	
					workReportId = reportBiz.getOrCreateWorkReportIdForGroupIdByYear(getClubId(),year, true);
					iwc.setSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_ID,Integer.toString(workReportId));
				}
				else{
					workReportId = Integer.parseInt(paramWorkReportId);
				}
				
				Text yearText = new Text(getWorkReportBusiness(iwc).getWorkReportById(workReportId).getYearOfReport().toString(),true,true,false);
				addToStepsExtraInfo(STEP_NAME_LOCALIZATION_KEY,yearText);
				
				iwc.removeSessionAttribute(WorkReportConstants.WR_SESSION_CLEAR);
				
			}
			else{
				
				addWorkReportSelectionForm(iwc);
			}
		}
		
	}




	protected void addWorkReportSelectionForm(IWContext iwc) throws RemoteException{

		Form reportSelectorForm = new Form();
		
		reportSelectorForm.maintainParameters(getParametersToMaintain());
		
		DropdownMenu dateSelector = getWorkReportBusiness(iwc).getYearDropdownMenu(-1);
		
		Table table = new Table(2,3);
		table.mergeCells(1,1,2,1);
		table.mergeCells(1,3,2,3);
		table.setAlignment(1,3,Table.HORIZONTAL_ALIGN_RIGHT);
		
		
		table.add(iwrb.getLocalizedString("workreportselector.select_year_of_report","Select work report year."),1,1);
		table.add(iwrb.getLocalizedString("workreportselector.year","Year"),1,2);
		table.add(dateSelector,2,2);		
		table.add(new HiddenInput(WorkReportConstants.WR_SESSION_PARAM_CLUB_ID,Integer.toString(getClubId())),2,2);	
		
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString("clubselector.continue","continue"));
		submit.setAsImageButton(true);
		
		table.add(submit,1,3);
		
		
		reportSelectorForm.add(table);
	
		add(reportSelectorForm);
	}
	

		
	}
	

