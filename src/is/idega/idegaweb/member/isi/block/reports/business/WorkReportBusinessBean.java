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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.block.media.business.MediaBusiness;
import com.idega.core.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.caching.Cache;
import com.idega.util.text.TextSoap;

/**
 * Description:	Use this business class to handle work report related business.
 * Copyright:    Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */
public class WorkReportBusinessBean extends MemberUserBusinessBean implements MemberUserBusiness, WorkReportBusiness {


	private static int SHEET_BOARD_PART = 0;
	private static int SHEET_ACCOUNT_PART = 1;
	private static int SHEET_MEMBER_PART = 2;
	
	private WorkReportGroupHome workReportGroupHome;
	private WorkReportClubAccountRecordHome workReportClubAccountRecordHome;
	private WorkReportAccountKeyHome workReportAccountKeyHome;
	private WorkReportHome workReportHome;
	private WorkReportClubMemberHome workReportClubMemberHome;
	private WorkReportBoardMemberHome workReportBoardMemberHome;
	
	private static final short COLUMN_MEMBER_NAME = 0;
	private static final short COLUMN_MEMBER_SSN = 1;
	private static final short COLUMN_MEMBER_STREET_NAME = 2;
	private static final short COLUMN_MEMBER_POSTAL_CODE = 3;
	private static final short COLUMN_BOARD_MEMBER = 4;
	
	
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
	
	public File getFileObjectForFileId(int fileId, IWMainApplication iwma){
		Cache file = MediaBusiness.getCachedFileInfo(fileId, iwma);
		
		return new File(file.getRealPathToFile());
	}
	
	public boolean importMemberPart(int workReportFileId, int workReportId){
		//TODO: Eiki clear members first
			System.out.println("Starting member importing from excel file...");
			
			WorkReport report = getWorkReportById(workReportId);
			report.setMemberFileId(workReportFileId);
			report.store();

			File file = getFileObjectForFileId(workReportFileId,getIWApplicationContext().getApplication());
			
			
			HSSFWorkbook excel;
			try {
				excel = new HSSFWorkbook(new FileInputStream(file));

				HSSFSheet members = excel.getSheetAt(SHEET_MEMBER_PART);
				int firstRow = 4;
				int lastRow = members.getLastRowNum();
				
				System.out.println("First row is at: "+firstRow);
				System.out.println("Last row is at: "+lastRow);
				
				//get the top row to get a list of leagues to use.
				HSSFRow headerRow = (HSSFRow) members.getRow(firstRow);
				Map leaguesMap = getLeaguesMapFromRow(headerRow);
				
				
				//iterate through the rows that contain the actual data and create the records in the database
				for (int i = (firstRow+1); i <= lastRow; i++) {
					HSSFRow row = (HSSFRow) members.getRow(i);
					
					if(row!=null){
						int firstCell = row.getFirstCellNum();
						int lastCell = row.getLastCellNum();
						
						String name = row.getCell(COLUMN_MEMBER_NAME).getStringCellValue();
						String ssn = getStringValueFromExcelNumberOrStringCell(row,COLUMN_MEMBER_SSN);
						ssn = (ssn.length()<10)? "0"+ssn : ssn;
						String streetName = row.getCell(COLUMN_MEMBER_STREET_NAME).getStringCellValue();
						String postalCode = getStringValueFromExcelNumberOrStringCell(row,COLUMN_MEMBER_POSTAL_CODE);
						String boardMember = row.getCell(COLUMN_BOARD_MEMBER).getStringCellValue();
						
						try {
							User user = this.getUser(ssn); 
							WorkReportClubMemberHome membHome = getWorkReportClubMemberHome();
							
							try {
								membHome.findClubMemberByUserIdAndWorkReportIdOrderedByMemberName(((Integer)user.getPrimaryKey()).intValue(),workReportId);
							}
							catch (FinderException e4) {
							//this should happen, we don't want them created twice	
							
								WorkReportClubMember member = membHome.create();
								member.setReportId(workReportId);
								member.setUserId(((Integer)user.getPrimaryKey()).intValue());							
								member.setName(name);
								member.setAsBoardMember( (boardMember!=null && "X".equals(boardMember.toUpperCase())) );
								member.setPersonalId(ssn);
								member.setStreetName(streetName);
								//member.setAge();
								//member.setDateOfBirth();
								
								try {
									PostalCode postal = getAddressBusiness().getPostalCodeHome().findByPostalCodeAndCountryId(postalCode,((Integer)getAddressBusiness().getCountryHome().findByCountryName("Iceland").getPrimaryKey()).intValue());
									member.setPostalCode(postal);
								}
								catch (FinderException e3) {
									//e3.printStackTrace();
								}
								
								member.store();
								
								//find which leagues the member belongs to
								//and create the many to many connections
								for (int j = 5; j <  lastCell ; j++) {
									HSSFCell leagueCell = row.getCell((short)j);
									
									WorkReportGroup league = (WorkReportGroup) leaguesMap.get(leagueCell.getStringCellValue());
									if(league!=null){
										try {
											league.addMember(member);
										}
										catch (IDOAddRelationshipException e5) {
											e5.printStackTrace();
										}
									}
									
								}
							}
						}
						catch (EJBException e1) {
							//user not found, move on
							System.err.println("User not found for ssn : "+ssn);
							e1.printStackTrace();
						}
						catch (CreateException e2) {
							//failed to create move on.
							e2.printStackTrace();
						}
						catch (FinderException ex){
							ex.printStackTrace();
						}
					}
					
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
	}

	/**
	 * Gets the cell value as string even if it is a number and strips all ".", "-" and trailing exponent "098098809E8"
	 * @param row object
	 * @param column number
	 * @return
	 */
	private String getStringValueFromExcelNumberOrStringCell(HSSFRow row, short columnNumber) {
		
		String cell = "";
		HSSFCell myCell = row.getCell((short)columnNumber);
		if( myCell.getCellType() == HSSFCell.CELL_TYPE_STRING){
			cell = myCell.getStringCellValue();
		}
		else{
			cell = Double.toString(myCell.getNumericCellValue());
		}
		System.out.println(cell);
		
		cell = TextSoap.findAndCut(cell,"-");
		cell = TextSoap.findAndCut(cell,".");
		
		int index = cell.indexOf("E");
		
		if(index!=-1){
			cell = cell.substring(0,index);
		}
		
		return cell;
	}

	/**
	 * @param headerRow, the first row of the members-part worksheet
	 */
	private Map getLeaguesMapFromRow(HSSFRow headerRow) {
		Map leagues = new HashMap();
		int lastCell = headerRow.getLastCellNum();
		WorkReportGroupHome home = getWorkReportGroupHome();
	/* fylla group tšfluna fyrst og seakja svo ur henni
		for (int j = 5; j <  lastCell ; j++) {
			String leagueName = headerRow.getCell((short)j).getStringCellValue();
			
			
		}
		*/
		return leagues;
	}
	

}//end of class