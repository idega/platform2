/*
 * Created on Aug 30, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.SubmitButton;

/**
 * Description: This class handles part B5 of the isi spec. Sending in a work report and checking stuff.<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportSender extends WorkReportSelector {
	

	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportsender.step_name";
	protected static final String PARAM_CHECK = "wr_wrs_check";
	protected static final String PARAM_SEND = "wr_wrs_send";
	protected static final String PARAM_UNSEND = "wr_wrs_unsend";
	protected static final String PARAM_TEXT = "wr_wrs_text";
	
	/**
	 * 
	 */
	public WorkReportSender() {
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
			if(iwc.isParameterSet(PARAM_UNSEND)){
				this.getWorkReportBusiness(iwc).unSendWorkReport(getWorkReportId());
			}
			
			if(iwc.isParameterSet(PARAM_CHECK)){//start the check
				add(checkWorkReport(iwc));
			}
			else if(iwc.isParameterSet(PARAM_SEND)){//confirming the send
				sendWorkReport(iwc);
			}
			else{//show the first message and the check button
				showFirstRespone(iwc);
			}
			
			
		}
		
		
	}
	

	private void showFirstRespone(IWContext iwc) throws RemoteException {
		boolean readOnly = getWorkReportBusiness(iwc).isWorkReportReadOnly(getWorkReportId());
				
		if(readOnly){//ALREADY SENT
			add(this.iwrb.getLocalizedString("workreportsender.report_is_read_only","The work report has already been sent with these comments : "));
			addBreak();
			String comments = getWorkReportBusiness(iwc).getWorkReportSentText(getWorkReportId());
			if( comments == null){
				comments = this.iwrb.getLocalizedString("workreportsender.no_comments","No comments.");
			}
			Text commentsText = new Text(comments,true,false,false);
			add( commentsText);
		
		
			if(iwc.isSuperAdmin() || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(getUserType())){
				Form form = new Form();
				form.addBreak();
				form.add(new HiddenInput(PARAM_UNSEND,"TRUE"));
				SubmitButton check = new SubmitButton(this.iwrb.getLocalizedString("workreportsender.open","reopen report"));
				check.setAsImageButton(true);
				form.add(check);
				form.maintainParameters(this.getParametersToMaintain());
				add(form);
			}
		}//NOT SENT YET
		else {
			Form form = new Form();
			form.add(this.iwrb.getLocalizedString("workreportsender.check_report_text","Press the button below to check the validity of this work report."));
			form.addBreak();
			form.add(new HiddenInput(PARAM_CHECK,"TRUE"));
			SubmitButton check = new SubmitButton(this.iwrb.getLocalizedString("workreportsender.check","check report"));
			check.setAsImageButton(true);
			form.add(check);
			form.maintainParameters(this.getParametersToMaintain());
			add(form);
		}	
	}



	private Form checkWorkReport(IWContext iwc) throws RemoteException{
		StringBuffer report = new StringBuffer();
	
		//a. is there an account for an empty division?
		boolean accountForEmpty = getWorkReportBusiness(iwc).isThereAYearlyAccountForAnEmptyDivision(this.workReportId);
		String accountResponse = "";
		
		if(accountForEmpty){
			accountResponse = this.iwrb.getLocalizedString("workreportsender.account_exist_for_empty_division","Account entries exist for an empty division! ");
			report.append(accountResponse);
		}
		
		//b. is there an account missing for a division with members
		boolean accountMissing = getWorkReportBusiness(iwc).isYearlyAccountMissingForADivisionWithMembers(this.workReportId);
		String accountMissingResponse = "";
		
		if(accountMissing){
			accountMissingResponse = this.iwrb.getLocalizedString("workreportsender.account_missing_for_a_division_with_members","Account entries are missing for a division!");
			report.append(accountMissingResponse);
		}
		
		//c. is the board info missing for a division with members or account info
		boolean boardMissing = getWorkReportBusiness(iwc).isBoardMissingForDivisionWithMembersOrYearlyAccount(this.workReportId);
		String boardMissingResponse = "";
		
		if(boardMissing){
			boardMissingResponse = this.iwrb.getLocalizedString("workreportsender.board_info_is_missing_for_a_division","Board info is missing for a division!");
			report.append(boardMissingResponse);
		}
		
		
		/////////
		String text = report.toString();
		Text commentsText = new Text(text,true,false,false);
		
		Form form = new Form();
		form.add(this.iwrb.getLocalizedString("workreportsender.review_comments","The check has ended. Please review the comments and either fix what needs to be fixed or send the report by clicking the button."));
		form.addBreak();
		form.addBreak();
		
		if(text.equals("")){
			form.add(this.iwrb.getLocalizedString("workreportsender.no_comments","No comments."));
		}
		else{
			form.add(commentsText);
		}
		
		form.addBreak();
		form.addBreak();
		SubmitButton check = new SubmitButton(this.iwrb.getLocalizedString("workreportsender.send","send report"));
		form.add(new HiddenInput(PARAM_SEND,"TRUE"));
		form.add(new HiddenInput(PARAM_TEXT,text));
		check.setAsImageButton(true);
		form.add(check);
		form.add(new PrintButton(this.iwrb.getLocalizedImageButton("workreportsender.print","print")));
		form.maintainParameters(this.getParametersToMaintain());
		
		return form;
		
	}
	
	private void sendWorkReport(IWContext iwc) throws RemoteException{
		if(getWorkReportBusiness(iwc).sendWorkReport(getWorkReportId(),iwc.getParameter(PARAM_TEXT),this.iwrb)){
			add(this.iwrb.getLocalizedString("workreportsender.has_been_sent","The work report has now been sent and is read only."));
		}
		else{
			add(this.iwrb.getLocalizedString("workreportsender.sending_failed","Sending failed! Please fix issues first!"));
		}
	}


}
