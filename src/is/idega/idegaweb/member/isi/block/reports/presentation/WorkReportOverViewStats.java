package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.util.IWTimestamp;
/**
 * This block is used to display stats on work reports
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */

public class WorkReportOverViewStats extends Block {
	private WorkReportBusiness reportBiz;
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	
	public WorkReportOverViewStats() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		Table table = new Table();
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		
		//create a table with this info
		//total nr. of members
		//total nr. of players
		//nr. of groups that have return some data
		//nr. of inactive groups
		//nr. of groups with each status
		table.add(iwrb.getLocalizedString("workreportoverviewstats.header","Summary"),1,1);

		table.add(iwrb.getLocalizedString("workreportoverviewstats.total_players","Players"),1,2);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfPlayersForWorkReportYear(getYear()),2,2);
		
		table.add(iwrb.getLocalizedString("workreportoverviewstats.total_members","Members"),1,3);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfMembersForWorkReportYear(getYear()),2,3);
		
		table.add(iwrb.getLocalizedString("workreportoverviewstats.total_competitors","Competitors"),1,4);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfCompetitorsForWorkReportYear(getYear()),2,4);


		table.add(iwrb.getLocalizedString("workreportoverviewstats.status_some_done","Some done"),1,5);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_SOME_DONE,getYear()),2,5);

		table.add(iwrb.getLocalizedString("workreportoverviewstats.status_done","Done"),1,6);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_DONE,getYear()),2,6);
/*
		table.add(iwrb.getLocalizedString("workreportoverviewstats.total_competitors","Competitors"),1,4);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfCompetitorsForWorkReportYear(getYear()),2,4);


		table.add(iwrb.getLocalizedString("workreportoverviewstats.total_competitors","Competitors"),1,4);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfCompetitorsForWorkReportYear(getYear()),2,4);
		
		getTotalCountOfWorkReportsByStatus
		*/

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
	protected int getYear() {
		return (new IWTimestamp(IWTimestamp.getTimestampRightNow()).getYear());
	}
	

	
	
}