package is.idega.idegaweb.member.isi.block.reports.business;
import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.MemberUserBusinessBean;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKey;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKeyHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMemberHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecord;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecordHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubMemberHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * Description:	Use this business class to handle work report related business.
 * Copyright:    Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */
public class WorkReportBusinessBean extends MemberUserBusinessBean implements MemberUserBusiness, WorkReportBusiness {
	
	private WorkReportGroupHome workReportGroupHome;
	private WorkReportClubAccountRecordHome workReportClubAccountRecordHome;
	private WorkReportAccountKeyHome workReportAccountKeyHome;
	private WorkReportHome workReportHome;
	private WorkReportClubMemberHome workReportClubMemberHome;
	private WorkReportBoardMemberHome workReportBoardMemberHome;
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	/**
	 * This method gets you the id of the workreport of the club and year specified. It will create a new report if it does not exist already.
	 * @param clubId
	 * @param yearStamp
	 * @return The id of the WorkReport for this club and year.
	 */
	public int getOrCreateWorkReportIdForClubIdByYear(int clubId, int year ) throws RemoteException{
		WorkReport report = null;
		
		try {
			report = getWorkReportHome().findWorkReportByClubIdAndYearOfReport(clubId,year);
		}
		catch (FinderException e) {
			System.out.println("[WorkReportBusinessBean] No report for clubId : "+clubId+" adn year : "+year+" creating a new one.");
			try {
				Group club;
				try {
					club = this.getGroupBusiness().getGroupByGroupID(clubId);
					report = getWorkReportHome().create();
					report.setClubId(clubId);
					report.setYearOfReport(year);
					//THIS IS CRAP IT SHOULD JUST USE .getName() !! palli bitch
					report.setClubName( (club.getMetaData(IWMemberConstants.META_DATA_CLUB_NAME)!=null) ? club.getMetaData(IWMemberConstants.META_DATA_CLUB_NAME) : club.getName() );
					report.setClubNumber(club.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
					report.setClubShortName(club.getMetaData(IWMemberConstants.META_DATA_CLUB_SHORT_NAME));
					report.store();
				}
				catch (FinderException e2) {
					e2.printStackTrace();
				}
			}
			catch (CreateException e1) {
				e1.printStackTrace();
			}
			
		}
		
		if(report != null ) return ((Integer)report.getPrimaryKey()).intValue();
		else return -1;
		
	}
		
	public WorkReportHome getWorkReportHome(){
		if(workReportHome==null){
			try{
				workReportHome = (WorkReportHome)IDOLookup.getHome(WorkReport.class);
			}
			catch(RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportHome;
	}
		
	public WorkReportClubMemberHome getWorkReportClubMemberHome(){
		if(workReportClubMemberHome==null){
			try{
				workReportClubMemberHome = (WorkReportClubMemberHome) IDOLookup.getHome(WorkReportClubMember.class);
			}
			catch(RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportClubMemberHome;
	}
	
	public WorkReportBoardMemberHome getWorkReportBoardMemberHome(){
		if(workReportBoardMemberHome==null){
			try{
				workReportBoardMemberHome = (WorkReportBoardMemberHome) IDOLookup.getHome(WorkReportBoardMember.class);
			}
			catch(RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportBoardMemberHome;
	}
	
	public WorkReportGroupHome getWorkReportGroupHome(){
		if(workReportGroupHome==null){
			try{
				workReportGroupHome = (WorkReportGroupHome) IDOLookup.getHome(WorkReportGroup.class);
			}
			catch(RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportGroupHome;
	}
	
	public WorkReportAccountKeyHome getWorkReportAccountKeyHome(){
		if(workReportAccountKeyHome==null){
			try{
				workReportAccountKeyHome = (WorkReportAccountKeyHome) IDOLookup.getHome(WorkReportAccountKey.class);
			}
			catch(RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportAccountKeyHome;
	}
	
	public WorkReportClubAccountRecordHome getWorkReportClubAccountRecordHome(){
		if(workReportClubAccountRecordHome==null){
			try{
				workReportClubAccountRecordHome = (WorkReportClubAccountRecordHome) IDOLookup.getHome(WorkReportClubAccountRecord.class);
			}
			catch(RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportClubAccountRecordHome;
	}
	
	public boolean createEntry(int reportID, String personalID) {
		try {
			User user = null;
			try {
				user = getUserHome().findByPersonalID(personalID);
			}
			catch (FinderException e) {
				return false;
			}
			
			Age age = new Age(user.getDateOfBirth());

			WorkReportClubMember member = getWorkReportClubMemberHome().create();
			member.setReportId(reportID);
			member.setName(user.getName());
			member.setPersonalId(personalID);
			member.setAge(age.getYears());
			member.setUserId(((Integer)user.getPrimaryKey()).intValue());
			if (true)
				member.setAsMale();
			else
				member.setAsFemale();
			
			member.store();
			return true;
		}
		catch (CreateException ce) {
			return false;
		}
	}
	
	
	public WorkReport getWorkReportById(int id){
		try {
			return getWorkReportHome().findByPrimaryKey(new Integer(id));
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}//end of class