/*
 * Created on Aug 30, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * Description: This class handles part B11 of the isi spec. Specifying the date of allowed workreport activity<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportCloser extends Block {
	
 
	protected static final String PARAM_TEMP_CLOSE = "wr_wrc_t_c";
	protected static final String PARAM_OPEN = "wr_wrc_o";
	protected WorkReportBusiness reportBiz;
	protected IWResourceBundle iwrb;
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	

	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	

	
	/**
	 * 
	 */
	public WorkReportCloser() {
		super();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
	
			//reopen
			if(iwc.isParameterSet(PARAM_OPEN)){
				//this.getWorkReportBusiness(iwc).unSendWorkReport(getWorkReportId());//TODO Eiki change to open report
				
			}
			else if(iwc.isParameterSet(PARAM_TEMP_CLOSE)){//confirming the send
				//closeWorkReport(iwc);
			}


			//draw stuff
			addSetupForm(iwc);
		
	}
	

	
	private void addSetupForm(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(WorkReportWindow.ACTION);
		
		Table table = new Table(2, 4);
		table.mergeCells(1,1,2,1);
		table.setRowAlignment(1,Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowAlignment(2,Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowAlignment(3,Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowAlignment(4,Table.HORIZONTAL_ALIGN_RIGHT);
		
		
		DateInput fromDate = new DateInput(WorkReportConstants.WR_APPLICATION_PARAM_FROM_DATE);
		String fDate = (String) iwc.getApplicationAttribute(WorkReportConstants.WR_APPLICATION_PARAM_FROM_DATE);
		
		
		DateInput toDate = new DateInput(WorkReportConstants.WR_APPLICATION_PARAM_TO_DATE);
		String tDate =  (String) iwc.getApplicationAttribute(WorkReportConstants.WR_APPLICATION_PARAM_TO_DATE);
		
		if(fDate!=null) {
			fromDate.setDate((new IWTimestamp(fDate).getDate()));
		}		

		if(tDate!=null) {
			toDate.setDate((new IWTimestamp(tDate).getDate()));
		}		
		
		SubmitButton save = new SubmitButton(iwrb.getLocalizedString("workreportscloser.save_button","save"));
		save.setAsImageButton(true);
		

		Link tempCloseAll = new Link(iwrb.getLocalizedString("workreportscloser.temp_close_button","temporarely close all"));
		tempCloseAll.maintainParameter(WorkReportWindow.ACTION,iwc);
		tempCloseAll.setAsImageButton(true);
		tempCloseAll.addParameter(PARAM_TEMP_CLOSE,"true");
		

		Link cancelTempClose = new Link(iwrb.getLocalizedString("workreportscloser.temp_close_button","temporarely close all"));
		cancelTempClose.maintainParameter(WorkReportWindow.ACTION,iwc);
		cancelTempClose.setAsImageButton(true);
		cancelTempClose.addParameter(PARAM_OPEN,"true");
		
		table.add(iwrb.getLocalizedString("workreportscloser.close_report_text","Here you set or change the timespan of the current work reports availability or temporarely make all reports read only for editing."),1,1);
		table.add(new Text(iwrb.getLocalizedString("workreportscloser.from_date","Start"),true,false,false) ,1,2);
		table.add(new Text(iwrb.getLocalizedString("workreportscloser.to_date","End"),true,false,false),2,2);
		table.add(fromDate,1,3);
		table.add(toDate,2,3);
		
		
		table.add(tempCloseAll,2,4);
		table.add(cancelTempClose,2,4);		
		table.add(save,2,4);
		
		

		form.add(table);	
		add(form);
		
		
	}


	protected WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwc) {
		if (reportBiz == null) {
			try {
				reportBiz = (WorkReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return reportBiz;
	}
}
