/*
 * Created on Aug 30, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.sql.Date;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
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
	protected static final String PARAM_SAVE = "wr_wrc_s";
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
		
		iwrb = this.getResourceBundle(iwc);
	
			//reopen
			if(iwc.isParameterSet(PARAM_TEMP_CLOSE)){
				getWorkReportBusiness(iwc).setAllWorkReportsTemporarelyReadOnly();
				
			}
			else if(iwc.isParameterSet(PARAM_OPEN)){
				getWorkReportBusiness(iwc).removeWorkReportsTemporarelyReadOnlyFlag();
			}
			else if(iwc.isParameterSet(PARAM_SAVE)){
				String fromDate = iwc.getParameter(WorkReportConstants.WR_BUNDLE_PARAM_FROM_DATE);
				String toDate = iwc.getParameter(WorkReportConstants.WR_BUNDLE_PARAM_TO_DATE);
				getWorkReportBusiness(iwc).setWorkReportOpenFromDateWithDateString(fromDate);
				getWorkReportBusiness(iwc).setWorkReportOpenToDateWithDateString(toDate);
				
			}


			//draw stuff
			addSetupForm(iwc);
		
	}
	

	
	private void addSetupForm(IWContext iwc) throws Exception{
		Form form = new Form();
		form.maintainParameter(WorkReportWindow.ACTION);
		
		Table table = new Table(2, 4);
		table.mergeCells(1,1,2,1);
		table.setRowAlignment(1,Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowAlignment(2,Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowAlignment(3,Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowAlignment(4,Table.HORIZONTAL_ALIGN_RIGHT);
		
		Date fDate = getWorkReportBusiness(iwc).getWorkReportOpenFromDate();
		Date tDate = getWorkReportBusiness(iwc).getWorkReportOpenToDate();
		
		DateInput fromDate = new DateInput(WorkReportConstants.WR_BUNDLE_PARAM_FROM_DATE);
		DateInput toDate = new DateInput(WorkReportConstants.WR_BUNDLE_PARAM_TO_DATE);
		
		IWTimestamp earliest = new IWTimestamp();
		int currentYear = earliest.getYear();
		earliest.addYears(2001-currentYear);//so the earliest selectable year is always 2001
		earliest.setMonth(1);
		earliest.setDay(1);
		
		fromDate.setYearRange(earliest.getYear(),currentYear+2);
		toDate.setYearRange(earliest.getYear(),currentYear+2);
		
		if(fDate!=null) {
			fromDate.setDate(fDate);
		}		

		if(tDate!=null) {
			toDate.setDate(tDate);
		}		
		
		SubmitButton save = new SubmitButton(PARAM_SAVE,iwrb.getLocalizedString("workreportscloser.save_button","save"));
		save.setAsImageButton(true);
		


	
		
		table.add(iwrb.getLocalizedString("workreportscloser.close_report_text","Here you set or change the timespan of the current work reports availability or temporarely make all reports read only for editing."),1,1);
		table.add(new Text(iwrb.getLocalizedString("workreportscloser.from_date","Start"),true,false,false) ,1,2);
		table.add(new Text(iwrb.getLocalizedString("workreportscloser.to_date","End"),true,false,false),2,2);
		table.add(fromDate,1,3);
		table.add(toDate,2,3);
		
	

		
		boolean isTempClosed = getWorkReportBusiness(iwc).areAllWorkReportsTemporarelyReadOnly();
		
		if(!isTempClosed) {
			SubmitButton tempCloseAll = new SubmitButton(PARAM_TEMP_CLOSE,iwrb.getLocalizedString("workreportscloser.temp_close_button","temporarely close all"));
			tempCloseAll.setAsImageButton(true);
			table.add(tempCloseAll,2,4);
		}
		else {
			SubmitButton cancelTempClose = new SubmitButton(PARAM_OPEN,iwrb.getLocalizedString("workreportscloser.temp_open_button","cancel temporarely closing"));
			cancelTempClose.setAsImageButton(true);
			table.add(cancelTempClose,2,4);	
		}
		
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
