/*
 * Created on Aug 30, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

/**
 * Description: This class handles part B5 of the isi spec. Sending in a work report and checking stuff.<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportCloser extends WorkReportSelector {
	

	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportscloser.step_name";
	protected static final String PARAM_CLOSE = "wr_wrc_send";
	protected static final String PARAM_OPEN = "wr_wrc_unsend";

	
	/**
	 * 
	 */
	public WorkReportCloser() {
		super();
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getWorkReportId()!=-1){//do nothing before we have the work report id
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
			
			//reopen
			if(iwc.isParameterSet(PARAM_OPEN)){
				this.getWorkReportBusiness(iwc).unSendWorkReport(getWorkReportId());//TODO Eiki change to open report
			}
			
			if(iwc.isParameterSet(PARAM_CLOSE)){//confirming the send
				closeWorkReport(iwc);
			}
			else{
				boolean readOnly = getWorkReportBusiness(iwc).isWorkReportReadOnly(getWorkReportId());
				
				if(readOnly){//ALREADY CLOSED
					add(iwrb.getLocalizedString("workreportscloser.report_is_read_only","The work report has already been closed."));
					addBreak();
					
					if(iwc.isSuperAdmin() || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(getUserType())){
						Form form = new Form();
						form.addBreak();
						form.add(new HiddenInput(PARAM_OPEN,"TRUE"));
						SubmitButton check = new SubmitButton(iwrb.getLocalizedString("workreportcloser.open","reopen report"));
						check.setAsImageButton(true);
						form.add(check);
						form.maintainParameters(this.getParametersToMaintain());
						add(form);
					}
				}//NOT CLOSED YET
				else {
					Form form = new Form();
					form.add(iwrb.getLocalizedString("workreportscloser.close_report_text","Press the button below to close the report to changes."));
					form.addBreak();
					form.add(new HiddenInput(PARAM_CLOSE,"TRUE"));
					SubmitButton check = new SubmitButton(iwrb.getLocalizedString("workreportscloser.close_report","close report"));
					check.setAsImageButton(true);
					form.add(check);
					form.maintainParameters(this.getParametersToMaintain());
					add(form);
				}
			}
			
			
		}
		
		
	}
	

	
	private void closeWorkReport(IWContext iwc) throws RemoteException{
		if(getWorkReportBusiness(iwc).closeWorkReport(getWorkReportId())){
			add(iwrb.getLocalizedString("workreportcloser.has_been_closed","The work report has now been closed and is read only."));
		}
		else{
			add(iwrb.getLocalizedString("workreportsender.closing_failed","Closing failed!"));
		}
	}


}
