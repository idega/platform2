package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.util.IWTimestamp;
/**
 * This block is used to display stats on work reports
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */

public class WorkReportOverView extends Block {
	private WorkReportBusiness reportBiz;
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	int year = -1;

	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	
	public WorkReportOverView() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		Table table = new Table(2, 1);
		if(year==-1) {
			year = (new IWTimestamp(IWTimestamp.getTimestampRightNow()).getYear());
		}
		WorkReportMultiEditor editor = new WorkReportMultiEditor();
		editor.setYear(year);
		
		table.add(editor, 1, 1);//this order so multieditor logic is done before the overview
	//	table.add(new WorkReportOverViewStats(),2,1);
		
		table.setWidthAndHeightToHundredPercent();
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
		table.setCellpaddingAndCellspacing(0);

		add(table);

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

	//TODO Make the year choosable
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
}