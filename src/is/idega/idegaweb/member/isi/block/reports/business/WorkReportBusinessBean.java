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
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMemberHome;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.block.media.business.MediaBusiness;
import com.idega.core.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
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
	private WorkReportMemberHome workReportMemberHome;
	private WorkReportBoardMemberHome workReportBoardMemberHome;
	
	private static final short COLUMN_MEMBER_NAME = 0;
	private static final short COLUMN_MEMBER_SSN = 1;
	private static final short COLUMN_MEMBER_STREET_NAME = 2;
	private static final short COLUMN_MEMBER_POSTAL_CODE = 3;
	
	private static final short COLUMN_BOARD_MEMBER_NAME = 2;
	private static final short COLUMN_BOARD_MEMBER_SSN = 3;
	private static final short COLUMN_BOARD_MEMBER_STREET_NAME = 4;
	private static final short COLUMN_BOARD_MEMBER_POSTAL_CODE = 5;
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	/**
	 * This method gets you the id of the workreport of the club and year specified. It will create a new report if it does not exist already.
	 * @param clubId
	 * @param yearStamp
	 * @return The id of the WorkReport for this club and year.
	 */
	public int getOrCreateWorkReportIdForClubIdByYear(int clubId, int year ) throws RemoteException{
		WorkReport report = null;
		
		createOrUpdateLeagueWorkReportGroupsForYear(year);
		
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
		
	public WorkReportMemberHome getWorkReportMemberHome(){
		if(workReportMemberHome==null){
			try{
				workReportMemberHome = (WorkReportMemberHome) IDOLookup.getHome(WorkReportMember.class);
			}
			catch(RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportMemberHome;
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
	
	
	
	public WorkReportMember createWorkReportMember(int reportID, String personalID) throws CreateException {

		User user = null;
		try {
			user = getUser(personalID);
		}
		catch (FinderException e) {
			return null;
		}
		
		Age age = new Age(user.getDateOfBirth());

		WorkReportMember member = getWorkReportMemberHome().create();
		member.setReportId(reportID);
		member.setName(user.getName());
		member.setPersonalId(personalID);
		member.setAge(age.getYears());
		member.setDateOfBirth( (new IWTimestamp(user.getDateOfBirth())).getTimestamp() );
		member.setUserId(((Integer)user.getPrimaryKey()).intValue());
		
		if (true)
			member.setAsMale();
		else
			member.setAsFemale();
		
		member.store();
		return member;
	
	}
	
	public WorkReportBoardMember createWorkReportBoardMember(int reportID, String personalID) throws CreateException {

		User user = null;
		try {
			user = getUser(personalID);
		}
		catch (FinderException e) {
			return null;
		}
		
		Age age = new Age(user.getDateOfBirth());

		WorkReportBoardMember member = getWorkReportBoardMemberHome().create();
		member.setReportId(reportID);
		member.setName(user.getName());
		member.setPersonalId(personalID);
		member.setAge(age.getYears());
		member.setDateOfBirth( (new IWTimestamp(user.getDateOfBirth())).getTimestamp() );
		member.setUserId(((Integer)user.getPrimaryKey()).intValue());
		
		if (true)
			member.setAsMale();
		else
			member.setAsFemale();
		
		member.store();
		return member;
	
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
	
	private File getFileObjectForFileId(int fileId){
		Cache file = MediaBusiness.getCachedFileInfo(fileId, this.getIWApplicationContext().getApplication());
		
		return new File(file.getRealPathToFile());
	}
	
	private HSSFWorkbook getExcelWorkBookFromFileId(int fileId) throws WorkReportImportException{
		HSSFWorkbook excel = null;
		File file = getFileObjectForFileId(fileId);
		
		try {
			excel = new HSSFWorkbook(new FileInputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new WorkReportImportException("workreportimportexception.file_not_found");
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new WorkReportImportException("workreportimportexception.could_not_read_file");
		}
		
		return excel;
	}
	
	public boolean importMemberPart(int workReportFileId, int workReportId) throws WorkReportImportException{
		
			System.out.println("Starting member importing from excel file for workreportid: "+workReportId);
		
		//clear the table first
			deleteWorkReportMembersForReport(workReportId);
		
			WorkReportMemberHome membHome = getWorkReportMemberHome();
			WorkReport report = getWorkReportById(workReportId);
			int year = report.getYearOfReport().intValue();
			
			//update or create the league groups so we can connect the users to them
			createOrUpdateLeagueWorkReportGroupsForYear(year);
		
			report.setMemberFileId(workReportFileId);
			report.store();
		
			HSSFWorkbook excel = getExcelWorkBookFromFileId(workReportFileId);
			
			
			HSSFSheet members = excel.getSheetAt(SHEET_MEMBER_PART);
			int firstRow = 4;
			int lastRow = members.getLastRowNum();
			
			//System.out.println("First row is at: "+firstRow);
			//System.out.println("Last row is at: "+lastRow);
			
			//get the top row to get a list of leagues to use.
			HSSFRow headerRow = (HSSFRow) members.getRow(firstRow);
			Map leaguesMap = getLeaguesMapFromRow(headerRow,year);
			
			
			//iterate through the rows that contain the actual data and create the records in the database
			for (int i = (firstRow+1); i <= lastRow; i++) {
				HSSFRow row = (HSSFRow) members.getRow(i);
				
				if(row!=null){
					int firstCell = row.getFirstCellNum();
					int lastCell = row.getLastCellNum();
					
		
					
					//String name = HSSFCellUtil.translateUnicodeValues(row.getCell(COLUMN_MEMBER_NAME)).getStringCellValue();
					String name = row.getCell(COLUMN_MEMBER_NAME).getStringCellValue();
					String ssn = getStringValueFromExcelNumberOrStringCell(row,COLUMN_MEMBER_SSN);
					ssn = (ssn.length()<10)? "0"+ssn : ssn;
					String streetName = getStringValueFromExcelNumberOrStringCell(row,COLUMN_MEMBER_STREET_NAME);
					String postalCode = getStringValueFromExcelNumberOrStringCell(row,COLUMN_MEMBER_POSTAL_CODE);
					
					
					try {
						//the user must already exist in the database
						User user = this.getUser(ssn);
						try {
							membHome.findWorkReportMemberByUserIdAndWorkReportId(((Integer)user.getPrimaryKey()).intValue(),workReportId);
						}
						catch (FinderException e4) {
						//this should happen, we don't want them created twice	
						
						
							WorkReportMember member = createWorkReportMember(workReportId,ssn);//sets basic data
							//member.setAsBoardMember( (boardMember!=null && "X".equals(boardMember.toUpperCase())) );
							if(streetName!=null && !"".equals(streetName)){	
								member.setStreetName(streetName);
							
								
								try {
									PostalCode postal = getAddressBusiness().getPostalCodeHome().findByPostalCodeAndCountryId(postalCode,((Integer)getAddressBusiness().getCountryHome().findByCountryName("Iceland").getPrimaryKey()).intValue());
									member.setPostalCode(postal);
								}
								catch (FinderException e3) {
									//e3.printStackTrace();
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							
							}
							
							member.store();
							
							//find which leagues the member belongs to
							//and create the many to many connections
							for (int j = 5; j <  lastCell ; j++) {
								HSSFCell leagueCell = row.getCell((short)j);
								
								if(leagueCell !=null){
									String check = leagueCell.getStringCellValue();
									boolean isChecked = (check!=null && !"".equals(check) && "X".equals(check.toUpperCase()) );
									if(isChecked){
										WorkReportGroup league = (WorkReportGroup) leaguesMap.get(new Integer(j));
										if(league!=null){
											try {
												league.addMember(member);
											}
											catch (IDOAddRelationshipException e5) {
												e5.printStackTrace();
												throw new WorkReportImportException("workreportimportexception.database_error_could_not_add_member_to_group");
											}
										}
									}
								}
								
							}
						}
					}
					catch (EJBException e1) {
						e1.printStackTrace();
						throw new WorkReportImportException("workreportimportexception.database_error");
					}
					catch (CreateException e2) {
						//failed to create move on.
						e2.printStackTrace();
						System.err.println("Failed to create user for ssn : "+ssn);
						throw new WorkReportImportException("workreportimportexception.database_error_failed_to_create_user");
					} 
					catch (FinderException e) {
						System.err.println("User not found for ssn : "+ssn+" skipping...");
					}
				}
			}
					
			return true;
	}
	
	public boolean importBoardPart(int workReportFileId, int workReportId) throws WorkReportImportException{
		
		UserTransaction transaction;
		
			System.out.println("Starting board and division importing from excel file...");
		
			deleteWorkReportBoardMembersForReport(workReportId);
	
			WorkReportBoardMemberHome membHome = getWorkReportBoardMemberHome();
			WorkReport report = getWorkReportById(workReportId);
			int year = report.getYearOfReport().intValue();
			createOrUpdateLeagueWorkReportGroupsForYear(year);
			
			report.setBoardFileId(workReportFileId);
			report.store();
		

	
			HSSFWorkbook excel = getExcelWorkBookFromFileId(workReportFileId);
		
			HSSFSheet members = excel.getSheetAt(SHEET_BOARD_PART);
			int firstRow = 6;
			int lastRow = members.getLastRowNum();
		
			System.out.println("First row is at: "+firstRow);
			System.out.println("Last row is at: "+lastRow);
		
		
			//iterate through the rows that contain the actual data and create the records in the database
		int i = firstRow;
			while ( i<=lastRow) {
			
				HSSFRow row = (HSSFRow) members.getRow(i);
			
				if(row!=null){
					int firstCell = row.getFirstCellNum();
					int lastCell = row.getLastCellNum();
				
					HSSFCell nameCell = row.getCell(COLUMN_BOARD_MEMBER_NAME);
					if(nameCell==null){
						break;//stop
					}
	
					String name = nameCell.getStringCellValue();
					if( name==null || name.indexOf("##")!=-1 ){
						break;//stop
					} 
					
					String ssn = getStringValueFromExcelNumberOrStringCell(row,COLUMN_BOARD_MEMBER_SSN);
					ssn = (ssn.length()<10)? "0"+ssn : ssn;
					String streetName = row.getCell(COLUMN_BOARD_MEMBER_STREET_NAME).getStringCellValue();
					String postalCode = getStringValueFromExcelNumberOrStringCell(row,COLUMN_BOARD_MEMBER_POSTAL_CODE);
		
				
					WorkReportBoardMember member;
					
					try {
						//the user must already exist in the database
						User user = this.getUser(ssn);
						try {
							
							member = membHome.findWorkReportBoardMemberByUserIdAndWorkReportId(((Integer)user.getPrimaryKey()).intValue(),workReportId);
							member.store();
						}
						catch (FinderException e4) {
						//this should happen, we don't want them created twice	
							member = createWorkReportBoardMember(workReportId,ssn);//sets basic data
							
							
							if(streetName!=null && !"".equals(streetName)){	
								member.setStreetName(streetName);
								
									
								try {
									PostalCode postal = getAddressBusiness().getPostalCodeHome().findByPostalCodeAndCountryId(postalCode,((Integer)getAddressBusiness().getCountryHome().findByCountryName("Iceland").getPrimaryKey()).intValue());
									member.setPostalCode(postal);
								}
								catch (FinderException e3) {
									//e3.printStackTrace();
								} catch (RemoteException e) {
									e.printStackTrace();
								}
								
							}
								
							member.store();
						
						}
					}
					catch (EJBException e1) {
						e1.printStackTrace();
					}
					catch (CreateException e2) {
						//failed to create move on.
						e2.printStackTrace();
						System.err.println("Failed to create user for ssn : "+ssn);
					} 
					catch (FinderException e) {
						System.err.println("User not found for ssn : "+ssn);
					}
				}
				
				i++;
				
			}
				
			return true;
		}
		
		
	
	public boolean importAccountPart(int workReportFileId, int workReportId) throws WorkReportImportException{
		
				System.out.println("Starting member importing from excel file...");
			
				deleteWorkReportMembersForReport(workReportId);
				
				WorkReportMemberHome membHome = getWorkReportMemberHome();
				WorkReport report = getWorkReportById(workReportId);
				int year = report.getYearOfReport().intValue();
				createOrUpdateLeagueWorkReportGroupsForYear(year);
		
				report.setAccountFileId(workReportFileId);
				report.store();
			

		
				HSSFWorkbook excel = getExcelWorkBookFromFileId(workReportFileId);
			
				HSSFSheet members = excel.getSheetAt(SHEET_ACCOUNT_PART);
				int firstRow = 4;
				int lastRow = members.getLastRowNum();
			
				System.out.println("First row is at: "+firstRow);
				System.out.println("Last row is at: "+lastRow);
			
				//get the top row to get a list of leagues to use.
				HSSFRow headerRow = (HSSFRow) members.getRow(firstRow);
				Map leaguesMap = getLeaguesMapFromRow(headerRow,year);
			
			
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
						
					
						try {

							User user = this.getUser(ssn); 
							
							try {
								membHome.findWorkReportMemberByUserIdAndWorkReportId(((Integer)user.getPrimaryKey()).intValue(),workReportId);
							}
							catch (FinderException e4) {
							//this should happen, we don't want them created twice	
						
								WorkReportMember member = membHome.create();
								member.setReportId(workReportId);
								member.setUserId(((Integer)user.getPrimaryKey()).intValue());							
								member.setName(name);
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
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							
								member.store();
							
								//find which leagues the member belongs to
								//and create the many to many connections
								for (int j = 5; j <  lastCell ; j++) {
									HSSFCell leagueCell = row.getCell((short)j);
								
									if(leagueCell !=null){
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
						}
						catch (EJBException e1) {
							e1.printStackTrace();
						}
						catch (CreateException e2) {
							//failed to create move on.
							e2.printStackTrace();
							System.err.println("Failed to create user for ssn : "+ssn);
						} 
						catch (FinderException e) {
							System.err.println("User not found for ssn : "+ssn);
						}
					}
				}
					
				return true;
		}

	/**
	 * @param year of report
	 */
	private void createOrUpdateLeagueWorkReportGroupsForYear(int year) {
		createOrUpdateWorkReportGroupsForYearAndGroupType(year,IWMemberConstants.GROUP_TYPE_LEAGUE);
	}

	/**
	 * @param report
	 */
	private void deleteWorkReportMembersForReport(int reportId) {
		try {
			Collection members = getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdOrderedByMemberName(reportId);
			Iterator iter = members.iterator();
			
			while (iter.hasNext()) {
				WorkReportMember memb = (WorkReportMember) iter.next();
				try {
					memb.remove();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoveException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		catch (FinderException e) {
			//do nothing because its empty
		}
	}
	
	/**
	 * @param report
	 */
	private void deleteWorkReportBoardMembersForReport(int reportId) {
		try {
			Collection members = getWorkReportBoardMemberHome().findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(reportId);
			Iterator iter = members.iterator();
			
			while (iter.hasNext()) {
				WorkReportMember memb = (WorkReportMember) iter.next();
				try {
					memb.remove();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoveException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		catch (FinderException e) {
			//do nothing because its empty
		}
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
			//HSSFCell myCell2 = HSSFCellUtil.translateUnicodeValues(myCell);
			cell = myCell.getStringCellValue();
		}
		else{
			cell = Double.toString(myCell.getNumericCellValue());
		}
		
		
		cell = TextSoap.findAndCut(cell,"-");
		cell = TextSoap.findAndCut(cell,".");
		
		int index = cell.indexOf("E");
		
		if(index!=-1){
			cell = cell.substring(0,index);
		}
		
		return cell;
	}
	
	/**
	 * Returns a collection of WorkReportGroup or an empty List
	 * @param year , the year of the report
	 * @param type , the group type
	 * @return A collection of WorkReportGroup or an empty List
	 */
	public Collection getAllWorkReportGroupsForYearAndType(int year, String type){
		try {
			return getWorkReportGroupHome().findAllWorkReportGroupsByGroupTypeAndYear(type,year);
		}
		catch (FinderException e) {
			//no group available return empty list
			return ListUtil.getEmptyList();
		}
		
	}
	
	/**
	 * Returns a collection of WorkReportGroup of the type IWMemor an empty List
	 * @param year , the year of the report
	 * @param type , the group type
	 * @return A collection of WorkReportGroup or an empty List
	 */
	public Collection getAllLeagueWorkReportGroupsForYear(int year){
		try {
			return getWorkReportGroupHome().findAllWorkReportGroupsByGroupTypeAndYear(IWMemberConstants.GROUP_TYPE_LEAGUE,year);
		}
		catch (FinderException e) {
			//no group available return empty list
			return ListUtil.getEmptyList();
		}
		
	}

	/**
	 * @param headerRow, the first row of the members-part worksheet
	 */
	private Map getLeaguesMapFromRow(HSSFRow headerRow, int year) throws WorkReportImportException {
		Map leagues = new HashMap();
		int lastCell = headerRow.getLastCellNum();
		WorkReportGroupHome home = getWorkReportGroupHome();
	
	
		for (int j = 5; j <  lastCell ; j++) {
			HSSFCell cell = headerRow.getCell((short)j);
			if(cell!=null){
				String leagueName = cell.getStringCellValue();
				//stupid framework returns "null" as a string
				if(leagueName!=null && !"".equals(leagueName)){
					String shortName = null;
				  String name = null;
					int index = leagueName.indexOf('-');
					shortName = (index!=-1)? leagueName.substring(0,index) : leagueName;
					name = (index!=-1)? leagueName.substring(index,leagueName.length()) : leagueName;
					
					
					WorkReportGroup group = null;
					
					try {
						group = getWorkReportGroupHome().findWorkReportGroupByShortNameAndYear(shortName,year);
					}
					catch (FinderException e) {
						e.printStackTrace();
						System.err.println("WorkReportGroup not found by short name : "+shortName+" trying group name");
						
						
						try {
							group = getWorkReportGroupHome().findWorkReportGroupByNameAndYear(name,year);
						}
						catch (FinderException e1) {
							throw new WorkReportImportException("workreportimportexception.league_not_found");
						}
						
					}
					
					if( group!=null ){
						leagues.put(new Integer(j),group);
					}
				}
				
				
			}
			
			
		}
		
		return leagues;
	}
	
	private Collection createOrUpdateWorkReportGroupsForYearAndGroupType(int year, String groupType){
		GroupBusiness groupBiz;
		try {
			groupBiz = getGroupBusiness();
			WorkReportGroupHome grHome = getWorkReportGroupHome();
		
			Collection groups = groupBiz.getGroupHome().findGroupsByType(groupType);
			Iterator groupIter = groups.iterator();
			while (groupIter.hasNext()) {
				Group group = (Group) groupIter.next();
				int groupId = ((Integer)group.getPrimaryKey()).intValue();
				WorkReportGroup wGroup = null;
				try{
					
					wGroup = grHome.findWorkReportGroupByGroupIdAndYear(groupId,year);
				}
				catch (FinderException e1) {
					try {
						wGroup = grHome.create();
					}
					catch (CreateException e) {
						e.printStackTrace();
					}
				}
				
				wGroup.setGroupId(groupId);
				wGroup.setName(group.getName());
				wGroup.setShortName(group.getMetaData(IWMemberConstants.META_DATA_CLUB_SHORT_NAME));
				wGroup.setGroupType(group.getGroupType());
				wGroup.setYearOfReport(year);
				
				wGroup.store();
								
			}
			
			
			
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			// do nothing return empty list
			 return ListUtil.getEmptyList();
		}
		
			
		
		
		
		return null;
	}
	
	/**
	 * Gets all the WorkReportMembers for the supplied WorkReport id
	 * @param workReportId
	 * @return a collection of WorkReportMember or an empty list
	 */
	public Collection getAllWorkReportMembersForWorkReportId(int workReportId){
		try {
			return getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdOrderedByMemberName(workReportId);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}
	
	/**
	 * Gets all the WorkReportBoardMembers for the supplied WorkReport id
	 * @param workReportId
	 * @return a collection of WorkReportMember or an empty list
	 */
	public Collection getAllWorkReportBoardMembersForWorkReportId(int workReportId){
		try {
			return getWorkReportBoardMemberHome().findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(workReportId);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}
	
  public boolean changeWorkReportGroupOfMember(WorkReportGroup oldGroup, WorkReportGroup newGroup, WorkReportMember member)  {
    TransactionManager manager = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
      manager.begin();
      if (oldGroup != null) {
        oldGroup.removeMember(member);
        oldGroup.store();
      }
      if (newGroup != null) {
        newGroup.addMember(member);
        newGroup.store();
      }
      manager.commit();
      return true;
    }
    catch (Exception ex)  {
      ex.printStackTrace(System.err);
      try {
        manager.rollback();
      }
      catch (javax.transaction.SystemException sysEx) {
        sysEx.printStackTrace(System.err);
        return false;
      }
      return false;
    }
  }
	

}//end of class
