package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
/**
 * This block is used to display stats on work reports
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */

public class WorkReportOverViewStats extends Block {
	private WorkReportBusiness reportBiz;
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	protected static final String COLOR_MIDDLE = "#DFDFDF";
	private int year = -1;
	
	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	
	public WorkReportOverViewStats() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		if(year==-1) {
			year = (new IWTimestamp(IWTimestamp.getTimestampRightNow()).getYear());
		}
		
		
		Table table = new Table(2,14);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setStyleAttribute("border","2px solid #000000");
		table.setRowColor(1,	COLOR_MIDDLE );
		table.setCellspacing(0);
		table.mergeCells(1,1,2,1);
		table.setHorizontalAlignment(Table.HORIZONTAL_ALIGN_CENTER);
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		
		//create a table with this info
		//total nr. of members
		//total nr. of players
		//nr. of groups that have return some data
		//nr. of inactive groups
		//nr. of groups with each status
		
		
		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.header","Summary"),true,false,false) ,1,1);

		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.total_players","Players"),true,false,false) ,1,2);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfPlayersForWorkReportYear(getYear()),2,2);
		
		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.total_members","Members"),true,false,false) ,1,3);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfMembersForWorkReportYear(getYear()),2,3);
		
		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.total_competitors","Competitors"),true,false,false) ,1,4);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfCompetitorsForWorkReportYear(getYear()),2,4);

		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_not_done","Not done"),true,false,false) ,1,5);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_NOT_DONE,getYear()),2,5);

		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_some_done","Some done"),true,false,false) ,1,6);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_SOME_DONE,getYear()),2,6);

		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_done","Done"),true,false,false) ,1,7);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_DONE,getYear()),2,7);
		
		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_no_report","No report"),true,false,false) ,1,8);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_NO_REPORT,getYear()),2,8);
		
		
		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_at_regional_union","At regional union"),true,false,false) ,1,9);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_AT_REGIONAL_UNION,getYear()),2,9);

		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_investigate","Investigate"),true,false,false) ,1,10);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_INVESTIGATE,getYear()),2,10);

		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_competititon_ban","Competititon ban"),true,false,false) ,1,11);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_COMPETITION_BAN,getYear()),2,11);
		
		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_should_be_banned","Should be banned"),true,false,false) ,1,12);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_SHOULD_BE_BANNED,getYear()),2,12);
		
		table.add(new Text(iwrb.getLocalizedString("workreportoverviewstats.status_continuance","Continuance"),true,false,false) ,1,13);
		table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_CONTINUANCE,getYear()),2,13);
		
		//TODO number of inactive clubs
		//table.add(iwrb.getLocalizedString("workreportoverviewstats.status_continuance","Continuance"),1,13);
		//table.add(""+this.getWorkReportBusiness(iwc).getTotalCountOfWorkReportsByStatusAndYear(WorkReportConstants.WR_STATUS_CONTINUANCE,getYear()),2,13);
		
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

	protected int getYear() {
		return year;
	}

	/**
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
		
	}
	

	
	
}